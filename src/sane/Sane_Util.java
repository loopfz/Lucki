package sane;
/* Sane_Util.java */

/* This file was originally sane-util.c of SANE 2.0.  Contains basic */
/* utility functions for handling SANE populations, mostly (but not  */
/* entirely) referenced from the menu rather than command line.      */
/* Cyndy Matuszek, 1998 */

import java.util.Random;
import java.io.*;

// these imported classes were originally part of header files; Config
// is the class containing all the configuration settings, and Neuron,
// Network and Best_net_structure were all structures defined in sane.h.


public class Sane_Util {

/*********************************************************/
/* the Create_pop(Neuron pop[]) method creates a random  */
/* population of neurons and passes it back.  Called     */
/* from Sane.java repeatedly and probably elsewhere.     */
/*********************************************************/

  public static void Create_pop(Neuron new_pop[]) {

    int i, j;

    if (Config.SEED_FLAG)
      for (i=0; i<Config.POP_SIZE; ++i) {
        new_pop[i].decoded = false; /*reset decoded flag*/
        for(j=0; j<Config.GENE_SIZE; ++j) {
        if ((j%2) == 1)
          new_pop[i].gene[j] = (Math.abs(Sane.myRandom.nextFloat()) - 0.5F);
        else
          new_pop[i].gene[j] = (Math.abs(Sane.myRandom.nextInt()) % (Config.NUM_INPUTS + Config.NUM_OUTPUTS));
        }  /* end for-j loop */
      }  /* end for-i loop */

    else  /* Config.SEED_FLAG = false */
      for (i=0; i<Config.POP_SIZE; ++i) {
        new_pop[i].decoded = false; /*reset decoded flag*/
        for(j=0; j<Config.GENE_SIZE; ++j) {
        if ((j%2) == 1)
          new_pop[i].gene[j] = (float) Normal_dist(0.0, 2.0);
        else
          new_pop[i].gene[j] = (Math.abs(Sane.myRandom.nextInt()) % (Config.NUM_INPUTS + Config.NUM_OUTPUTS));
        }  /* end for-j loop */
      }  /* end for-i loop */

  }  /* end Create_pop */

/**********************************************************/


/****************************************************/
/* sgn is a small math function, called mostly from */
/* sane-critic, but a little from Sane_NN as well.  */
/****************************************************/

  public static float sgn(float x) {

  if (x > 0)
    return 1.0F;
  else if (x < 0)
    return -1.0F;
  else
    return 0.0F;

  }  /* end sgn */

/**********************************************************/


/*********************************************************/
/* In the original sane-util.c, the normal dist function */
/* was defined last (I include this for navigation help) */
/* but I'm defining it here because it's first called in */
/* Create_pop, above.                                    */
/*********************************************************/

  public static double Normal_dist(double avg, double sd) {

    double fac, r, v1, v2;

    if (sd == 0.0)
      return avg;

    do {
      v1 = 2.0 * Sane.myRandom.nextDouble() - 1.0;
      v2 = 2.0 * Sane.myRandom.nextDouble() - 1.0;
      r = v1*v1 + v2*v2;
    } while (r >= 1.0);

    fac = Math.sqrt(-2.0 * (Math.log(r) / r));
//System.out.println("return of fac: " + fac);
//System.out.println("return of normdist: " + (v2*fac*sd + avg));
    return (v2*fac*sd + avg);

  }  /* end Normal_dist */

/**********************************************************/


/*********************************************************/
/* the Load_pop method loads a population from a binary  */
/* file into pop.
/*********************************************************/

  public static void Load_pop(Neuron pop[], String fname) {

    int i, j, tmp, g_size;
    FileInputStream myFile;
    DataInputStream myInputStream;

    try {
      myFile = new FileInputStream(fname);
      myInputStream = new DataInputStream(myFile);
      tmp = myInputStream.readInt(); /* read in 1st thing in file */
      if (tmp != Config.POP_SIZE) {
        System.out.println("Error - population size specified does not match population size of file");
        System.exit(1);
      }  /* end if */

      g_size = myInputStream.readInt();
      for (i=0; i<Config.POP_SIZE; ++i) {
        pop[i].decoded = false;  /*huh?*/
        for (j=0; j<g_size; ++j)
          pop[i].gene[j] = myInputStream.readFloat();
      }  /* end for-i loop */

      myFile.close();
    } catch (IOException e) {
      System.err.println(e);
      System.exit(1);
      }  /* end try/catch */

  }  /* end Load_pop */

/**********************************************************/


/*********************************************************/
/* the Load_partial method loads a saved population into */
/* the first NUM_HIDDEN spaces in pop.                   */
/*********************************************************/

  public static void Load_partial(Neuron pop[], String fname) {

    int i, j, tmp, g_size;
    FileInputStream myFile;
    DataInputStream myInputStream;

    try {
      myFile = new FileInputStream(fname);
      myInputStream = new DataInputStream(myFile);
      tmp = myInputStream.readInt(); /* read in 1st thing in file */
      if (tmp != Config.NUM_HIDDEN) {
        System.out.println("Error - population size specified does not match population size of file");
        System.exit(1);
      }  /* end if */

      g_size = myInputStream.readInt();
      for (i=0; i<Config.NUM_HIDDEN; ++i) {
        pop[i].decoded = false;  /*huh?*/
        for (j=0; j<g_size; ++j)
          pop[i].gene[j] = myInputStream.readFloat();
      }  /* end for-i loop */

      myFile.close();
    } catch (IOException e) {
//      System.err.println(e);
//      System.exit(1);
      }  /* end try/catch */

  }  /* end Load_partial */

/**********************************************************/


/*********************************************************/
/* Save_pop saves a neuron population pointed to by pop  */
/* into a binary file.                                   */
/*********************************************************/

  public static void Save_pop(Neuron pop[], String fname) {

    int i, j;
    FileOutputStream myFile;
    DataOutputStream myOutputStream;

    try {
      myFile = new FileOutputStream(fname);
      myOutputStream = new DataOutputStream(myFile);
      myOutputStream.writeInt(Config.POP_SIZE);
      myOutputStream.writeInt(Config.GENE_SIZE);

      for (i=0; i<Config.POP_SIZE; ++i) {
        for (j=0; j<Config.GENE_SIZE; ++j)
          myOutputStream.writeFloat(pop[i].gene[j]);
      }  /* end for-i loop */

      myFile.close();
    } catch (IOException e) {
      System.err.println(e);
      System.exit(1);
      }  /* end try/catch */

  }  /* end Save_pop */

/**********************************************************/


/**********************************************************/
/* Save_partial saves the first NUM_HIDDEN neurons in pop */
/* to a binary file.                                */
/**********************************************************/

  public static void Save_partial(Neuron pop[], String fname) {

    int i, j;
    FileOutputStream myFile;
    DataOutputStream myOutputStream;

    try {
      myFile = new FileOutputStream(fname);
      myOutputStream = new DataOutputStream(myFile);
      myOutputStream.writeInt(Config.NUM_HIDDEN);
      myOutputStream.writeInt(Config.GENE_SIZE);

      for (i=0; i<Config.NUM_HIDDEN; ++i) {
        for (j=0; j<Config.GENE_SIZE; ++j)
          myOutputStream.writeFloat(pop[i].gene[j]);
      }  /* end for-i loop */

      myFile.close();
    } catch (IOException e) {
      System.err.println(e);
      System.exit(1);
      }  /* end try/catch */

  }  /* end Save_partial */

/**********************************************************/


/*********************************************************/
/* Cluster_output.                                       */
/*********************************************************/

  public static void Cluster_output(Neuron pop[], String fname1, String fname2, int size) {

    int i, j, k, nino;
      nino = Config.NUM_INPUTS + Config.NUM_OUTPUTS;
    float weight[];
      weight = new float[nino];

    try {

      PrintWriter myFile1;
      myFile1  = new PrintWriter(new FileWriter(fname1));
      PrintWriter myFile2;
      myFile2  = new PrintWriter(new FileWriter(fname2));

      for(i=0;i<size;++i) {
        myFile2.println(i);
        for(j=0; j<nino; ++j)
          weight[j] = 0.0F;
        for(j=0; j<Config.GENE_SIZE; j+=2) { /*loop for # of connections in neuron*/
          k = (int) pop[i].gene[j];
          if (k >= 128)
            k = k % Config.NUM_INPUTS;
          else
            k = (k%Config.NUM_OUTPUTS) + Config.NUM_INPUTS;
          weight[k] += pop[i].gene[j+1];
        }  /* end for-j loop */
        for(j=0; j<nino; ++j)
          myFile1.print(weight[j] + " ");
        myFile1.println();
      }  /* end for-i loop */

    myFile1.close();
    myFile2.close();

    } catch (IOException e) {
      System.err.println(e);
      System.exit(1);
      }  /* end try/catch */

  }  /* end Cluster_output */

/**********************************************************/



}  /* end class Sane_Util */
