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
package shaft.poker.humanplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import shaft.poker.game.Card;
import shaft.poker.game.IAction;
import shaft.poker.game.IPlayer;
import shaft.poker.game.ITable;
import shaft.poker.game.table.IPlayerActionListener;
import shaft.poker.game.table.IPlayerContext;
import shaft.poker.game.table.IGameEventListener;

/**
 *
 * @author Thomas Schaffer <thomas.schaffer@epitech.eu>
 */
public class HumanPlayer implements IPlayer, IGameEventListener, IPlayerActionListener {
    
    private String _id;
    private List<Card> _holeCards;
    
    public HumanPlayer(ITable table) {
        System.out.println("Enter name: ");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        try {
            _id = in.readLine();
        } catch (IOException ex) {
            _id = "Player" + new Random().nextInt();
        }
        _holeCards = new ArrayList<>(2);
        table.registerEventListener(this);
        table.registerListenAllPlayerEvents(this);
    }

    @Override
    public String id() {
        return _id;
    }

    @Override
    public void dealCard(Card c) {
        _holeCards.add(c);
    }
    
    @Override
    public List<Card> holeCards() {
        return _holeCards;
    }

    @Override
    public IAction action(ITable table, IPlayerContext plContext) {
        System.out.println("#####" + _id + " ACTS #####");
        //System.out.println("--------------------");

        //System.out.println("--------------------");
        System.out.print("Hand: ");
        for (Card c : _holeCards) {
            System.out.print(c.toString() + " ");
        }
        System.out.println("Input selection:");
        System.out.println("[1] Fold");
        System.out.println("[2] Check/Call");
        System.out.println("[3] Bet");
        Scanner in = new Scanner(System.in);
        while (true) {
            int res = in.nextInt();
            switch (res) {
                case 1: return plContext.makeFold();
                case 2: return plContext.makeCall();
                case 3: return plContext.makeRaise(plContext.minRaise());
                default: System.out.println("Bad input.");
            }
        }
    }

    @Override
    public void roundBegin(ITable table, ITable.Round r) {
        System.out.println("%%% START OF BETTING ROUND: " + r.toString());
        System.out.print("Board: ");
        for (Card c : table.board()) {
            System.out.print(c.toString() + " ");
        }
        System.out.println();
    }

    @Override
    public void roundEnd(ITable table, ITable.Round r) {
        System.out.println("%%%");
    }

    @Override
    public void newHand(ITable table) {
        System.out.println("********************* NEW HAND *********************");
    }

    @Override
    public void gameAction(ITable table, String id, IPlayerContext plContext, ITable.ActionType type, int amount) {
        System.out.print("Player [" + id + "] action: " + type.toString());
        if (amount > 0) {
            System.out.print(" [" + amount + "]");
            if (plContext.stack() <= amount) {
                System.out.print(" (ALL IN)");
            }
        }
        System.out.println();
    }

    @Override
    public void leave(ITable table, String id) {
        System.out.println("Player [" + id + "] left the table");
    }

    @Override
    public void newGame(ITable table, int stackSize, int sBlind, int bBlind, int numPlayers) {
        System.out.println("### GAME START ###");
        System.out.println("Stack size: " + stackSize);
        System.out.println("Small blind: " + sBlind);
        System.out.println("Big blind: " + bBlind);
        System.out.println("Num players: " + numPlayers);
    }
    
}