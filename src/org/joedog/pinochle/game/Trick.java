package org.joedog.pinochle.game;

import java.util.Map;
import java.util.HashMap;

import org.joedog.pinochle.player.*;

public class Trick {
  private int     lead = -1; // the suit which was led
  private int     trump;     // the named trump suit
  private int     winner;    // the winning position
  private Card    card;      // the winning card
  private Deck    deck;      // easy to pass reference of all cards
  private Hand    hand;      // programmer's convenience...
  private boolean trumped;   // does the trick contain trump?
  private Map<Player, Card> cards = new HashMap<Player, Card>();  

  public Trick(int trump) {
    this.trump    = trump;
    this.trumped  = false;
    this.deck     = new Deck();
    this.hand     = new Hand();
  }

  public void add(Player player, Card card) {
    if (card.getSuit() == this.trump) {
      this.trumped = true;
    }
    this.hand.add(card);
    if (cards.size() == 0) { 
      this.card   = card;
      this.lead   = card.getSuit();
      this.winner = player.getPosition();
    } else {
      if (this.card.getSuit() == this.trump && card.getSuit() == this.trump) {
        this.trumped   = true;
        if (card.getRank() > this.card.getRank()) {
          this.card    = card;
          this.winner  = player.getPosition();
        }
      } else if (card.getSuit() == this.trump) {
        this.card    = card;
        this.winner  = player.getPosition();
        this.trumped = true;
      } else {
        if (card.getSuit() == this.lead) {
          if (card.getRank() > this.card.getRank()) {
            this.card   = card;
            if (! this.trumped) {
              this.winner = player.getPosition();
            }
          } 
        }
      }
    }
    cards.put(player, card);
    deck.add(card); 
  }

  /**
   * Returns true if there are no cards in the 
   * trick - an empty trick indicates a Player
   * has the lead.
   * <p>
   * @param  none
   * @return boolean	true if empty, false if not
  */
  public boolean isEmpty() {
    return (cards.size() == 0);
  }

  public int winner() {
    return this.winner;
  }

  public int getLeadingSuit() {
    return this.lead; 
  }

  public boolean containsTrump() {
    return this.trumped;
  }

  public int getTrump() {
    return this.trump;
  }

  public Card getWinningCard() {
    return this.card;
  }

  public Deck getCards() {
    return this.deck;
  }

  public int counters() {
    int counters = 0;
    for (Map.Entry<Player, Card> play : cards.entrySet()) {
      Card card = play.getValue();
      if (card.isCounter()) { 
        counters++;
      }
    }
    return counters;
  }

  public String toString() {
    if (hand.size() == 0) return "[empty]";
    return this.hand.toString();
  } 
}
