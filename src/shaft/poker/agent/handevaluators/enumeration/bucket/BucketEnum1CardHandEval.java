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
package shaft.poker.agent.handevaluators.enumeration.bucket;

import shaft.poker.agent.enumerationtools.EnumCandidate;
import java.util.ArrayList;
import java.util.List;
import shaft.poker.agent.IHandRange;
import shaft.poker.agent.handevaluators.enumeration.AEnumHandEvaluator;
import shaft.poker.game.Card;
import shaft.poker.game.IHand;
import shaft.poker.factory.PokerFactory;

/**
 *
 * @author Thomas Schaffer <thomas.schaffer@epitech.eu>
 */
public class BucketEnum1CardHandEval extends AEnumHandEvaluator {
    private static final double POTENTIAL_COMP_RATIO = 1.6;
   
    @Override
    public void compute(List<Card> holecards, List<Card> board, IHandRange range, int numPlayers) {
        initArrays();
        
        List<Card> boardCpy = new ArrayList(board);
        
        IHand ownCurrentHand = PokerFactory.buildHand(holecards, board);
        
        List<Card> drawCandidates = new ArrayList<>(Card.values());
        drawCandidates.removeAll(boardCpy);
        drawCandidates.removeAll(holecards);
        
        List<EnumCandidate> holeCandidates = EnumCandidate.buildCandidates(holecards, board);
                
        for (EnumCandidate cand : holeCandidates) {
            //System.out.println(cand.toString());
            
            IHand oppCurentHand = PokerFactory.buildHand(cand.cards(), board);
            int curIdx = ownCurrentHand.compareTo(oppCurentHand) + 1;
            //HPTotal[curIdx] += cand.weight() * range.handWeight(cand.cards().get(0), cand.cards().get(1));
            if (board.size() == 5) {
                HPTotal[curIdx] += cand.weight() * range.handWeight(cand.cards().get(0), cand.cards().get(1));
            }
            else {
                for (Card drawCand : drawCandidates) {
                    
                    if (!cand.cards().contains(drawCand)) {
                        boardCpy.add(drawCand);
                        
                        IHand ownFutureHand = PokerFactory.buildHand(holecards, boardCpy);
                        IHand oppFutureHand = PokerFactory.buildHand(cand.cards(), boardCpy);
                        
                        int futIdx = ownFutureHand.compareTo(oppFutureHand) + 1;
                        //System.out.println("HandWeight: " + range.handWeight(cand.cards().get(0), cand.cards().get(1)));
                        //System.out.println("Cand weight: " + cand.weight());
                        
                        HP[curIdx][futIdx] += cand.weight() * range.handWeight(cand.cards().get(0), cand.cards().get(1));
                        HPTotal[curIdx] += cand.weight() * range.handWeight(cand.cards().get(0), cand.cards().get(1));
                        
                        
                        boardCpy.remove(boardCpy.size() - 1);
                        //System.out.println("LOOP");
                    }
                }
            }
            
        }
        
        _numPl = numPlayers;
        calculatePotentials();
        _posPot *= POTENTIAL_COMP_RATIO;
        _negPot *= POTENTIAL_COMP_RATIO;
        
        //printData();
    }
    
}
