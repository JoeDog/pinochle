package org.joedog.pinochle.game;

import java.util.Map;
import java.util.HashMap;

import org.joedog.pinochle.player.*;

public class Trick {
  private int lead; // the suit which was led
  private Map<Player, Card> cards = new HashMap<Player, Card>();  

  public Trick() {

  }

  public void add(Player p, Card c) {
    if (cards.size() == 0) this.lead = c.getSuit();
    cards.put(p, c);
  }

  public int winner() {
    return Pinochle.SOUTH;
  }

  public int getLeadingSuit() {
    return this.lead; 
  }
}
