package sane;
/* Domain.java */

/* This file was originally part of Sane.java.  The Evaluate_net */
/* function and all other domain-specific files reside here; in  */
/* theory, no other Sane files except Config.java should need to */
/* be modified in order to implement a new domain.               */
/* Cyndy Matuszek, 1998 */

import java.util.Random;
import shaft.poker.agent.PlayerAgent;
import shaft.poker.agent.bettingstrategy.NeuralNetStrategy;
import shaft.poker.factory.PokerFactory;
import shaft.poker.game.ITable;

public class Domain {

// Global variable declarations.  The folowing are EXAMPLES.
//    public static BufferedReader user_input;
//    public static Random myRandom = new Random();
//    public static String filename;
//    public int evaluations = 0;
//    public float global_best = -9999999.0F;	/* F for "float" (grr) */
//    public float best_average = -9999999.0F;
    private static PokerFactory _factory = new PokerFactory();
    private static ITable _table;
    private static NeuralNetStrategy _netStrat;
    private static PlayerAgent _netAgent;
    private static int n = 0;
    private static int genCount = 0;

/**********************************************************/
/* Evaluate_net is the user_defined method that is called */
/* from Sane.java for each network to determine its       */
/* fitness.                                               */
/**********************************************************/

  static float Evaluate_net(Network net) {
      
      if (_table == null) {
          init_poker_env();
      }
      
      _netStrat.setNetwork(net);
      
      _table.runGame(100, 5000, 5, 10);
      
      int win = _netAgent.winnings();
      int stack = _netAgent.stack();
      int profit = stack - 5000;
      if (profit < 0) {
          profit = 0;
      }
      
      float fitness = ((float) stack) + ((float) profit) * 100;
      
      System.out.println("eval " + ++n + ": " + fitness + " [" + stack + "]");
      if (n == Config.NUM_TRIALS) {
          System.out.println("Generation " + ++genCount + " done");
          n = 0;
      }

      return fitness;
 
  }  /* end Evaluate_net */

    private static void init_poker_env() {
        _table = _factory.newLimitTable();
        _factory.genericModel(false);
        _factory.addSimpleAgent();
        _factory.addSimpleAgent();
        _factory.addSimpleAgent();
        _factory.addSimpleAgent();
        _factory.addSimpleAgent();
        _factory.addSimpleAgent();
        _factory.addSimpleAgent();
        _netAgent = (PlayerAgent) _factory.addBlankNeuralNetAgent();
        _netStrat = (NeuralNetStrategy) _netAgent.bettingStrategy();
    }

/**********************************************************/

}  /* end class Domain */
