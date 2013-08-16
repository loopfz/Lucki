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
package shaft.poker.agent.enumerationtools;

import java.util.ArrayList;
import java.util.List;
import shaft.poker.game.Card;

/**
 *
 * @author Thomas Schaffer <thomas.schaffer@epitech.eu>
 */
public class EnumCandidate {
    private List<Card> _cards;
    private int _weight;
    
    public EnumCandidate(List<Card> cards, int weight) {
        _cards = cards;
        _weight = weight;
    }
    
    public List<Card> cards() {
        return _cards;
    }
    
    public int weight() {
        return _weight;
    }
    
    public boolean generic() {
        return _weight == 1;
    }
    
    public boolean contentsEquals (Card c1, Card c2, boolean generic) {
        if (generic && _weight == 1) {
            return false;
        }
        if (_cards.size() != 2) {
            return false;
        }
        if ((_cards.get(0).rank() == c1.rank() && _cards.get(1).rank() == c2.rank())
                || (_cards.get(0).rank() == c2.rank() && _cards.get(1).rank() == c1.rank())) {
            return true;
        }
        return false;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        for (Card c : _cards) {
            sb.append(" | ");
            sb.append(c.toString());
        }
        
        sb.append(" | w:");
        sb.append(_weight);
        
        return sb.toString();
    }
    
    /*
     * Build buckets of candidates (potential opponent hole cards and turn/river draws)
     * to increase lookahead performance
     */
    public static List<EnumCandidate> buildCandidates(List<Card> holeCards, List<Card> board) {
        
        int[] cardsLeft = new int[14];
        
        List<Card.Suit> weakSuits = new ArrayList<>(4);
        List<Card.Suit> strongSuits = new ArrayList<>(4);
        List<EnumCandidate> holeCandidates = new ArrayList(300);
        
        List<Card> c1Candidates;
        List<Card> c2Candidates;
        
        // Candidates for 1st card (all values -holes -board)
        c1Candidates = new ArrayList(Card.values());
        if (holeCards != null) {
            c1Candidates.removeAll(holeCards);
        }
        c1Candidates.removeAll(board);
        
        // Candidates for 2nd card (identical for now)
        c2Candidates = new ArrayList<>(c1Candidates);
        
        // Potential Flush suits
        for (Card.Suit s : Card.Suit.values()) {
            int count = 0;
            for (Card c : board) {
                if (c.suit() == s) {
                    count++;
                }
            }
            // Can hit Flush with a single card of the suit
            if (count >= 3) {
                strongSuits.add(s);
            }
            // Can hit FLush with Suited hand
            else if (count >= 2) {
                weakSuits.add(s);
            }
        }
        
        // Add any suit with an empty (preflop) board, to distinguish suited/unsuited hands
        if (board.isEmpty()) {
            weakSuits.add(Card.Suit.CLUBS);
        }
        
        for (Card.Rank r : Card.Rank.values()) {
            cardsLeft[r.ordinal()] = 4;
        }
        for (Card c : holeCards) {
            if (!weakSuits.contains(c.suit())
                    && !strongSuits.contains(c.suit())) {
                cardsLeft[c.rank().ordinal()]--;
            }
        }
        for (Card c : board) {
            if (!weakSuits.contains(c.suit())
                    && !strongSuits.contains(c.suit())) {
                cardsLeft[c.rank().ordinal()]--;
            }
        }
        
        for (Card c1 : c1Candidates) {
            c2Candidates.remove(c1);
                       
            for (Card c2 : c2Candidates) {
                if (c1.suit() != c2.suit()) {
                        if (!weakSuits.contains(c1.suit())
                                && !weakSuits.contains(c2.suit())) {
                            
                            boolean redundant = false;
                            int weight = 1;
                            if (!strongSuits.contains(c1.suit())
                                    && !strongSuits.contains(c2.suit())) {
                                for (EnumCandidate cand : holeCandidates) {
                                    if (cand.contentsEquals(c1, c2, true)) {
                                        redundant = true;
                                        break;
                                    }
                                }
                                if (!redundant) {
                                    // Non redundant generic hand, compute weight using count of cards left in the deck
                                    // minus suited hands or hands with a strong suit card
                                    if (c1.rank() != c2.rank()) {
                                        weight = cardsLeft[c1.rank().ordinal()] * cardsLeft[c2.rank().ordinal()]
                                                - (weakSuits.size() * (cardsLeft[c1.rank().ordinal()] + cardsLeft[c2.rank().ordinal()]))
                                                - (strongSuits.size() * (cardsLeft[c1.rank().ordinal()] - 1))
                                                - (strongSuits.size() * (cardsLeft[c2.rank().ordinal()] - 1));
                                    }
                                    else {
                                        weight = (cardsLeft[c1.rank().ordinal()] * (cardsLeft[c1.rank().ordinal()] - 1)) / 2
                                                - (strongSuits.size() * (cardsLeft[c1.rank().ordinal()] - 1));
                                    }
                                }
                            }
                            
                            if (!redundant) {
                                // Non redundant, add to candidates
                                List<Card> cards = new ArrayList(2);
                                cards.add(c1);
                                cards.add(c2);
                                holeCandidates.add(new EnumCandidate(cards, weight));
                           }
                        }
                }
                else if (weakSuits.contains(c1.suit())) {
                    // Relevant suited hand, add itself (weight = 1) as candidate
                    List<Card> cards = new ArrayList(2);
                    cards.add(c1);
                    cards.add(c2);
                    holeCandidates.add(new EnumCandidate(cards, 1));
                }
            }
        }
        
       return holeCandidates;
        //_holeCandidates = holeCandidates;
        
        /*for (Suit s : Suit.values()) {
            if (!weakSuits.contains(s)) {
                int count = 0;
                for (Card c : board) {
                    if (c.suit() == s) {
                        count++;
                    }
                }
                for (Card c : holeCards) {
                    if (c.suit() == s) {
                        count++;
                    }
                }
                if (count + (5 - board.size()) >= 5) {
                    weakSuits.add(s);
                }
            }
        }*/
        
        /*
        c2Candidates = new ArrayList<>(c1Candidates);
        
        List<EnumCandidate> drawCandidates = new ArrayList<>(2);
        if (board.size() == 4) {
            _drawCandidates = null;
            return null;
            //c1Candidates = new ArrayList<>(1);
            //c1Candidates.add(board.get(3));
        }
        
        for (Card c1 : c1Candidates) {
            c2Candidates.remove(c1);
            
            for (Card c2 : c2Candidates) {
                if (weakSuits.contains(c1.suit())
                        || strongSuits.contains(c1.suit())) {
                    List<Card> cards = new ArrayList<>(2);
                    cards.add(c1);
                    cards.add(c2);
                    drawCandidates.add(new EnumCandidate(cards, 1 * (5 - board.size())));
                }
                else {
                    boolean redundant = false;
                    for (EnumCandidate cand : drawCandidates) {
                        if (cand.contentsEquals(c1, c2, true)) {
                            redundant = true;
                            break;
                        }
                    }
                    
                    if (!redundant) {
                        List<Card> cards = new ArrayList<>(2);
                        cards.add(c1);
                        cards.add(c2);
                        int weight;
                        if (c1.rank() != c2.rank()) {
                            weight = cardsLeft[c1.rank().ordinal()] * cardsLeft[c2.rank().ordinal()]
                                    - ((weakSuits.size() + strongSuits.size()) * (cardsLeft[c1.rank().ordinal()] - 1))
                                    - ((weakSuits.size() + strongSuits.size()) * (cardsLeft[c2.rank().ordinal()] - 1));
                        }
                        else {
                            weight = cardsLeft[c1.rank().ordinal()] * (cardsLeft[c1.rank().ordinal()] - 1) / 2
                                    - ((weakSuits.size() + strongSuits.size()) * (cardsLeft[c1.rank().ordinal()] - 1));
                        }
                        weight *= 2;
                        drawCandidates.add(new EnumCandidate(cards, weight));
                    }
                }
            }
        }
        
        _drawCandidates = drawCandidates;
        
        return null;*/
    }

}
