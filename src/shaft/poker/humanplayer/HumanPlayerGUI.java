/*
 * The MIT License
 *
 * Copyright 2013 loop.
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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import shaft.poker.game.Card;
import shaft.poker.game.IAction;
import shaft.poker.game.IPlayer;
import shaft.poker.game.ITable;
import shaft.poker.game.table.IActionBuilder;
import shaft.poker.game.table.IGameEventListener;
import shaft.poker.game.table.IPlayerActionListener;
import shaft.poker.game.table.IPlayerData;

/**
 *
 * @author loop
 */
public class HumanPlayerGUI extends javax.swing.JFrame implements IPlayer, IGameEventListener, IPlayerActionListener {
    
        
    private String _id;
    private final static String _iconPath = "img/";
    private ImageIcon[][] _cardImgs;
    
    private List<JLabel> boardCards;
    private List<JLabel> plIds;
    private List<JLabel> plAmts;
    private List<JLabel> plSts;
    
    private List<Card> _holeCards;
    private int _stack;
    
    private List<String> _players;
    
    private IActionBuilder _actionBuild;
    private IAction _chosenAction;

    /**
     * Creates new form TestGUI
     */
    public HumanPlayerGUI(ITable table) {
        initComponents();
        
        askId();
        _cardImgs = new ImageIcon[Card.Suit.values().length][Card.Rank.values().length];
        for (Card.Suit s : Card.Suit.values()) {
            for (Card.Rank r : Card.Rank.values()) {
                _cardImgs[s.ordinal()][r.ordinal()] = loadCard(r, s);
                if (_cardImgs[s.ordinal()][r.ordinal()] == null) {
                    System.out.println("got null card");
                }
            }
        }
        
        _holeCards = new ArrayList<>(2);        
        boardCards = new ArrayList<>(5);
        plIds = new ArrayList<>(8);
        plAmts = new ArrayList<>(8);
        plSts = new ArrayList<>(8);
        
        plIds.add(P1Name);
        plIds.add(P2Name);
        plIds.add(P3Name);
        plIds.add(P4Name);
        plIds.add(P5Name);
        plIds.add(P6Name);
        plIds.add(P7Name);
        plIds.add(P8Name);
        
        plAmts.add(P1Amt);
        plAmts.add(P2Amt);
        plAmts.add(P3Amt);
        plAmts.add(P4Amt);
        plAmts.add(P5Amt);
        plAmts.add(P6Amt);
        plAmts.add(P7Amt);
        plAmts.add(P8Amt);
        
        plSts.add(P1St);
        plSts.add(P2St);
        plSts.add(P3St);
        plSts.add(P4St);
        plSts.add(P5St);
        plSts.add(P6St);
        plSts.add(P7St);
        plSts.add(P8St);
        
        boardCards.add(boardC1);
        boardCards.add(boardC2);
        boardCards.add(boardC3);
        boardCards.add(boardC4);
        boardCards.add(boardC5);
        
        table.registerEventListener(this);
        table.registerListenAllPlayerEvents(this);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setVisible(true);
                setResizable(false);
            } 
        });
    }
    
    private ImageIcon loadCard(Card.Rank rank, Card.Suit suit) {
        String path = _iconPath + rank.toString() + "_" + suit.toString() + "2.png";
        
        ImageIcon ret = new ImageIcon(path);
        
        System.out.println("Loaded " + rank.toString() + " of " + suit.toString() + " h:" + ret.getIconHeight() + " w:" + ret.getIconWidth());
        
        return ret;
    }
    
    private void askId() {
        while (_id == null) {
            String id = (String) JOptionPane.showInputDialog("Input id");
            if (id != null && id.length() >= 3) {
                _id = id;
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        PanelP2 = new javax.swing.JPanel();
        P2Name = new javax.swing.JLabel();
        P2Amt = new javax.swing.JLabel();
        P2St = new javax.swing.JLabel();
        PanelP3 = new javax.swing.JPanel();
        P3Name = new javax.swing.JLabel();
        P3Amt = new javax.swing.JLabel();
        P3St = new javax.swing.JLabel();
        PanelP7 = new javax.swing.JPanel();
        P7Name = new javax.swing.JLabel();
        P7Amt = new javax.swing.JLabel();
        P7St = new javax.swing.JLabel();
        PanelP6 = new javax.swing.JPanel();
        P6Name = new javax.swing.JLabel();
        P6Amt = new javax.swing.JLabel();
        P6St = new javax.swing.JLabel();
        PanelP5 = new javax.swing.JPanel();
        P5Name = new javax.swing.JLabel();
        P5Amt = new javax.swing.JLabel();
        P5St = new javax.swing.JLabel();
        PanelP1 = new javax.swing.JPanel();
        P1Name = new javax.swing.JLabel();
        P1Amt = new javax.swing.JLabel();
        P1St = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        PanelP4 = new javax.swing.JPanel();
        P4Name = new javax.swing.JLabel();
        P4Amt = new javax.swing.JLabel();
        P4St = new javax.swing.JLabel();
        PanelP8 = new javax.swing.JPanel();
        P8Name = new javax.swing.JLabel();
        P8Amt = new javax.swing.JLabel();
        P8St = new javax.swing.JLabel();
        PanelBoard = new javax.swing.JPanel();
        boardC1 = new javax.swing.JLabel();
        boardC2 = new javax.swing.JLabel();
        boardC3 = new javax.swing.JLabel();
        boardC4 = new javax.swing.JLabel();
        boardC5 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        IdLabel = new javax.swing.JLabel();
        StackLabel = new javax.swing.JLabel();
        holeC1 = new javax.swing.JLabel();
        holeC2 = new javax.swing.JLabel();
        potLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        logArea = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();
        foldBtn = new javax.swing.JButton();
        callBtn = new javax.swing.JButton();
        betBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        PanelP2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        P2Name.setText("Player2");

        P2Amt.setText("P2AMT");

        P2St.setText("P2ST");

        javax.swing.GroupLayout PanelP2Layout = new javax.swing.GroupLayout(PanelP2);
        PanelP2.setLayout(PanelP2Layout);
        PanelP2Layout.setHorizontalGroup(
            PanelP2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelP2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelP2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(P2Name)
                    .addComponent(P2Amt)
                    .addComponent(P2St))
                .addContainerGap(43, Short.MAX_VALUE))
        );
        PanelP2Layout.setVerticalGroup(
            PanelP2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelP2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(P2Name)
                .addGap(18, 18, 18)
                .addComponent(P2Amt)
                .addGap(18, 18, 18)
                .addComponent(P2St)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel1.add(PanelP2, gridBagConstraints);

        PanelP3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        P3Name.setText("Player3");

        P3Amt.setText("P3AMT");

        P3St.setText("P3ST");

        javax.swing.GroupLayout PanelP3Layout = new javax.swing.GroupLayout(PanelP3);
        PanelP3.setLayout(PanelP3Layout);
        PanelP3Layout.setHorizontalGroup(
            PanelP3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelP3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelP3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(P3St)
                    .addComponent(P3Name)
                    .addComponent(P3Amt))
                .addContainerGap(57, Short.MAX_VALUE))
        );
        PanelP3Layout.setVerticalGroup(
            PanelP3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelP3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(P3Name)
                .addGap(18, 18, 18)
                .addComponent(P3Amt)
                .addGap(18, 18, 18)
                .addComponent(P3St)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel1.add(PanelP3, gridBagConstraints);

        PanelP7.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        P7Name.setText("Player7");

        P7Amt.setText("P7AMT");

        P7St.setText("P7ST");

        javax.swing.GroupLayout PanelP7Layout = new javax.swing.GroupLayout(PanelP7);
        PanelP7.setLayout(PanelP7Layout);
        PanelP7Layout.setHorizontalGroup(
            PanelP7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelP7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelP7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(P7Name)
                    .addComponent(P7Amt)
                    .addComponent(P7St))
                .addContainerGap(29, Short.MAX_VALUE))
        );
        PanelP7Layout.setVerticalGroup(
            PanelP7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelP7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(P7Name)
                .addGap(18, 18, 18)
                .addComponent(P7Amt)
                .addGap(18, 18, 18)
                .addComponent(P7St)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel1.add(PanelP7, gridBagConstraints);

        PanelP6.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        P6Name.setText("Player6");

        P6Amt.setText("P6AMT");

        P6St.setText("P6ST");

        javax.swing.GroupLayout PanelP6Layout = new javax.swing.GroupLayout(PanelP6);
        PanelP6.setLayout(PanelP6Layout);
        PanelP6Layout.setHorizontalGroup(
            PanelP6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelP6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelP6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(P6Name)
                    .addComponent(P6Amt)
                    .addComponent(P6St))
                .addContainerGap(43, Short.MAX_VALUE))
        );
        PanelP6Layout.setVerticalGroup(
            PanelP6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelP6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(P6Name)
                .addGap(18, 18, 18)
                .addComponent(P6Amt)
                .addGap(18, 18, 18)
                .addComponent(P6St)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel1.add(PanelP6, gridBagConstraints);

        PanelP5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        P5Name.setText("Player5");

        P5Amt.setText("P5AMT");

        P5St.setText("P5ST");

        javax.swing.GroupLayout PanelP5Layout = new javax.swing.GroupLayout(PanelP5);
        PanelP5.setLayout(PanelP5Layout);
        PanelP5Layout.setHorizontalGroup(
            PanelP5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelP5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelP5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(P5Name)
                    .addComponent(P5Amt)
                    .addComponent(P5St))
                .addContainerGap(57, Short.MAX_VALUE))
        );
        PanelP5Layout.setVerticalGroup(
            PanelP5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelP5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(P5Name)
                .addGap(18, 18, 18)
                .addComponent(P5Amt)
                .addGap(18, 18, 18)
                .addComponent(P5St)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel1.add(PanelP5, gridBagConstraints);

        PanelP1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        P1Name.setText("Player1");

        P1Amt.setText("P1AMT");

        P1St.setText("P1ST");

        javax.swing.GroupLayout PanelP1Layout = new javax.swing.GroupLayout(PanelP1);
        PanelP1.setLayout(PanelP1Layout);
        PanelP1Layout.setHorizontalGroup(
            PanelP1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelP1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelP1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(P1St)
                    .addComponent(P1Name)
                    .addComponent(P1Amt))
                .addContainerGap(29, Short.MAX_VALUE))
        );
        PanelP1Layout.setVerticalGroup(
            PanelP1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelP1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(P1Name)
                .addGap(18, 18, 18)
                .addComponent(P1Amt)
                .addGap(18, 18, 18)
                .addComponent(P1St)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel1.add(PanelP1, gridBagConstraints);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel8, new java.awt.GridBagConstraints());

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel9, new java.awt.GridBagConstraints());

        PanelP4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        P4Name.setText("Player4");

        P4Amt.setText("P4AMT");

        P4St.setText("P4ST");

        javax.swing.GroupLayout PanelP4Layout = new javax.swing.GroupLayout(PanelP4);
        PanelP4.setLayout(PanelP4Layout);
        PanelP4Layout.setHorizontalGroup(
            PanelP4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelP4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelP4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(P4Name)
                    .addComponent(P4Amt)
                    .addComponent(P4St))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        PanelP4Layout.setVerticalGroup(
            PanelP4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelP4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(P4Name)
                .addGap(18, 18, 18)
                .addComponent(P4Amt)
                .addGap(18, 18, 18)
                .addComponent(P4St)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel1.add(PanelP4, gridBagConstraints);

        PanelP8.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        P8Name.setText("Player8");

        P8Amt.setText("P8AMT");

        P8St.setText("P8ST");

        javax.swing.GroupLayout PanelP8Layout = new javax.swing.GroupLayout(PanelP8);
        PanelP8.setLayout(PanelP8Layout);
        PanelP8Layout.setHorizontalGroup(
            PanelP8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelP8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelP8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(P8St)
                    .addComponent(P8Name)
                    .addComponent(P8Amt))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        PanelP8Layout.setVerticalGroup(
            PanelP8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelP8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(P8Name)
                .addGap(18, 18, 18)
                .addComponent(P8Amt)
                .addGap(18, 18, 18)
                .addComponent(P8St)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel1.add(PanelP8, gridBagConstraints);

        PanelBoard.setBackground(new java.awt.Color(64, 128, 64));
        PanelBoard.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        boardC1.setText("Card1");

        boardC2.setText("Card2");

        boardC3.setText("Card3");

        boardC4.setText("Card4");

        boardC5.setText("Card5");

        javax.swing.GroupLayout PanelBoardLayout = new javax.swing.GroupLayout(PanelBoard);
        PanelBoard.setLayout(PanelBoardLayout);
        PanelBoardLayout.setHorizontalGroup(
            PanelBoardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelBoardLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(boardC1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(boardC2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(boardC3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(boardC4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(boardC5)
                .addContainerGap(93, Short.MAX_VALUE))
        );
        PanelBoardLayout.setVerticalGroup(
            PanelBoardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelBoardLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelBoardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(boardC1)
                    .addComponent(boardC2)
                    .addComponent(boardC3)
                    .addComponent(boardC4)
                    .addComponent(boardC5))
                .addContainerGap(86, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel1.add(PanelBoard, gridBagConstraints);

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        IdLabel.setText("Id");

        StackLabel.setText("Stack");

        holeC1.setText("Card1");

        holeC2.setText("Card2");

        potLabel.setText("Pot");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(StackLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(potLabel))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(IdLabel)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(holeC1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(holeC2)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(IdLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(StackLabel)
                    .addComponent(potLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(holeC1)
                    .addComponent(holeC2))
                .addContainerGap(71, Short.MAX_VALUE))
        );

        logArea.setColumns(20);
        logArea.setRows(5);
        jScrollPane1.setViewportView(logArea);

        foldBtn.setText("Fold");
        foldBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                foldBtnActionPerformed(evt);
            }
        });

        callBtn.setText("Check");
        callBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                callBtnActionPerformed(evt);
            }
        });

        betBtn.setText("Bet");
        betBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                betBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(foldBtn)
                .addGap(18, 18, 18)
                .addComponent(callBtn)
                .addGap(18, 18, 18)
                .addComponent(betBtn)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(foldBtn)
                    .addComponent(callBtn)
                    .addComponent(betBtn))
                .addContainerGap(39, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 603, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void betBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_betBtnActionPerformed
        _chosenAction = _actionBuild.makeRaise(_actionBuild.minRaise());
    }//GEN-LAST:event_betBtnActionPerformed

    private void callBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_callBtnActionPerformed
        _chosenAction = _actionBuild.makeCall();
    }//GEN-LAST:event_callBtnActionPerformed

    private void foldBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_foldBtnActionPerformed
        _chosenAction = _actionBuild.makeFold();
    }//GEN-LAST:event_foldBtnActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(HumanPlayerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HumanPlayerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HumanPlayerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HumanPlayerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new TestGUI().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel IdLabel;
    private javax.swing.JLabel P1Amt;
    private javax.swing.JLabel P1Name;
    private javax.swing.JLabel P1St;
    private javax.swing.JLabel P2Amt;
    private javax.swing.JLabel P2Name;
    private javax.swing.JLabel P2St;
    private javax.swing.JLabel P3Amt;
    private javax.swing.JLabel P3Name;
    private javax.swing.JLabel P3St;
    private javax.swing.JLabel P4Amt;
    private javax.swing.JLabel P4Name;
    private javax.swing.JLabel P4St;
    private javax.swing.JLabel P5Amt;
    private javax.swing.JLabel P5Name;
    private javax.swing.JLabel P5St;
    private javax.swing.JLabel P6Amt;
    private javax.swing.JLabel P6Name;
    private javax.swing.JLabel P6St;
    private javax.swing.JLabel P7Amt;
    private javax.swing.JLabel P7Name;
    private javax.swing.JLabel P7St;
    private javax.swing.JLabel P8Amt;
    private javax.swing.JLabel P8Name;
    private javax.swing.JLabel P8St;
    private javax.swing.JPanel PanelBoard;
    private javax.swing.JPanel PanelP1;
    private javax.swing.JPanel PanelP2;
    private javax.swing.JPanel PanelP3;
    private javax.swing.JPanel PanelP4;
    private javax.swing.JPanel PanelP5;
    private javax.swing.JPanel PanelP6;
    private javax.swing.JPanel PanelP7;
    private javax.swing.JPanel PanelP8;
    private javax.swing.JLabel StackLabel;
    private javax.swing.JButton betBtn;
    private javax.swing.JLabel boardC1;
    private javax.swing.JLabel boardC2;
    private javax.swing.JLabel boardC3;
    private javax.swing.JLabel boardC4;
    private javax.swing.JLabel boardC5;
    private javax.swing.JButton callBtn;
    private javax.swing.JButton foldBtn;
    private javax.swing.JLabel holeC1;
    private javax.swing.JLabel holeC2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea logArea;
    private javax.swing.JLabel potLabel;
    // End of variables declaration//GEN-END:variables

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
    public IAction action(ITable table, final IPlayerData plData, final IActionBuilder actionBuild) {
        final JFrame f = this;
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    _actionBuild = actionBuild;
                    
                    String opt2;
                    if (plData.amountToCall() == 0) {
                        opt2 = "Check";
                    }
                    else {
                        opt2 = "Call";
                    }
                    
                    callBtn.setText(opt2);
                    
                    foldBtn.setVisible(true);
                    callBtn.setVisible(true);
                    betBtn.setVisible(true);
                }
            });
        } catch (Exception e) {
            
        }

        
        while (_chosenAction == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                
            }
        }
                
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                foldBtn.setVisible(false);
                callBtn.setVisible(false);
                betBtn.setVisible(false);
            }
        });
        
        IAction ret = _chosenAction;
        _chosenAction = null;
        _actionBuild = null;
        
        return ret;
    }

    @Override
    public void setStack(int stack) {
        _stack = stack;
        StackLabel.setText("Stack: " + stack);
    }

    @Override
    public int stack() {
        return _stack;
    }

    @Override
    public void roundBegin(final ITable table, final ITable.Round r) {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    for (JLabel amt : plAmts) {
                        amt.setText("");
                    }
                    holeC1.setIcon(_cardImgs[_holeCards.get(0).suit().ordinal()][holeCards().get(0).rank().ordinal()]);
                    holeC2.setIcon(_cardImgs[_holeCards.get(1).suit().ordinal()][holeCards().get(1).rank().ordinal()]);
                    holeC1.setText("");
                    holeC2.setText("");
                    holeC1.setVisible(true);
                    holeC2.setVisible(true);
                    
                    int i = 0;
                    for (Card c : table.board()) {
                        boardCards.get(i).setIcon(_cardImgs[c.suit().ordinal()][c.rank().ordinal()]);
                        boardCards.get(i).setVisible(true);
                        i++;
                    }
                    
                    logArea.append("%%% Round [" + r.toString() + "] is starting %%%\n");
                }
            });
        } catch (Exception e) {
            
        }
    }

    @Override
    public void roundEnd(ITable table, ITable.Round r) {
        
    }

    @Override
    public void newHand(final ITable table) {
        _holeCards.clear();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                potLabel.setText("Pot: 0");
                for (JLabel boardCard : boardCards) {
                    boardCard.setVisible(false);
                    boardCard.setText("");
                }
                for (JLabel plAmt : plAmts) {
                    plAmt.setVisible(false);
                }
                for (JLabel plSt : plSts) {
                    plSt.setVisible(false);
                }
                
                plSts.get(getPlayer(table.playerSmallBlind())).setText("SB");
                plSts.get(getPlayer(table.playerBigBlind())).setText("BB");
                if (table.numberPlayers() == 2) {
                    plSts.get(getPlayer(table.playerSmallBlind())).setText("[D] SB");                    
                }
                else {
                    plSts.get(getPlayer(table.playerDealer())).setText("[D]");
                }
                plSts.get(getPlayer(table.playerSmallBlind())).setVisible(true);
                plSts.get(getPlayer(table.playerBigBlind())).setVisible(true);
                plSts.get(getPlayer(table.playerDealer())).setVisible(true);

                holeC1.setVisible(false);
                holeC2.setVisible(false);
            }

        });
    }

    @Override
    public void newGame(ITable table, final int stackSize, int sBlind, int bBlind, final List<String> players) {
        SwingUtilities.invokeLater(new Runnable() {       
            @Override
            public void run() {
                _players = players;
                IdLabel.setText(_id);
                logArea.setText("");
                for (JLabel plId : plIds) {
                    plId.setVisible(false);
                }

                int i = 0;
                for (String pl : _players) {
                    plIds.get(i).setText(pl + " (" + stackSize + ")");
                    plIds.get(i).setVisible(true);
                    i++;
                }
            }
        });
    }
    
    private int getPlayer(String id) {
        int i = 0;
        for (String pl : _players) {
            if (id.equals(pl)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    @Override
    public void winHand(ITable table, final IPlayerData data, final int amount) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setPlayerData(data);
                logArea.append("### [" + data.id() + "] wins the hand (" + amount + ")\n");
            }
        });
    }

    @Override
    public void gameAction(final ITable table, final IPlayerData plData, final ITable.ActionType type, final int amount) {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    setPlayerData(plData);
                    if (amount == 0) {
                        logArea.append("[" + plData.id() + "] ACTION: " + type.toString() + "\n");                    
                    }
                    else {
                        logArea.append("[" + plData.id() + "] ACTION: " + type.toString() + " (" + amount + ")\n");                                        
                    }
                    potLabel.setText("Pot: " + table.potSize());
                }
            });
        } catch (Exception e) {
            
        }
    }
    
    private void setPlayerData(IPlayerData plData) {
        int idx = getPlayer(plData.id());
        
        plIds.get(idx).setText(plData.id() + " (" + plData.stack() + ")");
        if (plData.moneyInPotForRound() > 0) {
            plAmts.get(idx).setText(Integer.toString(plData.moneyInPotForRound()));
            plAmts.get(idx).setVisible(true);
        }
    }

    @Override
    public void leave(ITable table, final IPlayerData plData) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                int idx = getPlayer(plData.id());
                plIds.get(idx).setVisible(false);
                plAmts.get(idx).setVisible(false);
                plSts.get(idx).setVisible(false);
                logArea.append("[" + plData.id() + "] left the table\n");
            }
        });
    }
}
