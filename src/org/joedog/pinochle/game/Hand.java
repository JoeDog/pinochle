package org.joedog.pinochle.game;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
import java.util.List;
import java.util.Iterator;

public class Hand {
  private ArrayList hand;  
  private int       ranks[]   = new int[] {
    Pinochle.ACE, Pinochle.TEN, Pinochle.KING, Pinochle.QUEEN, Pinochle.JACK,
  };

  /**
   * Create a new empty Pinochle hand
   * <p>
   * @return Hand
   */
  public Hand() {
    hand = new ArrayList();
  }

  /**
   * Return a List<Card> of all the cards in the Hand
   * <p>
   * @param  none
   * @return List<Card>
   */
  public List<Card> getCards() {
    return hand;
  }

  /**
   * Clear the hand of all cards
   * <p>
   * @param  none
   * @return void
   */
  public void reset() {
    hand.clear();
  }

  /** 
   * Return an ArrayList interpretation of the Hand
   * <p>
   * @param  none
   * @return ArrayList
   * @see    ArrayList
   */
  public ArrayList getHand() {
    return this.hand;
  }

  /**
   * Return an int which represents Card's postion
   * in the hand; this method returns the position 
   * of the first instance of RANK and SUIT.
   * <p>
   * @param  Card
   * @return int   position in the hand or -1 not found
   */
  public int position (Card card) {
    for (int i = 0; i < hand.size(); i++) {
      Card c = (Card)hand.get(i);
      if (c.matches(card)) {
        return i;  
      } 
    }
    return -1;
  }

  /** 
   * Returns an int which represents Card's position
   * in the hand AT or AFTER mark; a match is based on
   * the first occurance of RANK and SUIT
   * <p>
   * @param Card    the Card we're looking for
   * @param int     position in the deck at which we start looking
   */
  public int position (Card card, int mark) {
    for (int i = mark; i < hand.size(); i++) {
      Card c = (Card)hand.get(i);
      if (c.matches(card)) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Return an int which represents how many cards were
   * selected in the hand; generally these selects were
   * for passing or melding
   * <p>
   * @param  none
   * @return int    The total number of selected cards
   */
  public int numSelected() {
    int num = 0;
 
    for (int i = 0; i < hand.size(); i++) {
      Card tmp = (Card)hand.get(i);
      if (tmp.isSelected()) {
            num += 1;
      }
    }
    return num;
  }

  /**
   * Returns an int which represents how many instances
   * of Card the hand contains
   * <p>
   * @param  Card  The Card to match
   * @return int   The number of instances we've found 
   */
  public int contains (Card card) { 
    int found = 0;
    for (Card c: this.getCards()){ 
      if (c.matches(card)) {
        found ++;
      }
    } 
    return found;
  }

  /**
   * Returns an int which represents the number of 
   * instances of suit discovered in the hand
   * <p>
   * @param  int   The suit we're looking for
   * @return int   The number of instances we've found
   */
  public int contains (int suit) {
    int num = 0;
    for (Card c: this.getCards()) { 
      if (c.getSuit() == suit) {
        num ++;
      }
    } 
    return num;
  }

  /** 
   * Returns an int which represents the number of
   * singletons in our hand. A singleton is a bare
   * ace in a suit. With nothing to play off on the
   * other ace; we want to play our singletons
   * <p>
   * @param  none
   * @return int   the number of suits containing singletons
   */
  public int singletons() {
    int num = 0; //singletons
    for (int i = 0; i < 4; i++) {
      if (this.contains(new Card(Pinochle.ACE, i)) == 1 && this.contains(i) == 1) {
        num ++;
      }
    }
    return num;
  }

  public Card getSingleton() {
    Card tmp = null;
    for (int i = 0; i < 4; i++) {
      if (this.contains(new Card(Pinochle.ACE, i)) == 1 && this.contains(i) == 1) {
        tmp = new Card(Pinochle.ACE, i);
        break;
      }
    }
    // this check is probably not necessary, 
    // but you can't be too cautious these days...
    if (this.contains(tmp) > 0) 
      return tmp;
    else 
      return null;
  }

  public boolean canTop(Card card) {
    if (card == null) {
      return false;
    }
    for (Card c: this.getCards()){
      if (c.getSuit() == card.getSuit() && c.getRank() > card.getRank()) {
        return true;
      }
    }
    return false;
  }
 
  /**
   * Returns an int which represents the number of aces
   * of parameter suit contained within the Hand
   * <p>
   * @param  int  The suit we're looking for 
   * @return int  The number of aces found
   */
  public int aces (int suit) {
    int num = 0;
    for (Card c: this.getCards()) {
      if (c.getSuit() == suit && c.getRank() == Pinochle.ACE) 
        num ++;
    }
    return num;
  }
  
  /**
   * Returns an int which represents the number of queens
   * of parameter suit contained within the Hand
   * <p>
   * @param  int  The suit we're looking for 
   * @return int  The number of queens found
   */
  public int queens (int suit) {
    int num = 0;
    for (Card c: this.getCards()) {
      if (c.getSuit() == suit && c.getRank() == Pinochle.QUEEN) 
        num ++;
    }
    return num;
  }

  /**
   * Returns are card which is not in trump; 
   * generally invoked near the end of the 
   * hand in order to preserve trump for the
   * last trick.
   * <p>
   * @param  int   The named trump suit
   * @return Card  A non-trump Card
   */
  public Card nonTrump(int trump) {
    Card card = null;
    for (Card c: this.getCards()) {
      if (c != null && c.getSuit() != trump) {
        card = c;
      } 
    }
    return card;
  }

  public Card getLowest() {
    //XXX: should call contains first  
    Card card  = this.get(0); 
    for (Card c: this.getCards()) { 
      if (c != null && c.getRank() < card.getRank()) {
        card = c;
      }
    } 
    return card;
  }
  
  public Card getLowest(int suit) {
    //XXX: should call contains first  
    Card card = null; 
    for (Card c: this.getCards()) { 
      if (c.getSuit() == suit) {
        if (card == null) {
          card = c;
        } else {
          if (c.getRank() < card.getRank()) 
            card = c;
        }
      }
    } 
    return card;
  }

  /**
   * Returns a counter in 'suit' and null if 
   * none are available. 
   * <p>
   * @param  int  the suit we hope to follow
   * @return Card a counter (10, K) or null
   */
  public Card getCounter(int suit) {
    for (Card c: this.getCards()) { 
      if (c.getSuit() == suit && c.getRank() == Pinochle.KING) {
        return c;
      }
    }
    for (Card c: this.getCards()) { 
      if (c.getSuit() == suit && c.getRank() == Pinochle.TEN) {
        return c;
      }
    }
    return null;
  }
  
  /**
   * Returns a counter in any suit or null if 
   * none are available. 
   * <p>
   * @param  none  any suit will do
   * @return Card  a counter (10, K) or null
   */
  public Card getCounter() {
    for (Card c: this.getCards()) { 
      if (c.getRank() == Pinochle.KING) {
        return c;
      }
    }
    for (Card c: this.getCards()) { 
      if (c.getRank() == Pinochle.TEN) {
        return c;
      }
    }
    return null;
  }
  
  /**
   * Returns the total number of cards in the hand
   * which are higher than parameter card.
   * <p>
   * @param  Card  the card we're comparing 
   * @return int   the number of cards we have which beat it
   */
  public int cardsHigherThan(Card card) {
    int cnt = 0;
    for (int i = card.getRank()+1; i <= Pinochle.ACE; i++) {
      for (Card c: this.getCards()) { 
        if (c.getRank() > card.getRank()) {
          cnt += 1;
        }
      }
    }
    return cnt;
  }

  public Card beat(Card card) {
    int i = card.getRank()+1;
    for ( ; i <= Pinochle.ACE; i++) {
      for (Card c: this.getCards()) { 
        if (c.getRank() == i) {
          return c;
        }
      }
    }
    return null;
  }
  
  public Card getHighest(int suit) {
    //XXX: should call contains first  
    Card card = null; 
    for (Card c: this.getCards()) { 
      if (c.getSuit() == suit) {
        if (card == null) {
          card = c;
        } else {
          if (c.getRank() > card.getRank()) 
            card = c;
        }
      }
    } 
    return card;
  }

  public int nonCounters(int suit) {
    int  num  = 0;
    Card card = null; 
    for (Card c: this.getCards()) { 
      if (! c.isCounter()) {
        num ++;
      }
    } 
    return num;
  }

  public int counters(int suit) {
    int  num  = 0;
    Card card = null; 
    for (Card c: this.getCards()) { 
      if (c.isCounter() && c.getRank() != Pinochle.ACE) {
        num ++;
      }
    } 
    return num;
  }

  public Deck getSuit (int suit) {
    Deck deck = new Deck();
    for (Card c: this.getCards()) {
      if (c.isa(suit)) {
        deck.add(c);
      }  
    }  
    return deck;
  }

  public void meld (Card card) {
    meld(card, 1);
  }

  public void meld (Card card, int num) {
    int total = 0; 

    if (num == 0) return;
    for (Card c: this.getCards()) {
      if (c.matches(card)) {
        c.meld();
        total += 1;
        if (total== num) return;
      }  
    }
    return;
  }

  public Card pass (Card card) {
    Card tmp = null;
    for (Card c: this.getCards()) {
      if (c.matches(card)) {
        tmp = new Card(c); 
        return tmp;
      }
    }
    return null;
  }

  public void sort() {
    Comparator cc = Card.getComparator(
      Card.SortParameter.SUIT_ASCENDING,
      Card.SortParameter.RANK_DESCENDING
    );
    Collections.sort(hand, cc);
  }

  public void sortByPosition() {
    Comparator cc = Card.getComparator(
      Card.SortParameter.SUIT_ASCENDING,
      Card.SortParameter.RANK_DESCENDING
    );
    Collections.sort(hand, cc);

  }

  public void display() {
    System.out.println(this.toString());
    System.out.println(" ");
  }
  
  @Override
  public String toString() {
    String str = "";
    Comparator cc = Card.getComparator(
      Card.SortParameter.SUIT_ASCENDING,
      Card.SortParameter.RANK_DESCENDING
    );
    Collections.sort(hand, cc); 
    for (int i = 0; i < hand.size(); i++) { 
      Card c = (Card)hand.get(i);
      String m = (c.melded()==true) ? "[*] " : "";
      str = str + c.toString()+" "+m;
    }
    return str;
  }

  public String asRanks() {
    String str = "";
    Comparator cc = Card.getComparator(
      Card.SortParameter.SUIT_ASCENDING,
      Card.SortParameter.RANK_DESCENDING
    );
    Collections.sort(hand, cc); 
    for (int i = 0; i < hand.size(); i++) { 
      Card c = (Card)hand.get(i);
      str = str + Pinochle.store(c.getRank(), c.getSuit());
    }
    return str;
  }

  public String toMemory() {
    String str = "";
    for (int i = 0; i < 4; i++) {
      str += memorize(i);
      if (i < 3) str += "|";
    }
    return str;
  }

  public String memorize(int suit) {
    String str = "";
    Comparator cc = Card.getComparator(
      Card.SortParameter.SUIT_ASCENDING,
      Card.SortParameter.RANK_DESCENDING
    );
    Collections.sort(hand, cc); 
    for (int i = 0; i < hand.size(); i++) { 
      Card c = (Card)hand.get(i);
      if (c.getSuit() == suit) {
        str = str + Pinochle.store(c.getRank(), c.getSuit()); 
      }
    }
    return str;
  }

  public void add(Card c) {
    if (c == null)
      throw new NullPointerException("Can't add a null card to a hand.");
    hand.add(c);
  }

  public void remove (Card c) { 
    for (Iterator<Card> iterator = this.getCards().iterator(); iterator.hasNext(); ) {
      Card card = iterator.next();
      if (card.matches(c)) {
        iterator.remove();
        return; 
      }
    }
    return;
  } 

  public void remove(List<Integer> indices) {
    Collections.sort(indices, Collections.reverseOrder());
    for (int i : indices) {
      if (i < 0 || i >= hand.size())
        throw new IllegalArgumentException("Card does not exist: " + i);
      hand.remove(i);
    }
  }

  public void remove(int position) {
    if (position < 0 || position >= hand.size())
      throw new IllegalArgumentException("Position does not exist in hand: " + position);
    hand.remove(position);
  }

  public void removeAll() {
    for (Iterator<Card> iterator = this.getCards().iterator(); iterator.hasNext(); ) {
      Card card = iterator.next();
      iterator.remove();
    }
    return;
  }

  public int size() {
    return hand.size();
  }

  public Card get(int position) {
    if (position < 0 || position >= hand.size())
      throw new IllegalArgumentException("Position does not exist in hand: "+ position);
    return (Card)hand.get(position);
  }
}


