package org.joedog.pinochle.model;

public class Stats {
  private int nsmeld;
  private int nstake;
  private int ewmeld;
  private int ewtake;
  private int hands;

  public Stats() {
    this.nsmeld   = 0;
    this.nstake   = 0;
    this.ewmeld   = 0;
    this.ewtake   = 0;
    this.hands = 0;
  }

  public void addNSMeld(int meld) {
    this.hands   += 1;
    this.nsmeld  += meld;
  }

  public void addEWMeld(int meld) {
    this.ewmeld += meld;
  }
  
  public void addNSTake(int take) {
    this.nstake += take;
  }

  public void addEWTake(int take) {
    this.ewtake += take;
  }

  public int getNSMeld() {
    return this.nsmeld;
 }

  public int getEWMeld() {
    return this.ewmeld;
  }

  public int getNSTake() {
    return this.nstake;
  }

  public int getEWTake() {
    return this.ewtake;
  }

  public int getHands() {
    return this.hands;
  }
}
