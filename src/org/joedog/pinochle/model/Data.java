package org.joedog.pinochle.model;

public class Data {
  private int meld;
  private int allMeld;
  private int hiMeld;
  private int loMeld;
  private int take;
  private int allTake;
  private int hiTake;
  private int loTake;
  private int hand;
  private int hiHand;
  private int loHand;
  private int noTrick;
  private int game;
  private String  team;
  private boolean winner;
  private boolean bidder;

  public Data(String team) {
    this.team    = team;
    this.meld    = 0;
    this.allMeld = 0;
    this.loMeld  = 1000;
    this.hiMeld  = 0;
    this.take    = 0;
    this.loTake  = 1000;
    this.hiTake  = 0;
    this.hand    = 0;
    this.loHand  = 1000;
    this.hiHand  = 0;
    this.noTrick = 0;
    this.game    = 0;
    this.winner  = false;
    this.bidder  = false;
  }

  public void reset() {
    this.meld    = 0;
    this.allMeld = 0;
    this.loMeld  = 1000;
    this.hiMeld  = 0;
    this.take    = 0;
    this.loTake  = 1000;
    this.hiTake  = 0;
    this.hand    = 0;
    this.loHand  = 1000;
    this.hiHand  = 0;
    this.noTrick = 0;
    this.game    = 0;
    this.winner  = false;
    this.bidder  = false;
  }

  public void resetHand() {
    this.meld   = 0;
    this.take   = 0;
    this.hand   = 0;
  }
    
  public void addMeld(int meld) {
    this.loMeld = (meld < this.loMeld) ? meld : this.loMeld; 
    this.hiMeld = (meld > this.hiMeld) ? meld : this.hiMeld;
    this.meld  += meld;
  }

  public void addTake(int take) {
    this.loTake = (take < this.loTake) ? take : this.loTake;
    this.hiTake = (take > this.hiTake) ? take : this.hiTake;
    this.take  += take;
  }

  public void addHand(int bid, int wscore) {
    this.meld   = (this.take == 0)  ? 0 : this.meld;
    this.hand   = (this.take == 0)  ? 0 : this.meld + this.take;
    if (this.bidder == true) {
      this.hand = (this.hand < bid) ? (bid * -1) : this.hand;
    } else {
    }
    this.loHand = (this.hand < this.loHand) ? this.hand : this.loHand;
    this.hiHand = (this.hand > this.hiHand) ? this.hand : this.hiHand;
    this.game  += this.hand;

    if (this.game >= wscore) {
      this.winner = true;
    }
  }

  public void setBidder(boolean bidder) {
    this.bidder = bidder;
  }

  public void addNoTricker() {
    this.noTrick += 1;
  }

  public String getTeam() {
    return this.team;
  }

  public int getMeld() {
    return this.meld;
  }

  public int getTotalMeld() {
    return this.allMeld;
  }

  public int getTake() {
    return this.take;
  }

  public int getTotalTake() {
    return this.allTake;
  }

  public int getHand() {
    return this.hand;
  }

  public int getGame() {
    return this.game;
  }

  public boolean isBidder() {
    return this.bidder;
  }

  public boolean isWinner() {
    return this.winner;
  }
}
