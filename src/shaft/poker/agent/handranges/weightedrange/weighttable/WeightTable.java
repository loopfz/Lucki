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
package shaft.poker.agent.handranges.weightedrange.weighttable;

import java.util.ArrayList;
import java.util.List;
import shaft.poker.agent.handranges.weightedrange.IWeightTable;
import shaft.poker.game.Card;
import shaft.poker.game.Card.*;
import shaft.poker.game.table.IGameEventListener;
import shaft.poker.game.ITable;
import shaft.poker.game.ITable.*;
import shaft.poker.game.table.IPlayerData;

/**
 *
 * @author Thomas Schaffer <thomas.schaffer@epitech.eu>
 */
public class WeightTable implements IWeightTable, IGameEventListener {
    
    // Dimensions: [Rank1][Suit1][Rank2][Suit2][Weight]
    private double[][][][] _table1;
    private double[][][][] _table2;
    
    private double[][][][] _tableFrom;
    private double[][][][] _tableTo;
    
    List<WeightChange> _delayedChanges;
    
    public WeightTable(ITable table) {
        _table1 = new double[Rank.values().length][Suit.values().length][Rank.values().length][Suit.values().length];
        _table2 = new double[Rank.values().length][Suit.values().length][Rank.values().length][Suit.values().length];

        _delayedChanges = new ArrayList<>(100);
        
        table.registerEventListener(this);
    }
    
    private void setWeight(Rank r1, Suit s1, Rank r2, Suit s2, double weight) {
        _tableTo[r1.ordinal()][s1.ordinal()][r2.ordinal()][s2.ordinal()] = weight;
    }

    @Override
    public double getWeight(Card c1, Card c2) {
        return _tableFrom[c1.rank().ordinal()][c1.suit().ordinal()][c2.rank().ordinal()][c2.suit().ordinal()];
    }

    @Override
    public void suspendSetWeight(Card c1, Card c2, double weight) {
        _delayedChanges.add(new WeightChange(c1, c2, weight));
    }
    
    @Override
    public void setWeight(Card c1, Card c2, double weight) {
        setWeight(c1.rank(), c1.suit(), c2.rank(), c2.suit(), weight);
    }

    @Override
    public void setWeight(Rank r1, Rank r2, double weight) {
        for (Suit s1 : Suit.values()) {
            for (Suit s2 : Suit.values()) {
                if (r1 != r2 || s1 != s2) {
                    setWeight(r1, s1, r2, s2, weight);
                }
            }
        }
    }

    @Override
    public void initReweight() {
        _tableFrom = _table1;
        _tableTo = _table2;
    }

    @Override
    public void consolidate() {
        _tableFrom = _table2;
        
        for (WeightChange change : _delayedChanges) {
            setWeight(change.card1(), change.card2(), change.newWeight());
        }
        
        _delayedChanges.clear();
    }

    @Override
    public void roundBegin(ITable table, Round r) {
        if (r == Round.PREFLOP) {
            for (double[][][] arr1 : _table1) {
                for (double[][] arr2 : arr1) {
                    for (double[] arr3 : arr2) {
                        for (int i = 0; i < arr3.length; i++) {
                            arr3[i] = 1.0;
                        }
                    }
                }
            }
        }

        _tableFrom = _table1;
        _tableTo = _table2;
    }
    
    @Override
    public void roundEnd(ITable table, ITable.Round r) {
        double[][][][] tmp = _table1;
        _table1 = _table2;
        _table2 = tmp;
    }

    @Override
    public void newHand(ITable table) {
        
    }

    @Override
    public void newGame(ITable table, int stackSize, int sBlind, int bBlind, List<String> players) {
        
    }

    @Override
    public void winHand(ITable table, IPlayerData data, int amount) {
        
    }
    
}
