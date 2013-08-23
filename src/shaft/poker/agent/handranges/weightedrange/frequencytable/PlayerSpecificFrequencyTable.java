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
package shaft.poker.agent.handranges.weightedrange.frequencytable;

import java.util.List;
import shaft.poker.game.table.IPlayerData;
import shaft.poker.agent.handranges.weightedrange.IFrequencyTable;
import shaft.poker.game.table.IPlayerActionListener;
import shaft.poker.game.ITable;
import shaft.poker.game.ITable.*;
import shaft.poker.game.table.*;

/**
 *
 * @author Thomas Schaffer <thomas.schaffer@epitech.eu>
 */
public class PlayerSpecificFrequencyTable implements IFrequencyTable, IPlayerActionListener, IGameEventListener {
    
    // Dimensions: [BettingRound][LastAction][NumBets][Fold/Call/Bet]
    private int[][][][] _freqs;
    IFrequencyTable _defaultFreqs;
    private String _playerId;
    private static final int MINIMUM_DATA = 20;
    private ActionType _lastAction;
    
    public PlayerSpecificFrequencyTable(String playerId, ITable table) {
        _freqs = new int[4][4][3][3];
        _defaultFreqs = new GenericFrequencyTable();
        _playerId = playerId;
        table.registerPriorityListenerForPlayer(playerId, this);
        table.registerEventListener(this);
        //TODO persistence
    }

    @Override
    public double estimatedStrength(ActionType a, int numBets, Round bettingRound) {
        int total = 0;
        
        if (numBets > 2) {
            numBets = 2;
        }
        
        int lastIdx = ActionType.values().length;
        if (_lastAction != null) {
            lastIdx = _lastAction.ordinal();
        }
        
        for (int c : _freqs[bettingRound.ordinal()][lastIdx][numBets]) {
            total += c;
        }
        
        double actualFreq = 0.0;
        int count = total;

        for (int i = 2; i >= a.ordinal(); i--) {
            count -= _freqs[bettingRound.ordinal()][lastIdx][numBets][i];
        }
        
        if (total > 0) {
            actualFreq = (double) count / (double) total;
        }
        
        // Linearly interpolate with generic default frequency table if not enough data has been collected
        if (total < MINIMUM_DATA) {
            double defFreq = _defaultFreqs.estimatedStrength(a, numBets, bettingRound);
            actualFreq = (actualFreq * (total / ((double) MINIMUM_DATA)))
                    + (defFreq * ((MINIMUM_DATA - total) / ((double) MINIMUM_DATA)));
        }
        
        return actualFreq;
    }
    
    private void doIncrease(int numBets, Round r, ActionType a) {
        if (numBets > 2) {
            numBets = 2;
        }
        
        int lastIdx = ActionType.values().length;
        if (_lastAction != null) {
            lastIdx = _lastAction.ordinal();
        }
        
        _freqs[r.ordinal()][lastIdx][numBets][a.ordinal()]++;
        _lastAction = a;
    }

    @Override
    public void gameAction(ITable table, IPlayerData plData, ActionType type, int amount) {
        doIncrease(table.numberBets(), table.round(), type);
    }

    @Override
    public void leave(ITable table, IPlayerData plData) {
        //table.unregisterPriorityListenerForPlayer(_playerId, this);
    }

    @Override
    public void roundBegin(ITable table, Round r) {
        _lastAction = null;
    }

    @Override
    public void roundEnd(ITable table, Round r) {
        
    }

    @Override
    public void newHand(ITable table) {
        
    }

    @Override
    public void newGame(ITable table, int stackSize, int sBlind, int bBlind, List<String> players) {
        _freqs = new int[4][4][3][3];
        for (int i = 0; i < _freqs.length; i++) {
            for (int j = 0; j < _freqs[i].length; j++) {
                for (int k = 0; k < _freqs[i][j].length; k++) {
                    for (int l = 0; l < _freqs[i][j][k].length; l++) {
                        _freqs[i][j][k][l] = 0;
                    }
                }
            }
        }
    }

    @Override
    public void winHand(ITable table, IPlayerData data, int amount) {
        
    }
    
}