package org.joedog.pinochle.game;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
import java.util.List;
import java.util.Iterator;

public class Hand {
  private ArrayList hand;  

  public Hand() {
    hand = new ArrayList();
  }

  public List<Card> getCards() {
    return hand;
  }

  public void reset() {
    hand.clear();
  }

  public ArrayList getHand() {
    return this.hand;
  }

  public int position (Card card) {
    for (int i = 0; i < hand.size(); i++) {
      Card c = (Card)hand.get(i);
      if (c.matches(card)) {
        return i;  
      } 
    }
    return -1;
  }

  public int position (Card card, int mark) {
    for (int i = mark; i < hand.size(); i++) {
      Card c = (Card)hand.get(i);
      if (c.matches(card)) {
        return i;
      }
    }
    return -1;
  }

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

  public int contains (Card card) { 
    int found = 0;
    for (Card c: this.getCards()){ 
      if (c.matches(card)) {
        found ++;
      }
    } 
    return found;
  }

  public int contains (int suit) {
    int num = 0;
    for (Card c: this.getCards()) { 
      if (c.getSuit() == suit) {
        num ++;
      }
    } 
    return num;
  }

  public int aces (int suit) {
    int num = 0;
    for (Card c: this.getCards()) {
      if (c.getSuit() == suit && c.getRank() == Pinochle.ACE) 
        num ++;
    }
    return num;
  }
  
  public int queens (int suit) {
    int num = 0;
    for (Card c: this.getCards()) {
      if (c.getSuit() == suit && c.getRank() == Pinochle.QUEEN) 
        num ++;
    }
    return num;
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


