package org.joedog.pinochle.model;

import java.util.Map;
import java.util.HashMap;

public class Stats {
  private int     hands;
  private String  winner = ""; 
  private boolean over; 
  private Map<String, Data> data = new HashMap<String, Data>();

  public Stats() {
    this.hands  =  0;
    this.winner = "";
  }

  public Stats (String [] teams) {
    for (String t : teams) {
      this.data.put(t, new Data(t));
    }
  }

  public void reset() {
    this.hands  =  0;
    this.winner = "";
    for (Map.Entry<String, Data> d : this.data.entrySet()) {
      (d.getValue()).reset();
    }
  }

  public void resetHand() {
    for (Map.Entry<String, Data> d : this.data.entrySet()) {
      (d.getValue()).resetHand();
    }
  }

  public void addHand() {
    this.hands += 1;
  }

  public void addPlayerData(String team) {
    this.data.put(team, new Data(team));
  }

  public void addMeld(String team, int meld) {
    (this.data.get(team)).addMeld(meld);
  }

  public void addTake(String team, int take) {
    (this.data.get(team)).addTake(take);
  }

  public void setBidder(String team) {
    for (Map.Entry<String, Data> d : this.data.entrySet()) {
      if (d.getKey().equals(team)) {
        (d.getValue()).setBidder(true);
      }
    }
  }

  public void resetBidder() {
    for (Map.Entry<String, Data> d : this.data.entrySet()) {
      (d.getValue()).setBidder(false);
    }
  }

  public void tally(int bid, int wscore) {
    int wins =  0;
    for (Map.Entry<String, Data> d  : this.data.entrySet()) {
      (d.getValue()).addHand(bid, wscore);
      if ((d.getValue()).isWinner()) wins += 1;
    }
    if (wins == 1) {
      for (Map.Entry<String, Data> d  : this.data.entrySet()) {
        if ((d.getValue()).isWinner()) this.winner = (d.getValue()).getTeam();
      }
    }
    if (wins > 1) {
      for (Map.Entry<String, Data> d  : this.data.entrySet()) {
        if ((d.getValue()).isWinner() && (d.getValue()).isBidder()) {
          this.winner = (d.getValue()).getTeam();
        }
      }
    }
  }

  public int getHands() {
    return this.hands;
  }

  public int getMeld(String team) {
    return (this.data.get(team)).getMeld();
  }

  public int getTotalMeld(String team) {
    return (this.data.get(team)).getTotalMeld();
  }

  public int getTake(String team) {
    return (this.data.get(team)).getTake();
  }

  public int getTotalTake(String team) {
    return (this.data.get(team)).getTotalTake();
  }

  public int getHand(String team) {
    return (this.data.get(team)).getHand();
  }

  public int getGame(String team) {
    return (this.data.get(team)).getGame();
  }

  public String getWinner() {
    return this.winner;
  }

  public boolean hasWinner() {
    return (this.winner != null && this.winner.length() > 0);
  } 
} 
