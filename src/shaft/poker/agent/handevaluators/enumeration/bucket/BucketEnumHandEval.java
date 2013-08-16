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
import shaft.poker.game.Card.*;
import shaft.poker.game.IHand;
import shaft.poker.game.factory.*;

/**
 *
 * @author Thomas Schaffer <thomas.schaffer@epitech.eu>
 */
public class BucketEnumHandEval extends AEnumHandEvaluator {
    
    
    @Override
    public void compute(List<Card> holecards, List<Card> board, IHandRange range, int numPlayers) {
        initArrays();
        
        List<Card> boardCpy = new ArrayList(board);
        
        IHand ownCurrentHand = ComponentFactory.buildHand(holecards, board);
        
        List<EnumCandidate> holeCandidates = EnumCandidate.buildCandidates(holecards, board);
                
        for (EnumCandidate cand : holeCandidates) {
            
            IHand oppCurentHand = ComponentFactory.buildHand(cand.cards(), board);
            int curIdx = ownCurrentHand.compareTo(oppCurentHand) + 1;
            
            List<Card> turnCand;
            List<Card> riverCand = new ArrayList<>(Card.values());
            riverCand.removeAll(boardCpy);
            riverCand.removeAll(holecards);
            riverCand.removeAll(cand.cards());
            if (board.size() == 4) {
                turnCand = new ArrayList<>(1);
                turnCand.add(board.get(3));
                if (boardCpy.size() == 4) {
                    boardCpy.remove(3);                    
                }
            }
            else {
                turnCand = new ArrayList<>(riverCand);
            }
            
            for (Card turn : turnCand) {
                riverCand.remove(turn);
                
                for (Card river : riverCand) {
                    boardCpy.add(turn);
                    boardCpy.add(river);
                    
                    IHand ownFutureHand = ComponentFactory.buildHand(holecards, boardCpy);
                    IHand oppFutureHand = ComponentFactory.buildHand(cand.cards(), boardCpy);
                    
                    int futIdx = ownFutureHand.compareTo(oppFutureHand) + 1;
                    HP[curIdx][futIdx] += cand.weight() * range.handWeight(cand.cards().get(0), cand.cards().get(1));
                    HPTotal[curIdx] += cand.weight() * range.handWeight(cand.cards().get(0), cand.cards().get(1));
                    
                    boardCpy.remove(3);
                    boardCpy.remove(3);
                }
            }
        }

        _numPl = numPlayers;        
        calculatePotentials();

        printData();
    }
    
}
