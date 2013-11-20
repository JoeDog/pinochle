package org.joedog.pinochle.player;

import org.joedog.pinochle.game.*;
import org.joedog.pinochle.util.*;

public class Brain {
  private Deck    deck;
  private Hand    melds[]   = new Hand[4];
  private boolean trumped[] = new boolean[] {false, false, false, false};
  private int     ranks[]   = new int[] {
    Pinochle.ACE, Pinochle.TEN, Pinochle.KING, Pinochle.QUEEN, Pinochle.JACK,
  };

  public Brain() {
    this.deck = new Deck();
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
   * We're going to remember every card that 
   * was played; it doesn't matter who played
   * it -- we only care that it was played.
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
    int cnt = 0;
    Debug.print("Looking for cards higher than: "+card.toString());
    for (int i = Pinochle.ACE; i > card.getRank(); i--) {
      cnt += deck.contains(new Card(i, card.getSuit()));  
    }
    Debug.print("cardsHigherThan "+card.toString()+": "+cnt); 
    return cnt;
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
