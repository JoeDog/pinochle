package org.joedog.pinochle.player;

import org.joedog.pinochle.control.GameController;
import org.joedog.pinochle.view.Setting;
import org.joedog.pinochle.game.*;
import java.net.URL;
import java.awt.Canvas;
import java.util.Random;

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
  public   int         myBid  = 0;
  public   int         pBid   = 0; 
  public   Assessment  assessment;
  public   boolean     bidder = false;

  public Player () {
    newHand();
  }

  public void takeCard(Card c) {
    this.hand.add(c);
  }

  public void refresh() {
    //this.hand.display();
    this.setting.refresh(this.hand);
  }

  public synchronized void newHand() {
    this.hand   = new Hand();
    this.myBid  = 0;
    this.maxBid = 0;
    this.pBid   = 0;
  }

  public int assessHand() {
    meld = new Meld(this.hand);
    assessment   = meld.assessment(); 
    this.maxBid  = assessment.maxBid();
    this.maxBid += guts();
    return 1;
  }

  private int guts() {
    Random r = new Random();
    int    n = 101;
    int    i = r.nextInt() % n;
    int  num =  1 + i;

    if (num >= 90) {
      System.out.println(this.name+" is feeling confident: +4");
      return 4;
    }
    if (num >= 80) {
      System.out.println(this.name+" is feeling pretty good: +3");
      return 3;
    }
    if (num >= 70) {
      System.out.println(this.name+" is feeling okay: +2");
      return 2;
    }
    if (num >= 60) {
      System.out.println(this.name+" is feeling meh: +1");
      return 1;
    }
    if (num < 11) {
      System.out.println(this.name+" is NOT feeling confident: -2");
      return -2;
    }
    return 0;
  }

  public void setup(Setting setting, int position, int partner, String name) {
    this.setting  = setting;
    this.position = position;
    this.partner  = partner;
    this.name     = name;
  } 

  public void showHand() {
    System.out.println(this.name+" "+this.hand.toString());
  }

  public String handToString() {
    return this.name+" "+this.hand.toString();
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

  public int lastBid() {
    return this.myBid;
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
  
  public abstract void remember(Card card);

  public abstract Card playCard(Trick trick);

  public abstract int bid(int bid); 

  public abstract int bid(int bid, int pbid);

  public abstract int nameTrump();

  public abstract Deck passCards(boolean bidder);

  public abstract void takeCards(Deck d);

  public abstract int meld();

  public abstract void clearMeld();
}
