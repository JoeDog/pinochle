package org.joedog.pinochle.view;

import java.awt.Container;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class BidDialog extends JFrame {
  private Point     p;
  private int       x, y;
  private int       value;
  private int       bid;
  private JComboBox bidList;
  private JButton   okay;
  private JButton   pass;
  private Container dialog;
  private JPanel    buttons;
  private JLabel    header;
  private JLabel    suit;

  public BidDialog(int bid) {
    this.bid = bid;
    this.getMainPanel();
  }

  private void getMainPanel() {
    this.dialog = this.getContentPane();
    this.dialog.setLayout(null);
    this.dialog.add(this.getSuitLabel(), null);
    this.header = new JLabel();
    this.header.setBounds(new Rectangle(70, 10, 120, 20));
    this.header.setText("Select a bid:");
    this.dialog.add(this.header, null);
    this.dialog.add(getComboBox(bid), null);
    this.buttons = new JPanel();
    this.buttons.setBounds(new Rectangle(70, 60, 120, 30));
    this.buttons.setBackground(Color.RED);
    this.dialog.add(buttons, null);
    this.setPreferredSize(new Dimension(268,129));
    //JRootPane root = this.getRootPane();
    //root.setDefaultButton(saveButton);
    //this.addWindowListener(new WindowAdapter() {
    //  public void windowGainedFocus(WindowEvent e) {
    //    saveButton.requestFocusInWindow();
    //  }
    //});
    this.pack();
    this.setVisible(true);
    this.setLocation(500, 300);
  }

  public int getValue() {
    return this.value;
  }
  
  public int getX() {
    return this.x;
  }

  public int getY() {
    return this.y;
  }

  private JLabel getSuitLabel() {
    if (suit == null) {
      suit = new JLabel();
    }
    suit.setIcon(new TrumpIcon(3));
    suit.setBounds(new Rectangle(15,5,50,50));
    return suit;
  }

  private JComboBox getComboBox(int bid) {
    Integer bids[] = new Integer[16];

    for (int i = 0; i < bids.length; i++) {
      bids[i] = (bid+i+1);
    }
    if (bidList == null) {
      bidList = new JComboBox(bids);
    }
    bidList.setSelectedIndex(0);
    bidList.setPreferredSize(new Dimension(60,20));
    bidList.setBounds(new Rectangle(70, 35, 160, 20));
    bidList.addItemListener(new ItemListener(){
      public void itemStateChanged(ItemEvent ie){
        //pane.setInputValue(bidList.getSelectedItem());
      }
    }); 
    return bidList;
  }
  
  public static void main(final String[] args) {
    BidDialog bs = new BidDialog(16);
    int v = bs.getValue();
    int x = bs.getX();
    int y = bs.getY();
    System.out.println("Value: "+v+" Coordinates: "+x+", "+y);
  }
}
