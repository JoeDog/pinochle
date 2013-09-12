package org.joedog.pinochle.game;

public class Assessment {
  private int suit;
  private int meld;
  private int aces;
  private int taces;
  private int index;
  private int trump;
  private int [] hearts;
  private int [] clubs;
  private int [] diamonds;
  private int [] spades;
  private final static int limit = 3; // this should be provided by the controller
  
  public Assessment() {
    hearts   = new int[limit];
    clubs    = new int[limit];
    diamonds = new int[limit];
    spades   = new int[limit];
  }

  public int getMeld() {
    return this.meld;
  }

  public int getTrumpCount() {
    return this.trump;
  }

  public int getTrumpAces() {
    return this.taces;
  }

  public void setSuit(int suit) {
    this.suit = suit;
  } 

  public void setMeld(int meld) {
    this.meld = meld;
  }

  public void setAces(int aces) {
    this.aces = aces;
  }

  public void setTrumpCount(int trump) {
    this.trump = trump;
  }

  public void setTrumpAces(int aces) {
    this.taces = aces;
  }

  public int getTrump() {
    return this.suit;
  }

  public int maxBid() {
    int bid = this.meld;
    bid += this.aces*2;
    bid += (4-this.trump>0) ? (4-this.trump)*2 : 2;
    bid += 3;
    return bid;
  }
  
  public void add(int index, int suit) {
    switch (suit) {
      case Pinochle.HEARTS:
        if (hearts.length < limit) hearts[hearts.length-1] = index;
        break;
      case Pinochle.CLUBS:
        if (clubs.length < limit) clubs[clubs.length-1] = index;
        break;
      case Pinochle.DIAMONDS:
        if (diamonds.length < limit) diamonds[diamonds.length-1] = index;
        break;
      case Pinochle.SPADES:
        if (spades.length < limit) spades[spades.length-1] = index;
        break;
    }
    return;
  }

  public String toString() {
    String suit = "";
    switch (this.suit) {
      case Pinochle.HEARTS:
        suit = "Hearts";
        break;
      case Pinochle.CLUBS:
        suit = "Clubs";
        break;
      case Pinochle.DIAMONDS:
        suit = "Diamonds";
        break;
      case Pinochle.SPADES:
        suit = "Spades";
        break;
    }
    return "Trump: "+suit+"; Meld: "+this.meld+"; Trump Aces: "+this.taces+"; Trump cards: "+this.trump;
  }
}
