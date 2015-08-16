package org.joedog.pinochle.game;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

import org.joedog.util.Debug;
import org.joedog.pinochle.player.*;

public class Trick {
  private int     trump;     // the named trump suit
  private int     winner;    // the winning position
  private Card    card;      // the winning card
  private Card    lead;      // the winning card
  private Hand    hand;      // programmer's convenience...
  private boolean trumped;   // does the trick contain trump?
  private Map<Player, Card> cards = new HashMap<Player, Card>();  

  public Trick(int trump) {
    this.trump    = trump;
    this.trumped  = false;
    this.hand     = new Hand();
  }

  public void add(Player player, Card card) {
    if (card.getSuit() == this.trump) {
      this.trumped = true;
    }
    switch (player.getPosition()) {
      case Pinochle.NORTH: 
        card.setLocation(450,180);
        break; 
      case Pinochle.SOUTH: 
        card.setLocation(500,245);
        break; 
      case Pinochle.EAST: 
        card.setLocation(550,215);
        break; 
      case Pinochle.WEST:
        card.setLocation(400,215);
        break;
    } 
    this.hand.add(card);
    if (cards.size() == 0) { 
      this.card   = card;
      this.lead   = card;
      this.winner = player.getPosition();
      Debug.print(player.getName()+" led with "+this.lead.toString());
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
        if (card.getSuit() == this.lead.getSuit()) {
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
    return (this.lead == null) ? -1 : this.lead.getSuit(); 
  }

  public Card getLeadingCard() {
    return this.lead;
  }

  public boolean isTrumped() {
    return this.trumped;
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

  public ArrayList <Card> getCards() {
    ArrayList <Card> list = new ArrayList<Card>();
    for (Map.Entry<Player, Card> play : cards.entrySet()) {
      list.add(play.getValue());
    }
    return list;
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
    String str = "";
    if (hand.size() == 0) return "[empty]";
    for (Map.Entry<Player, Card> play : cards.entrySet()) {
      String star = "";
      Player p = play.getKey();
      Card   c = play.getValue();
      star = (p.getPosition() == this.winner()) ? "*" : "";
      str += p.getName()+"("+c.toString()+")"+star+" ";
    }
    return str;
  } 
}
