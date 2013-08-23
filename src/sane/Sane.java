package sane;
/* Sane.java */

/* This file was originally skeleton-main.c of SANE 2.0, designed as a */
/* starting point for implementing SANE to new domains; SANE is run as */
/* the main method from this file, and all other SANE files are called */
/* from here.                                                          */
/* Cyndy Matuszek, 1998 */

import java.util.Random;
import java.io.*;


public class Sane {

// this section of declarations is from sane.h originally.
    public static int generation;
    public static String savefile;
    public static Best_net_structure best_nets[] = new Best_net_structure[Config.NUM_TRIALS];
    public static Best_net_structure actual_best_nets[] = new Best_net_structure[Config.NUM_TRIALS];

// here I define user_input as a stream reading all input for the rest
// of the program.
    public static BufferedReader user_input;

// need a global random instance, since it gets seeded in Sane.java and
// used elsewhere
    public static Random myRandom = new Random();

// if the user is operating on files, this "filename" var will be used
// to get results of prompting for filename.
    public static String filename;

// this section of declarations appears in skeleton-main.c originally.
    public int evaluations = 0;
    public float global_best = -9999999.0F;	/* F for "float" (grr) */
    public float best_average = -9999999.0F;


/*********************************************************/
/* The main method.                                      */
/*********************************************************/

  public static void main(String[] args) {

// here I'm setting up actual_best_nets to be an array of best_net_structures;
// done at the beginning of main because it needs to be done only once, in a
// global sense
    for (int i=0; i<Config.NUM_TRIALS; i++)
      actual_best_nets[i] = new Best_net_structure();

// declare and initialize variables and structures
    int i, j, k;
    float x, y, z;
    int choice = 0;
    int cycles = 0;

    user_input = new BufferedReader(new InputStreamReader(System.in)); // :P

    Neuron actual_pop[] = new Neuron[Config.POP_SIZE];
    Neuron pop[] = new Neuron[Config.POP_SIZE];
    Neuron npop[] = new Neuron[Config.NUM_HIDDEN];
    Network net = new Network();

      /* initialize neuron structure */
      for (i=0;i<Config.POP_SIZE;++i)
        actual_pop[i] = new Neuron();
      for (i=0;i<Config.POP_SIZE;++i)
        pop[i] = actual_pop[i];
      for (i=0;i<Config.NUM_HIDDEN;++i)
        npop[i] = pop[i];

// start actual processing.

    if (args.length != 0) {
        /* if first arg is x, test saved network files */
      if (args[0].charAt(0) == 'x') {
          Find_champion(npop);
          System.exit(0);
      }	/* end "if first arg is x" -- "if args[0].charAt(0)..." */

// seed with given seed value; if we get here, there are args, and the
// first arg isn't x, so we have three args assumed, of which the third
// one ([2]) is a seed value.  Note: start with a #args tester and set
// some damn booleans for what we're trying to do.

    myRandom.setSeed(Integer.parseInt(args[2]));

    if (Config.RANDOM_POP)
      Sane_Util.Create_pop(pop);
    else
      Sane_Util.Load_pop(pop,args[0]);
    savefile = args[0];
    Sane_EA.Evolve(pop,Integer.parseInt(args[1]));
    System.exit(0);
      }	/* end "if args.length !=0..." */

// we fall through to menu presentation if no arguments are given

    System.out.print("Enter a seed: "); 
    try {
      choice = Integer.parseInt(user_input.readLine());
    } catch (IOException e) {
      System.err.println(e);
      System.exit(1);
      }
    myRandom.setSeed(choice);

    while (true) {
      choice = menu();
      switch (choice) {
        case 1: /* evaluate subpopulation */
                Sane_NN.Build_net(net, npop);
                Domain.Evaluate_net(net);
                break;
        case 2: /* create random population */
                Sane_Util.Create_pop(pop);
                System.out.println("Random population created.");
                break;
        case 3: /* load population file */
                System.out.print("Enter filename of popfile to load: "); 
                  try {
                  filename = user_input.readLine();
                  } catch (IOException e) {
                  System.err.println(e);
                  System.exit(1);
                  } /* end catch */
                Sane_Util.Load_pop(pop,filename);
                //System.out.println("Diversity = " + Sane_Util.Phi(pop)); 
                break;
        case 4: /* save population to file */
                System.out.print("Enter filename to save population to: "); 
                  try {
                  filename = user_input.readLine();
                  } catch (IOException e) {
                  System.err.println(e);
                  System.exit(1);
                  } /* end catch */
                Sane_Util.Save_pop(pop,filename);
                break;
        case 5: /* load subpopulation */
                System.out.print("Enter filename of subpop to load: "); 
                  try {
                  filename = user_input.readLine();
                  } catch (IOException e) {
                  System.err.println(e);
                  System.exit(0);
                  } /* end catch */
                Sane_Util.Load_partial(pop,filename);
                break;
        case 6: /* save subpopulation to file */
                System.out.print("Enter filename to save subpopulation to: ");
                  try {
                  filename = user_input.readLine();
                  } catch (IOException e) {
                  System.err.println(e);
                  System.exit(1);
                  } /* end catch */
                Sane_Util.Save_partial(pop,filename);
                break;
        case 7: /* find champion (best network) */
                Find_champion(npop);
                break;
        case 9: Sane_Util.Cluster_output(pop,"r-vectors.txt","r-names.txt",Config.POP_SIZE);
                break;
        default:
                System.exit(0);

      }  /* end switch */

    }  /* end while */
      
  }  /* end main */

/*********************************************************/
/* The menu method, used directly  as part of main.      */
/*********************************************************/

  static int menu() {

    int i;
    int choice = 0;

    System.out.println();
    System.out.println("------------Menu------------");
    System.out.println(" 1. Evaluate Subpopulation");
    System.out.println(" 2. Create Random Population");
    System.out.println(" 3. Load Population File");
    System.out.println(" 4. Save Population To File");
    System.out.println(" 5. Load Subpopulation");
    System.out.println(" 6. Save Subpopulation");
    System.out.println(" 7. Find Champion");
    System.out.println(" 9. Output Network Vectors for PCA");
    System.out.print("Choice: ");

// here I'm not flushing the input stream before I read in the choice of
// menu item, but I should.
    try {
      choice = Integer.parseInt(user_input.readLine());
    } catch (IOException e) {
      System.err.println(e);
      System.exit(1);
      }

    return choice;

  }	/* end menu */

/*********************************************************/


/********************************************************************/
/* Eval_pop evaluates entire populations to get the average fitness */
/* of each neuron and the fitness of each network blueprint.  This  */
/* function is called by Evolve and should put the fitness of each  */
/* neuron in the neuron's fitness field (pop[i].fitness).           */
/********************************************************************/

  static void Eval_pop(Neuron pop[]) {
  
    int i, j, k;
//int spam2;
//float spam = 0;
    int best[] = new int[Config.TOP_NETS];
    float pop_average;
    float total_fitness = 0.0F;
    float the_best = -999999.0F;
    String best_name;
    Network net = new Network();
    Neuron net_pop[] = new Neuron[Config.NUM_HIDDEN];
    Neuron the_best_net[] = new Neuron[Config.NUM_HIDDEN];
  
    /* make new file name for best network */
    i = generation % 1000;
    best_name = ("" + (i/100) + ((i%100)/10) + ((i%100)%10) + "best.bin");
    
    /* reset fitness and test values of each neuron */
    for (i=0; i<Config.POP_SIZE; ++i) {
      pop[i].fitness = 0;
      pop[i].ranking = 0;
      pop[i].tests = 0;
    }
  
    /* eval stage */
    for (i=0; i<Config.NUM_TRIALS; ++i) {
    
      if ((i < Config.TOP_NETS) == true)
        for (j=0; j<Config.NUM_HIDDEN; ++j) {
          net_pop[j] = best_nets[i].neuron[j];
          ++net_pop[j].tests;
        } /* end for-j */
      else  /* find random subpopulation */
        for (j=0; j<Config.NUM_HIDDEN; ++j) {
          net_pop[j] = pop[Math.abs(myRandom.nextInt()) % Config.POP_SIZE];
          best_nets[i].neuron[j] = net_pop[j];
          ++net_pop[j].tests;
        } /* end for-j */
        
      Sane_NN.Build_net(net, net_pop);
      best_nets[i].fitness = 0.0F;
      /*evaluate network*/
      for (j=0; j<Config.NUM_ATTEMPTS; ++j)
        best_nets[i].fitness += Domain.Evaluate_net(net);
//debugging code
//for (spam2=0; spam2<Config.POP_SIZE; ++spam2)
//System.out.println("right after evaluate_net called- best_nets[" + i + "] fitness: " + best_nets[i].fitness);
      total_fitness += best_nets[i].fitness;
  
    }  /* end for-i loop */
  
    pop_average = total_fitness / Config.NUM_TRIALS;
    Sane_EA.Sort_best_networks();

    /* each neuron's fitness is determined from the best 5 networks */
    /* it participated in.  It may be better to use the best 3.     */
    for (j=0; j<Config.NUM_TRIALS; ++j)
      for (k=0; k<Config.NUM_HIDDEN; ++k)
        if (best_nets[j].neuron[k].ranking < 5) {
          ++best_nets[j].neuron[k].ranking;
          best_nets[j].neuron[k].fitness += best_nets[j].fitness;
        } /* end if <5 */
    
    /* save best networks to a file */
    Sane_Util.Save_partial(best_nets[0].neuron, best_name);



//debugging code
//for (i=0; i<Config.POP_SIZE; ++i)
//  System.out.println("end of eval_pop- pop[" + i + "] fitness: " + pop[i].fitness);
  
  }  /* end Eval_pop */

/********************************************************************/


/**********************************************************/
/* Find_champion reads in the best networks (from files)  */
/* and evaluates them.  The performance of each network   */
/* is reported in report.txt; this is a way to find the   */
/* best overall network.                                  */
/**********************************************************/

  static void Find_champion(Neuron npop[]) {

// define some values.
    int i, j;
    float fitness;
    float best = -9999999.0F;	/* F for "float" (grr) */
    String subname;
    Network net = new Network();

// define an output stream that will be redirected to various files,
// as appropriate.
    FileWriter myOutputStream;

// start chugging on producing files.

    for (j=0;j<1000;++j) {
      subname = ("" + j/100) + ((j/10)%10) + (j%10) + "best.bin";

      Sane_Util.Load_partial(npop, subname);
      Sane_NN.Build_net(net, npop);
      fitness = 0;

      /* evaluate network */
      for (i=0;i<10;++i)
        fitness += Domain.Evaluate_net(net);
      if (fitness > best)
        best = fitness;
      try {
        /* this is "try to open report.txt for appending" -- true=append */
        myOutputStream = new FileWriter("report.txt", true);
        myOutputStream.write(j + "	" + best/10.0F + "\n");
        myOutputStream.close();
        } catch (IOException e) {
//        System.err.println(e);
//        System.exit(0);
        } /* end catch */
    }  /* end for-j loop */

    try {
      myOutputStream = new FileWriter("analyze.out", true);
      myOutputStream.write(j + "	" + best/10.0F + "\n");
      myOutputStream.close();
      } catch (IOException e) {
      System.err.println(e);
      System.exit(0);
      } /* end catch */

    System.out.println(j + "  " + best/10.0F + "\n");
 
  }  /* end Find_champion */

/*********************************************************/


}  /* end class Sane */
