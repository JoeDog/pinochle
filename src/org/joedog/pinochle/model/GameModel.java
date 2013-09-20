package org.joedog.pinochle.model;

import java.util.Properties;

import org.joedog.pinochle.controller.*;

public class GameModel extends AbstractModel {
  private Configuration conf  = null;
  private int    decks;
  private int    status;
  private String trump; 
  private String bid;
  private String bidder;

  public GameModel () {
    conf = Configuration.getInstance();
    if (conf.getProperty("DeckSize")!=null && (conf.getProperty("DeckSize")).equals("double")) {
      this.decks = 2;
    } else {
      this.decks = 1;
    }
  }

  /**
   * This method sets trump for the hand in progress
   * @param  trump - a string interpretation of Pinochle.SUIT
   * @return void
   * @see    game/Pinochle.java
   */
  public void setGameTrump(String trump) {
    String tmp = this.trump;
    this.trump = trump;
    firePropertyChange(GameController.TRUMP, tmp, this.trump);
  }

  public void setGameBid(String bid) {
    this.bid   = bid;
  }

  public void setGameBidder(String bidder) {
    this.bidder = bidder;
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

  public void setMainX(String X) {
    conf.setProperty("MainX", X);
  }

  public void setMainY(String Y) {
    conf.setProperty("MainY", Y);
  }

  public void setConfigX(String X) {
    conf.setProperty("ConfigX", X);
  }

  public void setConfigY(String Y) {
    conf.setProperty("ConfigY", Y);
  }

  public void setPlayerEastName(String name) {
    conf.setProperty("PlayerEastName", name);
  }

  public void setPlayerWestName(String name) {
    conf.setProperty("PlayerWestName", name);
  }

  public void setPlayerNorthName(String name) {
    conf.setProperty("PlayerNorthName", name);
  }

  public void setPlayerSouthName(String name) {
    conf.setProperty("PlayerSouthName", name);
  }

  public void setPlayerEastType(String type) {
    conf.setProperty("PlayerEastType", type);
  }

  public void setPlayerWestType(String type) {
    conf.setProperty("PlayerWestType", type);
  }

  public void setPlayerNorthType(String type) {
    conf.setProperty("PlayerNorthType", type);
  }

  public void setPlayerSouthType(String type) {
    conf.setProperty("PlayerSouthType", type);
  }

  public void setDeckSize(String size) {
    conf.setProperty("DeckSize", size);
  }

  public void setBidType(String type) {
    conf.setProperty("BidType", type);
  }

  public void setWinningScore(String score) {
    conf.setProperty("WinningScore", score);
  }

  public void setMinimumBid(String bid) {
    conf.setProperty("MinimumBid", bid);
  }

  public String getConfigX() {
    return conf.getProperty("ConfigX");
  }

  public String getConfigY() {
    return conf.getProperty("ConfigY");
  }

  public String getMainX() {
    return conf.getProperty("MainX");
  }

  public String getMainY() {
    return conf.getProperty("MainY");
  }

  public String getPlayerEastName() {
    return conf.getProperty("PlayerEastName");
  }

  public String getPlayerWestName() {
    return conf.getProperty("PlayerWestName");
  }

  public String getPlayerNorthName() {
    return conf.getProperty("PlayerNorthName");
  }

  public String getPlayerSouthName() {
    return conf.getProperty("PlayerSouthName");
  }

  public String getPlayerEastType() {
    return conf.getProperty("PlayerEastType");
  }

  public String getPlayerWestType() {
    return conf.getProperty("PlayerWestType");
  }

  public String getPlayerNorthType() {
    return conf.getProperty("PlayerNorthType");
  }

  public String getPlayerSouthType() {
    return conf.getProperty("PlayerSouthType");
  }

  public String getWinningScore() {
    if (conf.getProperty("WinningScore")==null) {
      return (decks==2) ? "500" : "300";
    }
    return conf.getProperty("WinningScore");
  }

  public String getMinimumBid() {
    if (conf.getProperty("MinimumBid")==null) {
      return (decks==2) ? "50" : "16";
    }
    return conf.getProperty("MinimumBid");
  }

  public String getDeckSize() {
    if (conf.getProperty("DeckSize")==null) {
      return "single"; 
    }
    return conf.getProperty("DeckSize");
  }

  public String getBidType() {
    if (conf.getProperty("BidType")==null) {
      return "single"; 
    }
    return conf.getProperty("BidType");
  }

  public String getGameBid() {
    return this.bid;
  }

  public String getGameTrump() {
    return this.trump;
  }

  public String getGameBidder() {
    return this.bidder;
  }

  public int getGameStatus() {
    return this.status;
  }

  public void reset() {
  }

  public void display() {
  }

  public void save() {
    conf.save();
  }
}
