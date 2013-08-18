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


import shaft.poker.game.table.*;
import java.util.ArrayList;
import java.util.List;
import shaft.poker.agent.IHandRange;
import shaft.poker.agent.handranges.IPlayerRange;
import shaft.poker.game.*;
import shaft.poker.game.ITable.*;

/**
 *
 * @author Thomas Schaffer <thomas.schaffer@epitech.eu>
 */
public class CompositeFieldRange implements IHandRange, IPlayerActionListener {
    
    private IWeightTable _wt;
    private List<IPlayerRange> _fieldRanges;
    
    public CompositeFieldRange(ITable table, IWeightTable wt) {
        _wt = wt;
        _fieldRanges = new ArrayList<>(10);
        table.registerListenAllPlayerEvents(this);
    }
    
    public void addRange(IPlayerRange range) {
        _fieldRanges.add(range);
        triggerReweight();
    }

    @Override
    public double handWeight(Card c1, Card c2) {
        return _wt.getWeight(c1, c2);
    }

    @Override
    public void gameAction(ITable table, IPlayerData plData, ActionType type, int amount) {
        if (type != ActionType.CALL || amount > 0) {
            triggerReweight();
        }
    }

    @Override
    public void leave(ITable table, IPlayerData plData) {
        
    }

    private void triggerReweight() {
        for (Card c1 : Card.values()) {
            for (Card c2 : Card.values()) {
                if (c1 != c2) {
                    IPlayerRange max = null;
                    for (IPlayerRange range : _fieldRanges) {
                        if (range.activePlayer()) {
                            if (max == null || max.handWeight(c1, c2) < range.handWeight(c1, c2)) {
                                max = range;
                            }
                        }
                    }
                    if (max != null) {
                        _wt.setWeight(c1, c2, max.handWeight(c1, c2));
                    }
                }
            }
        }
    }
}
