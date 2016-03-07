package org.joedog.pinochle.view;
/**
 * Copyright (C) 2013-2016
 * Jeffrey Fulmer - <jeff@joedog.org>, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *--
 */

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Component;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JRootPane;
import javax.swing.DefaultCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.event.*;
import java.util.Enumeration;

import org.joedog.pinochle.control.Game;
import org.joedog.pinochle.view.actions.*;

public class Configuration extends JFrame implements Viewable {
  private JLabel header = new JLabel(" Set Application Preferences"); 
  private JLabel labelPlayers;
  private JLabel labelEastName;
  private JLabel labelWestName;
  private JLabel labelNorthName;
  private JLabel labelSouthName;
  private JLabel labelGame;
  private JLabel labelScore;
  private JLabel labelDeckSize;
  private JLabel labelVariations;
  private JLabel labelMinimumBid;
  private JLabel labelBidType;
  private JLabel labelOverstick;
  private FocusTextField textEastName;
  private FocusTextField textWestName;
  private FocusTextField textNorthName;
  private FocusTextField textSouthName;
  private FocusTextField textWinningScore;
  private FocusTextField textMinimumBid;
  private JRadioButton playSingle;
  private JRadioButton playDouble;
  private JRadioButton playSingleBid;
  private JRadioButton playAuctionBid;
  private JRadioButton playTopTrump;
  private JRadioButton playTopAll;
  private ButtonGroup  deckGroup;
  private ButtonGroup  bidGroup;
  private ButtonGroup  topGroup;
  private JButton    saveButton;
  private JPanel south;
  private JPanel configPanel;
  private JPanel deckSelectPanel;
  private JPanel bidSelectPanel;
  private JPanel topSelectPanel;
  private JScrollPane scroll;
  private Container pinochle;
  private String playerEastName;
  private String playerWestName;
  private String playerNorthName;
  private String playerSouthName;
  private JComboBox  playerEastType;
  private JComboBox  playerWestType;
  private JComboBox  playerNorthType;
  private JComboBox  playerSouthType;
  private DefaultComboBoxModel comboBoxModel;
  private Game control;
  private int width;
  private int height;
  
  public Configuration (Game control) {
    super("Pinochle Configuration");
    this.control      = control;
    this.control.addView(this);
    this.playerEastName  = control.getModelStringProperty("PlayerEastName");
    this.playerWestName  = control.getModelStringProperty("PlayerWestName");
    this.playerNorthName = control.getModelStringProperty("PlayerNorthName");
    this.playerSouthName = control.getModelStringProperty("PlayerSouthName");
    this.display();
  }

  public void display() {
    pinochle = this.getContentPane();
    pinochle.setLayout(new BorderLayout());
    int [] point = control.getModelArrayProperty("ConfigPosition");
    if (point == null) {
      point = new int [] {10, 10};
    }
    this.width  = 480;
    this.height = 520;
    this.setPreferredSize(new Dimension(this.width,this.height));
    this.setDefaultCloseOperation(control.save());
    this.scroll = new JScrollPane(getJPanelConfig());
    this.south  = new JPanel();
    this.add(header, BorderLayout.NORTH);
    south.add(getButtonSave());
    pinochle.add(scroll, BorderLayout.CENTER);
    pinochle.add(south,  BorderLayout.SOUTH);
    JRootPane root = this.getRootPane();
    root.setDefaultButton(saveButton);
    this.addWindowListener(new WindowAdapter() {
      public void windowGainedFocus(WindowEvent e) {
        saveButton.requestFocusInWindow();
      }
    });
    this.pack();
    this.setVisible(true);
    this.setLocation(point[0], point[1]);
  }

  private JPanel getJPanelConfig() {
    if (configPanel == null) {
      labelPlayers  = new JLabel();
      labelPlayers.setBounds(new Rectangle(19, 10, 160, 16));
      labelPlayers.setText("<html><b>Pinochle Players</b></html>");
      labelEastName = new JLabel();
      labelEastName.setBounds(new Rectangle(19, 34, 120, 16));
      labelEastName.setText("East Name:");
      labelWestName = new JLabel();
      labelWestName.setBounds(new Rectangle(19, 58, 120, 16));
      labelWestName.setText("West Name:");
      labelNorthName = new JLabel();
      labelNorthName.setBounds(new Rectangle(19, 82, 120, 16));
      labelNorthName.setText("North Name:");
      labelSouthName = new JLabel();
      labelSouthName.setBounds(new Rectangle(19, 106, 120, 16));
      labelSouthName.setText("South Name:");
      labelGame  = new JLabel();
      labelGame.setBounds(new Rectangle(19, 142, 120, 24));
      labelGame.setText("<html><b>Game Setup</b></html>");
      labelScore  = new JLabel();
      labelScore.setBounds(new Rectangle(19, 166, 120, 24));
      labelScore.setText("Winning Score:");
      labelMinimumBid  = new JLabel();
      labelMinimumBid.setBounds(new Rectangle(19, 190, 120, 24));
      labelMinimumBid.setText("Minimum Bid:");
      labelDeckSize  = new JLabel();
      labelDeckSize.setBounds(new Rectangle(19, 214, 120, 24));
      labelDeckSize.setText("Deck Size:");
      labelVariations  = new JLabel();
      labelVariations.setBounds(new Rectangle(19, 238, 120, 24));
      labelVariations.setText("<html><b>Variations:</b></html>");
      labelBidType  = new JLabel();
      labelBidType.setBounds(new Rectangle(19, 262, 120, 24));
      labelBidType.setText("Bid Type:");
      labelOverstick = new JLabel();
      labelOverstick.setBounds(new Rectangle(19, 286, 120, 24));
      labelOverstick.setText("Overstick rule: ");
      configPanel = new JPanel();
      configPanel.setLayout(null);
      configPanel.add(labelPlayers,  null);
      configPanel.add(labelEastName, null);
      configPanel.add(labelWestName, null);
      configPanel.add(labelNorthName, null);
      configPanel.add(labelSouthName, null);
      configPanel.add(getEastNameField(), null);
      configPanel.add(getWestNameField(), null);
      configPanel.add(getNorthNameField(), null);
      configPanel.add(getSouthNameField(), null);
      configPanel.add(getEastTypeBox(), null);
      configPanel.add(getWestTypeBox(), null);
      configPanel.add(getNorthTypeBox(), null);
      configPanel.add(getSouthTypeBox(), null);
      configPanel.add(labelGame, null);
      configPanel.add(labelScore, null);
      configPanel.add(getWinningScoreField(), null);
      configPanel.add(labelMinimumBid, null);
      configPanel.add(getMinimumBidField(), null);
      configPanel.add(labelBidType, null);
      configPanel.add(getBidSelectPanel(), null);
      configPanel.add(labelDeckSize, null);
      configPanel.add(getDeckSelectPanel(), null);
      configPanel.add(labelVariations, null);
      configPanel.add(labelOverstick, null);
      configPanel.add(getTopSelectPanel(), null);
    }
    return configPanel;
  }

  private FocusTextField getEastNameField() {
    if (textEastName == null) {
      textEastName = new FocusTextField(control.getModelStringProperty("PlayerEastName"));
      textEastName.setBounds(new Rectangle(136, 34, 161, 20));
    }
    return textEastName;
  }

  private FocusTextField getWestNameField() {
    if (textWestName == null) {
      textWestName = new FocusTextField(control.getModelStringProperty("PlayerWestName"));
      textWestName.setBounds(new Rectangle(136, 58, 161, 20));
    }
    return textWestName;
  }

  private FocusTextField getNorthNameField() {
    if (textNorthName == null) {
      textNorthName = new FocusTextField(control.getModelStringProperty("PlayerNorthName"));
      textNorthName.setBounds(new Rectangle(136, 82, 161, 20));
    }
    return textNorthName;
  }

  private FocusTextField getSouthNameField() {
    if (textSouthName == null) {
      textSouthName = new FocusTextField(control.getModelStringProperty("PlayerSouthName"));
      textSouthName.setBounds(new Rectangle(136, 106, 161, 20));
    }
    return textSouthName;
  }

  private FocusTextField getWinningScoreField() {
    if (textWinningScore == null) {
      textWinningScore = new FocusTextField(control.getModelStringProperty("WinningScore"));
      textWinningScore.setBounds(new Rectangle(136, 166, 161, 20));
    }
    return textWinningScore;
  }

  private FocusTextField getMinimumBidField() {
    if (textMinimumBid == null) {
      textMinimumBid = new FocusTextField(control.getModelStringProperty("MinimumBid"));
      textMinimumBid.setBounds(new Rectangle(136, 190, 161, 20));
    }
    return textMinimumBid;
  }

  private JComboBox getEastTypeBox() {
    if (playerEastType == null) {
       int i = 1;
       comboBoxModel  = new DefaultComboBoxModel();
       comboBoxModel.addElement("Human");
       comboBoxModel.addElement("Computer");
       playerEastType = new JComboBox();
       playerEastType.setModel(comboBoxModel);
       playerEastType.setBounds(new Rectangle(304, 34, 110, 20));
       i = control.getModelIntProperty("PlayerEastType"); 
       if (i < 0) i = 1;
       playerEastType.setSelectedIndex(i);
    }
    return playerEastType;
  }

  private JComboBox getWestTypeBox() {
    if (playerWestType == null) {
       int i = 1;
       comboBoxModel  = new DefaultComboBoxModel();
       comboBoxModel.addElement("Human");
       comboBoxModel.addElement("Computer");
       playerWestType = new JComboBox();
       playerWestType.setModel(comboBoxModel);
       playerWestType.setBounds(new Rectangle(304, 58, 110, 20));
       i = control.getModelIntProperty("PlayerWestType");
       if (i < 0) i = 1;
       playerWestType.setSelectedIndex(i);
    }
    return playerWestType;
  }

  private JComboBox getNorthTypeBox() {
    if (playerNorthType == null) {
       int i = 1;
       comboBoxModel  = new DefaultComboBoxModel();
       comboBoxModel.addElement("Human");
       comboBoxModel.addElement("Computer");
       playerNorthType = new JComboBox();
       playerNorthType.setModel(comboBoxModel);
       playerNorthType.setBounds(new Rectangle(304, 82, 110, 20));
       i = control.getModelIntProperty("PlayerNorthType");
       if (i < 0) i = 1;
       playerNorthType.setSelectedIndex(i);
    }
    return playerNorthType;
  }

  private JComboBox getSouthTypeBox() {
    if (playerSouthType == null) {
       int i = 0;
       comboBoxModel  = new DefaultComboBoxModel();
       comboBoxModel.addElement("Human");
       comboBoxModel.addElement("Computer");
       playerSouthType = new JComboBox();
       playerSouthType.setModel(comboBoxModel);
       playerSouthType.setBounds(new Rectangle(304, 106, 110, 20));
       i = control.getModelIntProperty("PlayerSouthType");
       if (i < 0) i = 0;
       playerSouthType.setSelectedIndex(i);
    }
    return playerSouthType;
  }

  private JPanel getBidSelectPanel() {
    if (playAuctionBid == null) {
      playAuctionBid = new JRadioButton("Auction Bid");
      playAuctionBid.setActionCommand("auction");
      //playAuctionBid.addActionListener(new BidButtonListener(this.control));
    }
    if (playSingleBid == null) {
      playSingleBid = new JRadioButton("Single Bid");
      playSingleBid.setActionCommand("single");
      //playSingleBid.addActionListener(new BidButtonListener(this.control));
    }
    if (bidSelectPanel == null) {
      bidSelectPanel = new JPanel();
      bidGroup       = new ButtonGroup();
      bidGroup.add(playAuctionBid);
      bidGroup.add(playSingleBid);
      if ((control.getModelStringProperty("BidVariation")).equals("auction")) {
        playAuctionBid.setSelected(true);
      } else {
        playSingleBid.setSelected(true);
      }
      bidSelectPanel.setLayout(new java.awt.GridLayout(1, 2));
      bidSelectPanel.add(playAuctionBid);
      bidSelectPanel.add(playSingleBid);
      bidSelectPanel.setBounds(new Rectangle(136, 262, 220, 20));
    }
    return bidSelectPanel;
  }

  private JPanel getDeckSelectPanel() {
    if (playSingle == null) {
      playSingle = new JRadioButton("Single");
      playSingle.setActionCommand("single");
      //playSingle.addActionListener(new DeckButtonListener(this.control));
    }
    if (playDouble == null) {
      playDouble = new JRadioButton("Double");
      playDouble.setActionCommand("double");
      //playDouble.addActionListener(new DeckButtonListener(this.control));
      playDouble.setEnabled(false);
    }
    if (deckSelectPanel == null) {
      deckSelectPanel = new JPanel();
      deckGroup       = new ButtonGroup();
      deckGroup.add(playSingleBid);
      deckGroup.add(playAuctionBid);
      if (control.getModelIntProperty("DeckSize") == 2) {
        playDouble.setSelected(true);
      } else {
        playSingle.setSelected(true);
      }
      deckSelectPanel.setLayout(new java.awt.GridLayout(1, 2));
      deckSelectPanel.add(playSingle);
      deckSelectPanel.add(playDouble);
      deckSelectPanel.setBounds(new Rectangle(136, 214, 220, 20));
    }
    return deckSelectPanel;
  }

  private JPanel getTopSelectPanel() {
    if (playTopTrump == null) {
      playTopTrump = new JRadioButton("Trump Only");
      playTopTrump.setActionCommand("trump");
      //playTopTrump.addActionListener(new TopButtonListener(this.control));
    }
    if (playTopAll == null) {
      playTopAll = new JRadioButton("All Suits");
      playTopAll.setActionCommand("all");
      //playTopAll.addActionListener(new TopButtonListener(this.control));
    }
    if (topSelectPanel == null) {
      topSelectPanel = new JPanel();
      topGroup       = new ButtonGroup();
      topGroup.add(playTopTrump);
      topGroup.add(playTopAll);
      if (control.getModelBooleanProperty("TopVariation") == true) {
        playTopAll.setSelected(true);
      } else {
        playTopTrump.setSelected(true);
      }
      topSelectPanel.setLayout(new java.awt.GridLayout(1, 2));
      topSelectPanel.add(playTopAll);
      topSelectPanel.add(playTopTrump);
      topSelectPanel.setBounds(new Rectangle(136, 286, 220, 20));
    }
    return topSelectPanel;
  }

  private JButton getButtonSave() {
    if (saveButton == null) {
      saveButton = new JButton();
      saveButton.setText("Save");
      saveButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent e) {
          control.setModelProperty("PlayerEastName",  textEastName.getText());
          control.setModelProperty("PlayerWestName",  textWestName.getText());
          control.setModelProperty("PlayerNorthName", textNorthName.getText());
          control.setModelProperty("PlayerSouthName", textSouthName.getText());
          control.setModelProperty("PlayerEastType",  String.valueOf(playerEastType.getSelectedItem()).toLowerCase());
          control.setModelProperty("PlayerWestType",  String.valueOf(playerWestType.getSelectedItem()).toLowerCase());
          control.setModelProperty("PlayerNorthType", String.valueOf(playerNorthType.getSelectedItem()).toLowerCase());
          control.setModelProperty("PlayerSouthType", String.valueOf(playerSouthType.getSelectedItem()).toLowerCase());
          control.setModelProperty("ConfigPosition",  "("+getX()+","+getY()+")");
          control.setModelProperty("WinningScore",    textWinningScore.getText());
          control.setModelProperty("MinimumBid",      textMinimumBid.getText());
          control.setModelProperty("BidVariation",    getSelectedButtonValue(bidGroup));
          control.setModelProperty("TopVariation",    getSelectedButtonValue(topGroup));
          control.save();
          closeFrame();
        }
      });
    }
    return saveButton;
  }

  private String getSelectedButtonValue(ButtonGroup buttonGroup) {
    String value;
    for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
      AbstractButton button = buttons.nextElement();

      if (button.isSelected()) {
        value = button.getText();
        if (value.equals("Single Bid")) 
          return "single";
        if (value.equals("Auction Bid"))
          return "auction";
        if (value.equals("All Suits"))
          return "0";
        if (value.equals("Trump Only"))
          return "1";
        return value;
      }
    }
    return null;
  }

  private void closeFrame(){
    this.control.pause(false);
    this.setVisible(false);
  }

  static class FocusTextField extends JTextField { 
    public FocusTextField(String s) {
      super(s);
      addFocusListener(new FocusListener() {
        @Override
        public void focusGained(FocusEvent e) {
          FocusTextField.this.select(0, getText().length());
        }

        @Override
        public void focusLost(FocusEvent e) {
          FocusTextField.this.select(0, 0);
        }
      });
    }
  }

  class TextFieldEditor extends DefaultCellEditor {
    public TextFieldEditor() {
      super(new JTextField());
      this.setClickCountToStart(1);
    }

    public Component 
    getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int col) {
      JTextField editor = (JTextField) super.getTableCellEditorComponent(table, value, isSelected, row, col);
      editor.setText("");
      return editor;
    }
  }

  public void modelPropertyChange(PropertyChangeEvent e) {
    if (e.getNewValue() == null) return;
  }
}
