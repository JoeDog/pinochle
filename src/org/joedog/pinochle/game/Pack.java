package org.joedog.pinochle.game;

import java.util.Stack;
import java.util.Observable;

public class Pack extends Observable {
  private Stack<Card> pack;

  public Pack() {
    pack = new Stack<Card>();
  }

  public void add(Card card) {
    pack.addElement(card);
    setChanged();
    notifyObservers();
  }

  public Card remove(int index) {
    Card c = (Card) pack.remove(index);
    setChanged();
    notifyObservers();
    return c;
  }

  public Card get(int index) {
    return (Card) pack.get(index);
  }

  public Stack<Card> getCards() {
    return this.pack;
  }

  public int count() {
    return pack.size();
  }
}
