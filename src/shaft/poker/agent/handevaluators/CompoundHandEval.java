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
package shaft.poker.agent.handevaluators;

import java.util.List;
import shaft.poker.agent.IHandEvaluator;
import shaft.poker.agent.IHandRange;
import shaft.poker.game.Card;

/**
 *
 * @author Thomas Schaffer <thomas.schaffer@epitech.eu>
 */
public class CompoundHandEval implements IHandEvaluator {
    private IHandEvaluator _preflop;
    private IHandEvaluator _postflop;
    private IHandEvaluator _lastCall;
    
    public CompoundHandEval(IHandEvaluator preflop, IHandEvaluator postflop) {
        _preflop = preflop;
        _postflop = postflop;
    }

    @Override
    public void compute(List<Card> holecards, List<Card> board, IHandRange range, int numPlayers) {
        _lastCall = (board.isEmpty() ? _preflop : _postflop);
        _lastCall.compute(holecards, board, range, numPlayers);
    }

    @Override
    public double effectiveHandStrength() {
        return _lastCall.effectiveHandStrength();
    }
    
    @Override
    public double rawHandStrength() {
        return _lastCall.rawHandStrength();
    }
    
    @Override
    public double posPotential() {
        return _lastCall.posPotential();
    }

    @Override
    public double negPotential() {
        return _lastCall.negPotential();
    }
}
