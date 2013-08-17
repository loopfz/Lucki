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
package shaft.poker.agent.handranges.weightedrange;

import java.util.List;
import shaft.poker.agent.IHandEvaluator;
import shaft.poker.agent.IHandRange;
import shaft.poker.agent.enumerationtools.EnumCandidate;
import shaft.poker.agent.handranges.IPlayerRange;
import shaft.poker.game.Card;
import shaft.poker.game.Card.*;
import shaft.poker.game.table.IPlayerActionListener;
import shaft.poker.game.ITable;
import shaft.poker.game.ITable.*;
import shaft.poker.game.table.IPlayerContext;
import shaft.poker.game.table.IGameEventListener;

/**
 *
 * @author Thomas Schaffer <thomas.schaffer@epitech.eu>
 */
public class PlayerWTRange implements IPlayerRange, IPlayerActionListener, IGameEventListener {

    private String _playerId;
    private IWeightTable _wt;
    private IFrequencyTable _freq;
    private IHandEvaluator _eval;
    private IHandRange _compositeRange;
    private static final double VARIANCE_FACTOR = 0.4;
    private static final double LOW_WEIGHT = 0.01;
    private static final double HIGH_WEIGHT = 1.00;
    private boolean _dead;
    private boolean _active;
    
    public PlayerWTRange(String playerId, IWeightTable wt, IFrequencyTable freq, IHandEvaluator eval, IHandRange compRange, ITable table) {
        _playerId = playerId;
        _wt = wt;
        _freq = freq;
        _eval = eval;
        _compositeRange = compRange;
        table.registerListenerForPlayer(playerId, this);
        table.registerEventListener(this);
    }
    
    private void reweight(double threshold, List<Card> board, int numPlayers, double potOdds) {
        double variance;
        
        variance = VARIANCE_FACTOR * (1 - threshold);
        
        List<EnumCandidate> holeCards = EnumCandidate.buildCandidates(null, board);
        
        _wt.initReweight();
        
        for (EnumCandidate holeCandidate : holeCards) {
            _eval.compute(holeCandidate.cards(), board, _compositeRange, numPlayers);
            
            if (board.size() < 5 && _eval.posPotential() >= potOdds) {
                continue;
            }
            
            double reweight;
            
            if (threshold < variance) {
                double r = 1.0 - (2 * threshold / (threshold + variance));
                reweight = r + (_eval.effectiveHandStrength() / (threshold + variance));
            }
            else {
                reweight = (_eval.effectiveHandStrength() - threshold + variance) / (2 * variance);                
            }
            
            if (reweight < LOW_WEIGHT) {
                reweight = LOW_WEIGHT;
            }
            else if (reweight > HIGH_WEIGHT) {
                reweight = HIGH_WEIGHT;
            }
            
            double weight = _wt.getWeight(holeCandidate.cards().get(0), holeCandidate.cards().get(1));
            weight *= reweight;
            if (weight < LOW_WEIGHT) {
                weight = LOW_WEIGHT;
            }
            
            if (holeCandidate.generic()) {
                _wt.setWeight(holeCandidate.cards().get(0).rank(), holeCandidate.cards().get(1).rank(), weight);
            }
            else {
                _wt.suspendSetWeight(holeCandidate.cards().get(0), holeCandidate.cards().get(1), weight);
            }
        }
        _wt.consolidate();
    }
    
    private void triggerReweight(ITable table, ActionType a, double potOdds) {
        double threshold = _freq.estimatedStrength(a, table.numberBets(), table.round());
        
        reweight(threshold, table.board(), table.numberActivePlayers(), potOdds);
    }

    @Override
    public double handWeight(Card c1, Card c2) {
        return _wt.getWeight(c1, c2);
    }

    @Override
    public void gameAction(ITable table, String id, IPlayerContext plContext, ActionType type, int amount) {
        if (type != ActionType.FOLD) {
            if (type != ActionType.CALL || amount > 0) {
                triggerReweight(table, type, plContext.potOdds());                
            }
        }
        else {
            _active = false;
        }
    }

    @Override
    public void leave(ITable table, String id) {
        //table.unregisterListenerForPlayer(_playerId, this);
        _dead = true;
        _active = false;
    }

    @Override
    public void roundBegin(ITable table, Round r) {
        if (!_dead) {
            _active = true;
        }
    }

    @Override
    public void roundEnd(ITable table, Round r) {
        
    }

    @Override
    public boolean activePlayer() {
        return _active;
    }

    @Override
    public void newHand(ITable table) {
        
    }

    @Override
    public void newGame(ITable table, int stackSize, int sBlind, int bBlind, int numPlayers) {
        _active = true;
        _dead = false;
    }

}
