package org.joedog.pinochle.player;
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

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.lang.Thread;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import org.joedog.util.*;
import org.joedog.pinochle.control.Game;
import org.joedog.pinochle.game.*;
import org.joedog.pinochle.view.TrumpDialog;
import org.joedog.pinochle.view.BidDialog;

public class Human extends Player {
  private Game control;

  public Human(Game control) {
    this.type    = HUMAN;
    this.control = control;
  }

  public void takeCard(Card c) {
    c.setFaceUp();
    this.hand.add(c);
  }

  public void takeCards(Deck d) {
    this.myBid = 0;
    for (Card c: d.getCards()) {
      c.setFaceUp();
      c.select(true);
      this.hand.add(c);
    }
    Sleep.milliseconds(1300);
    this.hand.deselectAll();
    this.hand.sort();
    this.hand.relocate();
  }

  public void save() {}

  /**
   * Returns a Human bid with no consideration
   * for the partner's bid. 
   * <p>
   * @param  int  the bid to beat
   * @param  int  our partner's bid (ignored by this class)
   */ 
  public int bid(int bid, int pbid, boolean opponents) {
    return this.bid(bid);
  }

  public int bid(int bid) {
    if (myBid == -1) return myBid;
    BidDialog bd = new BidDialog(this.control, bid); 
    this.myBid   = (Integer)bd.getValue();
    if (this.myBid < 0) {
      Debug.print(this.name+" passed.");
      this.setText("Bid: Pass");
    } else {
      this.setText("Bid: "+this.myBid);
    }
    return this.myBid;
  }

  public int nameTrump() {
    String suits[] = new String[]{"Hearts", "Clubs", "Diamonds", "Spades"};
    JFrame frame   = new JFrame("Trump");
    TrumpDialog td = new TrumpDialog(this.control);
    this.bidder    = true;
    this.memory    = this.hand.toMemory();

    //XXX: this is probably a Bad Idea to instantiate this here
    String trump = (String)td.getValue();
    if (trump == null)            return Pinochle.SPADES;
    if (trump.equals("Hearts"))   return Pinochle.HEARTS;
    if (trump.equals("Clubs"))    return Pinochle.CLUBS;
    if (trump.equals("Diamonds")) return Pinochle.DIAMONDS;
    return Pinochle.SPADES;
  }

  public Deck passCards(boolean bidder) {
    int  selected = 0;
    Deck deck     = new Deck();
    
    //XXX: the okay button should be disabled until three cards are selected
    //XXX: three should be dynamic -- selected from config....
    this.control.addPassButton();
    while (! this.control.isPassable()) {
      if (hand.numSelected() == 3) {
        this.control.enablePassButton(true);
      } else {
        this.control.enablePassButton(false);
      } 
      Sleep.milliseconds(500);
    }  
    for (Iterator<Card> iterator = hand.getCards().iterator(); iterator.hasNext(); ) {
      Card card = iterator.next();
      if (card.isSelected()) {
        deck.add(card);
        iterator.remove();
      }
    }
    this.hand.sort();
    this.hand.relocate();
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
    int trump = control.getModelIntProperty("Trump");
    this.control.addMeldButton();
    while (! this.control.isMeldable()) {
      Sleep.milliseconds(500);  
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
    return m.getMeld();
  }

  public void clearMeld() {
    for (Card card: this.hand.getCards()) {
      card.unmeld();
      card.setFaceUp();
      card.select(false);
    }
  }

  public void remember(Deck cards) {
    // You're on your own, Human....
  }

  public void remember(Card card) {
    // You're on your own, Human....
  }

  public void clear() {
    // You're on your own, Human....
  }

  public void remember(int meld, int take) {
    if (!this.bidder) return;
    if (this.memory == null || this.memory.length() < 2) return;
    int game = (meld+take);

    this.memory += "|"+game;
    Logger.remember(memtxt, memory);
    this.memory = new String("");
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
    Rules rules  = new Rules(this.control);
    boolean okay = false;
    do {
      card = this.hand.getSelected();
      okay = rules.isLegitPlay(trick, this.hand, card);
      Sleep.milliseconds(50);  
    } while (! okay);
    card.play();
    this.hand.deselectAll();
    this.hand.remove(card.getId());
    return card;
  }
}

