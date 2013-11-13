package org.joedog.pinochle.game;

import java.util.Stack;
import java.util.Observable;

public class Pack extends Observable {
  private Stack<Card> pack;

  /**
   * Create a new pack of cards
   * <p>
   * @return Pack
   */
  public Pack() {
    pack = new Stack<Card>();
  }

  /**
   * Add a Card to the Pack
   * <p>
   * @param  Card 
   * @return void
   */
  public void add(Card card) {
    pack.addElement(card);
    setChanged();
    notifyObservers();
  }

  /**
   * Remove a card at position index from the pack
   * <p>
   * @param int   the Stack index position of the card
   * @return      Card 
   */
  public Card remove(int index) {
    Card c = (Card) pack.remove(index);
    setChanged();
    notifyObservers();
    return c;
  }

  /** 
   * Return a reference to the card at Pack position index
   * <p>
   * @param  int   the Stack index position of the card
   * @return       Card
   */
  public Card get(int index) {
    return (Card) pack.get(index);
  }

  /**
   * Return a Stack representation of the Cards in the Pack
   * <p>
   * @param  none
   * @return Stack<Card>
   */ 
  public Stack<Card> getCards() {
    return this.pack;
  }

  /** 
   * Return an int representation of the size of the Pack 
   * [same value as count()]
   * <p> 
   * @param  none
   * @return int
   */
  public int size() {
    return pack.size();
  }

  /** 
   * Return an int representation of the count of the Pack 
   * [same value as size()]
   * <p> 
   * @param  none
   * @return int
   */
  public int count() {
    return pack.size();
  }
}
