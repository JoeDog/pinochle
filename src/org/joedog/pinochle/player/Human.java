package org.joedog.pinochle.player;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.lang.Thread;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import org.joedog.pinochle.control.GameController;
import org.joedog.pinochle.game.*;
import org.joedog.pinochle.util.*;
import org.joedog.pinochle.view.Setting;
import org.joedog.pinochle.view.TrumpDialog;

public class Human extends Player {
  private GameController controller;

  public Human(GameController controller) {
    this.type = HUMAN;
    this.controller = controller;
  }

  public void takeCard(Card c) {
    c.setFaceUp();
    this.hand.add(c);
  }

  public void takeCards(Deck d) {
    this.myBid = 0;
    for (Card c: d.getCards()) {
      c.setFaceUp();
      this.hand.add(c);
    }
    this.hand.sort();
    this.setting.refresh(this.hand);
    this.setting.refresh();
  }

  /**
   * Returns a Human bid with no consideration
   * for the partner's bid. 
   * <p>
   * @param  int  the bid to beat
   * @param  int  our partner's bid (ignored by this class)
   */ 
  public int bid(int bid, int pbid) {
    return this.bid(bid);
  }

  public int bid(int bid) {
    int tmp       = bid + 1;
    String bids[] = new String[16];
   
    if (myBid == -1) return myBid;

    for (int i = 0; i < bids.length; i++) {
      bids[i] = ""+tmp;
      tmp++;
    }
    String num   = (String) JOptionPane.showInputDialog(null, 
      "Bid", "Bid", JOptionPane.QUESTION_MESSAGE, null, bids, bids[0]
    );
    try {
      myBid = Integer.parseInt(num);
    } catch(Exception ex) {
      // insert error handling if conversion to string is not successful
      myBid = -1;
    }
    if (this.myBid < 0) 
      this.setting.setText("Bid: Pass");
    else
      this.setting.setText("Bid: "+this.myBid);
    return myBid;
  }

  public int nameTrump() {
    String suits[] = new String[]{"Hearts", "Clubs", "Diamonds", "Spades"};
    JFrame frame   = new JFrame("Trump");
    TrumpDialog td = new TrumpDialog();
    this.bidder    = true;

    //XXX: this is probably a Bad Idea to instantiate this here
    String trump = (String)td.getValue();
    if (trump == null)            return Pinochle.SPADES;
    if (trump.equals("Hearts"))   return Pinochle.HEARTS;
    if (trump.equals("Clubs"))    return Pinochle.CLUBS;
    if (trump.equals("Diamonds")) return Pinochle.DIAMONDS;
    return Pinochle.SPADES;
  }

  public Deck passCards(boolean bidder) {
    Deck    deck     = new Deck();
    boolean thinking = true;
    JFrame  frame    = new JFrame("Pass cards...");
    int     selected = 0;
    
    //XXX: the okay button should be disabled until three cards are selected
    //XXX: three should be dynamic -- selected from config....
    //while (hand.numSelected() < 3) {
    this.controller.addPassButton();
    this.setting.refresh();
    while (! this.controller.isPassable()) {
      if (hand.numSelected() == 3) {
        this.controller.enablePassButton();
      } else {
        this.controller.disablePassButton();
      } 
      try {
        Thread.sleep(500);
      } catch (Exception e) {}
    }  
    for (Iterator<Card> iterator = hand.getCards().iterator(); iterator.hasNext(); ) {
      Card card = iterator.next();
      if (card.isSelected()) {
        deck.add(card);
        iterator.remove();
      }
    }
    this.setting.refresh(this.hand);
    Debug.print(this.name+" passed:\t"+deck.toString());
    return deck;
  } 

  /**
   * Pauses play while a Human selects meld from their
   * hand. The meld is tallied by a Meld {@Link Meld} 
   * object and returned as a score by the method
   * <p>
   * @return      int (the meld score)
   */
  public int meld() {
    Hand tmp  = new Hand();

    int trump = controller.getIntProperty("GameTrump");
    this.controller.addMeldButton();
    this.setting.refresh();
    while (! this.controller.isMeldable()) {
      try { 
        Thread.sleep(500);  
      } catch (Exception e) {}
    }
    for (Iterator<Card> iterator = hand.getCards().iterator(); iterator.hasNext(); ) {
      Card card = iterator.next();
      if (card.isSelected()) {
        tmp.add(card);  
      }
    }

    Meld m = new Meld(tmp, trump);
    for (Iterator<Card> iterator = tmp.getCards().iterator(); iterator.hasNext(); ) {
      Card card = iterator.next();
      if (card.melded()) {
        this.hand.meld(card);
      }
    } 
    this.setting.setText("Meld: "+m.getMeld());
    return m.getMeld();
  }

  public void clearMeld() {
    for (Card card: this.hand.getCards()) {
      card.unmeld();
      card.setFaceUp();
    }
  }

  public void remember(Deck cards) {
    // You're on your own, Human....
  }

  public void remember(Card card) {
    // You're on your own, Human....
  }

  public void finish (int status) {
  }

  /**
   * Returns a card selected by a Human player The
   * card is verified {@Link verfied} to see if it
   * complies with the rules of Pinochle
   * <p>
   * @param Trick   The current trick into which we play
   * @return        Card  
   */
  public Card playCard(Trick trick) {
    Card  card;
    Rules rules  = new Rules(this.controller);
    boolean okay = false;
    do {
      while (! this.controller.isPlayable()) {
        try { 
          Thread.sleep(500);  
        } catch (Exception e) {}
      }
      card = this.setting.getCard();
      okay = rules.isLegitPlay(trick, this.hand, card);
    } while (! okay);
    this.hand.remove(card);
    this.setting.refresh(this.hand);
    return card;
  }
}

