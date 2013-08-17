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
package shaft.poker.game.table;

import java.util.ArrayList;
import java.util.List;
import shaft.poker.game.IPlayer;
import shaft.poker.game.ITable;
import shaft.poker.game.ITable.*;

/**
 *
 * @author Thomas Schaffer <thomas.schaffer@epitech.eu>
 */
public class PlayerData implements IGameEventListener {
    private IPlayer _pl;
    private List<IPlayerActionListener> _listeners;
    private List<IPlayerActionListener> _prioListeners;
    private int _moneyInPotForRound;
    private int _totalMoneyInPot;
    private int _stack;
    private int _maxWin;
    
    public PlayerData(IPlayer pl, ITable table) {
        _pl = pl;
        _listeners = new ArrayList<>(10);
        _prioListeners = new ArrayList<>(10);
        table.registerEventListener(this);
    }
    
    public IPlayer player() {
        return _pl;
    }
    
    public List<IPlayerActionListener> listeners() {
        return _listeners;
    }
    
    public List<IPlayerActionListener> prioListeners() {
        return _prioListeners;
    }
    
    public int stack() {
        return _stack;
    }
    
    public boolean allIn() {
        return _stack > 0;
    }
    
    private void addMoneyInPot(int money) {
        _moneyInPotForRound += money;
        _totalMoneyInPot += money;
    }
    
    public void addToStack(int money) {
        _stack += money;
    }
    
    public int placeMoney(int amount) {
        if (amount > _stack) {
            amount = _stack;
        }
        addMoneyInPot(amount);
        _stack -= amount;
        
        return amount;
    }
    
    public int maxWin() {
        return _maxWin;
    }
    
    public void updateMaxWin(PlayerData pl, int amount) {
        int diff = pl._totalMoneyInPot - _totalMoneyInPot;
        if (diff >= 0) {
            if (amount - diff > 0) {
                _maxWin += amount - diff;
            }
        }
    }
    
    public int winPot(int winnings) {
        if (winnings > _maxWin) {
            winnings = _maxWin;
        }
        _stack += winnings;
        return winnings;
    }
    
    public int leftToCall(int toCall) {
        return toCall - _moneyInPotForRound;
    }
    
    public double potOdds(int toCall, int potSize) {
        int amtLeft = leftToCall(toCall);
        return (((double) amtLeft) / ((double) (amtLeft + potSize)));
    }
    
    @Override
    public void roundBegin(ITable table, Round r) {
        _moneyInPotForRound = 0;
    }

    @Override
    public void roundEnd(ITable table, ITable.Round r) {
        
    }

    @Override
    public void newHand(ITable table) {
        _maxWin = 0;
        _totalMoneyInPot = 0;
    }

    @Override
    public void newGame(ITable table, int stackSize, int sBlind, int bBlind, int numPlayers) {
        _stack = stackSize;
    }
}