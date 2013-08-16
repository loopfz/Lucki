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
import java.util.List;
import shaft.poker.game.Card;
import shaft.poker.game.IAction;
import shaft.poker.game.IDeck;
import shaft.poker.game.IHand;
import shaft.poker.game.IPlayer;
import shaft.poker.game.ITable;
import shaft.poker.game.factory.ComponentFactory;

/**
 *
 * @author Thomas Schaffer <thomas.schaffer@epitech.eu>
 */
public class PokerTable implements ITable {
    
    private int _numberHandsInGame;
    private int _numberActivePlayers;
    private int _numberTotalPlayers;
    private Round _currentRound;
    private int _numberBets;
    private int _numberCallers;
    private int _amountCall;
    private int _pot;
    
    private int _startStackSize;
    
    private IPlayerContext _plContext;
    
    private int _sBlind;
    private int _bBlind;
    
    private IDeck _deck;
    private List<Card> _board;
    
    private List<PlayerData> _players;
    private List<PlayerData> _playersToAct;
    private List<PlayerData> _playersActed;
    private List<PlayerData> _deadPlayers;
    
    private List<IGameEventListener> _roundListeners;
    private List<IPlayerActionListener> _allEventsListeners;
    
    // Dimension: [BettingRound]
    private int[] _cardsToDraw = {0, 3, 1, 1};
    
    public PokerTable(List<IPlayer> players, IPlayerContext plContext) {
        _plContext = plContext;
        
        _numberTotalPlayers = players.size();
        _players = new ArrayList<>(_numberTotalPlayers);
        _playersToAct = new ArrayList<>(_numberTotalPlayers);
        _playersActed = new ArrayList<>(_numberTotalPlayers);
        _deadPlayers = new ArrayList<>(_numberTotalPlayers);
        
        _deck = ComponentFactory.buildDeck();
        _board = new ArrayList<>(5);
        
        _roundListeners = new ArrayList<>(25);
        _allEventsListeners = new ArrayList<>(25);

        for (IPlayer pl : players) {
            _players.add(new PlayerData(pl, this));
        }
    }
    
    @Override
    public void runGame(int hands, int stackSize, int sBlind, int bBlind) {
        _sBlind = sBlind;
        _bBlind = bBlind;
        _numberHandsInGame = 0;
        _players.addAll(_deadPlayers);
        for (IGameEventListener listener : _roundListeners) {
            listener.newGame(this, stackSize);
        }
        while (_players.size() > 1 && _numberHandsInGame < hands) {
            _numberHandsInGame++;

            _pot = 0;
            _amountCall = _bBlind;
            _currentRound = null;
            boolean skipBlinds = true;
            
            _board.clear();
            _deck.shuffle();
            
            _deck.burnCard();
            for (int i = 0; i < 2; i++) {
                for (PlayerData pl : _players) {
                    pl.player().dealCard(_deck.drawCard());
                }
            }
            
            // Post blind bets
            playerBet(_players.get(0), _sBlind);
            playerBet(_players.get(1), _bBlind);
            
            while (_currentRound != Round.RIVER && _numberActivePlayers > 1) {
                if (_currentRound == null) {
                    _currentRound = Round.PREFLOP;
                }
                else {
                    _currentRound = Round.values()[_currentRound.ordinal() + 1];
                }
                
                _playersToAct.clear();
                _playersActed.clear();
                _playersToAct.addAll(_players);
                _numberActivePlayers = _playersToAct.size();
                
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
                    
                    int i = 0;
                    Iterator<PlayerData> activePlayers = _playersToAct.iterator();
                    PlayerData pl = null;
                    
                    while (activePlayers.hasNext()) {
                        i++;
                        if (skipBlinds && i <= 2) {
                            continue;
                        }
                        
                        pl = activePlayers.next();
                        activePlayers.remove();

                        if (pl.allIn()) {
                            _playersActed.add(pl);
                            continue;
                        }
                        
                        _plContext.setContext(pl.leftToCall(_amountCall), _pot, _bBlind, pl.stack());
                        IAction act = pl.player().action(this, _plContext);
                        
                        if (act == null || act.type() == ActionType.FOLD) {
                            _numberActivePlayers--;
                            broadcastGameAction(pl.player().id(), ActionType.FOLD, 0, pl.prioListeners());
                            broadcastGameAction(pl.player().id(), ActionType.FOLD, 0, pl.listeners());
                            broadcastGameAction(pl.player().id(), ActionType.FOLD, 0, _allEventsListeners);
                        }
                        else {
                            if (act.type() == ActionType.CALL) {
                                _playersActed.add(pl);
                                playerCall(pl);
                            }
                            if (act.type() == ActionType.BET) {
                                playerBet(pl, act.amount());
                                raised = true;
                            }
                            broadcastGameAction(pl.player().id(), act.type(), act.amount(), pl.prioListeners());
                            broadcastGameAction(pl.player().id(), act.type(), act.amount(), pl.listeners());
                            broadcastGameAction(pl.player().id(), act.type(), act.amount(), _allEventsListeners);
                            playerUpdateMaxWinnings(pl, act.amount());
                            if (raised) {
                                break;
                            }
                         }
                        
                    }
                    skipBlinds = false;
                    if (raised) {
                        _playersToAct.addAll(_playersActed);
                        _playersActed.clear();
                        _playersActed.add(pl);
                        _numberActivePlayers = _playersToAct.size() + 1;
                        _numberCallers = 0;
                    }
                }
                
                if (_currentRound == Round.RIVER) {
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
                                IHand oHand = ComponentFactory.buildHand(pl.player().holeCards(), _board);
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
                        }
                        
                        int winnings = _pot / (_splitters.size() + 1);
                        _pot -= winner.winPot(winnings);
                        for (PlayerData split : _splitters) {
                            _pot -= split.winPot(winnings);
                        }
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
            
            Iterator<PlayerData> plIt = _players.iterator();
            while (plIt.hasNext()) {
                PlayerData pl = plIt.next();
                
                if (pl.stack() <= 0) {
                    plIt.remove();
                    for (IPlayerActionListener listener : pl.prioListeners()) {
                        listener.leave(this, pl.player().id());
                    }
                    for (IPlayerActionListener listener : pl.listeners()) {
                        listener.leave(this, pl.player().id());
                    }
                    _deadPlayers.add(pl);
                }
            }
            
        }
    }
    
    private void broadcastGameAction(String id, ActionType type, int amount, List<IPlayerActionListener> listeners) {
        for (IPlayerActionListener listener : listeners) {
            listener.gameAction(this, id, _plContext, type, amount);
        }
    }

    private void playerBet(PlayerData pl, int amount) {
        _pot += pl.placeMoney(amount);
        _numberBets++;
        _amountCall += amount;
    }
    
    private void playerCall(PlayerData pl) {
        _pot += pl.placeMoney(pl.leftToCall(_amountCall));
        _numberCallers++;
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
    public Round round() {
        return _currentRound;
    }

    @Override
    public int numberActivePlayers() {
        return _numberActivePlayers;
    }

    @Override
    public List<Card> board() {
        return _board;
    }

    @Override
    public void registerListenerForPlayer(String id, IPlayerActionListener listener) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void unregisterListenerForPlayer(String id, IPlayerActionListener listener) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void registerPriorityListenerForPlayer(String id, IPlayerActionListener listener) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void unregisterPriorityListenerForPlayer(String id, IPlayerActionListener listener) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void registerEventListener(IGameEventListener listener) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void unregisterEventListener(IGameEventListener listener) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void registerListenAllPlayerEvents(IPlayerActionListener aThis) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}