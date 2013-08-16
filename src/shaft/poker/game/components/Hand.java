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
package shaft.poker.game.components;

import java.util.ArrayList;
import java.util.List;
import shaft.poker.game.Card;
import shaft.poker.game.Card.*;
import shaft.poker.game.IHand;

/**
 *
 * @author Thomas Schaffer <thomas.schaffer@epitech.eu>
 */
public class Hand implements IHand {
    
    private HandType _type;
    private List<Card> _cards;
    
    public Hand(List<Card> holeCards, List<Card> board) {
        _cards = new ArrayList(5);
        
        // TODO
        // determine hand from hole+board
        // fill handType + card list
        
        List<Card> availCards = new ArrayList<>(board);
        availCards.addAll(holeCards);

        analyzeHand(availCards);
    }

    @Override
    public HandType type() {
        return _type;
    }

    @Override
    public List<Card> cards() {
        return _cards;
    }

    @Override
    public int compareTo(IHand o) {
        if (_type.ordinal() < o.type().ordinal()) {
            return -1;
        }
        else if (_type.ordinal() > o.type().ordinal()) {
            return 1;
        }
        
        List<Card> oHand = o.cards();
        
        for (int i = 0; i < _cards.size(); i++) {
            if (_cards.get(i).rank().ordinal() < oHand.get(i).rank().ordinal()) {
                return -1;
            }
            else if (_cards.get(i).rank().ordinal() > oHand.get(i).rank().ordinal()) {
                return 1;
            }
        }
        /*
        System.out.println("-------");
        System.out.println("TIED:");
        for (Card c : _cards) {
            System.out.println(c.toString());
        }
        System.out.println("&");
        for (Card c : oHand) {
            System.out.println(c.toString());
        }*/
        
        return 0;
    }

    private void analyzeHand(List<Card> availCards) {
        int[] cardsByRank = new int[Rank.values().length];
        int[] cardsBySuit = new int[Suit.values().length];
        
        for (Card c : availCards) {
            cardsByRank[c.rank().ordinal()]++;
            cardsBySuit[c.suit().ordinal()]++;
        }
        
        int largeCardsNum = 1;
        int largeCardsRank = 0;
        int smallCardsNum = 1;
        int smallCardsRank = 0;
        for (int i = Rank.values().length - 1; i > 0; i--) {
            if (cardsByRank[i] > largeCardsNum) {
                smallCardsNum = largeCardsNum;
                smallCardsRank = largeCardsRank;
                largeCardsNum = cardsByRank[i];
                largeCardsRank = i;
            }
            else if (cardsByRank[i] > smallCardsNum) {
                smallCardsNum = cardsByRank[i];
                smallCardsRank = i;
            }
        }
        
        int topStraight = 0;
        boolean straight = false;
        for (int i = 0; i <= Rank.TEN.ordinal(); i++) {
            if (cardsByRank[i] >= 1
                    && cardsByRank[i + 1] >= 1
                    && cardsByRank[i + 2] >= 1
                    && cardsByRank[i + 3] >= 1
                    && cardsByRank[i + 4] >= 1) {
                straight = true;
                topStraight = i + 4;
                break;
            }
        }
        
        boolean flush = false;
        Suit flushSuit = null;
        for (Suit s : Suit.values()) {
            if (cardsBySuit[s.ordinal()] >= 5) {
                flush = true;
                flushSuit = s;
                break;
            }
        }
        
        if (straight && flush) {
            if (buildStraightFlush(availCards, Rank.values()[topStraight], flushSuit)) {
                return ;
            }
        }       
        if (largeCardsNum == 4) {
            buildFourOfAKind(availCards, Rank.values()[largeCardsRank]);
        }
        else if (largeCardsNum >= 3 && smallCardsNum >= 2) {
            buildFullHouse(availCards, Rank.values()[largeCardsRank], Rank.values()[smallCardsRank]);
        }
        else if (flush) {
            buildFlush(availCards, flushSuit);
        }
        else if (straight) {
            buildStraight(availCards, Rank.values()[topStraight]);
        }
        else if (largeCardsNum >= 2 && smallCardsNum >= 2) {
            buildTwoPair(availCards, Rank.values()[largeCardsRank], Rank.values()[smallCardsRank]);
        }
        else if (largeCardsNum >= 2) {
            buildPair(availCards, Rank.values()[largeCardsRank]);
        }
        else {
            buildHighCard(availCards);
        }
    }
    
    private void lookupRankAndAdd(List<Card> availCards, int iter, Rank rank) {
        for (int i = 0; i < iter; i++) {
            Card tmp = null;
            for (Card c : availCards) {
                if (c.rank() == rank) {
                    tmp = c;
                    break;
                }
            }
            if (tmp != null) {
                availCards.remove(tmp);
                _cards.add(tmp);
            }
        }
    }
    
    private void lookupHighestAndAdd(List<Card> availCards, int iter, Suit suit) {
        for (int i = 0; i < iter; i++) {
            Card tmp = null;
            for (Card c : availCards) {
                if ((tmp == null || c.compareTo(tmp) >= 1) && (suit == null || c.suit() == suit)) {
                    tmp = c;
                }
            }
            if (tmp != null) {
                availCards.remove(tmp);
                _cards.add(tmp);
            }
        }
    }

    private void buildFullHouse(List<Card> availCards, Rank rankUpper, Rank rankLower) {
        lookupRankAndAdd(availCards, 3, rankUpper);
        lookupRankAndAdd(availCards, 2, rankLower);
        _type = HandType.FULL_HOUSE;
    }

    private void buildFourOfAKind(List<Card> availCards, Rank rank) {
        lookupRankAndAdd(availCards, 4, rank);
        lookupHighestAndAdd(availCards, 1, null);
        _type = HandType.FOUR_OF_A_KIND;
    }

    private void buildTwoPair(List<Card> availCards, Rank rankUpper, Rank rankLower) {
        lookupRankAndAdd(availCards, 2, rankUpper);
        lookupRankAndAdd(availCards, 2, rankLower);
        lookupHighestAndAdd(availCards, 1, null);
        _type = HandType.TWO_PAIR;
    }

    private void buildPair(List<Card> availCards, Rank rank) {
        lookupRankAndAdd(availCards, 2, rank);
        lookupHighestAndAdd(availCards, 3, null);
        _type = HandType.PAIR;
    }

    private void buildHighCard(List<Card> availCards) {
        lookupHighestAndAdd(availCards, 5, null);
        _type = HandType.HIGH_CARD;
    }

    private boolean buildStraightFlush(List<Card> availCards, Rank rank, Suit flushSuit) {
        List<Card> availCpy = new ArrayList(availCards);
        buildStraight(availCpy, rank);
        
        boolean straightFlush = true;
        Suit s = null;
        for (Card c : _cards) {
            if (s == null) {
                s = c.suit();
            }
            if (s != c.suit()) {
                straightFlush = false;
                break;
            }
        }
        
        if (!straightFlush) {
            _cards.clear();
            _type = null;
            return false;
        }
        
        _type = HandType.STRAIGHT_FLUSH;
        if (rank == Rank.ACE) {
            _type = HandType.ROYAL_FLUSH;
        }
        
        return true;
    }

    private void buildFlush(List<Card> availCards, Suit flushSuit) {
        lookupHighestAndAdd(availCards, 5, flushSuit);
        _type = HandType.FLUSH;
    }
    
    private void buildStraight(List<Card> availCards, Rank rank) {
        for (int i = 0; i < 5; i++) {
            lookupRankAndAdd(availCards, 1, Rank.values()[rank.ordinal() - i]);
        }
        _type = HandType.STRAIGHT;
    }

    
}
