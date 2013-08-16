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
package shaft.poker.agent;

import java.util.ArrayList;
import java.util.List;
import shaft.poker.game.Card;
import shaft.poker.game.IAction;
import shaft.poker.game.IPlayer;
import shaft.poker.game.ITable;
import shaft.poker.game.table.IPlayerContext;
import shaft.poker.game.table.IGameEventListener;

/**
 *
 * @author Thomas Schaffer <thomas.schaffer@epitech.eu>
 */
public class PlayerAgent implements IPlayer, IGameEventListener {
    
    private String _playerId;
    private IHandEvaluator _eval;
    private IHandRange _compositeRange;
    private IBettingStrategy _strat;
    private List<Card> _holeCards;
    
    public PlayerAgent(ITable table, String id, IHandEvaluator eval, IHandRange fieldRange, IBettingStrategy strat) {
        _playerId = id;
        _eval = eval;
        _compositeRange = fieldRange;
        _strat = strat;
        _holeCards = new ArrayList<>(2);
        table.registerEventListener(this);
    }

    @Override
    public String id() {
        return _playerId;
    }

    @Override
    public void dealCard(Card c) {
        _holeCards.add(c);
    }
    
    @Override
    public List<Card> holeCards() {
        return _holeCards;
    }

    @Override
    public IAction action(ITable table, IPlayerContext plContext) {
        _eval.compute(_holeCards, table.board(), _compositeRange, table.numberActivePlayers());
        
        return _strat.action(table, plContext, _eval.effectiveHandStrength(), _eval.negPotential());
    }

    @Override
    public void roundBegin(ITable table, ITable.Round r) {
        
    }

    @Override
    public void roundEnd(ITable table, ITable.Round r) {
        
    }

    @Override
    public void newHand(ITable table) {
        _holeCards.clear();
    }

    @Override
    public void newGame(ITable table, int stackSize) {
        
    }
    
}
