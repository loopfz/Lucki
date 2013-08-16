package sane;
/* Sane_NN.java */

/* This file was originally sane-nn.c of SANE 2.0.  Contains the code */
/* for building, activating and printing a neural network from a      */
/* subpopulation of SANE neurons.                                     */
/* Cyndy Matuszek, 1998 */

import java.util.Random;

public class Sane_NN {

/************************************************************/
/* Build_net builds a neural network from the subpopulation */
/* of neurons pointed to by pop.  The resulting network is  */
/* defined in net.                                          */
/************************************************************/

  public static void Build_net(Network net, Neuron pop[]) {

    int i, j, k;
    int from, to, label;
    float weight;

    for (i=0; i<Config.NUM_HIDDEN; ++i) {
      net.hidden[i] = pop[i];
      if (pop[i].decoded == false)
        Gene_to_weights(pop[i]);
    } /* end for */

  }  /* end Build_net */

/**********************************************************/


/**********************************************************/
/* called by build_net only.                              */
/**********************************************************/

  static void Gene_to_weights(Neuron n) {

    int i;

    n.numin = 0;
    n.numout = 0;
    
    for (i=0; i<Config.GENE_SIZE; i+=2) {
      if (n.gene[i] < Config.NUM_INPUTS) {
        n.in_conn[n.numin] = (int) n.gene[i];
        n.in_delta[n.numin] = 0.0F;
        n.in_weight[n.numin++] = n.gene[i+1];
      } /* endif */
      else {
        n.out_conn[n.numout] = (int) n.gene[i] - Config.NUM_INPUTS;
//System.out.println("i: " + i + "   out_conn gets: " + ((int) n.gene[i] - Config.NUM_INPUTS));
        n.out_delta[n.numout] = 0.0F;
        n.out_weight[n.numout++] = n.gene[i+1];
      } /*end else*/
    } /* end for */
    
    n.decoded = true;

  }  /* end Gene_to_weights */

/**********************************************************/


/**********************************************************/
/* called by Sane_EA.Evolve.                              */
/**********************************************************/

  static void Weights_to_gene(Neuron n) {

    int i, in, out;

    in = 0;
    out = 0;
    
    for (i=0; i<Config.GENE_SIZE; i+=2) {
      if (n.gene[i] < Config.NUM_INPUTS)
        n.gene[i+1] = n.out_weight[out++];
      else
        n.gene[i+1] = n.in_weight[in++];
    } /* end for */

  }  /* end Weights_to_gene */

/**********************************************************/


/***********************************************************/
/* Activate_net activates the neural network.  This is the */
/* heart of using sane.  The input layer must be set up    */
/* before activate_net is called.                          */
/***********************************************************/

  public static void Activate_net(Network net) {

    int i, j;
    int from;
    float weight;
    double sum, max;
    double outsum[] = new double[Config.NUM_OUTPUTS];
    Neuron h;
    
    max = -999999.0;
    
    /* reset output layer */
    for (i=0; i<Config.NUM_OUTPUTS; ++i)
      net.sum[i] = 0.0F;
      
    for (i=0; i<Config.NUM_HIDDEN; ++i) {
      h = net.hidden[i];
      sum = 0.0F;
      for(j=0; j<h.numin; ++j)
        sum += h.in_weight[j] * net.input[h.in_conn[j]];
      h.sigout = Sigmoid((float) sum);
//System.out.println("h.numout: " + h.numout);
      for(j=0; j<h.numout; ++j)
//{
//System.out.println("i: " + i + "   j: " + j + "   h.out_conn[j]: " + h.out_conn[j] + "   h.out_weight[j]: " +  h.out_weight[j]);
        net.sum[h.out_conn[j]] += h.out_weight[j]*h.sigout;
//}
    } /* end for */
    for(i=0; i<Config.NUM_OUTPUTS; ++i) {
      net.sigout[i] = Sigmoid(net.sum[i]);
      if (net.sum[i] >= max) {
        net.winner = i;
        max = net.sum[i];
      } /* end if */
    } /* end for */

  }  /* end Activate_net */

/**********************************************************/


/****************************************************************/
/* sigmoid and gauss are some standard little math functions,   */
/* which java probably already has secreted in some library     */
/* somewhere, but it's easier to implement them than find them. */
/****************************************************************/

static float Sigmoid(float sum) {
 
  return (float) (1.0F/(1 + Math.exp(-sum)));

}  /* end Sigmoid */

/****************************************************************/

static float Gauss(float sum, float sd) {

  return (float) (Math.exp(-(sum*sum/(2*(sd*sd)))));
  
}  /* end Gauss */

/****************************************************************/


}  /* end class Sane_NN */
