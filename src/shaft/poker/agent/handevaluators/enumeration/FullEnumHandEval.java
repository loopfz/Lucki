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

import java.util.ArrayList;
import java.util.List;
import shaft.poker.agent.IHandRange;
import shaft.poker.agent.handevaluators.AHandEvaluator;
import shaft.poker.game.Card;
import shaft.poker.game.IHand;
import shaft.poker.factory.PokerFactory;

/**
 *
 * @author Thomas Schaffer <thomas.schaffer@epitech.eu>
 */
public class FullEnumHandEval extends AEnumHandEvaluator {
    
    @Override
    public void compute(List<Card> holecards, List<Card> board, IHandRange range, int numPlayers) {
        initArrays();
        
        List<Card> boardCpy = new ArrayList(board);
        
        IHand ownCurrentHand = PokerFactory.buildHand(holecards, board);
        
        List<Card> c1Cand = new ArrayList(Card.values());
        for (Card c : holecards) {
            c1Cand.remove(c);
        }
        for (Card c : board) {
            c1Cand.remove(c);
        }
        
        List<Card> c2Cand = new ArrayList<>(c1Cand);
        
        for (Card c1 : c1Cand) {
            c2Cand.remove(c1);
            
            for (Card c2 : c2Cand) {
                List<Card> candCards = new ArrayList<>(2);
                candCards.add(c1);
                candCards.add(c2);
                
                IHand oppCurentHand = PokerFactory.buildHand(candCards, board);
                int curIdx = ownCurrentHand.compareTo(oppCurentHand) + 1;
                
                List<Card> turnCand;
                if (board.size() == 4) {
                    turnCand = new ArrayList<>(1);
                    turnCand.add(board.get(3));
                    if (boardCpy.size() == 4) {
                        boardCpy.remove(3);                        
                    }
                }
                else {
                    turnCand = new ArrayList<>(c1Cand);
                    turnCand.remove(c1);
                    turnCand.remove(c2);
                }
                List<Card> riverCand = new ArrayList<>(turnCand);
                
                for (Card turn : turnCand) {
                    riverCand.remove(turn);
                    
                    for (Card river : riverCand) {

                        boardCpy.add(turn);
                        boardCpy.add(river);
                        
                        IHand ownFutureHand = PokerFactory.buildHand(holecards, boardCpy);
                        IHand oppFutureHand = PokerFactory.buildHand(candCards, boardCpy);
                        
                        int futIdx = ownFutureHand.compareTo(oppFutureHand) + 1;
                        HP[curIdx][futIdx] += 1 * range.handWeight(candCards.get(0), candCards.get(1));
                        HPTotal[curIdx] += 1 * range.handWeight(candCards.get(0), candCards.get(1));
         
                        boardCpy.remove(3);
                        boardCpy.remove(3);
                    }
                }
            }
        }
        
        _numPl = numPlayers;
        calculatePotentials();

        //printData();
    }
    
}
