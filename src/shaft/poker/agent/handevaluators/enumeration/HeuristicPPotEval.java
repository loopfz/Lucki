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

import java.util.List;
import shaft.poker.agent.IHandRange;
import shaft.poker.agent.enumerationtools.EnumCandidate;
import shaft.poker.factory.PokerFactory;
import shaft.poker.game.*;
import shaft.poker.game.Card.*;
import shaft.poker.game.IHand.HandType;

/**
 *
 * @author Thomas Schaffer <thomas.schaffer@epitech.eu>
 */
public class HeuristicPPotEval extends AEnumHandEvaluator {
    
    @Override
    public void compute(List<Card> holecards, List<Card> board, IHandRange range, int numPlayers) {
        initArrays();
        
        IHand ownCurrentHand = PokerFactory.buildHand(holecards, board);

        List<EnumCandidate> holeCandidates = EnumCandidate.buildCandidates(holecards, board);
                
        for (EnumCandidate cand : holeCandidates) {

            IHand oppCurentHand = PokerFactory.buildHand(cand.cards(), board);
            int curIdx = ownCurrentHand.compareTo(oppCurentHand) + 1;
            HPTotal[curIdx] += cand.weight() * range.handWeight(cand.cards().get(0), cand.cards().get(1));
        }
        
        _numPl = numPlayers;
        calculatePotentials();

        if (board.size() < 5) {
            
            int[] suits = new int[Suit.values().length];
            int[] ranks = new int[Rank.values().length];
            
            int outs = 0;
            int flushes = 0;
            
            for (Card c : holecards) {
                suits[c.suit().ordinal()]++;
                ranks[c.rank().ordinal()]++;
            }
            for (Card c : board) {
                suits[c.suit().ordinal()]++;
                ranks[c.rank().ordinal()]++;
            }
            
            for (int i = 0; i < suits.length; i++) {
                if (suits[i] == 4) {
                    flushes++;
                    outs += 9;
                }
            }
            
            for (int i = Rank.DEUCE.ordinal(); i < Rank.JACK.ordinal(); i++) {
                if (ranks[i] >= 1
                        && ranks[i + 1] >= 1
                        && ranks[i + 2] >= 1
                        && ranks[i + 3] >= 1) {
                    outs += 8 - flushes * 2;
                }
            }
            
            if (holecards.get(0).rank() == holecards.get(1).rank()) {
                int onBoard = 0;
                for (Card c : board) {
                    if (c.rank() == holecards.get(0).rank()) {
                        onBoard++;
                    }
                }
                outs += 2 - onBoard;
                if (ownCurrentHand.type() == HandType.THREE_OF_A_KIND) {
                    outs += 8;
                    if (board.size() == 4) {
                        outs++;
                    }
                }
            }
            
            if (outs < 10) {
                _posPot = ((double) outs) * 4.0 / 100.0;
            }
            else {
                _posPot = (((double) outs) * 3.0 + 9.0) / 100.0;
            }
        }
        
        //printData();
    }
    
}
