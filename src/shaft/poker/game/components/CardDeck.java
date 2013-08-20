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
import java.util.Random;
import shaft.poker.game.Card;
import shaft.poker.game.IDeck;

/**
 *
 * @author Thomas Schaffer <thomas.schaffer@epitech.eu>
 */
public class CardDeck implements IDeck {
    
    protected List<Card> _deck;
    private static final int SHUFFLE_COUNT = 100;
    private Random _rnd = new Random();
    private int _drawCount;
    
    public CardDeck() {
        //_deck = new ArrayList<>(Card.values().size());
        shuffle();
    }
    
    @Override
    public final void shuffle() {
        Card tmp;
        
        _drawCount = 0;
        
        //_deck.clear();
        //_deck.addAll(Card.values());
        _deck = new ArrayList<>(Card.values());
        
        for (int i = 0; i < SHUFFLE_COUNT; i++) {
            int c1 = _rnd.nextInt(_deck.size() - 1);
            int c2 = _rnd.nextInt(_deck.size() - 1);
            
            tmp = _deck.get(c1);
            _deck.set(c1, _deck.get(c2));
            _deck.set(c2, tmp);
        }
    }
    
    
        @Override
        public Card drawCard() {
            if (_deck.size() == 0) {
                System.out.println("empty deck, drawCount = " + _drawCount);
            }
            else {
                _drawCount++;
            }
            return _deck.remove(0);
        }

        @Override
        public void burnCard() {
            drawCard();
        }

        @Override
        public int deckSize() {
            return _deck.size();
        }
    
}
