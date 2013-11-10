package org.joedog.pinochle.player;

import java.util.Random;
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
    if (controller.cheatMode()) {
      c.setFaceUp();
    } else {
      c.setFaceDown();
    }
    this.hand.add(c);
  }

  public void takeCards(Deck d) {
    for (Card c: d.getCards()) {
      if (controller.cheatMode()) {
        c.setFaceUp();
      } else {
        c.setFaceDown();
      }
      this.hand.add(c);
    }
    this.hand.sort();
    this.setting.refresh(this.hand);
    this.setting.refresh();
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

  public void clearMeld() {
    for (Card card: this.hand.getCards()) {
      card.unmeld();
      if (controller.cheatMode()) {
        card.setFaceUp();
      } else {
        card.setFaceDown();
      }
    }
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
    this.setting.refresh(this.hand);
    return deck;
  }

  public void finish (int status) {
  }

  public Card playCard(Trick trick) {
    Card card = null;
    Card temp = null;
    Card high = null;
    int  suit = trick.getLeadingSuit();
    if (suit < Pinochle.HEARTS || suit > Pinochle.SPADES) {
      // we either have the lead or WTF?
      if (this.hand.aces(trick.getTrump()) > 0) { 
        if (this.hand.contains(new Card(trick.getTrump(), Pinochle.ACE)) > 0) {
          card = new Card(trick.getTrump(), Pinochle.ACE);
        }
      } else {
        for (int i = Pinochle.HEARTS; i < Pinochle.SPADES; i++) {
          if (this.hand.aces(i) > 0) {  
            card = new Card(i, Pinochle.ACE);
            break;
          }
        }
      }
      if (card == null) {
        System.out.println("hand.size: "+this.hand.size());
        Random r = new Random();
        int    h = (this.hand.size() > 2) ? this.hand.size() : 2;
        int    i = r.nextInt(h-1) + 1;
        card = this.hand.get(i-1);
      }
    }
    if (this.hand.contains(suit) > 0) {
      // can we beat it??
      temp = this.hand.getHighest(suit);
      high = trick.getWinningCard(); 
      if (temp != null && temp.getRank() > high.getRank()) {
        // XXX: should consult memory to see if this play can stand
        // XXX: should check to see if the winning card is my partners
        card = temp;
      } else {
        if (trick.winner() == this.partner) {
          System.out.println(this.name+" says, 'Good job, partner. Here's a counter'");
          card = this.hand.getCounter(suit);
          if (card == null) {
            System.out.println(this.name+" says, 'Scratch that, I don't have a counter'");
            card = this.hand.getLowest(suit);
          }
        } else {
          System.out.println(this.name+" says, :-P");
          card = this.hand.getLowest(suit);
        }
      }
    } else {
      System.out.println(this.name+" is in the else case");
      int cnt;
      int num = 100; // short suit
      int sel = 100; // selected short suit
      if (trick.winner() == this.partner) {
        System.out.println(this.name+" says, 'Good job, partner. Here's a counter'");
        for (int i = Pinochle.HEARTS; i < Pinochle.SPADES; i++) {
          cnt = this.hand.contains(i);
          if (cnt == 0) continue;
          else if (cnt < num && this.hand.counters(i) > 0) sel = i; 
        }
      } else {
        System.out.println(this.name+" says, 'Stinky partner, no counter for you'");
        for (int i = Pinochle.HEARTS; i < Pinochle.SPADES; i++) {
          cnt = this.hand.contains(i);
          if (cnt == 0) continue;
          else if (cnt < num && this.hand.nonCounters(i) > 0) sel = i; 
        }
      }
      if (sel < 100) {
        System.out.println(this.name+" sel is less than 100");
        if (trick.winner() == this.partner) {
          card = this.hand.getCounter(sel);
        } else {
          card = this.hand.getLowest(sel);
        }
      } else {
        System.out.println(this.name+" sel is NOT less than 100");
        if (trick.winner() == this.partner) {
          card = this.hand.getCounter();
        } else {
          card = this.hand.getLowest();
        }
      }
    }
    if (card == null) {
      card = this.hand.get(0);
    }
    this.hand.remove(card);
    this.setting.refresh(this.hand);
    return card;
  }
}
