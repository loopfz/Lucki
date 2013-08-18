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
public class PreflopHandGroups extends AHandEvaluator {

    private static int[][] _handGroups = {
        {1, 1, 2, 2, 3, 5, 5, 5, 5, 5, 5, 5, 5},
        {2, 1, 2, 3, 4, 6, 7, 7, 7, 7, 7, 7, 7},
        {3, 4, 1, 3, 4, 5, 7, 9, 9, 9, 9, 9, 9},
        {4, 5, 5, 1, 3, 4, 6, 8, 9, 9, 9, 9, 9},
        {6, 6, 6, 5, 2, 4, 5, 7, 9, 9, 9, 9, 9},
        {8, 8, 8, 7, 7, 3, 4, 5, 8, 9, 9, 9, 9},
        {9, 9, 9, 8, 8, 7, 4, 5, 6, 8, 9, 9, 9},
        {9, 9, 9, 9, 9, 9, 8, 5, 5, 6, 8, 9, 9},
        {9, 9, 9, 9, 9, 9, 9, 8, 6, 7, 7, 9, 9},
        {9, 9, 9, 9, 9, 9, 9, 9, 8, 6, 6, 7, 9},
        {9, 9, 9, 9, 9, 9, 9, 9, 9, 8, 7, 7, 8},
        {9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 7, 8},
        {9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 7}
    };
    
    private static int[] _groupCount = {
        5, 5, 6, 8, 17, 10, 19, 15, 84
    };

    @Override
    public void compute(List<Card> holecards, List<Card> board, IHandRange range, int numPlayers) {

        Card c1 = holecards.get(0);
        Card c2 = holecards.get(1);
        
        if (c1.rank().ordinal() < c2.rank().ordinal()) {
            Card tmp = c1;
            c1 = c2;
            c2 = tmp;
        }
        
        if (c1.suit() == c2.suit()) {
            Card tmp = c1;
            c1 = c2;
            c2 = tmp;
        }
        
        int group = _handGroups[c1.rank().ordinal()][c2.rank().ordinal()];
        double count = 0;
        
        for (int i = 0; i < group - 1; i++) {
            count += _groupCount[i];
        }
        
        count += ((double) _groupCount[group - 1]) / 2.0;
        
        _handStr = 1.0 - count / 169;
        _handStr = Math.pow(_handStr, numPlayers);
    }
}
