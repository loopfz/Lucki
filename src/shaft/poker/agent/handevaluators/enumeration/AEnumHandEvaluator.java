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
package shaft.poker.agent.handevaluators.enumeration;

import shaft.poker.agent.handevaluators.AHandEvaluator;

/**
 *
 * @author Thomas Schaffer <thomas.schaffer@epitech.eu>
 */
public abstract class AEnumHandEvaluator extends AHandEvaluator {
    
    protected double[][] HP = new double[3][3];
    protected double[] HPTotal = new double[3];
    
    protected void initArrays() {
        for (int i = 0; i < HP.length; i++) {
            for (int j = 0; j < HP[i].length; j++) {
                HP[i][j] = 0;
            }
        }
        
        for (int i = 0; i < HPTotal.length; i++) {
            HPTotal[i] = 0;
        }
    }
    
    protected void calculatePotentials() {
        _posPot = (HP[0][2] + HP[0][1] / 2 + HP[1][2] / 2) / (HPTotal[0] + HPTotal[1] / 2);
        _negPot = (HP[2][0] + HP[1][0] / 2 + HP[2][1] / 2) / (HPTotal[2] + HPTotal[1] / 2);
        _handStr = (HPTotal[2] + HPTotal[1] / 2) / (HPTotal[0] + HPTotal[1] + HPTotal[2]);
        _handStr = Math.pow(_handStr, _numPl - 1);
    }
    
    protected void printData() {
        System.out.println("HPTotal [AHEAD]:" + HPTotal[2] + " | [TIED}: " + HPTotal[1] + " | [BEHIND]: " + HPTotal[0]);
        System.out.println("HP[BEHIND][BEHIND]: " + HP[0][0]);
        System.out.println("HP[BEHIND][TIED]: " + HP[0][1]);
        System.out.println("HP[BEHIND][AHEAD]: " + HP[0][2]);
        System.out.println("------");
        System.out.println("HP[TIED][BEHIND]: " + HP[1][0]);
        System.out.println("HP[TIED][TIED]: " + HP[1][1]);
        System.out.println("HP[TIED][AHEAD]: " + HP[0][2]);
        System.out.println("-------");
        System.out.println("HP[AHEAD][BEHIND]: " + HP[2][0]);
        System.out.println("HP[AHEAD][TIED]: " + HP[2][1]);
        System.out.println("HP[AHEAD][AHEAD]: " + HP[0][0]);
        
        System.out.println("PPot: " + _posPot);
        System.out.println("NPot: " + _negPot);
        System.out.println("RawStr: " + _handStr);
        
        System.out.println("EHS: " + effectiveHandStrength());
    }
}
