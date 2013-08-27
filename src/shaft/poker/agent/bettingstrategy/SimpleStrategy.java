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
package shaft.poker.agent.bettingstrategy;

import java.util.List;
import shaft.poker.agent.IBettingStrategy;
import shaft.poker.agent.IHandEvaluator;
import shaft.poker.game.Card;
import shaft.poker.game.IAction;
import shaft.poker.game.ITable;
import shaft.poker.game.ITable.Round;
import shaft.poker.game.table.IActionBuilder;
import shaft.poker.game.table.IGameEventListener;
import shaft.poker.game.table.IPlayerData;

/**
 *
 * @author Thomas Schaffer <thomas.schaffer@epitech.eu>
 */
public class SimpleStrategy implements IBettingStrategy, IGameEventListener {
    
    private static final double VALUE_2BET = 0.85;
    private static final double VALUE_1BET = 0.50;
    
    private static final double BLINDSTEAL_RATE = 0.35;
    private static final double PREFLOP_LATE_LIMPS_RATE = 0.6;
    private static final double CONTINUATION_BET_RATE = 0.3;
    private static final double FLOP_POT_STEAL_RATE = 0.3;
    
    private boolean _preflopBetter;
    
    public SimpleStrategy(ITable table) {
        table.registerEventListener(this);
    }

    private IAction doBets(ITable table, IPlayerData plData, IActionBuilder actionBuild, int n) {
        if (table.numberBets() < n) {
            //System.out.println("Decision: VALUE BET");
            return actionBuild.makeRaise(actionBuild.minRaise());
        }
        else if (plData.moneyInPotForRound() > 0 || plData.betsToCall() <= n) {
            //System.out.println("Decision: VALUE CALL");
            _preflopBetter = false;
            return actionBuild.makeCall();
        }
        _preflopBetter = false;
        return actionBuild.makeFold();
    }
    
    @Override
    public IAction action(ITable table, List<Card> holeCards, IPlayerData plData, IActionBuilder actionBuild, IHandEvaluator eval) {
        
        Card c1 = holeCards.get(0);
        Card c2 = holeCards.get(1);
        
        if (c1.rank().ordinal() < c2.rank().ordinal()) {
            Card tmp = c1;
            c1 = c2;
            c2 = tmp;
        }
        
        // Variable malus [0.0..0.1] for early positions
        double positionNeg = 0.10 * (((double)table.numberPlayersToAct()) / ((double) table.numberActivePlayers() - 1));
        
        // Value bets
        if (eval.effectiveHandStrength() - positionNeg >= VALUE_2BET) {
            if (table.round() == Round.PREFLOP) {
                _preflopBetter = true;
            }
            return doBets(table, plData, actionBuild, 2);
        }
        else if (eval.effectiveHandStrength() - positionNeg >= VALUE_1BET) {
            if (table.round() == Round.PREFLOP) {
                _preflopBetter = true;
            }
            return doBets(table, plData, actionBuild, 1);
        }
        
        if (table.round() == Round.PREFLOP) {
            _preflopBetter = false;
            if (plData.position() == table.numberActivePlayers()) {
                // On the dealer button
                if (table.numberBets() == 0) {
                    // No action
                    if (Math.random() <= BLINDSTEAL_RATE) {
                        // Try to steal blinds 35% of the time
                        _preflopBetter = true;
                        //System.out.println("Decision: BLIND STEAL");
                        return actionBuild.makeRaise(actionBuild.minRaise());
                    }
                }
            }
            if (table.numberPlayersToAct() <= 1 && table.numberCallers() >= 2 && table.numberBets() <= 1) {
                if (c1.rank() == c2.rank()
                        || (c1.suit() == c2.suit()
                        && c1.rank().ordinal() == c2.rank().ordinal() + 1)) {
                    // Pocket pair or suited connectors
                    if (Math.random() <= PREFLOP_LATE_LIMPS_RATE) {
                        // Limp to try to flop a monster hand 60% of the time
                        //System.out.println("Decision: LATE CHEAP LIMP");
                        return actionBuild.makeCall();
                    }
                }
            }
        }
        else if (table.round() == Round.FLOP) {
            if (table.numberBets() == 0) {
                if (_preflopBetter) {
                    // Strong preflop image, no action, try a continuation bet 30% of the time
                    if (Math.random() <= CONTINUATION_BET_RATE) {
                        //System.out.println("Decision: CONTINUATION BET");
                        return actionBuild.makeRaise(actionBuild.minRaise());
                    }
                }
                if (plData.position() == table.numberActivePlayers()) {
                    // Last position, action checked to us
                    if (table.potSize() >= 5 * actionBuild.minRaise()) {
                        // Decent pot size
                        if (Math.random() <= FLOP_POT_STEAL_RATE) {
                            // Try to steal the pot 30% of the time
                            //System.out.println("Decision: POT STEAL");
                            return actionBuild.makeRaise(actionBuild.minRaise());
                        }
                    }
                }
            }
        }
        
        // Free check
        if (plData.amountToCall() == 0) {
            //System.out.println("Decision: FREE CHECK");
            return actionBuild.makeCall();
        }
        
        // Drawing hands, check pot odds
        if ((table.round() != Round.RIVER && eval.posPotential() >= plData.potOdds(table.potSize()))
                || eval.effectiveHandStrength() >= plData.potOdds(table.potSize())) {
            //System.out.println("Decision: DRAWING HAND / POT ODDS");
            return actionBuild.makeCall();
        }
        
        if (Math.random() <= (((double) plData.totalMoneyInPot()) / ((double) table.potSize())) / 1.5) {
            // Make the call now and then, depending on the amount already invested in the pot
            return actionBuild.makeCall();
        }
        
        // Last resort, fold the hand
        return actionBuild.makeFold();
    }

    @Override
    public void roundBegin(ITable table, ITable.Round r) {
        
    }

    @Override
    public void roundEnd(ITable table, ITable.Round r) {
        
    }

    @Override
    public void newHand(ITable table) {
        _preflopBetter = false;
    }

    @Override
    public void newGame(ITable table, int stackSize, int sBlind, int bBlind, List<String> players) {
        
    }

    @Override
    public void winHand(ITable table, IPlayerData data, int amount) {
        
    }
    
}
