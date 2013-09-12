package org.joedog.pinochle.player;

import org.joedog.pinochle.controller.GameController;
import org.joedog.pinochle.game.*;

public class Computer extends Player {
  //private final static String type = "COMPUTER";
  
  private GameController controller;

  public Computer(GameController controller) {
    this.type = COMPUTER;
    this.controller = controller;
  }
 
  public void takeCard(Card c) {
    c.setFaceUp();
    this.hand.add(c);
    //this.setting.display(this.hand);
  }

  public int bid(int bid) {
    if (myBid == -1) return myBid;

    if (bid > this.maxBid) {
      System.out.println(this.name+": Pass");
      this.setting.bid("Pass");
      return -1;
    }
    this.myBid = bid+1; // XXX: hard-coded auction
    this.setting.bid(""+this.myBid);
    return this.myBid;
  }

  public int meld() {
    int trump = controller.getIntProperty("GameTrump");
    Meld m = new Meld(this.hand, trump);
    return m.getMeld();
  }

  public int nameTrump() {
    if (this.assessment != null) {
      return assessment.getTrump();
    }
    // WTF?? 
    return Pinochle.SPADES;
  }

  public Deck passCards(boolean bidder) {
    Deck deck = null;
    int trump = controller.getIntProperty("GameTrump");
    System.out.println("passCards.BEFORE: "+this.hand.toString());
    deck = meld.passables(bidder, 3, trump);
    System.out.println(this.getName() + " is passing: "+deck.toString());
    this.refresh();
    System.out.println("passCards.AFTER: "+this.hand.toString());
    return deck;
  }

  public void finish (int status) {
  }

  public void takeTurn() {
    this.hand.remove(0);
    //this.table.repaint();
  }
}
