package sane;
/** Sane_EA.java **/

/* This file was originally sane-ea.c of SANE 2.0.  Contains common  */
/* evolutionary algorithm functions for SANE, of which Evolve is the */
/* main one called externally.                                       */
/* Cyndy Matuszek, 1998 */

import sane.Best_net_structure;
import java.util.Random;
import java.io.*;

public class Sane_EA {

// need the number of breeding operations per ELITE neuron
  public static int OP_PER_ELITE = Config.NUM_BREED / Config.ELITE;

  static Random myRandom = new Random();


/************************************************************/
/* Evolve is the main genetic function.  After a population */
/* is evaluated, the top ELITE neurons are bred with one    */
/* another, creating NUM_BREED*2 new neurons that replace   */
/* the worst neurons.  The population is then mutated and   */
/* tested to obtain fitness.  They are then sorted by their */
/* fitness level.                                           */
/* A network-level evolution has also been added.  The best */
/* networks are kept in the structure best_nets and are     */
/* sorted, bred and mutated like neurons.                   */
/************************************************************/

  public static void Evolve(Neuron pop[], int cycles) {

// declare and initialize variables and structures
    int i, j, k, l;
    int tmp, num_bits;
    Neuron temp_neuron;
    Neuron breeder[] = new Neuron[2];

    FileWriter myOutputStream;
    
    boolean ok;

      /* initialize best_nets structure */
      for (i=0;i<Config.NUM_TRIALS;++i) {
	  int temp;
        Sane.best_nets[i] = Sane.actual_best_nets[i];
        for(j=0; j<Config.NUM_HIDDEN; ++j) {
	    do {
		temp = Math.abs(myRandom.nextInt()) % Config.POP_SIZE;
		temp_neuron = pop[temp];
		ok = true;
		for (k = 0; k < j; k++) {
		    if (Sane.best_nets[i].neuron[k] == temp_neuron) {
			ok = false;
			break;
		    }
		}
	    } while (!ok);
	    Sane.best_nets[i].neuron[j] = temp_neuron;
	   }
      }  /* end for-i */

    if (Config.SEED_FLAG)
      Sane_Seed.Seed_pop(pop);

// start actual processing.

// cycle population
    for (Sane.generation=0; Sane.generation<cycles; ++Sane.generation) {

      for(j=0; j<Config.POP_SIZE; ++j)  /* reset decoded flag */
        pop[j].decoded = false;
      Sane.Eval_pop(pop);
      if (Config.LAMARCKIAN) {  /* if we are doing Lamarckian, write back */
        for (j=0; j<Config.POP_SIZE; ++j)
          if (pop[j].decoded == true)
            Sane_NN.Weights_to_gene(pop[j]);
      }  /* end if-Lamarckian */

      Qsort_neurons(pop, 0, Config.POP_SIZE-1);
      for (j=0; j<Config.POP_SIZE; ++j)
        pop[j].ranking = j;   /* assign ranking, necessary for net. cross */
      for (j=0; j<Config.NUM_BREED; ++j)   /* mate with any neuron */
        One_pt_crossover(pop[j%Config.ELITE], pop[Find_mate(j%Config.ELITE)],
                         pop[Config.POP_SIZE - (1+j*2)],
                         pop[Config.POP_SIZE - (2+j*2)]);

      /* mutate neuron population */
      for (j=Config.NUM_BREED; j<Config.POP_SIZE; ++j)
        for (i=0; i<Config.GENE_SIZE; ++i) {
          if (i%2 == 1) {
            if ((Math.abs(myRandom.nextInt()) %100) < (Config.MUT_RATE * 2))
              if ((Math.abs(myRandom.nextInt()) %20) == 0)
                pop[j].gene[i] = -pop[j].gene[i];
              else
                pop[j].gene[i] = (float) Sane_Util.Normal_dist(pop[j].gene[i],1.0F);
          }  /* end if i%2=1 */
          else if ((Math.abs(myRandom.nextInt()) % 100) < Config.MUT_RATE)
            pop[j].gene[i] = (Math.abs(myRandom.nextInt()) % (Config.NUM_INPUTS + Config.NUM_OUTPUTS));
        }  /* end for-i loop */

        Sort_best_networks();
        /* breed network population */
        for (j=0; j<Config.TOP_NETS_BREED; ++j)
          New_network_one_pt_crossover(j, pop);

        /* mutate network structure */
        for (j=Config.TOP_NETS_BREED; j<Config.TOP_NETS; ++j)
          for (i=0; i<Config.NUM_HIDDEN; ++i)
            if ((Math.abs(myRandom.nextInt()) % 1000) < Config.MUT_RATE)
              Sane.best_nets[j].neuron[i] = pop[Math.abs(myRandom.nextInt()) % Config.POP_SIZE];

        /* if at SAVE_CYCLE, save popfile and print status to look */
        if (!((Sane.generation % Config.SAVE_CYCLE) == 1)) {
          Sane_Util.Save_pop(pop, Sane.savefile);
          try {
            myOutputStream = new FileWriter("look", true);
            myOutputStream.write("cycle: " + Sane.generation + " top 5: ");
            myOutputStream.write("" + pop[0].fitness + ", " + pop[1].fitness);
            myOutputStream.write(", " + pop[2].fitness + ", " + pop[3].fitness);
            myOutputStream.write(", " + pop[4].fitness + "\n");
            myOutputStream.close();
            } catch (IOException e) {
            System.err.println(e);
            System.exit(0);
          }  /* end try/catch */

        }  /* end if-!-Sane.generation etc */

    }  /* End for-generation loop */

  }  /* end Evolve method */

/**********************************************************/


/***********************************************************/
/* Find_mate returns a mate for a given neuron.  The mate  */
/* must have a fitness level greater than the original     */
/* neuron, except for the best neuron.  This function is   */
/* very aggressive--it might be better to pick a random    */
/* neuron from the ELITE population.                       */
/***********************************************************/

  public static int Find_mate(int num) {

  int i, j;

  if (num == 0)
    return (Math.abs(myRandom.nextInt()) % Config.ELITE);
  else
    return (Math.abs(myRandom.nextInt()) % num);

  }  /* end Find_mate method */

/**********************************************************/


/***********************************************************/
/* One_pt_crossover mates 2 parents, creating 2 children.  */
/* The children's genes are taken from one crossover point */
/* of the parents.  SANE appears to perform best with a    */
/* one-point crossover.                                    */
/***********************************************************/

  public static void One_pt_crossover(Neuron parent1, Neuron parent2,
                                      Neuron child1, Neuron child2) {

  int i;
  int cross1;
  Neuron temp;

  /* find crossover point */
  cross1 = (Math.abs(myRandom.nextInt()) % Config.GENE_SIZE);
  
  /* randomize child positions--impt. for network level */
  if ((Math.abs(myRandom.nextInt()) % 2) == 1) {
    temp = child1;
    child1 = child2;
    child2 = temp;
  }
  for (i=0; i<cross1; ++i) {
    child1.gene[i] = parent1.gene[i];
    child2.gene[i] = parent2.gene[i];
  }
    for (i=cross1; i<Config.GENE_SIZE; ++i) {
    child1.gene[i] = parent2.gene[i];
    child2.gene[i] = parent1.gene[i];
  }
  if ((Math.abs(myRandom.nextInt()) % 2) == 1)
    for (i=0; i<Config.GENE_SIZE; ++i)
      child1.gene[i] = parent1.gene[i];
  else
    for (i=0; i<Config.GENE_SIZE; ++i)
      child2.gene[i] = parent1.gene[i];

  }  /* end One_pt_crossover method */

/**********************************************************/


/**********************************************************/
/* Two_pt_crossover mates 2 parents, creating 2 children. */
/* The children's genes are taken from two crossover      */
/* points of the parents.                                 */
/**********************************************************/

  public static void Two_pt_crossover(Neuron parent1, Neuron parent2,
                                      Neuron child1, Neuron child2) {

    int i;
    int cross1, cross2;

    /* find crossover points */
    cross1 = (Math.abs(myRandom.nextInt()) % Config.GENE_SIZE);
    cross2 = (Math.abs(myRandom.nextInt()) % Config.GENE_SIZE);
    if (cross2 < cross1) {
      i = cross1;
      cross1 = cross2;
      cross2 = i;
    }  /* end if */
  
    for (i=0; i<cross1; ++i) {
      child1.gene[i] = parent1.gene[i];
      child2.gene[i] = parent2.gene[i];
    }
    for (i=cross1; i<cross2; ++i) {
      child1.gene[i] = parent2.gene[i];
      child2.gene[i] = parent1.gene[i];
    }
    for (i=cross2; i<Config.GENE_SIZE; ++i) {
      child1.gene[i] = parent1.gene[i];
      child2.gene[i] = parent2.gene[i];
    }

  }  /* end Two_pt_crossover method */

/**********************************************************/


/******************************************************************/
/* Network_one_pt_crossover performs a crossover on two networks. */
/* Each network is an array of pointers to neurons so the         */
/* crossover op just exchanges neurons.                           */
/*                                                                */
/* When assigning neurons to the new networks, if any neuron is   */
/* a "breeding" neuron, either itself or one of its children will */
/* be assigned, with equal probabability.  This allows new        */
/* networks to point to offspring neurons.  This serves as the    */
/* mutation operator in the network population.                   */
/******************************************************************/

  public static void Network_one_pt_crossover(int which, Neuron pop[]) {

    int i, j, k;
    int cross1, mate, child1, child2;
    
    if (which >= 1)
      mate = (Math.abs(myRandom.nextInt()) % which);
    else
      mate = (Math.abs(myRandom.nextInt()) % Config.TOP_NETS_BREED);
      
    child1 = (Config.TOP_NETS - (1+which*2));
    child2 = (Config.TOP_NETS - (2+which*2));

    /* find crossover point */
    cross1 = (Math.abs(myRandom.nextInt()) % Config.NUM_HIDDEN);
  
    for (i=0; i<cross1; ++i) {
      j = Sane.best_nets[which].neuron[i].ranking;
      if ((j<Config.NUM_BREED) && ((Math.abs(myRandom.nextInt()) % 2) == 1))
        Sane.best_nets[child1].neuron[i] = pop[Config.POP_SIZE -
                                               ((Math.abs(myRandom.nextInt()) % 2)
                                               +1) + j*2];
      else
        Sane.best_nets[child1].neuron[i] = pop[j];
      j = Sane.best_nets[mate].neuron[i].ranking;
      if ((j<Config.NUM_BREED) && ((Math.abs(myRandom.nextInt()) % 2) == 1))
        Sane.best_nets[child2].neuron[i] = pop[Config.POP_SIZE -
                                               ((Math.abs(myRandom.nextInt()) % 2)
                                               +1) + j*2];
      else
        Sane.best_nets[child2].neuron[i] = pop[j];
    } /* end hideous for loop */

    for (i=cross1; i<Config.NUM_HIDDEN; ++i) {
      j = Sane.best_nets[which].neuron[i].ranking;
      if ((j<Config.NUM_BREED) && ((Math.abs(myRandom.nextInt()) % 2) == 1))
        Sane.best_nets[child2].neuron[i] = pop[Config.POP_SIZE -
                                               ((Math.abs(myRandom.nextInt()) % 2)
                                               +1) + j*2];
      else
        Sane.best_nets[child2].neuron[i] = pop[j];
      j = Sane.best_nets[mate].neuron[i].ranking;
      if ((j<Config.NUM_BREED) && ((Math.abs(myRandom.nextInt()) % 2) == 1))
        Sane.best_nets[child1].neuron[i] = pop[Config.POP_SIZE -
                                               ((Math.abs(myRandom.nextInt()) % 2)
                                               +1) + j*2];
      else
        Sane.best_nets[child1].neuron[i] = pop[j];
    } /* end hideous for loop */

    if (Config.NUM_HIDDEN > 10) {
      Sane.best_nets[child1].neuron[Math.abs(myRandom.nextInt()) % Config.NUM_HIDDEN]
                             = pop[Math.abs(myRandom.nextInt()) % Config.POP_SIZE];
      Sane.best_nets[child2].neuron[Math.abs(myRandom.nextInt()) % Config.NUM_HIDDEN]
                             = pop[Math.abs(myRandom.nextInt()) % Config.POP_SIZE];
    } /* end if */
    
  }  /* end Network_one_pt_crossover method */

/**********************************************************/


/********************************************************************/
/* New_network_one_pt_crossover is like network_one_point_crossover */
/* except it uses only knowledge of the ELITE parameter to reassign */
/* the neuron pointers.  If ELITE<NUM_BREED, neurons in the top     */
/* ELITE will have more than one breeding operation.                */
/********************************************************************/
/* I'm going to hell for what I had to do to make this work. */

  public static void New_network_one_pt_crossover(int which, Neuron pop[]) {

    int i, j, k;
    int cross1, mate, child1, child2;

    if (which >= 1)
      mate = (Math.abs(myRandom.nextInt()) % which);
    else
      mate = (Math.abs(myRandom.nextInt()) % Config.TOP_NETS_BREED);

    child1 = (Config.TOP_NETS - (1+which*2));
    child2 = (Config.TOP_NETS - (2+which*2));

    /* find crossover point */
    cross1 = (Math.abs(myRandom.nextInt()) % Config.NUM_HIDDEN);

    for (i=0; i<cross1; ++i) {
      j = Sane.best_nets[which].neuron[i].ranking;
      if ((j<Config.ELITE) && ((Math.abs(myRandom.nextInt()) % 2) == 0))
        Sane.best_nets[child1].neuron[i] = pop[Config.POP_SIZE -
                                               ((Math.abs(myRandom.nextInt()) % 2)
                                               +1) + (j*
                                               (Math.abs(myRandom.nextInt()) %
                                               OP_PER_ELITE)*2)];
      else
        Sane.best_nets[child1].neuron[i] = pop[j];
      j = Sane.best_nets[mate].neuron[i].ranking;
      if ((j<Config.ELITE) && ((Math.abs(myRandom.nextInt()) % 2) == 0))
        Sane.best_nets[child2].neuron[i] = pop[Config.POP_SIZE -
                                               ((Math.abs(myRandom.nextInt()) % 2)
                                               +1) + (j*
                                               (Math.abs(myRandom.nextInt()) %
                                               OP_PER_ELITE)*2)];
      else
        Sane.best_nets[child2].neuron[i] = pop[j];
    } /* end hideous for loop */

    for (i=cross1; i<Config.NUM_HIDDEN; ++i) {
      j = Sane.best_nets[which].neuron[i].ranking;
      if ((j<Config.ELITE) && ((Math.abs(myRandom.nextInt()) % 2) == 0))
        Sane.best_nets[child2].neuron[i] = pop[Config.POP_SIZE -
                                               ((Math.abs(myRandom.nextInt()) % 2)
                                               +1) + (j*
                                               (Math.abs(myRandom.nextInt()) %
                                               OP_PER_ELITE)*2)];
      else
        Sane.best_nets[child2].neuron[i] = pop[j];
      j = Sane.best_nets[mate].neuron[i].ranking;
      if ((j<Config.ELITE) && ((Math.abs(myRandom.nextInt()) % 2) == 0))
        Sane.best_nets[child1].neuron[i] = pop[Config.POP_SIZE -
                                               ((Math.abs(myRandom.nextInt()) % 2)
                                               +1) + (j*
                                               (Math.abs(myRandom.nextInt()) %
                                               OP_PER_ELITE)*2)];
      else
        Sane.best_nets[child1].neuron[i] = pop[j];
    } /* end hideous for loop */

    if (Config.NUM_HIDDEN > 10) {
      Sane.best_nets[child1].neuron[Math.abs(myRandom.nextInt()) % Config.NUM_HIDDEN]
                             = pop[Math.abs(myRandom.nextInt()) % Config.POP_SIZE];
      Sane.best_nets[child2].neuron[Math.abs(myRandom.nextInt()) % Config.NUM_HIDDEN]
                             = pop[Math.abs(myRandom.nextInt()) % Config.POP_SIZE];
    } /* end if */

  }  /* end New_network_one_pt_crossover method */

/**********************************************************/


/*****************************************************************/
/* Sort_best_networks does an insertion sort on neurons based on */
/* their fitness values.                                         */
/*****************************************************************/

static void Sort_best_networks() {

  int i, j;
  float k;
  Best_net_structure temp;
  
  for (i=0; i<Config.NUM_TRIALS; ++i)
    for (j=Config.NUM_TRIALS-1; j>i; --j)
      if (Sane.best_nets[j].fitness > Sane.best_nets[j-1].fitness) {
        temp = Sane.best_nets[j];
        Sane.best_nets[j] = Sane.best_nets[j-1];
        Sane.best_nets[j-1] = temp;
      }

}  /* end Sort_best_networks */

/********************************************************************/


/****************************************************************/
/* quick-sort the neurons of pop.  Needed for large populations */
/* (>50) to make processing handle-able.  Contains the method   */
/* Qpartition_neurons.                                          */
/****************************************************************/

static void Qsort_neurons(Neuron pop[], int p, int r) {

  int q;
  
  if (p<r) {
    q = Qpartition_neurons(pop, p, r);
    Qsort_neurons(pop, p, q);
    Qsort_neurons(pop,q+1,r);
  }

}  /* end Qsort_neurons */

/* this next bit is its own method--partition info for qsort */
/*************************************************************/
static int Qpartition_neurons(Neuron pop[], int p, int r) {

  int i, j;
  Neuron x, temp;
  
  x = pop[p];
  i = p-1;
  j = r+1;
//System.out.println("j: " + j );
  while (true) {
    do{
      --j;
    }while (pop[j].fitness < x.fitness);
    do{
      ++i;
    }while (pop[i].fitness > x.fitness);
    if (i<j) {
      temp = pop[i];
      pop[i] = pop[j];
      pop[j] = temp;
    } /* end 1st do */
    else
      return j;
  } /* end while true */
  
}  /* end Qpartition_neurons */

/********************************************************************/



}  /* end class Sane_EA */
