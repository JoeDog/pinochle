package org.joedog.pinochle.model;

import java.util.Properties;
import org.joedog.pinochle.game.Pinochle;
import org.joedog.util.NumberUtils;
import org.joedog.util.TextUtils;
import org.joedog.util.Logger;
import org.joedog.pinochle.control.Constants;

public class Match extends AbstractModel {
  private Config        conf;
  private int           round     = 0;
  private Scores        scores    = null;
  private static Match  _instance = null;
  private static Object mutex     = new Object();
   
  private Match() {
    this.round  = 1;
    this.scores = new Scores();
    this.conf   = Config.getInstance();
    this.conf.setProperty("Status", ""+Pinochle.DEAL);
    this.conf.setProperty("Bid",    "0");
    this.conf.setProperty("Bidder", "-1");
  }

 public synchronized static Match getInstance() {
    if (_instance == null) {
      synchronized(mutex) {
        if (_instance == null) {
          _instance = new Match();
        }
      }
    }
    return _instance;
  }

  public void save() {}

  public void showScores() {
    System.out.println(this.scores.toString());
  }

  /**
   * Getters and setters
   * Status     num
   * Dealer     num
   * Active     num 
   * Bidder     num
   * Trump      num
   * Turn       num
   * Bid        (num,num)
   * Meld       (num,num)
   * Score      (num,num)
   * TotalScore (num,num)
   */

  /**
   * Sets the game status for the match
   * <p>
   * @param  String
   * @return
   */
  public void setStatus(String status) {
    int s;
    if (! NumberUtils.isNumeric(status)) {
      this.conf.setProperty("Status", ""+Pinochle.DEAL);
      return;
    }

    s = Integer.parseInt(status);
    if (s < Pinochle.DEAL || s > Pinochle.OVER) {
      this.conf.setProperty("Status", ""+Pinochle.DEAL);
      return;
    }
    this.conf.setProperty("Status", status);
  }

  /**
   * Returns the status of the match
   * <p>
   * @param  void
   * @return String
   */
  public String getStatus() {
    int s;
    String tmp = this.conf.getProperty("Status");
    if (! NumberUtils.isNumeric(tmp)) {
      return ""+Pinochle.DEAL; 
    } 
    s = Integer.parseInt(tmp);
    if (s < Pinochle.DEAL || s > Pinochle.OVER) {
      return ""+Pinochle.DEAL;
    } 
    return this.conf.getProperty("Status");
  }

  /**
   * Designates at which table position the dealer is seated
   * <p>
   * @param  String  The position of the dealer 
   * @return void
   */
  public void setDealer(String value) {
    int i = 1;
    if (! NumberUtils.isNumeric(value)) { 
      conf.setProperty("Dealer", "1");
      return;
    }
    if (value.matches("\\d+")) {
      i = Integer.parseInt(value);
    }
    if (i > Pinochle.PLAYERS[Pinochle.PLAYERS.length-1]) {
      conf.setProperty("Dealer", "1");
      return;
    }
    conf.setProperty("Dealer", ""+i);
  }

  /**
   * Returns the table position of the dealer starting at 1.
   * <p>
   * @param  void
   * @return String
   */
  public String getDealer() {
    if (conf.getProperty("Dealer") == null) {
      conf.setProperty("Dealer", "1");
    }
    return conf.getProperty("Dealer", "1");
  }

  /**
   * Sets the index of the active player at the table
   * <p>
   * @param  String  The position of the active player
   * @return void
   */
  public void setActive(String value) {
    int i = 1;
    if (! NumberUtils.isNumeric(value)) { 
      conf.setProperty("Dealer", "1");
      return;
    }
    if (value.matches("\\d+")) {
      i = Integer.parseInt(value);
    }
    if (i > Pinochle.PLAYERS[Pinochle.PLAYERS.length-1]) {
      conf.setProperty("Active", "1");
      return;
    }
    conf.setProperty("Active", ""+i);
  }

  /**
   * Returns the index of the active player at the table starting at 1
   * <p>
   * @param  void 
   * @return String  The index of the active player
   */
  public String getActive() {
    if (conf.getProperty("Active") == null) {
      conf.setProperty("Active", "1");
    }
    return conf.getProperty("Active", "1");
  }

  /** 
   * Sets the trump suit for the hand. (Or to -1 for reset)
   * <p>
   * @param  String  The numeric representation of the suit as a string
   * @return void
   */
  public void setTrump(String value) {
    conf.setProperty("Trump", value);
    firePropertyChange(Constants.BIDDER, "setTrump", value);
  }

  /**
   * Returns the numeric value of the named trump suit as a String
   * <p>
   * @param  void
   * @return String
   */
  public String getTrump() {
    return conf.getProperty("Trump", "-1");
  }

  /**
   * Sets a numeric value with represents the bidder's seat 
   * <p>
   * @param  String
   * @return void
   */
  public void setBidder(String value) {
    this.conf.setProperty("Bidder", value);
    firePropertyChange(Constants.BIDDER, "setBidder", value);
  }

  /**
   * Returns a String which represents the numeric value of the 
   * Bidder's seat position
   * <p>
   * @param  void
   * @return String
   */ 
  public String getBidder() {
    return this.conf.getProperty("Bidder", ""+Pinochle.EAST);
  }

  /**
   * Parses a String representation of an array, i.e., (num,num), 
   * sets the high number to the bid and adds the array to the 
   * running score model.
   * <p>
   * @param  String  (num,num) in which the high score is the bid
   * @return void
   */
  public void setBid(String value) {
    int high   = 0;
    int [] bid = TextUtils.toArray(value);

    if (bid == null || bid.length < 2) {
      System.err.println("ERROR: invalid Bid format; should be (num,num): "+value);
      bid = new int[] {0,0}; 
    }
    for (int i = 0; i < bid.length; i++) {
      if (bid[i] > high) high = bid[i];
    }   
    this.conf.setProperty("Bid", ""+high);
    this.scores.addBid(new Score(bid[0], bid[1])); 
    firePropertyChange(Constants.BID, "setBid", ""+high);
  }

  /**
   * Parses a String representation of an array, i.e., (num, num)
   * in which the first number represents North-South's meld
   * and the second represents the meld of East-West; this score 
   * is assigned to the current round.
   * <p>
   * @param  String  (num,num) North-South, East-West meld scores
   * @return void 
   */
  public void setMeld(String value) {
    int [] meld = TextUtils.toArray(value);

    if (meld == null || meld.length < 2) {
      System.err.println("ERROR: invalid Bid format; should be (num,num): "+value);
      meld = new int[] {0,0}; 
    }
    Score s = new Score(meld);

    this.conf.setProperty("Meld", ""+s.toString());
    this.scores.addMeld(this.round, s);
    firePropertyChange(Constants.MELD, "setMeld", s.toString());
  }

  /**
   * Parses a String representation of an array, i.e., (num, num)
   * in which the first number represents North-South's counter take
   * and the second represents those of East-West; the score is 
   * assigned to the current round.
   * <p>
   * @param  String  (num,num) North-South, East-West take scores
   * @return void  
   */
  public void setTake(String value) {
    int [] take = TextUtils.toArray(value);

    if (take == null || take.length < 2) {
      System.err.println("ERROR: invalid Bid format; should be (num,num): "+value);
      take = new int[] {0,0}; 
    }
    Score s = new Score(take);
    this.scores.addTake(this.round, s);
    firePropertyChange(Constants.TAKE, "setTake", this.scores.getTake(this.round).toString());
  }

  /**
   * Scoring is assigned in an ArrayList of HashMaps; the round
   * represents the hand number and (round-1) is the index for
   * the ArrayList in which we store our points.
   * <p>
   * @param  String  A string representation of an int
   * @return void
   */
  public void setRound(String value) {
    int num;
    /**
     * It's less messy to test the String than it
     * is to deal with NumberFormatExceptions...
     */
    if (NumberUtils.isNumeric(value)) {
      num = Integer.parseInt(value);
    } else {
      num = this.round;
    }
    this.round = num;
  }

  public String getWinner() {
    return ""+this.scores.hasWinner();
  }

  public void addItUp() {
    int     win    = 300;
    boolean winner = false;
    String  tmp    = conf.getWinningScore();
    if (NumberUtils.isNumeric(tmp)) {
      win = Integer.parseInt(tmp);
    }
    winner = this.scores.total(this.round, win);
    firePropertyChange(Constants.TAKE,  "setTake",  this.scores.getTake(this.round).toString());
    firePropertyChange(Constants.TOTAL, "setTotal", this.scores.getTotal(this.round).toString());
    firePropertyChange(Constants.SCORE, "setScore", this.scores.getScore().toString());
    if (winner) {
      firePropertyChange(Constants.WINNER, "setWinner", this.scores.getScore().toString());
      conf.setProperty("Status", ""+Pinochle.OVER);
    }
    Logger.log(this.scores.toString());
  }

  public String getNSHand() {
    Score s = this.scores.getTotal(this.round);
    return ""+s.getScore(Score.ONE);
  }

  public String getEWHand() {
    Score s = this.scores.getTotal(this.round);
    return ""+s.getScore(Score.TWO);
  }

  public String getNSMeld() {
    Score s = this.scores.getMeld(this.round);
    return ""+s.getScore(Score.ONE);
  }

  public String getEWMeld() {
    Score s = this.scores.getMeld(this.round);
    return ""+s.getScore(Score.TWO);
  }

  public String getNSTake() {
    Score s = this.scores.getTake(this.round);
    return ""+s.getScore(Score.ONE);
  }

  public String getEWTake() {
    Score s = this.scores.getTake(this.round);
    return ""+s.getScore(Score.TWO);
  }

  public void resetGameScore() {
    String reset = "(0,0)";
    firePropertyChange(Constants.MELD,  "setMeld",  reset);
    firePropertyChange(Constants.TAKE,  "setTake",  reset);
    firePropertyChange(Constants.TOTAL, "setTotal", reset);
  }

  public void resetMatchScore() {
    String reset = "(0,0)";
    this.scores.clear();
    firePropertyChange(Constants.MELD,  "setMeld",  reset);
    firePropertyChange(Constants.TAKE,  "setTake",  reset);
    firePropertyChange(Constants.TOTAL, "setTotal", reset);
    firePropertyChange(Constants.SCORE, "setScore", reset);
  }
}
