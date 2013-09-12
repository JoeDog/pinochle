package org.joedog.pinochle.player;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import org.joedog.pinochle.controller.GameController;
import org.joedog.pinochle.game.*;
import org.joedog.pinochle.view.Setting;

public class Human extends Player {
  private GameController controller;

  public Human(GameController controller) {
    this.type = HUMAN;
    this.controller = controller;
  }

  public void takeCard(Card c) {
    c.setFaceUp();
    this.hand.add(c);
    //this.setting.display(this.hand); 
  }

  public int bid(int bid) {
    int tmp       = bid + 1;
    String bids[] = new String[16];
   
    if (myBid == -1) return myBid;

    for (int i = 0; i < bids.length; i++) {
      bids[i] = ""+tmp;
      tmp++;
    }
    JFrame frame = new JFrame("Bid");
    String num   = (String) JOptionPane.showInputDialog(frame, 
      "Bid", "Bid", JOptionPane.QUESTION_MESSAGE, null, bids, bids[0]
    );
    try {
      myBid = Integer.parseInt(num);
    } catch(Exception ex) {
      // insert error handling if conversion to string is not successful
      myBid = -1;
    }
    if (this.myBid < 0) 
      this.setting.bid("Pass");
    else
      this.setting.bid(""+this.myBid);
    return myBid;
  }

  public int meld() {
    return 8;
  }

  public int nameTrump() {
    String suits[] = new String[]{"Hearts", "Clubs", "Diamonds", "Spades"};
    JFrame frame   = new JFrame("Trump");
    String trump   = (String) JOptionPane.showInputDialog(frame, 
      "Name trump", "Trump", JOptionPane.QUESTION_MESSAGE, null, suits, suits[0]
    ); 
    if (trump.equals("Hearts"))   return Pinochle.HEARTS;
    if (trump.equals("Clubs"))    return Pinochle.CLUBS;
    if (trump.equals("Diamonds")) return Pinochle.DIAMONDS;
    return Pinochle.SPADES;
  }

  public Deck passCards(boolean bidder) {
    Deck deck    = new Deck();
    JFrame frame = new JFrame("Pass cards...");
    int selected = 0;
    //XXX: the okay button should be disabled until three cards are selected
    //XXX: three should be dynamic -- selected from config....
    while (hand.numSelected() < 3) {
      try {
        Thread.sleep(1000);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }  
    JOptionPane.showMessageDialog(frame, "Pass three cards to your partner....");
    for (Iterator<Card> iterator = hand.getCards().iterator(); iterator.hasNext(); ) {
      Card card = iterator.next();
      if (card.isSelected()) {
        deck.add(card);
        iterator.remove();
      }
    }
    System.out.println(getName()+" is passing: "+deck.toString());
    this.setting.refresh(this.hand);
    return deck;
  } 

  public void finish (int status) {
  }

  public void takeTurn() {
    this.hand.remove(0);
    this.setting.repaint();
  }
}

