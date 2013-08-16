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

import shaft.poker.agent.handranges.weightedrange.IFrequencyTable;
import shaft.poker.game.ITable;
import shaft.poker.game.ITable.*;

/**
 *
 * @author Thomas Schaffer <thomas.schaffer@epitech.eu>
 */
public class GenericFrequencyTable implements IFrequencyTable {
    
    // Expert data used by University of Alberta's Poki implementation
    private static final double[][] _default = {
        {0.0, 0.5, 0.5},
        {0.5, 0.3, 0.2},
        {0.7, 0.2, 0.1}
    };
    
    @Override
    public double estimatedStrength(ActionType a, int numBets, Round bettingRound) {
        if (numBets > 2) {
            numBets = 2;
        }
        
        double ret = 1.0;
        for (int i = 2; i >= a.ordinal(); i--) {
            ret -= _default[numBets][i];
        }
        
        return ret;
    }
}
