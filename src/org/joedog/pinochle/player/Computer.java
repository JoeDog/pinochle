package org.joedog.pinochle.player;

import java.util.Random;
import org.joedog.pinochle.control.GameController;
import org.joedog.pinochle.game.*;
import org.joedog.pinochle.util.*;

public class Computer extends Player {
  private GameController controller;
  private Brain          brain;

  public Computer(GameController controller) {
    this.type = COMPUTER;
    this.controller = controller;
    this.brain = new Brain();
  }

  @Override
  public synchronized void newHand() {
    this.hand   = new Hand();
    this.brain  = new Brain();
    this.myBid  = 0;
    this.maxBid = 0;
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

  public void remember(Deck d) {
    if (d != null || d.size() > 1) {
      this.brain.remember(d);
    }
  }

  public void remember(Card c) {
    if (c != null) {
      this.brain.remember(c);
    }
  }

  public int bid (int bid) {
    if (myBid == -1) return myBid;

    if (bid > this.maxBid) {
      this.setting.setText("Bid: Pass");
      myBid = -1;
      return myBid;
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
    if (trick.getLeadingSuit() == -1) {
      card = leadingPlay(trick);
    } else {
      card = followingPlay(trick);
    }
    if (card == null) {
      card = this.hand.get(0);
    }
    this.hand.remove(card);
    this.setting.refresh(this.hand);
    return card;
  }

  private Card leadingPlay(Trick trick) {
    Card card = null;
    Card temp = null;
    Card high = null;
    int  suit = trick.getLeadingSuit();
    int  s[]  = new int[] {
      Pinochle.HEARTS, 
      Pinochle.CLUBS, 
      Pinochle.DIAMONDS, 
      Pinochle.SPADES
    };
    if (this.hand.aces(trick.getTrump()) > 0) {
      if (this.hand.contains(new Card(Pinochle.ACE, trick.getTrump())) > 0) {
        card = new Card(Pinochle.ACE, trick.getTrump());
        Debug.print(this.name+" unleashes his fire power: "+card.toString());
      }
    } else {
      Debug.print(this.name+" is looking for aces");
      for (int i = 0; i < 4; i++) {
        if (this.hand.aces(i) > 0) {
          card = new Card(Pinochle.ACE, i);
          Debug.print(this.name+" fires an ace: "+card.toString());
          break;
        }
      }
    }
    if (card == null) {
      Debug.print(this.name+" checked and found no aces....");
      // Let's see if we have a high card....
      // XXX: we need to remove the position check. 
      // XXX: we're gonna use NS as a control now.
      shuffle(s);
      for (int i : s) {
        if (brain.haveHighest(this.hand, i) == true) {
          if (i == trick.getTrump() && ! brain.outstandingTrump(this.hand, i)) {
            System.out.println("Let's save our trump");
            continue;  
          }
          card = this.hand.getHighest(i);
          Debug.print(this.name+" says, I'll hit you with my best shot: "+card.toString()); 
          if (card == null) return card;
          break;
        }
      }
    }
    if (card == null) {
      // Let's play Beat the Queen....
      if (this.hand.contains(new Card(Pinochle.QUEEN, trick.getTrump())) > 0 && 
          brain.outstandingTrump(this.hand, trick.getTrump())) {
        card = new Card(Pinochle.QUEEN, trick.getTrump());
        Debug.print(this.name+" says, this is a good time to play beat the queen: "+card.toString());
        if (card != null) return card;
      }
    }
    if (card == null) {
      // Okay, any queen will do....
      for (int i = 0; i < 4; i++) {
        if (this.hand.queens(i) > 0) {
          card = new Card(Pinochle.QUEEN, i);
          Debug.print(this.name+" says, Oh, well, this queen will have to do: "+card.toString());
          break;
        }
      }
    }
    while (card == null) {
      Random r = new Random();
      int    h = (this.hand.size() > 2) ? this.hand.size() : 2;
      int    i = r.nextInt(h-1) + 1;
      Debug.print("Random i: "+i+" hand size: "+hand.size());
      card = this.hand.get(i-1);
      Debug.print(this.name+" Look what I pulled out of my ass: "+card.toString());
    }
    return card;
  }

  private Card followingPlay(Trick trick) {
    Card card  = null;
    Card temp  = null;
    Card high  = null;
    int  suit  = trick.getLeadingSuit();
    int  trump = trick.getTrump();
    if (this.hand.contains(suit) > 0) {
      /** 
       * We must follow suit; can we beat it?
       */
      temp = this.hand.getHighest(suit);
      high = trick.getWinningCard();
      if (trick.winner() == this.partner) {
        // partner has it, but will it stand?
        int total   = this.brain.cardsHigherThan(high);
        int winners = this.hand.cardsHigherThan(high);
        Debug.print(this.name+"'s partner is winning but will it stand?");
        if (winners > 0 && winners < total) {
          // Oh noes, there are outstanding cards
          // which can beat my partner...
          Debug.print(this.name+"'s partner's card can be beaten!!!");
          if (this.brain.haveHighest(this.hand, suit)) {
            card = temp;
            Debug.print("Never fear! "+this.name+" is here!!! "+card.toString());
            if (card != null) return card;
          } else {
            card = this.hand.getLowest(suit);
            Debug.print(this.name+" says, 'Sorry, partner. They're gonna take it....' ("+card.toString()+")");
            if (card != null) return card;
          }
        } else if (total == 0) {
          // My parter is gonna win it
          card = this.hand.getCounter(suit);
          if (card == null) 
             card = this.hand.getLowest(suit);
          Debug.print(this.name+" says, '(226) Good job! I tried to give a counter' ("+card.toString()+")");
          if (card != null) return card;
        } else {
          card = this.hand.getLowest(suit);
          Debug.print(this.name+" is no help at all: "+card.toString());
          if (card != null) return card;
        }
      } else if (this.brain.haveHighest(this.hand, suit)) {
        if (temp.getRank() <= high.getRank()) {
          card = this.hand.getLowest(suit);
          Debug.print(this.name+" says, 'Crap, we can only tie...' playing lowest: "+card.toString());
          if (card != null) return card;
        } else {
          card = temp;
          Debug.print(this.name+" has the highest card and [s]he's gonna play it! "+card.toString());
          if (card != null) return card;
        }
      } else {
        card = this.hand.getLowest(suit);
        Debug.print(this.name+" is powerless to do anything: "+card.toString());
        if (card != null) return card;
      }
    } else {
      int cnt;
      int num = 100; // short suit
      int sel = 100; // selected short suit
      if (this.hand.contains(trump) > 0) {
        // we are required by the rules to play it....
        Card tmp = trick.getWinningCard();
        if (tmp.getSuit() == trump) {
          // somebody already trumped
          card = this.hand.beat(tmp);
          if (card == null) {
            Debug.print(this.name+" can't out-trump the "+tmp.toString()+" (playing off)");
            card = this.hand.getLowest(trump);
            if (card != null) return card;
          }
        } else {
          // nobody trumped but we must do that     
          card = this.hand.getLowest(trump);
          Debug.print("Nobody trumped. "+this.name+" is gonna try "+card.toString());
          if (card != null) return card;
        }
      }
      if (trick.winner() == this.partner) {
        for (int i = 0; i < 4; i++) { // loop thru the suits
          cnt = this.hand.contains(i);
          if (cnt == 0) continue;
          else if (cnt < num && this.hand.counters(i) > 0) sel = i;
        }
      } else {
        for (int i = Pinochle.HEARTS; i < Pinochle.SPADES; i++) {
          cnt = this.hand.contains(i);
          if (cnt == 0) continue;
          else if (cnt < num && this.hand.nonCounters(i) > 0) sel = i;
        }
      }
      if (card != null) return card;
      if (card == null && sel < 100) {
        if (trick.winner() == this.partner) {
          card = this.hand.getCounter(sel);
          if (card == null) 
            card = this.hand.getCounter();
          Debug.print(this.name+" says, '(281) Good job! I'll to give you a counter: "+card.toString()+"'");
          if (card != null) return card;
        } else {
          card = this.hand.getLowest(sel);
          if (card.getRank() == Pinochle.ACE) 
            card = this.hand.getLowest();
          Debug.print(this.name+" says, '(300) Bad guys got it. Here's my worst card: "+card.toString()+"'");
          if (card != null) return card;
        }
      } else {
        if (trick.winner() == this.partner) {
          card = this.hand.getCounter();
          if (card == null)  
            card = this.hand.getLowest();
          Debug.print(this.name+" says, '(291) Good job! I tried to give you a counter: "+card.toString()+"'");
          if (card != null) return card;
        } else {
          card = this.hand.getLowest();
          Debug.print(this.name+" says, '(310) Bad guys got it. Here's my worst card: "+card.toString()+"'");
          if (card != null) return card;
        }
      }
    }
    return card;
  }

  private static void shuffle(int[] a) {
    int n = a.length;
    Random random = new Random();
    random.nextInt();
    for (int i = 0; i < n; i++) {
      int change = i + random.nextInt(n - i);
      swap(a, i, change);
    }
  }

  private static void swap(int[] a, int i, int change) {
    int helper = a[i];
    a[i] = a[change];
    a[change] = helper;
  }
}
