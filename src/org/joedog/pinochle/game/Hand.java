package org.joedog.pinochle.game;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
import java.util.List;
import java.util.Iterator;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.joedog.pinochle.model.Location;

public class Hand implements ListModel {
  private ArrayList hand;  
  private ArrayList listeners = new ArrayList();
  private Location  location  = null;
  private int       width     = 372;
  private int       csize     = 62;
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
   * Sets the hand layout for the hand
   * <p>
   * @param  HandLayout
   * @return void
   */
  public void setLayout(Location start, int width){
    this.location = start;
    this.width    = width;
  }

  public void relocate() {
    //if (this.csize == 0) this.csize = this.hand.get(1).getImageWidth();
    // XXX: have to make csize dynamic!!!
    int x = this.location.getX();
    int y = this.location.getY();
    Card tmp = null;
    for (Card c: this.getCards()){
      if (c.played()) {
        continue;
      }
      x += 20;
      c.setLocation(x, y);  
      c.resetWidth();
      tmp = c;
    }
    if (tmp != null) {
      tmp.setWidth(tmp.getImageWidth());
    }
  }

  /**
   * Returns a Card object whose rank and suit match 
   * the parameters
   * <p>
   * @param  int   A card's rank
   * @return int   A card's suit
   */
  public Card getCard(int rank, int suit) {
    for (Card c: this.getCards()){
      if (c.isa(rank, suit)) {
        return c; 
      }
    }
    return null;
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

  public Card getSelected() {
    for (Card c: this.getCards()){ 
      if (c.isSelected()) {
        return c;
      } 
    }
    return null;
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

  public boolean isMarried(Card card) {
    if (card.getRank() == Pinochle.KING) {
      Card queen = new Card(Pinochle.QUEEN, card.getSuit());
      if (this.contains(queen) >= 1) {
        // If the numbers match, he's married
        return (this.contains(card) == this.contains(queen) || this.contains(card) > this.contains(queen));
      } else {
        return false; 
      }
    }
    if (card.getRank() == Pinochle.QUEEN) {
      Card king = new Card(Pinochle.KING, card.getSuit());
      if (this.contains(king) >= 1) {
        // If the numbers match, she's married
        return (this.contains(card) == this.contains(king) || this.contains(card) > this.contains(king)); 
      } else {
        return false; 
      }
    }
    return false;
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

  /**
   * Returns the number of cards left in
   * the hand which are not members of the
   * trump suit.
   * <p>
   * @param   int  The named trump suit
   * @return  int  The count of non-trump cards
   */
  public int nonTrumpCount(int trump) {
    int count = 0;
    for (Card c: this.getCards()) {
      if (c != null && c.getSuit() != trump) {
        count++;
      }
    }
    return count;
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
    for (Card c: this.getCards()) { 
      if (c.getRank() > card.getRank() && c.getSuit() == card.getSuit()) {
        cnt += 1;
      }
    }
    return cnt;
  }

  public Card beat(Card card) {
    return this.beat(card, false);
  }

  public Card beat(Card card, boolean counter) {
    int i = card.getRank()+1;
    int s = card.getSuit();
    for ( ; i <= Pinochle.ACE; i++) {
      for (Card c: this.getCards()) { 
        if (c.getSuit() == s && c.getRank() == i) {
          return c;
        }
      }
    }
    Card tmp = null;
    if (counter == true && this.counters(card.getSuit()) > 0) { 
      tmp = this.getCounter(card.getSuit());
      if (tmp != null) return tmp;
    }
    tmp = this.getLowest(card.getSuit());
    if (tmp != null) return tmp;

    return this.getLowest();
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

  public int selectCount() {
    int num = 0;
    for (Card card : this.getCards()) {
      if (card.isSelected()) {
        num++;
      } 
    }
    return num;
  }

  public void deselectAll() {
    for (Card card : this.getCards()) {
      card.select(false);
    }
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
    boolean b = false;
    if (c == null)
      throw new NullPointerException("Can't add a null card to a hand.");
    b = hand.add(c);
  }

 
  /**
   * Removes a card like Card c
   * <p>
   * @param  Card  Remove a card like this one
   * @return void
   */ 
  public void remove (Card c) { 
    for (Iterator<Card> iterator = this.getCards().iterator(); iterator.hasNext(); ) {
      Card card = iterator.next();
      if (card.matches(c)) {
        iterator.remove();
        this.notifyListeners();
        return; 
      }
    }
    return;
  } 

  /**
   * Removes a card by its numeric ID.
   * <p>
   * @param  int  the id of the card to remove; hand.remove(card.getId());
   * @return void
   */
  public void remove (int id) {
    for (Iterator<Card> iterator = this.getCards().iterator(); iterator.hasNext(); ) {
      Card card = iterator.next();
      if (id == card.getId()) {
        iterator.remove();
        this.notifyListeners();
        return;
      }
    }
    return;
  }

  public void removeAll() {
    for (Iterator<Card> iterator = this.getCards().iterator(); iterator.hasNext(); ) {
      Card card = iterator.next();
      iterator.remove();
    }
    this.notifyListeners();
    return;
  }

  public Card get(int position) {
    if (position < 0 || position >= hand.size())
      throw new IllegalArgumentException("Position does not exist in hand: "+ position);
    return (Card)hand.get(position);
  }

  public int size() {
    return hand.size();
  }

  /**
   * The following methods are in support of the ListModel interface...
   */
  public int getSize() {
    return this.size();
  }

  public Card getElementAt(int index) {
    return this.get(index);
  }

  public void removeListDataListener(javax.swing.event.ListDataListener l) {
    listeners.remove(l);
  }

  public void addListDataListener(javax.swing.event.ListDataListener l) {
    listeners.add(l);
  }

  private void notifyListeners() {
    ListDataEvent le = new ListDataEvent(
      this, ListDataEvent.CONTENTS_CHANGED, 0, this.getSize()
    );
    for (int i = 0; i < listeners.size(); i++) {
      ((ListDataListener)listeners.get(i)).contentsChanged(le);
    }
  }
}


