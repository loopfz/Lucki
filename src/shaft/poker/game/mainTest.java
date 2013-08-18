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
package shaft.poker.game;

import java.awt.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import shaft.poker.agent.IHandEvaluator;
import shaft.poker.agent.IHandRange;
import shaft.poker.agent.handevaluators.enumeration.PreflopPrecomputedEval;
import shaft.poker.agent.handevaluators.enumeration.FullEnumHandEval;
import shaft.poker.agent.handevaluators.enumeration.bucket.BucketEnumHandEval;
import shaft.poker.agent.handevaluators.enumeration.bucket.BucketEnum1CardHandEval;
import shaft.poker.agent.handranges.UniformRange;
import shaft.poker.agent.handranges.weightedrange.IFrequencyTable;
import shaft.poker.agent.handranges.weightedrange.frequencytable.GenericFrequencyTable;
import shaft.poker.game.components.DebugDeck;
import shaft.poker.factory.PokerFactory;

/**
 *
 * @author Thomas Schaffer <thomas.schaffer@epitech.eu>
 */
public class mainTest {
    public static void main(String[] args) {
       
        PokerFactory factory = new PokerFactory();
        
        ITable table = factory.newLimitTable();
        factory.addSimpleAgent();
        factory.addSimpleAgent();
        factory.addSimpleAgent();
        factory.addHumanPlayer();
        
        table.runGame(1000, 2000, 5, 10);
    }
}
