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
import shaft.poker.agent.IHandRange;
import shaft.poker.game.Card;

/**
 *
 * @author Thomas Schaffer <thomas.schaffer@epitech.eu>
 */
public class PreflopStatsEval extends AHandEvaluator {
    
    // Dimensions: [Rank card1][Rank card2]
    // Value = Hand group as defined by Sklansky & Malmuth
    private int[][] _values = {
        {1, 1, 2, 2, 3, 5, 5, 5, 5, 5, 5, 5, 5},
        {2, 1, 2, 3, 4, 6, 7, 7, 7, 7, 7, 7, 7},
        {3, 4, 1, 3, 4, 5, 7, 0, 0, 0, 0, 0, 0},
        {4, 5, 5, 1, 3, 4, 6, 8, 0, 0, 0, 0, 0},
        {6, 6, 6, 5, 2, 4, 5, 7, 0, 0, 0, 0, 0},
        {8, 8, 8, 7, 7, 3, 4, 5, 8, 0, 0, 0, 0},
        {0, 0, 0, 8, 8, 7, 4, 5, 6, 8, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 8, 5, 5, 6, 8, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 8, 6, 7, 7, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 8, 6, 6, 7, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 7, 7, 8},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7, 8},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7}
    };
    
        private int[][] _rank = {
        {1, 4, 5, 8, 10, 19, 22, 29, 31, 25, 30, 32, 0},
        {11, 2, 7, 9, 14, 23, 37, 0, 0, 0, 0, 0, 0},
        {18, 20, 3, 13, 15, 26, 0, 0, 0, 0, 0, 0, 0},
        {28, 33, 34, 6, 16, 24, 0, 0, 0, 0, 0, 0, 0},
        {38, 0, 0, 0, 12, 21, 36, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 17, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 27, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 35, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
    
    private double[] groupCount = {
        0, 2.5, 7.5, 13, 20, 
    };
    
    @Override
    public void compute(List<Card> holecards, List<Card> board, IHandRange range, int numPlayers) {
        
    }
    
}
