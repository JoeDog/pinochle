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

import org.joedog.util.*;
import org.joedog.pinochle.game.*;

public class Brain {
  private Deck    deck;
  private int     deckSize;
  private Hand    melds[]   = new Hand[4];
  private boolean trumped[] = new boolean[] {false, false, false, false};
  private int     ranks[]   = new int[] {
    Pinochle.ACE, Pinochle.TEN, Pinochle.KING, Pinochle.QUEEN, Pinochle.JACK,
  };

  public Brain() {
    this.deck     = new Deck();
    this.deckSize = 1;
  }

  public Brain(int deckSize) {
    this.deck     = new Deck();
    this.deckSize = deckSize;
  }

  /**
   * We're going to remember each card melded
   * by each player; this method embeds that
   * knowledge inside our Brain
   * <p>
   * @param Deck  a deck which consists of all cards melded by player
   * @param int   the player who melded those cards
   * @see         org.joedog.pinochle.game.Pinochle 
   */
  public void remember(Deck cards, int player) {
    for (int i = 0; i < cards.count(); i++) {
      Card tmp = cards.dealCard(i);
      melds[player].add(tmp);
    }
  }

  /** 
   * Commit a single card to memory; generally
   * this represents a card that was played as
   * it was played.
   * <p>
   * @param Card    the card that was played
   */
  public void remember(Card card) {
    Card c = null;
    if (card != null) {
      c = new Card(card); 
      this.deck.add(c);
    }
  }

  /**
   * Commit all cards in Deck to memory; this 
   * generally represents a subset of cards 
   * such as the members of a Trick.
   * <p>
   * @param  Deck   the cards we commit to memory
   * @return void
   */
  public void remember(Deck cards) {
    for (int i = 0; i < cards.size(); i++) {
      this.remember(cards.get(i));
    }
  }

  /**
   * Sets the trumped value of that suit 
   * to true; invoke this method as soon
   * as someone trumps the suit
   * <p>
   * @param  int    The suit which was trumped
   * @return void
   */
  public void trumped(int suit) {
    trumped[suit] = true;
  }

  /** 
   * Mister Short-term Memory clears all
   * knowledge at the start of each hand;
   * this means we reset our deck and each 
   * melded card knowledge
   * <p>
   * @return    void 
   */
  public void refresh() {
    deck = new Deck();
    for (int i = 0; i < 4; i++) {
      melds[i]   = new Hand();  
      trumped[i] = false;
    }
  }

  public void forget() {
    for (int i = deck.size() - 1; i >= 0; i--) {
      deck.remove(i);
    }
  }

  public boolean outstandingTrump(Hand hand, int trump) {
    int num = deck.contains(trump) + hand.contains(trump);  
    // XXX: HARD-CODE ALERT!!!!!!!!
    if (num == 12) return false; // no outstanding trump
    else return true;
  }

  /**
   * Returns true if Hand contains the highest 
   * available card in the suit
   * <p>
   * @param  Hand  The player's hand 
   * @param  int   The suit we're examining
   * @return boolean 
   */
  public boolean haveHighest(Hand hand, int suit) {
    int rank = highest(suit);
    Card tmp = new Card(rank, suit);
    return (hand.contains(tmp) > 0);    
  }

  /**
   * Returns true if Card is the highest available
   * card in the suit
   * <p>
   * @param Card     The card to test
   * @param int      The suit we're examining
   * @return boolean 
   */
  public boolean isHighest(Card card) {
    int rank = highest(card.getSuit());
    return (card.getRank() > rank);
  }

  public int cardsHigherThan(Card card) {
    int num = ((Pinochle.ACE - card.getRank()) * (2 *this.deckSize));
    for (int i = Pinochle.ACE; i > card.getRank(); i--) {
      num -= deck.contains(new Card(i, card.getSuit()));  
    }
    return num;
  }

  /**
   * Returns the rank of the highest card 
   * available in a suit
   * <p>
   * @param  suit   The suit we're looking for
   * @return int    Rank of the highest available card
   */
  private int highest(int suit) {
    int  cnt   = 0; 
    for (int i = 0; i < ranks.length; i++) {
      cnt = deck.contains(new Card(ranks[i], suit));
      if (cnt < 2) return ranks[i];
    }
    // WTF? We'll err on the side of caution...
    return Pinochle.ACE; 
  }
}
