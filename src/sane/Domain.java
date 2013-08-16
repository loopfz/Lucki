package sane;
/* Domain.java */

/* This file was originally part of Sane.java.  The Evaluate_net */
/* function and all other domain-specific files reside here; in  */
/* theory, no other Sane files except Config.java should need to */
/* be modified in order to implement a new domain.               */
/* Cyndy Matuszek, 1998 */

import java.util.Random;

public class Domain {

// Global variable declarations.  The folowing are EXAMPLES.
//    public static BufferedReader user_input;
//    public static Random myRandom = new Random();
//    public static String filename;
//    public int evaluations = 0;
//    public float global_best = -9999999.0F;	/* F for "float" (grr) */
//    public float best_average = -9999999.0F;


/**********************************************************/
/* Evaluate_net is the user_defined method that is called */
/* from Sane.java for each network to determine its       */
/* fitness.                                               */
/**********************************************************/

  static float Evaluate_net(Network net) {

/* This is a sample Evaluate_net function that tries to teach a network
   to sum two random inputs and produce the sum in the output. */

    float ReturnVal = 0.0F;
    Random EvalNetRandom = new Random();

    /* put a random value between 0 and .5 in the inputs */
    net.input[0] = (EvalNetRandom.nextFloat()) / 2.0F;
    net.input[1] = (EvalNetRandom.nextFloat()) / 2.0F;

    /* activate the net */
    Sane_NN.Activate_net(net);

    /* after the net is activated, net.sigout contains the results */
    /* of trying to sum the inputs.  So if we add the inputs, and  */
    /* then subtract net.sigout, we'll get an error.  Minimizing   */
    /* this error is our goal.                                     */
    ReturnVal = 100 - Math.abs(((net.input[0]*100.0F) + (net.input[1]*100.0F)) - (net.sigout[0]*100.0F));

/* For debugging, list out returnvals as we go */
System.out.println("net.input[0]: " + net.input[0]);
System.out.println("net.input[1]: " + net.input[1]);
System.out.println("net.sigout[0]: " + net.sigout[0]);
System.out.println("ReturnVal: " + ReturnVal);
System.out.println("");

    return(ReturnVal);
 
  }  /* end Evaluate_net */

/**********************************************************/

}  /* end class Domain */
