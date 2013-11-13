package org.joedog.pinochle.player;

import org.joedog.pinochle.game.*;

public class Brain {
  private Deck    deck;
  private Hand    melds[]   = new Hand[4];
  private boolean trumped[] = new boolean[] {false, false, false, false};

  public Brain() {
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
  public void remember(Deck deck, int player) {
    for (int i = 0; i < deck.count(); i++) {
      Card tmp = deck.dealCard(i);
      melds[player].add(tmp);
    }
  }

  /** 
   * We're going to remember every card that 
   * was played; it doesn't matter who played
   * it -- we only care that it was played.
   * <p>
   * @param Card    the card that was played
   */
  public void remember(Card card) {
    deck.add(card);
  }

  public void remember(Deck cards) {
    for (int i = 0; i < deck.size(); i++) {
      this.remember(deck.get(i));
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
  public void newHand() {
    deck = new Deck();
    for (int i = 0; i < 4; i++) {
      melds[i]   = new Hand();  
      trumped[i] = false;
    }
  }

  /**
   * Returns true if Hand contains the highest 
   * available card in the suit
   * <p>
   * @param  Hand  The player's hand 
   * @param  int   The suit we're examining
   * @return boolean 
   */
  public boolean containsHighest(Hand hand, int suit) {
    int rank = highest(suit);
    return (hand.contains(new Card(rank, suit)) > 0);    
  }

  /**
   * Returns the rank of the highest card 
   * available in a suit
   * <p>
   * @param  suit   The suit we're looking for
   * @return int    Rank of the highest available card
   */
  private int highest(int suit) {
    int  rank = -1;
    for (int i = 0; i < deck.count(); i++) {
      Card card;
      card = deck.get(i);      
      rank = (card.getRank() > rank) ? card.getRank() : rank;
    }
    return rank;
  }
}
