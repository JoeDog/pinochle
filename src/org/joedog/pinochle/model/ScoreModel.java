package org.joedog.pinochle.model;

import java.util.Properties;
import org.joedog.pinochle.game.Pinochle;
import org.joedog.pinochle.control.*;
import org.joedog.pinochle.util.*;

public class ScoreModel extends AbstractModel {
  private int bid      = 0;
  private int over     = 0;
  private int status   = GameController.DEAL;
  private int dealer   = Pinochle.SOUTH;
  private int trump    = Pinochle.HEARTS;
  private int active   = Pinochle.WEST;
  private Stats stats;
  private Configuration conf = null;

  public ScoreModel() {
    this.stats = new Stats(new String []{"NS","EW"});
    conf = Configuration.getInstance();
  } 

  public ScoreModel(String[] teams) {
    this.stats = new Stats(teams);
  }

  /**
   * This method sets the status of the hand in terms
   * of the phase we're in, i.e., bid, meld, etc.
   * @param  status - a string interpretation of GameController.STATUS
   * @return void
   */
  public void setGameStatus(String status) {
    this.status = Integer.parseInt(status);
  }

  public int getGameStatus() {
    return this.status;
  }

  public void setDealer(String player) {
    int i = Integer.parseInt(player);
    this.dealer = i;
  }


  public void resetHand() {
    stats.resetHand();
    firePropertyChange(GameController.RESET, "RESET", "hand");
  }

  public void resetGame() {
    this.resetHand();
    this.stats.reset();
    firePropertyChange(GameController.RESET, "RESET", "game");
  }

  public void resetGame(String [] teams) {
    this.resetHand();
    this.stats   = new Stats(teams);
    firePropertyChange(GameController.RESET, "RESET", "game");
  }

  public String getDealer() {
    return ""+this.dealer;
  }

  public void setGameBid(String b) {
    this.bid = Integer.parseInt(b);
    firePropertyChange(GameController.HIGH_BID, "HIBID", ""+this.bid);
  }

  public String getGameBid() {
    return ""+this.bid;
  }

  public void setBidder(String player) {
    int i = Integer.parseInt(player);
    this.stats.resetBidder(); // clear both 
    this.active = i;
    if (i % 2 == 0) {
      this.stats.setBidder("NS");
    } else {
      this.stats.setBidder("EW");
    }
  }

  /**
   * This method sets trump for the hand in progress
   * @param  trump - a string interpretation of Pinochle.SUIT
   * @return void
   * @see    game/Pinochle.java
   */
  public void setGameTrump(String trump) {
    this.trump = Integer.parseInt(trump);
    firePropertyChange(GameController.TRUMP, "TRUMP", ""+this.trump);
  }

  public String getGameTrump() {
    return ""+this.trump;
  }

  public void setActivePlayer(String position) {
    this.active = Integer.parseInt(position);
  }

  public String getActivePlayer() {
    return ""+this.active;
  }

  public void setMeld(String m) {
    String[] str = m.split("\\|",-1); 
    int i = Integer.parseInt(str[1]);
    stats.addMeld(str[0], i);
    firePropertyChange(GameController.MELD_SCORE, str[0]+"MELD", ""+stats.getMeld(str[0]));
  }

  public void setTake(String c) {
    String[] str = c.split("\\|",-1); 
    int i = Integer.parseInt(str[1]);
    stats.addTake(str[0], i);
    firePropertyChange(GameController.TAKE_SCORE, str[0]+"TAKE", ""+stats.getTake(str[0]));
    int t = (this.deckSize() == 2) ? 50 : 25; 
    if (stats.getTake(str[0]) == t) {
      stats.addNoTricker(str[0]);
    }
  }
  
  public void addScore() {
    stats.tally(this.bid, this.winningScore());
    if (stats.hasWinner()) {
      this.status = GameController.OVER;
    }
    Debug.print(this.getGameInfo()); 

    if (stats.hasWinner()) {
      firePropertyChange(GameController.WINNER, "NONE", stats.getWinner());
    } 
    firePropertyChange(GameController.MELD_SCORE, "NSMELD", ""+stats.getMeld("NS"));
    firePropertyChange(GameController.MELD_SCORE, "EWMELD", ""+stats.getMeld("EW"));
    firePropertyChange(GameController.TAKE_SCORE, "NSTAKE", ""+stats.getTake("NS"));
    firePropertyChange(GameController.TAKE_SCORE, "EWTAKE", ""+stats.getTake("EW"));
    firePropertyChange(GameController.HAND_SCORE, "NSHAND", ""+stats.getHand("NS"));
    firePropertyChange(GameController.HAND_SCORE, "EWHAND", ""+stats.getHand("EW"));
    firePropertyChange(GameController.GAME_SCORE, "NSGAME", ""+stats.getGame("NS"));
    firePropertyChange(GameController.GAME_SCORE, "EWGAME", ""+stats.getGame("EW"));
  }

  public String getGameInfo() {
    String s = String.format(
      "%14s %4d  %4d\n%14s %4d  %4d\n%14s %4d  %4d\n%14s %4d  %4d\n%14s %4d  %4d\n%14s %4d  %4d\n",
      "Meld:",         stats.getTotalMeld("NS"),   stats.getTotalMeld("EW"),
      "Take:",         stats.getTotalTake("NS"),   stats.getTotalTake("EW"),
      "Game:",         stats.getGame("NS"),        stats.getGame("EW"),
      "No Trickers:",  stats.getNoTrickers("NS"),  stats.getNoTrickers("EW"),
      "Highest Meld:", stats.getHighestMeld("NS"), stats.getHighestMeld("EW"),
      "Highest Take:", stats.getHighestTake("NS"), stats.getHighestTake("EW") 
    );
    return s;
  }
  
  private int winningScore() {
    int i = Integer.parseInt(conf.getProperty("WinningScore"));
    return i;
  }

  private int deckSize() {
    if ((conf.getProperty("DeckSize")).equals("double")) {
      return 2;
    } else {
      return 1;
    }
  }
}
