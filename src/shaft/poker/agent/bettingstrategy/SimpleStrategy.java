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

import shaft.poker.agent.IBettingStrategy;
import shaft.poker.agent.IHandEvaluator;
import shaft.poker.game.IAction;
import shaft.poker.game.ITable;
import shaft.poker.game.table.IActionBuilder;
import shaft.poker.game.table.IPlayerData;

/**
 *
 * @author Thomas Schaffer <thomas.schaffer@epitech.eu>
 */
public class SimpleStrategy implements IBettingStrategy {
    
    private static final double THRESHOLD_2BETS = 0.85;
    private static final double THRESHOLD_1BET = 0.50;
    private static int THRESHOLD_BLUFF = 10;

    @Override
    public IAction action(ITable table, IPlayerData plData, IActionBuilder actionBuild, IHandEvaluator eval) {
        double positionNeg = 0.10 * (((double)table.numberPlayersToAct()) / ((double) table.numberActivePlayers() - 1));
        if (eval.effectiveHandStrength() - positionNeg >= THRESHOLD_2BETS) {
            return doBets(table, plData, actionBuild, 2);
        }
        else if (eval.effectiveHandStrength() - positionNeg >= THRESHOLD_1BET) {
            return doBets(table, plData, actionBuild, 1);
        }
        else if (eval.posPotential() >= plData.potOdds(table.potSize())) {
            return actionBuild.makeCall();
        }
        else if (table.potSize() > THRESHOLD_BLUFF * table.bigBlind()) {
            if (table.numberPlayersToAct() == 0 && table.numberActivePlayers() <= 3) {
                if (table.numberBets() == 0) {
                    if (Math.random() <= 0.1) {
                        return actionBuild.makeRaise(actionBuild.minRaise());
                    }
                }
            }
        }
       if (plData.amountToCall() == 0) {
            return actionBuild.makeCall();
        }
       return actionBuild.makeFold();
    }

    private IAction doBets(ITable table, IPlayerData plData, IActionBuilder actionBuild, int n) {
        if (table.numberBets() < n) {
            return actionBuild.makeRaise(actionBuild.minRaise());
        }
        else if (plData.moneyInPotForRound() > 0 || plData.betsToCall() <= n) {
            return actionBuild.makeCall();
        }
        return actionBuild.makeFold();
    }
    
}
