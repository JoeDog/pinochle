package org.joedog.pinochle.view;

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

import org.joedog.pinochle.control.*;
import org.joedog.pinochle.view.actions.*;

public class ConfigView extends JFrame {
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
  private GameController controller;
  private int width;
  private int height;
  
  public ConfigView (GameController controller) {
    super("Pinochle Configuration");
    this.controller      = controller;
    this.playerEastName  = controller.getProperty("PlayerEastName");
    this.playerWestName  = controller.getProperty("PlayerWestName");
    this.playerNorthName = controller.getProperty("PlayerNorthName");
    this.playerSouthName = controller.getProperty("PlayerSouthName");
    this.display();
  }

  public void display() {
    //Dimension dim   = Toolkit.getDefaultToolkit().getScreenSize();
    pinochle = this.getContentPane();
    pinochle.setLayout(new BorderLayout());
    int x = controller.getIntProperty("ConfigX");
    int y = controller.getIntProperty("ConfigY");
    this.width  = 480;
    this.height = 520;
    this.setPreferredSize(new Dimension(this.width,this.height));
    this.setDefaultCloseOperation(controller.save());
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
    this.setLocation(x, y);
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
      textEastName = new FocusTextField(controller.getProperty("PlayerEastName"));
      textEastName.setBounds(new Rectangle(136, 34, 161, 20));
    }
    return textEastName;
  }

  private FocusTextField getWestNameField() {
    if (textWestName == null) {
      textWestName = new FocusTextField(controller.getProperty("PlayerWestName"));
      textWestName.setBounds(new Rectangle(136, 58, 161, 20));
    }
    return textWestName;
  }

  private FocusTextField getNorthNameField() {
    if (textNorthName == null) {
      textNorthName = new FocusTextField(controller.getProperty("PlayerNorthName"));
      textNorthName.setBounds(new Rectangle(136, 82, 161, 20));
    }
    return textNorthName;
  }

  private FocusTextField getSouthNameField() {
    if (textSouthName == null) {
      textSouthName = new FocusTextField(controller.getProperty("PlayerSouthName"));
      textSouthName.setBounds(new Rectangle(136, 106, 161, 20));
    }
    return textSouthName;
  }

  private FocusTextField getWinningScoreField() {
    if (textWinningScore == null) {
      textWinningScore = new FocusTextField(controller.getProperty("WinningScore"));
      textWinningScore.setBounds(new Rectangle(136, 166, 161, 20));
    }
    return textWinningScore;
  }

  private FocusTextField getMinimumBidField() {
    if (textMinimumBid == null) {
      textMinimumBid = new FocusTextField(controller.getProperty("MinimumBid"));
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
       i = controller.getIntProperty("PlayerEastType"); 
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
       i = controller.getIntProperty("PlayerWestType"); 
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
       i = controller.getIntProperty("PlayerNorthType"); 
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
       i = controller.getIntProperty("PlayerSouthType"); 
       if (i < 0) i = 0;
       playerSouthType.setSelectedIndex(i);
    }
    return playerSouthType;
  }

  private JPanel getBidSelectPanel() {
    if (playSingleBid == null) {
      playSingleBid = new JRadioButton("Single Bid");
      playSingleBid.setActionCommand("single");
      playSingleBid.addActionListener(new BidButtonListener(this.controller));
    }
    if (playAuctionBid == null) {
      playAuctionBid = new JRadioButton("Auction Bid");
      playAuctionBid.setActionCommand("auction");
      playAuctionBid.addActionListener(new BidButtonListener(this.controller));
    }
    if (bidSelectPanel == null) {
      bidSelectPanel = new JPanel();
      bidGroup       = new ButtonGroup();
      bidGroup.add(playSingleBid);
      bidGroup.add(playAuctionBid);
      if ((controller.getProperty("BidVariation")).equals("auction")) {
        playAuctionBid.setSelected(true);
      } else {
        playSingleBid.setSelected(true);
      }
      bidSelectPanel.setLayout(new java.awt.GridLayout(1, 2));
      bidSelectPanel.add(playSingleBid);
      bidSelectPanel.add(playAuctionBid);
      bidSelectPanel.setBounds(new Rectangle(136, 262, 220, 20));
    }
    return bidSelectPanel;
  }

  private JPanel getDeckSelectPanel() {
    if (playSingle == null) {
      playSingle = new JRadioButton("Single");
      playSingle.setActionCommand("single");
      playSingle.addActionListener(new DeckButtonListener(this.controller));
    }
    if (playDouble == null) {
      playDouble = new JRadioButton("Double");
      playDouble.setActionCommand("double");
      playDouble.addActionListener(new DeckButtonListener(this.controller));
      playDouble.setEnabled(false);
    }
    if (deckSelectPanel == null) {
      deckSelectPanel = new JPanel();
      deckGroup       = new ButtonGroup();
      deckGroup.add(playSingleBid);
      deckGroup.add(playAuctionBid);
      if ((controller.getProperty("DeckSize")).equals("double")) {
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
      playTopTrump.addActionListener(new TopButtonListener(this.controller));
    }
    if (playTopAll == null) {
      playTopAll = new JRadioButton("All Suits");
      playTopAll.setActionCommand("all");
      playTopAll.addActionListener(new TopButtonListener(this.controller));
    }
    if (topSelectPanel == null) {
      topSelectPanel = new JPanel();
      topGroup       = new ButtonGroup();
      topGroup.add(playTopTrump);
      topGroup.add(playTopAll);
      if ((controller.getProperty("TopVariation")).equals("0")) {
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
          controller.setProperty("PlayerEastName",  textEastName.getText());
          controller.setProperty("PlayerWestName",  textWestName.getText());
          controller.setProperty("PlayerNorthName", textNorthName.getText());
          controller.setProperty("PlayerSouthName", textSouthName.getText());
          controller.setProperty("PlayerEastType",  ""+(playerEastType.getSelectedIndex()));
          controller.setProperty("PlayerWestType",  ""+(playerWestType.getSelectedIndex()));
          controller.setProperty("PlayerNorthType", ""+(playerNorthType.getSelectedIndex()));
          controller.setProperty("PlayerSouthType", ""+(playerSouthType.getSelectedIndex()));
          controller.setProperty("ConfigX",  Integer.toString(getX()));
          controller.setProperty("ConfigY",  Integer.toString(getY()));
          controller.setProperty("WinningScore",  textWinningScore.getText());
          controller.setProperty("MinimumBid",    textMinimumBid.getText());
          controller.setProperty("BidVariation",  getSelectedButtonValue(bidGroup));
          controller.setProperty("TopVariation",  getSelectedButtonValue(topGroup));
          controller.save();
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
    this.controller.pause(false);
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
}
