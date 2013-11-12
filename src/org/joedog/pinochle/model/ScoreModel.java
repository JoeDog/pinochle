package org.joedog.pinochle.model;

import java.util.Properties;
import org.joedog.pinochle.game.Pinochle;
import org.joedog.pinochle.control.*;

public class ScoreModel extends AbstractModel {
  private int bid      = 0;
  private int dealer   = Pinochle.SOUTH;
  private int trump    = Pinochle.HEARTS;
  private int active   = Pinochle.WEST;
  private int wscore   = 300;
  private int meld[]   = new int[] {0, 0};
  private int take[]   = new int[] {0, 0};
  private int hand[]   = new int[] {0, 0};
  private int game[]   = new int[] {0, 0};
  private int bidder;

  public ScoreModel() {

  } 

  public void setDealer(String player) {
    int i = Integer.parseInt(player);
    this.dealer = i;
  }

  public void setGameWinningScore(String score) {
    int i = Integer.parseInt(score);
    this.wscore = i;
  }

  public void resetHand() {
    System.out.println("HEY! I'm resetting the hand!!!");
    this.meld[0]   = 0;
    this.meld[1]   = 0;
    this.take[0]   = 0;
    this.take[1]   = 0;
    this.hand[0]   = 0;
    this.hand[1]   = 0;
    this.bidder    = -125; 
    firePropertyChange(GameController.RESET, "RESET", "hand");
  }

  public void resetGame() {
    this.resetHand();
    this.game[0] = 0;
    this.game[1] = 0;
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
    this.bidder = i;
    this.active = i;
    System.out.println("SET DA BIDDER: "+this.bidder+"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
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

  public void setNSMeld(String m) {
    int i = Integer.parseInt(m);
    this.meld[0] = i;
    firePropertyChange(GameController.MELD_SCORE, "NSMELD", ""+this.meld[0]);
  }

  public void setEWMeld(String m) {
    int i = Integer.parseInt(m);
    this.meld[1] = i;
    firePropertyChange(GameController.MELD_SCORE, "EWMELD", ""+this.meld[1]);
  }

  public void setNSTake(String c) {
    int i = Integer.parseInt(c);
    this.take[0] += i;
    firePropertyChange(GameController.TAKE_SCORE, "NSTAKE", ""+this.take[0]);
  }

  public void setEWTake(String c) {
    int i = Integer.parseInt(c);
    this.take[1] += i;
    firePropertyChange(GameController.TAKE_SCORE, "EWTAKE", ""+this.take[1]);
  }

  public synchronized void addScore() {
    System.out.println("HERE is MY BIDDER: "+this.bidder);
    this.hand[0] = this.meld[0]+this.take[0];
    if (this.bidder % 2 == 0 && this.hand[0] < this.bid) {
      this.hand[0] = (this.bid * -1);
    }
    firePropertyChange(GameController.HAND_SCORE, "NSHAND", ""+this.hand[0]);
    this.hand[1] = this.meld[1]+this.take[1];
    if (this.bidder % 2 != 0 && this.hand[1] < this.bid) {
      this.hand[1] = (this.bid * -1);
    }
    firePropertyChange(GameController.HAND_SCORE, "EWHAND", ""+this.hand[1]);
    this.game[0] += this.hand[0];
    firePropertyChange(GameController.GAME_SCORE, "NSGAME", ""+this.game[0]);
    this.game[1] += this.hand[1];
    firePropertyChange(GameController.GAME_SCORE, "EWGAME", ""+this.game[1]);

    if (this.game[0] > this.wscore && this.game[1] >= this.wscore) {
      System.out.println("BOTH ARE OVER: "+this.wscore+" bidders: "+this.bidder);
      if (this.bidder % 2 == 0) {
        firePropertyChange(GameController.WINNER, "NONE", "NORTH_SOUTH");
        System.out.println("this.bidder[0]: "+this.bidder);
      } else  {
        firePropertyChange(GameController.WINNER, "NONE", "EAST_WEST");
        System.out.println("this.bidder[1]: "+this.bidder);
      }
    } else if (this.game[0] >= this.wscore) {
      firePropertyChange(GameController.WINNER, "NONE", "NORTH_SOUTH");
      System.out.println("WINNER, WINNER, CHICKEN DINNER: NORTH/SOUTH");
    } else if (this.game[1] >= this.wscore) {
      System.out.println("WINNER, WINNER, CHICKEN DINNER: EAST/WEST");
      firePropertyChange(GameController.WINNER, "NONE", "EAST_WEST");
    }
  }
}
