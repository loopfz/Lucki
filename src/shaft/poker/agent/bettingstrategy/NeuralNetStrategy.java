/*
 * The MIT License
 *
 * Copyright 2013 Thomas Schaffer <thomas.schaffer@epitech.eu>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package shaft.poker.agent.bettingstrategy;

import java.util.List;
import sane.Network;
import sane.Sane_NN;
import shaft.poker.agent.*;
import shaft.poker.game.*;
import shaft.poker.game.table.*;

/**
 *
 * @author Thomas Schaffer <thomas.schaffer@epitech.eu>
 */
public class NeuralNetStrategy implements IBettingStrategy, IGameEventListener {
    
    private Network _net;
    private boolean _mainBetter;
    private double _handImage;
    
    public NeuralNetStrategy(ITable table) {
        table.registerEventListener(this);
    }

    @Override
    public IAction action(ITable table, List<Card> holeCards, IPlayerData plData, IActionBuilder actionBuild, IHandEvaluator eval) {
        
        // Effective Hand Strength
        _net.input[0] = (float) eval.rawHandStrength();
        
        // Positive potential, treated separately from RawStr to encourage the nets to pick up on things like potential vs pot odds
        _net.input[1] = (float) eval.posPotential();
        
        // Negative Potential
        _net.input[2] = (float) eval.negPotential();
        
        // Pot Odds
        _net.input[3] = (float) plData.potOdds(table.potSize());
        
        // Position based on the dealer button (small blind = 1, big blind = 2, UTG = 3, ...)
        // Ratio by number active players to achieve a measure [0..1] with 1.0 being last position
        // _net.input[4] = ((float) plData.position()) / ((float) table.numberActivePlayers());
        // Discarded, redundant (See proportion of active players which have already acted, better measure)
        
        // Proportion of the players which are still active
        _net.input[4] = ((float) table.numberActivePlayers()) / ((float) table.numberPlayers());
        
        // Proportion of the active players which have already acted
        _net.input[5] = ((float) (table.numberActivePlayers() - table.numberPlayersToAct())) / ((float) table.numberActivePlayers());
        
        // Number of bets in the round, maxed at 2
        int numBets = table.numberBets();
        if (numBets > 2) {
            numBets = 2;
        }
        _net.input[6] = ((float) numBets) / 2.0f;
        
        // Image at the table
        // Measure ranging from [0..1], 0.0 being weakest and 1.0 strongest
        // Compound measure computed from boolean value _mainBetter each round
        // with a decreasing weight the older the round is
        // At turn [0.75] = 0.25(mainBetter preflop) + 0.5(mainBetter flop)
        // At river [0.375] = 0.125(mainBetter preflop) + 0.25(mainBetter flop)
        // etc.
        _net.input[7] = (float) _handImage;
        
        // Proportion of the pot contributed by the agent
        _net.input[8] = ((float) plData.totalMoneyInPot()) / ((float) table.potSize());
        
        // Current round
        // Measure ranging from [0..1] with 0.0 being preflop and river being 1.0
        _net.input[9] = ((float) table.round().ordinal()) / 3.0f;
        
        Sane_NN.Activate_net(_net);
        
        if (_net.winner == 2 && table.numberBets() < table.maxBets()) {
            _mainBetter = true;
            return actionBuild.makeRaise(actionBuild.minRaise());
        }
        else if (_net.winner >= 1 || plData.amountToCall() == 0) {
            _mainBetter = false;
            return actionBuild.makeCall();
        }
        return actionBuild.makeFold();
    }
    
    public void setNetwork(Network net) {
        _net = net;
    }

    @Override
    public void roundBegin(ITable table, ITable.Round r) {
        _mainBetter = false;
    }

    @Override
    public void roundEnd(ITable table, ITable.Round r) {
        _handImage /= 2;
        if (_mainBetter) {
            _handImage += 0.5;
        }
    }

    @Override
    public void newHand(ITable table) {
        _handImage = 0;
    }

    @Override
    public void newGame(ITable table, int stackSize, int sBlind, int bBlind, int numPlayers) {
        
    }

    @Override
    public void winHand(ITable table, IPlayerData data, int amount) {
        
    }
    
}
