package org.joedog.pinochle.player;

import org.joedog.pinochle.control.GameController;
import org.joedog.pinochle.game.*;

public class Computer extends Player {
  //private final static String type = "COMPUTER";
  
  private GameController controller;

  public Computer(GameController controller) {
    this.type = COMPUTER;
    this.controller = controller;
  }
 
  public void takeCard(Card c) {
    c.setFaceDown();
    this.hand.add(c);
  }

  public int bid(int bid) {
    if (myBid == -1) return myBid;

    if (bid > this.maxBid) {
      this.setting.setText("Bid: Pass");
      return -1;
    }
    this.myBid = bid+1; // XXX: hard-coded auction
    this.setting.setText("Bid: "+this.myBid);
    return this.myBid;
  }

  public int meld() {
    int trump = controller.getIntProperty("GameTrump");
    Meld m = new Meld(this.hand, trump);
    this.setting.setText("Meld: "+m.getMeld());
    return m.getMeld();
  }

  public int nameTrump() {
    this.bidder = true;
    if (this.assessment != null) {
      return assessment.getTrump();
    }
    // WTF?? 
    return Pinochle.SPADES;
  }

  public Deck passCards(boolean bidder) {
    Deck deck = null;
    int trump = controller.getIntProperty("GameTrump");
    deck = meld.passables(bidder, 3, trump);
    this.setting.refresh();
    return deck;
  }

  public void finish (int status) {
  }

  public void takeTurn() {
    this.hand.remove(0);
    //this.table.repaint();
  }
}
