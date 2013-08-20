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
package shaft.poker.game.table;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import shaft.poker.game.Card;
import shaft.poker.game.IAction;
import shaft.poker.game.IDeck;
import shaft.poker.game.IHand;
import shaft.poker.game.IPlayer;
import shaft.poker.game.ITable;
import shaft.poker.factory.PokerFactory;

/**
 *
 * @author Thomas Schaffer <thomas.schaffer@epitech.eu>
 */
public class PokerTable implements ITable {
    
    private static final int MAXBETS_ROUND = 4;
    
    private int _numberHandsInGame;
    private int _numberActivePlayers;
    private int _numberTotalPlayers;
    private Round _currentRound;
    private int _numberBets;
    private int _numberCallers;
    private int _amountCall;
    private int _pot;
    
    private int _cardsDrawn;
    
    private IActionBuilder _actionBuild;
    
    private int _sBlind;
    private int _bBlind;
    
    private IDeck _deck;
    private List<Card> _board;
    
    private LinkedList<PlayerData> _players;
    private LinkedList<PlayerData> _playersToAct;
    private LinkedList<PlayerData> _playersActed;
    private LinkedList<PlayerData> _deadPlayers;
    
    private List<IGameEventListener> _roundListeners;
    private List<IPlayerActionListener> _allEventsListeners;
    
    // Dimension: [BettingRound]
    private int[] _cardsToDraw = {0, 3, 1, 1};
    
    public PokerTable(IActionBuilder actionBuild) {
        _actionBuild = actionBuild;
        
        _numberTotalPlayers = 0;
        _players = new LinkedList<>();
        _playersToAct = new LinkedList<>();
        _playersActed = new LinkedList<>();
        _deadPlayers = new LinkedList<>();
        
        _deck = PokerFactory.buildDeck();
        _board = new ArrayList<>(5);
        
        _roundListeners = new ArrayList<>(25);
        _allEventsListeners = new ArrayList<>(25);
    }
    
    public void addPlayer(IPlayer pl) {
        _players.add(new PlayerData(pl, this));
        _numberTotalPlayers++;
    }
    
    @Override
    public void runGame(int hands, int stackSize, int sBlind, int bBlind) {
        _sBlind = sBlind;
        _bBlind = bBlind;
        _numberHandsInGame = 0;
        _players.addAll(_deadPlayers);
        _deadPlayers.clear();
        for (IGameEventListener listener : _roundListeners) {
            listener.newGame(this, stackSize, _sBlind, _bBlind, _numberTotalPlayers);
        }
        while (_players.size() > 1 && _numberHandsInGame < hands) {
            _numberHandsInGame++;

            _pot = 0;
            _amountCall = _bBlind;
            _currentRound = null;
            boolean sb = false;
            boolean bb = false;
            boolean blinds = false;
            
            _board.clear();
            _deck.shuffle();
            
            for (IGameEventListener listener : _roundListeners) {
                listener.newHand(this);
            }
            
            _deck.burnCard();
            for (int i = 0; i < 2; i++) {
                for (PlayerData pl : _players) {
                    pl.player().dealCard(_deck.drawCard());
                }
            }
            
            int pos = 1;
            for (PlayerData pl : _players) {
                pl.setPosition(pos++);
            }
            
            _numberActivePlayers = _players.size();
            _playersToAct.clear();
            _playersActed.clear();
            _playersToAct.addAll(_players);
            _numberActivePlayers = _playersToAct.size();
                
            while (_currentRound != Round.RIVER && _numberActivePlayers > 1) {
                int cardsDrawn2 = _cardsDrawn;
                if (_currentRound == null) {
                    _currentRound = Round.PREFLOP;
                }
                else {
                    _currentRound = Round.values()[_currentRound.ordinal() + 1];
                }
                
                _numberCallers = 0;
                _numberBets = 0;
                _amountCall = 0;
                
                if (_cardsToDraw[_currentRound.ordinal()] > 0) {
                    _deck.burnCard();
                    for (int i = _cardsToDraw[_currentRound.ordinal()]; i > 0; i--) {
                        _board.add(_deck.drawCard());
                    }                    
                }
                
                for (IGameEventListener listener : _roundListeners) {
                    listener.roundBegin(this, _currentRound);
                }
                
                while (_playersToAct.size() > 0) {
                    boolean raised = false;
                    
                    Iterator<PlayerData> activePlayers = _playersToAct.iterator();
                    PlayerData pl = null;
                    
                    while (activePlayers.hasNext()) {
                 
                        pl = activePlayers.next();
                        activePlayers.remove();
                        
                        if (pl.allIn() || _numberActivePlayers == 1) {
                            _playersActed.add(pl);
                            continue;
                        }
                        
                        blinds = false;
                        _actionBuild.setContext(this, pl);
                        IAction act;
                        if (!sb || !bb) {
                            blinds = true;
                            if (!sb) {
                                act = _actionBuild.makeSBlind();
                                sb = true;
                            }
                            else {
                                act = _actionBuild.makeBBlind();
                                bb = true;
                            }
                        }
                        else {
                            act = pl.player().action(this, pl, _actionBuild);   
                        }
                        
                        if (act == null || act.type() == ActionType.FOLD) {
                            _numberActivePlayers--;
                            broadcastGameAction(pl, ActionType.FOLD, 0, pl.prioListeners());
                            broadcastGameAction(pl, ActionType.FOLD, 0, pl.listeners());
                            broadcastGameAction(pl, ActionType.FOLD, 0, _allEventsListeners);
                        }
                        else {
                            if (act.type() == ActionType.CALL) {
                                _playersActed.add(pl);
                                playerCall(pl, act.amount());
                            }
                            if (act.type() == ActionType.BET) {
                                if (_numberBets < MAXBETS_ROUND) {
                                    playerBet(pl, act.amount(), blinds);
                                    raised = true;                                    
                                }
                                else {
                                    _playersActed.add(pl);
                                    playerCall(pl, act.amount());
                                }
                            }
                            broadcastGameAction(pl, act.type(), act.amount(), pl.prioListeners());
                            broadcastGameAction(pl, act.type(), act.amount(), pl.listeners());
                            broadcastGameAction(pl, act.type(), act.amount(), _allEventsListeners);
                            if (raised) {
                                break;
                            }
                         }
                        
                    }
                    if (raised) {
                        _playersToAct.addAll(_playersActed);
                        _playersActed.clear();
                        _numberActivePlayers = _playersToAct.size() + 1;
                        _numberCallers = 0;
                        if (blinds) {
                            _playersToAct.add(pl);
                        }
                        else {
                            _playersActed.add(pl);                            
                        }
                    }
                }
                
                if (_currentRound == Round.RIVER || _playersActed.size() == 1) {
                    while (_pot > 0) {
                        
                        PlayerData winner = null;
                        List<PlayerData> _splitters = new ArrayList<>(_playersActed.size() + 1);
                        int winnerIdx = 0;
                        
                        if (_playersActed.size() > 1) {
                            
                            IHand winHand = null;
                            Iterator<PlayerData> it = _playersActed.iterator();
                            int i = 0;
                            
                            while (it.hasNext()) {
                                PlayerData pl = it.next();
                                IHand oHand = PokerFactory.buildHand(pl.player().holeCards(), _board);
                                int cmp;
                                
                                if (winHand == null || (cmp = winHand.compareTo(oHand)) < 0) {
                                    winner = pl;
                                    winHand = oHand;
                                    _splitters.clear();
                                    winnerIdx = i;
                                }
                                else if (cmp == 0) {
                                    _splitters.add(pl);
                                }
                                i++;
                            }
                        }
                        else if (_playersActed.size() == 1) {
                            winner = _playersActed.get(0);
                        }
                        else {
                            // Discarding odd chip left (odd split amount etc.)
                            _pot = 0;
                            break;
                        }
                        
                        int winnings = _pot / (_splitters.size() + 1);
                        int didWin = winner.winPot(winnings);
                        _pot -= didWin;
                        for (IGameEventListener listener : _roundListeners) {
                            listener.winHand(this, winner, didWin);
                        }
                        for (PlayerData split : _splitters) {
                            didWin = split.winPot(winnings);
                            _pot -= didWin;
                            for (IGameEventListener listener : _roundListeners) {
                                listener.winHand(this, winner, didWin);
                            }
                        }
                        _playersActed.remove(winner);
                        _playersActed.removeAll(_splitters);
                    }
                }
                else {
                    _playersToAct.addAll(_players);
                    _playersToAct.retainAll(_playersActed);
                    _playersActed.clear();
                }
                
                for (IGameEventListener listener : _roundListeners) {
                    listener.roundEnd(this, _currentRound);
                }
            }
                        
            if (_players.size() > 1) {
                PlayerData dealer = _players.removeLast();
                _players.addFirst(dealer);
            }
            
            Iterator<PlayerData> plIt = _players.iterator();
            while (plIt.hasNext()) {
                PlayerData pl = plIt.next();
                
                if (pl.stack() <= 0) {
                    plIt.remove();
                    for (IPlayerActionListener listener : pl.prioListeners()) {
                        listener.leave(this, pl);
                    }
                    for (IPlayerActionListener listener : pl.listeners()) {
                        listener.leave(this, pl);
                    }
                    _deadPlayers.add(pl);
                }
            }

        }
    }
    
    private void broadcastGameAction(IPlayerData plData, ActionType type, int amount, List<IPlayerActionListener> listeners) {
        for (IPlayerActionListener listener : listeners) {
            listener.gameAction(this, plData, type, amount);
        }
    }

    private void playerBet(PlayerData pl, int amount, boolean blind) {
        if (pl.amountToCall() > 0) {
            playerCall(pl, pl.amountToCall());
        }
        _pot += pl.placeMoney(amount);
        if (!blind) {
            _numberBets++;            
        }
        _amountCall += amount;
        _numberCallers = 0;
        playerUpdateMaxWinnings(pl, amount);
        for (PlayerData o : _players) {
            if (pl != o) {
                o.betAgainst(amount, blind);
            }
        }
    }
    
    private void playerCall(PlayerData pl, int amount) {
        _pot += pl.placeMoney(pl.amountToCall());
        _numberCallers++;
        playerUpdateMaxWinnings(pl, amount);
    }
    
    private void playerUpdateMaxWinnings(PlayerData pl, int amount) {
        for (PlayerData o : _playersActed) {
            if (pl != o) {
                o.updateMaxWin(pl, amount);                
            }
        }
        for (PlayerData o : _playersToAct) {
            if (pl != o) {
                o.updateMaxWin(pl, amount);                
            }
        }
    }

    @Override
    public int numberBets() {
        return _numberBets;
    }
    
    @Override
    public int numberCallers() {
        return _numberCallers;
    }

    @Override
    public Round round() {
        return _currentRound;
    }
    
    @Override
    public int potSize() {
        return _pot;
    }

    @Override
    public int numberActivePlayers() {
        return _numberActivePlayers;
    }
    
    @Override
    public int numberPlayersToAct() {
        return _playersToAct.size();
    }
    
    @Override
    public int numberPlayers() {
        return _players.size();
    }

    @Override
    public List<Card> board() {
        return _board;
    }
    
    @Override
    public int smallBlind() {
        return _sBlind;
    }
    
    @Override
    public int bigBlind() {
        return _bBlind;
    }

    @Override
    public void registerListenerForPlayer(String id, IPlayerActionListener listener) {
        for (PlayerData pl : _players) {
            if (pl.player().id().equals(id)) {
                pl.listeners().add(listener);
                break;
            }
        }
    }

    @Override
    public void registerPriorityListenerForPlayer(String id, IPlayerActionListener listener) {
        for (PlayerData pl : _players) {
            if (pl.player().id().equals(id)) {
                pl.prioListeners().add(listener);
                break;
            }
        }
    }

    @Override
    public void registerEventListener(IGameEventListener listener) {
        _roundListeners.add(listener);
    }

    @Override
    public void registerListenAllPlayerEvents(IPlayerActionListener listener) {
        _allEventsListeners.add(listener);
    }

    @Override
    public String playerSmallBlind() {
        if (_players.size() > 0) {
            return _players.get(0).id();
        }
        return null;
    }

    @Override
    public String playerBigBlind() {
        if (_players.size() > 1) {
            return _players.get(1).id();
        }
        return null;
    }

    @Override
    public String playerDealer() {
        if (_players.size() > 1) {
            if (_players.size() > 2) {
                return _players.getLast().id();
            }
            else {
                return _players.getFirst().id();
            }
        }
        return null;
    }
    
}