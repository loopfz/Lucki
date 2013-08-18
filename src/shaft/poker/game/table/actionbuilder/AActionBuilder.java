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
package shaft.poker.game.table.actionbuilder;

import shaft.poker.game.IAction;
import shaft.poker.game.ITable;
import shaft.poker.game.ITable.ActionType;
import shaft.poker.game.table.*;

/**
 *
 * @author Thomas Schaffer <thomas.schaffer@epitech.eu>
 */
public abstract class AActionBuilder implements IActionBuilder {
            
    protected int _toCall;
    protected int _sBlind;
    protected int _bBlind;
    protected int _potSize;
    protected int _plStack;
    
    @Override
    public void setContext(ITable table, IPlayerData plData) {
        _toCall = plData.amountToCall();
        _potSize = table.potSize();
        _sBlind = table.smallBlind();
        _bBlind = table.bigBlind();
        _plStack = plData.stack();
    }

    @Override
    public int minRaise() {
        return _bBlind;
    }
    
    @Override
    public IAction makeSBlind() {
        int amount = _sBlind;
        if (amount > _plStack) {
            amount = _plStack;
        }
        return new GameAction(ActionType.BET, amount);
    }
    
    @Override
    public IAction makeBBlind() {
        int amount = _bBlind - _sBlind;
        if (amount > _plStack) {
            amount = _plStack;
        }
        return new GameAction(ActionType.BET, amount);
    }

    @Override
    public IAction makeFold() {
        return new GameAction(ActionType.FOLD, 0);
    }
    
    @Override
    public IAction makeCall() {
        int amount = _toCall;
        if (_plStack < _toCall) {
            amount = _plStack;
        }
        return new GameAction(ITable.ActionType.CALL, amount);
    }
    
    @Override
    public IAction makeRaise(int amount) {
        if (amount > 0) {
            if (amount > minRaise() || amount == maxRaise()) {
                if (amount <= maxRaise()) {
                    return new GameAction(ITable.ActionType.BET, amount);
                }
            }
        }
        return null;
    }
}
