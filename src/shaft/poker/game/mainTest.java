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
import shaft.poker.game.factory.ComponentFactory;

/**
 *
 * @author Thomas Schaffer <thomas.schaffer@epitech.eu>
 */
public class mainTest {
    public static void main(String[] args) {
        /*for (Card c1 : Card.values()) {
            for (Card c2 : Card.values()) {
                System.out.println(c1.toString() + " & " + c2.toString());
            }
        }*/
        
        /*
        List<Card> hole = new ArrayList<>(2);
        List<Card> board = new ArrayList<>(5);
        
        DebugDeck deck = new DebugDeck();
        
        hole.add(deck.selectDraw(Card.Rank.ACE, Card.Suit.HEARTS));
        hole.add(deck.selectDraw(Card.Rank.KING, Card.Suit.SPADES));
        board.add(deck.selectDraw(Card.Rank.FOUR, Card.Suit.CLUBS));
        board.add(deck.selectDraw(Card.Rank.FIVE, Card.Suit.SPADES));
        board.add(deck.selectDraw(Card.Rank.JACK, Card.Suit.DIAMONDS));
        */

       /* hole.add(deck.drawCard());
        hole.add(deck.drawCard());
        board.add(deck.drawCard());
        board.add(deck.drawCard());
        board.add(deck.drawCard());*/
        //board.add(deck.drawCard());
        /*board.add(deck.selectDraw(Card.Rank.FOUR, Card.Suit.CLUBS));
        board.add(deck.selectDraw(Card.Rank.TEN, Card.Suit.CLUBS));
        board.add(deck.selectDraw(Card.Rank.KING, Card.Suit.CLUBS));*/
 
        /*
        System.out.println("------ BOARD ------");
        for (Card c : board) {
            System.out.println(c.toString());
        }
        System.out.println("------ HOLE CARDS ------");
        for (Card c : hole) {
            System.out.println(c.toString());
        }
        System.out.println("------");
        */
             
        /*
        IHandEvaluator eval = new BucketEnumHandEval();
        IHandEvaluator eval2 = new FullEnumHandEval();
        IHandEvaluator eval3 = new BucketEnum1CardHandEval();
        IHandRange range = new UniformRange();
        
        IFrequencyTable freq = new GenericFrequencyTable();
        
        double test = freq.estimatedStrength(ITable.ActionType.BET, 0, ITable.Round.TURN);
        
        System.out.println("Estimated EHS from freq: " + test);
        */

        /*for (int i = 0; i < 1000; i++) {
            eval3.compute(hole, board, range, 6);            
        }*/

        
       /* System.out.println("########## BUCKET EVAL #########");        
        eval.compute(hole, board, range, 2);
        System.out.println("########## FULL EVAL #########");
        eval2.compute(hole, board, range, 2);
        System.out.println("########## BUCKET 1-CARD EVAL #########");
        eval3.compute(hole, board, range, 2);
        */
        /*
        while (testDeck.deckSize() > 0) {
            try {
                ICard c1 = testDeck.drawCard();
                ICard c2 = testDeck.drawCard();
                
                System.out.println("Hand: " + c1.toString() + " & " + c2.toString());
                System.in.read();
            } catch (IOException ex) {
                Logger.getLogger(mainTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }*/
    }
}
