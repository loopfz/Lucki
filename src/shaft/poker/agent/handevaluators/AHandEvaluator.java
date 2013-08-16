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

import shaft.poker.agent.IHandEvaluator;

/**
 *
 * @author Thomas Schaffer <thomas.schaffer@epitech.eu>
 */
public abstract class AHandEvaluator implements IHandEvaluator {
    protected double _handStr;
    protected double _posPot;
    protected double _negPot;
    protected int _numPl;
    
    @Override
    public double effectiveHandStrength() {
        return _handStr + (1 - _handStr) * _posPot;
    }
    
    @Override
    public double rawHandStrength() {
        return _handStr;
    }
    
    @Override
    public double posPotential() {
        return _posPot;
    }

    @Override
    public double negPotential() {
        return _negPot;
    }
}
