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
package shaft.poker.factory;

import java.util.ArrayList;
import java.util.List;
import shaft.poker.agent.IBettingStrategy;
import shaft.poker.agent.IHandEvaluator;
import shaft.poker.agent.IHandRange;
import shaft.poker.agent.PlayerAgent;
import shaft.poker.agent.bettingstrategy.NeuralNetStrategy;
import shaft.poker.agent.bettingstrategy.SimpleStrategy;
import shaft.poker.agent.handevaluators.CompoundHandEval;
import shaft.poker.agent.handevaluators.PreflopHandGroups;
import shaft.poker.agent.handevaluators.enumeration.bucket.BucketEnum1CardHandEval;
import shaft.poker.agent.handranges.IPlayerRange;
import shaft.poker.agent.handranges.weightedrange.CompositeFieldRange;
import shaft.poker.agent.handranges.weightedrange.IFrequencyTable;
import shaft.poker.agent.handranges.weightedrange.PlayerWTRange;
import shaft.poker.agent.handranges.weightedrange.frequencytable.GenericFrequencyTable;
import shaft.poker.agent.handranges.weightedrange.frequencytable.PlayerSpecificFrequencyTable;
import shaft.poker.agent.handranges.weightedrange.weighttable.WeightTable;
import shaft.poker.game.*;
import shaft.poker.game.components.*;
import shaft.poker.game.table.PokerTable;
import shaft.poker.game.table.actionbuilder.LimitRules;
import shaft.poker.humanplayer.HumanPlayer;

/**
 *
 * @author Thomas Schaffer <thomas.schaffer@epitech.eu>
 */
public class PokerFactory {
    
    private List<IPlayer> _players;
    private PokerTable _table;
    
    private List<CompositeFieldRange> _fieldRanges;
    private List<IPlayerRange> _playerRanges;
    
    private boolean _genericModel;
    
    private static String[] _agentNames = {
        "Boris",
        "Jeff",
        "John",
        "Clark",
        "Marcel",
        "Mary",
        "Anna",
        "Joan",
        "Batman"
    };
    private static int _nameCount = 0;
    
    
    public PokerFactory() {
        _players = new ArrayList<>(10);
        _fieldRanges = new ArrayList<>(10);
        _playerRanges = new ArrayList<>(10);
    }
    
    public void genericModel(boolean generic) {
        _genericModel = generic;
    }
    
    private IFrequencyTable buildFreqTable(String id) {
        if (_genericModel) {
            return new GenericFrequencyTable();
        }
        return new PlayerSpecificFrequencyTable(id, _table);
    }
    
    public ITable newLimitTable() {
        _table = new PokerTable(new LimitRules());
        return _table;
    }
    
    public void addHumanPlayer() {
        CompositeFieldRange compRange = new CompositeFieldRange(_table, new WeightTable(_table));
        addPlayer(new HumanPlayer(_table), compRange);
    }
    
    public void addSimpleAgent() {
        addAgent(new BucketEnum1CardHandEval(), new SimpleStrategy());
    }
    
    public IPlayer addNeuralNetAgent() {
        return addAgent(new BucketEnum1CardHandEval(), new NeuralNetStrategy(_table));
    }
    
    private IPlayer addAgent(IHandEvaluator eval, IBettingStrategy strat) {
        String id = _agentNames[_nameCount++];
        CompositeFieldRange compRange = new CompositeFieldRange(_table, new WeightTable(_table));
        IPlayer agent = new PlayerAgent(_table, id, new CompoundHandEval(new PreflopHandGroups(), eval), compRange, strat);
        addPlayer(agent, compRange);
        return agent;
    }
    
    private void addPlayer(IPlayer player, CompositeFieldRange compRange) {
        _table.addPlayer(player);
        for (IPlayerRange range : _playerRanges) {
            compRange.addRange(range);
        }
        IPlayerRange ownRange = new PlayerWTRange(player.id(), new WeightTable(_table), buildFreqTable(player.id()),
                new CompoundHandEval(new PreflopHandGroups(), new BucketEnum1CardHandEval()), compRange, _table);
        for (CompositeFieldRange fieldRange : _fieldRanges) {
            fieldRange.addRange(ownRange);
        }
        _fieldRanges.add(compRange);
        _playerRanges.add(ownRange);
    }
    
    
        
    public static IDeck buildDeck() {
        return new CardDeck();
    }

    public static IHand buildHand(List<Card> holeCards, List<Card> board) {
        return new Hand(holeCards, board);
    }
}
