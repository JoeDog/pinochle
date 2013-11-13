package org.joedog.pinochle.player;

import org.joedog.pinochle.control.GameController;
import org.joedog.pinochle.view.Setting;
import org.joedog.pinochle.game.*;
import java.net.URL;
import java.awt.Canvas;

public abstract class Player {
  public static final  int HUMAN    = 0;
  public static final  int COMPUTER = 1;
  protected Hand       hand;
  protected Meld       meld;
  public   int         partner;
  public   int         position;
  public   int         type;
  public   String      name;
  public   Setting     setting;
  public   int         maxBid;
  public   int         myBid;
  public   Assessment  assessment;
  public   boolean     bidder = false;

  public Player () {
    newHand();
  }

  public void takeCard(Card c) {
    this.hand.add(c);
  }

  public void refresh() {
    this.hand.display();
    this.setting.refresh(this.hand);
  }

  public synchronized void newHand() {
    this.hand   = new Hand();
    this.myBid  = 0;
    this.maxBid = 0;
    System.out.println(this.name+"'s newHand(): "+myBid);
  }

  public int assessHand() {
    meld = new Meld(this.hand);
    assessment   = meld.assessment(); 
    this.maxBid  = assessment.maxBid();
    return 1;
  }

  public void setup(Setting setting, int position, int partner, String name) {
    this.setting  = setting;
    this.position = position;
    this.partner  = partner;
    this.name     = name;
  } 

  public void showHand() {
    this.hand.display();
  }

  public boolean wonBid() {
    return this.bidder;
  }

  public int getPosition() {
    return this.position;
  }

  public int getPartner() {
    return this.partner;
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

  public String getName() {
    return this.name;
  }

  public int getType () {
    return this.type;
  }

  public Hand getHand() {
    return this.hand;
  }

  public abstract void remember(Deck cards);

  public abstract Card playCard(Trick trick);

  public abstract int bid(int bid); 

  public abstract int nameTrump();

  public abstract Deck passCards(boolean bidder);

  public abstract void takeCards(Deck d);

  public abstract int meld();

  public abstract void clearMeld();
}
