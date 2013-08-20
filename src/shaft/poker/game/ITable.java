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

import shaft.poker.game.table.IGameEventListener;
import shaft.poker.game.table.IPlayerActionListener;
import java.util.List;
import shaft.poker.agent.handranges.weightedrange.CompositeFieldRange;
import shaft.poker.agent.handranges.weightedrange.PlayerWTRange;

/**
 *
 * @author Thomas Schaffer <thomas.schaffer@epitech.eu>
 */
public interface ITable {
   
    enum ActionType {
        FOLD,
        CALL,
        BET
    }
    
    enum Round {
        PREFLOP,
        FLOP,
        TURN,
        RIVER
    }
    
    public void runGame(int hands, int stackSize, int sBlind, int bBlind);
    
    public int numberBets();
    public int maxBets();
    public int numberCallers();
    public Round round();
    public int potSize();
    
    public int numberPlayers();
    public int numberActivePlayers();
    public int numberPlayersToAct();
    
    public int smallBlind();
    public int bigBlind();
    
    public List<Card> board();
    
    public String playerSmallBlind();
    public String playerBigBlind();
    public String playerDealer();
    
    public void registerListenerForPlayer(String id, IPlayerActionListener listener);
    public void registerPriorityListenerForPlayer(String id, IPlayerActionListener listener);
    public void registerListenAllPlayerEvents(IPlayerActionListener listener);
    public void registerEventListener(IGameEventListener listener);

}
