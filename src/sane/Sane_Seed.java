package sane;
/* Sane_Seed.java */

/* This file was originally sane-seed.c of SANE 2.0.  Contains the */
/* functions for seeding a population with preexisting domain      */
/* knowledge.  The domain knowledge is specified in the file       */
/* seed.txt, which contains a series of input/output pairs.  The   */
/* i/o pairs represent some examples off decisions using some rule */
/* of thumb strategies or expert opinions.                         */
/* Cyndy Matuszek, 1998 */

import java.util.Random;
import java.io.*;

public class Sane_Seed {

// need our own versions of these
  public static float S_ETA = Config.ETA;
  public static float S_ALPHA = Config.ALPHA;
  
  public static float seed_in[][] = new float[Config.NUM_SEED_EXAMPLES][Config.NUM_INPUTS];
  public static float seed_out[][] = new float[Config.NUM_SEED_EXAMPLES][Config.NUM_OUTPUTS];


/*************************************************************/
/* Seed_pop is the main seeding function.  It cycles through */
/* the number of networks to seed (declared by SEED_NETS),   */
/* seeds them, and then writes the new weights back to the   */
/* neuron genes.                                             */
/*************************************************************/

  public static void Seed_pop(Neuron pop[]) {
  
    int i, j, k;
    Network net = new Network();
    
    Load_seed_data("seed.txt");
    
    for (i=0; i<Config.SEED_NETS; ++i) {
      Sane_NN.Build_net(net, Sane.best_nets[i].neuron);
      Seed_cycle(net, Config.SEED_TRIALS);
      for (j=0; j<Config.NUM_HIDDEN; ++j)
        Sane_NN.Weights_to_gene(Sane.best_nets[i].neuron[j]);
    } /* end for */

  }  /* end Seed_pop */

/**********************************************************/


/***********************************************************************/
/* Seed_cycle uses straight backprop learning with learning rate S_ETA */
/* S_ETA and a momentum term S_ALPHA to modify the weights based on    */
/* performance over the training examples.                             */
/***********************************************************************/

  public static void Seed_cycle(Network net, int stop) {
  
    int i, j, k, n;
    double h_err, delta, e;
    double mse = 0.0F;
    double err[] = new double[Config.NUM_OUTPUTS];
    Neuron h;

    for(i=0; i<stop; ++i) {
      System.out.println("Cycle " + i + ", MSE " + mse/600);
      mse = 0.0;
      for(j=0; j<Config.NUM_SEED_EXAMPLES; ++j) {
        for(k=0; k<Config.NUM_INPUTS; ++k)
          net.input[k] = seed_in[j][k];
        Sane_NN.Activate_net(net);
        /* get output errors*/
        for (k=0; k<Config.NUM_OUTPUTS; ++k)
          err[k] = (seed_out[j][k] - net.sigout[k]) 
                    * net.sigout[k] * (1-net.sigout[k]);

        for(k=0; k<Config.NUM_OUTPUTS; ++k) {
          e = seed_out[j][k] - net.sigout[k];
          mse += e*e;
        }
   
        /*error propogation*/
        for(k=0; k<Config.NUM_HIDDEN; ++k) {
          h = net.hidden[k];
          h_err = 0.0;
          for(n=0; n<h.numout; ++n) {
            h_err += err[h.out_conn[n]] * h.out_weight[n];
            delta = S_ETA * err[h.out_conn[n]] * h.sigout
                    + S_ALPHA * h.out_delta[n];
            h.out_weight[n] += delta;
            h.out_delta[n] = (float) delta;
          } /* end for-n */

          if (h_err != 0.0F) {
            h_err = h_err * h.sigout * (1.0F - h.sigout);
            for(n=0;n<h.numin;++n) {
              delta = S_ETA * h_err * Sane_Util.sgn(net.input[h.in_conn[n]])
                      + S_ALPHA * h.in_delta[n];
              h.in_weight[n] += delta;
              h.in_delta[n] = (float) delta;
            } /* end for-n */
          } /* end if h-err */
        } /* end for-k */
      } /* end for-j */
    } /* end for-i */

  }  /* end Seed_cycle */

/**********************************************************/


/**************************************************************/
/* Load_seed_data loads the i/o pairs from the file seed.txt. */
/* All data must be one float per line (note, this is NOT the */
/* same as in Sane 2.0).                                      */
/**************************************************************/

  public static void Load_seed_data(String fname) {
  
    int i,j;
    char temp_q[][] = new char[Config.NUM_SEED_EXAMPLES][Config.NUM_INPUTS];
    int temp_a[] = new int[Config.NUM_SEED_EXAMPLES];
    
    BufferedReader myReader;

    try {

      myReader = new BufferedReader(new FileReader(fname));
      for(i=0; i<Config.NUM_SEED_EXAMPLES; ++i) {
        for(j=0; j<Config.NUM_INPUTS; ++j)
          seed_in[i][j] = new Float(myReader.readLine()).floatValue();
        for(j=0; j<Config.NUM_OUTPUTS; ++j)
          seed_out[i][j] = new Float(myReader.readLine()).floatValue();
      } /* end for-i */

    } catch (IOException e) {
    System.err.println(e);
    System.exit(1);
    }  /* end try/catch */

  }  /* end Load_seed_data */

/**********************************************************/


}  /* end class Sane_Seed */
