package org.joedog.pinochle.model;

import java.util.Properties;
import org.joedog.pinochle.game.Pinochle;
import org.joedog.pinochle.player.Player;
import org.joedog.pinochle.control.*;

public class GameModel extends AbstractModel {
  private Configuration conf  = null;
  private int     decks;
  private int     nsmeld;
  private int     ewmeld;
  private int     nscounters;
  private int     ewcounters; 
  private boolean simulation = false;
  private String  active;
  private String  trump; 
  private String  bid;

  public GameModel () {
    conf = Configuration.getInstance();
    if (conf.getProperty("DeckSize")!=null && (conf.getProperty("DeckSize")).equals("double")) {
      this.decks = 2;
    } else {
      this.decks = 1;
    }
    if (conf.getProperty("PlayerNorthType")!=null && conf.getProperty("PlayerSouthType")!=null &&
        conf.getProperty("PlayerNorthType")!=null && conf.getProperty("PlayerSouthType")!=null ){
      int N = Integer.parseInt(conf.getProperty("PlayerNorthType"));
      int S = Integer.parseInt(conf.getProperty("PlayerSouthType"));
      int E = Integer.parseInt(conf.getProperty("PlayerEastType"));
      int W = Integer.parseInt(conf.getProperty("PlayerWestType"));
      if (N > 0 && S > 0 && E > 0 && W > 0) {
        this.simulation = true;
      } 
    }
    if (conf.getProperty("Debug") != null && conf.getProperty("Debug").equals("true")) {
      System.getProperties().put("pinochle.debug", "true");
    }
    if (conf.getProperty("WinningScore")==null) {
      conf.setProperty("WinningScore", (decks==2) ? "500" : "300");
    }
  }

  public String getConfigStatus() {
    return String.valueOf(conf.isConfigured());
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

  public void setDialogX(String X) {
    conf.setProperty("DialogX", X);
  }

  public void setDialogY(String Y) {
    conf.setProperty("DialogY", Y);
  }

  public void setPlayerEastName(String name) {
    conf.setProperty("PlayerEastName", name);
    firePropertyChange(GameController.RECONFIG, "EASTNAME", name);
  }

  public void setPlayerWestName(String name) {
    conf.setProperty("PlayerWestName", name);
    firePropertyChange(GameController.RECONFIG, "WESTNAME", name);
  }

  public void setPlayerNorthName(String name) {
    conf.setProperty("PlayerNorthName", name);
    firePropertyChange(GameController.RECONFIG, "NORTHNAME", name);
  }

  public void setPlayerSouthName(String name) {
    conf.setProperty("PlayerSouthName", name);
    firePropertyChange(GameController.RECONFIG, "SOUTHNAME", name);
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

  public void setBidVariation(String variation) {
    conf.setProperty("BidVariation", variation);
  }

  public void setTopVariation(String variation) {
    if (variation.equals("trump")) {
      conf.setProperty("TopVariation", "1");
    } else {
      conf.setProperty("TopVariation", "0");
    }
  }

  public void setWinningScore(String score) {
    conf.setProperty("WinningScore", score);
  }

  public void setMinimumBid(String bid) {
    conf.setProperty("MinimumBid", bid);
  }

  public String getHeadless() {
    if (conf.getProperty("Headless") == null || conf.getProperty("Headless").length() < 1) {
      return "false";
    } 
    return conf.getProperty("Headless");
  }

  public String getMainX() {
    return conf.getProperty("MainX");
  }

  public String getMainY() {
    return conf.getProperty("MainY");
  }

  public String getConfigX() {
    return conf.getProperty("ConfigX");
  }

  public String getConfigY() {
    return conf.getProperty("ConfigY");
  }

  public String getDialogX() {
    if (conf.getProperty("DialogX") == null) {
      return Integer.toString(650);
    }
    return conf.getProperty("DialogX");
  }

  public String getDialogY() {
    if (conf.getProperty("DialogY") == null) {
      return Integer.toString(400);
    }
    return conf.getProperty("DialogY");
  }

  public String getPlayerEastName() {
    String name = conf.getProperty("PlayerEastName");
    if (name != null) return name;
    else return "East";
  }

  public String getPlayerWestName() {
    String name = conf.getProperty("PlayerWestName");
    if (name != null) return name;
    else return "West";
  }

  public String getPlayerNorthName() {
    String name = conf.getProperty("PlayerNorthName");
    if (name != null) return name;
    else return "North";
  }

  public String getPlayerSouthName() {
    String name = conf.getProperty("PlayerSouthName");
    if (name != null) return name;
    else return "South";
  }

  public String getPlayerEastType() {
    String type = conf.getProperty("PlayerEastType");
    if (type == null || ! isNumeric(type)) {
      this.setPlayerEastType(""+Player.COMPUTER);
      return ""+Player.COMPUTER;
    } else {
      return type;
    }
  }

  public String getPlayerWestType() {
    String type = conf.getProperty("PlayerWestType");
    if (type == null || ! isNumeric(type)) {
      this.setPlayerWestType(""+Player.COMPUTER);
      return ""+Player.COMPUTER;
    } else {
      return type;
    }
  }

  public String getPlayerNorthType() {
    String type = conf.getProperty("PlayerNorthType");
    if (type == null || ! isNumeric(type)) {
      this.setPlayerNorthType(""+Player.COMPUTER);
      return ""+Player.COMPUTER;
    } else {
      return type;
    }
  }

  public String getPlayerSouthType() {
    String type = conf.getProperty("PlayerSouthType");
    if (type == null || ! isNumeric(type)) {
      this.setPlayerSouthType(""+Player.HUMAN);
      return ""+Player.HUMAN;
    } else {
      return type;
    }
  }

  public String getWinningScore() {
    if (conf.getProperty("WinningScore")==null) {
      return (decks==2) ? "500" : "300";
    }
    return conf.getProperty("WinningScore");
  }

  public String getCheatMode() {
    String key = MD5((String)conf.getProperty("Cheat"));

    if (key==null || key.length() < 2) {
      return "false";
    } 

    if (key.equals("6f40ce1466318bc16e9541c437609de5")) {
      return "true";
    }
    return "false";
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

  public String getBidVariation() {
    if (conf.getProperty("BidVariation")==null) {
      return "0"; 
    }
    return conf.getProperty("BidVariation");
  }

  public String getTopVariation() {
    if (conf.getProperty("TopVariation") == null) {
      conf.setProperty("TopVariation", "0");
      return "0";
    }
    return conf.getProperty("TopVariation");
  }

  public String getSimulation() {
    return ""+this.simulation;
  }
  
  public String getCardCount() {
    if (this.decks == 1) return "48";
    else return "80";
  }

  public void reset() {
  }

  public void display() {
  }

  public void setOurCounters(String counters) {
    int i = Integer.parseInt(counters);
    this.nscounters += i;
    firePropertyChange(GameController.OURS, "OURS", ""+this.nscounters);
    return;
  }

  public String getOurCounters() {
    return ""+this.nscounters;
  }

  public void setTheirCounters(String counters) {
    int i = Integer.parseInt(counters);
    this.ewcounters += i;
    firePropertyChange(GameController.THEIRS, "THEIRS", ""+this.ewcounters);
    return;
  }
  
  public String getTheirCounters() {
    return ""+this.ewcounters;
  }

  public void save() {
    conf.save();
  }

  public String MD5(String md5) {
    if (md5==null || md5.length() < 2) return "haha";

    try {
      java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
      byte[] array = md.digest(md5.getBytes());
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < array.length; ++i) {
        sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
      }
      return sb.toString();
    } catch (java.security.NoSuchAlgorithmException e) { }
    return null;
  }

  public static boolean isNumeric(String str) {
    return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
  }
}
