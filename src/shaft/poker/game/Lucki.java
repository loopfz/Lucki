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

import javax.swing.JFrame;
import shaft.poker.factory.PokerFactory;

/**
 *
 * @author Thomas Schaffer <thomas.schaffer@epitech.eu>
 */
public class Lucki {
    public static void main(String[] args) {
       
        PokerFactory factory = new PokerFactory();
        
        ITable table = factory.newLimitTable();
        factory.addSimpleAgent();
        factory.addSimpleAgent();
        factory.addSimpleAgent();
        factory.addSimpleAgent();
        factory.addSimpleAgent();
        factory.addTrainedNeuralNetAgent("NN_1st");
        factory.addTrainedNeuralNetAgent("NN_2nd");
        //factory.addHumanPlayer();
        JFrame gui = (JFrame) factory.addGUIHumanPlayer();
        
        table.runGame(1000, 1000, 5, 10);
    }
}
