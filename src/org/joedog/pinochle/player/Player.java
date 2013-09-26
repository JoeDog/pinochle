package org.joedog.pinochle.player;

import org.joedog.pinochle.controller.GameController;
import org.joedog.pinochle.view.Setting;
import org.joedog.pinochle.game.*;
import java.net.URL;
import java.awt.Canvas;

public abstract class Player {
  public static final int HUMAN    = 1;
  public static final int COMPUTER = 2;
  protected Hand    hand;
  protected Meld    meld;
  public int        position;
  public int        type;
  public String     name;
  public Setting    setting;
  public int        maxBid;
  public int        myBid;
  public Assessment assessment;

  public Player () {
    newHand();
  }

  public void takeCard(Card c) {
    this.hand.add(c);
  }

  public void takeCards(Deck d) {
    for (Card c: d.getCards()) {
      if (this.type == HUMAN) {
        c.setFaceUp();
      } else {
        c.setFaceDown();
      }
      this.hand.add(c);
    } 
    this.hand.sort();
    this.setting.refresh(this.hand);
    this.setting.refresh();
  }

  public void refresh() {
    this.hand.display();
    this.setting.refresh(this.hand);
  }

  public void newHand() {
    this.hand   = new Hand();
    this.myBid  = 0;
    this.maxBid = 0;
  }

  public int assessHand() {
    meld = new Meld(this.hand);
    assessment   = meld.assessment(); 
    this.maxBid  = assessment.maxBid();
    return 1;
  }

  public void setup(Setting setting, int position, String name) {
    this.setting  = setting;
    this.position = position;
    this.name     = name;
  } 

  public void showHand() {
    this.hand.display();
  }

  /**
   * This is a programmer's helper; it will
   * never be called in the final product....
   */
  public void clearHand() {
    while (this.hand.size() > 0) {
      this.hand.remove(0);
    }  
  }

  public void clearMeld() {
    for (Card card: this.hand.getCards()) {
      card.unmeld();
    }
  }

  public String getName() {
    return this.name;
  }

  public int getType () {
    return this.type;
  }

  public abstract void takeTurn();

  public abstract int bid(int bid); 

  public abstract int nameTrump();

  public abstract Deck passCards(boolean bidder);

  public abstract int meld();
}
