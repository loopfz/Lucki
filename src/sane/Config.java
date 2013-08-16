package sane;
/* Config.java */

/** all the static values in sane.h; eventually will be dynamic */

public class Config {

// random
//=====================================================================
public static int SAVE_CYCLE = 25;	/* how often pop. is saved */

public static int GENE_SIZE = 24;	/* number of connections/neuron, * 2 */
public static int POP_SIZE = 1000;	/* total number of neurons */
public static int NUM_HIDDEN = 12;	/* # of neurons in a network */
public static int NUM_BREED = 200;	/* # of breeding neurons */
public static int ELITE = 200;
public static int MUT_RATE = 2;		/* mutation rate in 10%'s */
public static int NUM_TRIALS = 100;	/* the number of networks created per
					   generation. must be >= TOP_NETS. */

// These next definitions pertain to the network level evolution.
//=====================================================================
public static int TOP_NETS = 100;	/* # of networks kept track of per
					   generation */
public static int TOP_NETS_BREED = 20;	/* how many of those are breeders;
					   should be < one-third of TOP_NETS */

// some definitions for our neural networks
//=====================================================================
public static int NUM_INPUTS = 2;	/* # of inputs */
public static int NUM_OUTPUTS = 1;	/* # of output units */
public static int MAX_CONNECTIONS = 400; /* max # connections/unit */

// These were originally part of skeleton-main.c, but are really
// config settings
//=====================================================================
public static int NUM_ATTEMPTS = 1;		/* # of times network is
						   evaluated */
public static boolean RANDOM_POP = true;	/* start w/ random pop? */


// next set of definitions are for local learning
//=====================================================================
public static float ETA = 0.1F;
public static float ALPHA = 0.9F;
public static float DECAY_ETA = 0;	/* float? */
public static boolean CRITIC = false;	/* use an AHC to train networks */
public static boolean LOCAL_LEARN = false; 	/* use your own learning
                                                   rule to train */
public static boolean LAMARCKIAN = false;	/* write learned info back
						   to chromosome */
public static int LL_LOWER_LEVEL = -10;	/* the performance drops or increases*/
public static int LL_UPPER_LEVEL = 10;	/* at which reinforcement happens */
public static boolean MAXIMIZE_R = false;	/* trying to max (true) or min (false) reward? */
public static int LOCAL_STEPS = 500;	/* defines the # of actions rewards
					   should be evaluated over for
					   local_learning. */


// some definitions for the population seeding routines
//=====================================================================
public static boolean SEED_FLAG = false;	/* whether to seed */
public static int NUM_SEED_EXAMPLES = 100;	/* # of examples in seed.txt */
public static int SEED_NETS = 25;		/* # of networks to seed */
public static int SEED_TRIALS = 100;		/* # of bp learning cycles
                                                   per net */

}
