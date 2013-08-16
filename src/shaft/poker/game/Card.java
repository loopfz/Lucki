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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Thomas Schaffer <thomas.schaffer@epitech.eu>
 */
public class Card implements Comparable<Card> {

    public enum Rank {
        DEUCE,
        THREE,
        FOUR,
        FIVE,
        SIX,
        SEVEN,
        EIGHT,
        NINE,
        TEN,
        JACK,
        QUEEN,
        KING,
        ACE
    }
    
    public enum Suit {
        CLUBS,
        SPADES,
        HEARTS,
        DIAMONDS
    }
    
    private final Rank _rank;
    private final Suit _suit;
    private static final List<Card> _values = new ArrayList<Card>(Rank.values().length * Suit.values().length) {
        {
            for (Suit suit : Suit.values()) {
                for (Rank rank : Rank.values()) {
                    add(new Card(rank, suit));
                }
            }
        }
    };
    
    public Card(Rank rank, Suit suit) {
        _rank = rank;
        _suit = suit;
    }
    
    @Override
    public String toString() {
        return _rank.toString() + " of " + _suit.toString();
    }
    
    public Rank rank() {
        return _rank;
    }
    
    public Suit suit() {
        return _suit;
    }
    
    public static List<Card> values() {
        return _values;
    }
    
    @Override
    public int compareTo(Card o) {
        if (_rank.ordinal() > o._rank.ordinal()) {
            return 1;
        }
        else if (_rank.ordinal() < o._rank.ordinal()) {
            return -1;
        }
        return 0;
    }

}
