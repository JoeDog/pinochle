package org.joedog.pinochle.game;

import java.util.Map;
import java.util.HashMap;

import org.joedog.pinochle.player.*;

public class Trick {
  private int     lead = -1; // the suit which was led
  private int     trump;     // the named trump suit
  private int     winner;    // the winning position
  private Card    card;      // the winning card
  private boolean trumped;   // does the trick contain trump?
  private Map<Player, Card> cards = new HashMap<Player, Card>();  

  public Trick(int trump) {
    this.trump    = trump;
    this.trumped  = false;
  }

  public void add(Player player, Card card) {
    if (card.getSuit() == this.trump) {
      this.trumped = true;
    }
    if (cards.size() == 0) { 
      this.card   = card;
      this.lead   = card.getSuit();
      this.winner = player.getPosition();
    } else {
      if (this.card.getSuit() == this.trump && card.getSuit() == this.trump) {
        System.out.println("hand contains trump; new card is trump");
        if (card.getRank() > this.card.getRank()) {
          System.out.println("new card out trumps current winner!");
          this.card   = card;
          this.winner = player.getPosition();
        }
      } else if (card.getSuit() == this.trump) {
        System.out.println("we played first trump on the trick!!!");
        this.card   = card;
        this.winner = player.getPosition();
      } else {
        if (card.getSuit() == this.lead) {
          System.out.println("we're following a non-trump suit");
          if (card.getRank() > this.card.getRank()) {
            this.card   = card;
            this.winner = player.getPosition();
          } 
        }
      }
    }
    cards.put(player, card);
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
}
