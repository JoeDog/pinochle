package org.joedog.pinochle.model;
/**
 * Copyright (C) 2013-2016
 * Jeffrey Fulmer - <jeff@joedog.org>, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *--
 */

import java.awt.Point;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Enumeration;
import org.joedog.util.Setup;
import org.joedog.util.Debug;
import org.joedog.util.NumberUtils;
import org.joedog.util.FileUtils;
import org.joedog.util.FileLineIterator;
import org.joedog.util.FileLineReader;
import org.joedog.pinochle.control.Constants;

public class Config extends AbstractModel {
  private Attributes    conf      = null;
  private static String sep       = java.io.File.separator;
  private static String cfgdir    = System.getProperty("user.home")+sep+".pinochle";
  private static String cfgfile   = cfgdir+sep+"game.conf";
  private static String memfile   = cfgdir+sep+"memory.txt";
  private static String hsfile    = cfgdir+sep+"scores-2.0.data";
  private static String cardfile  = cfgdir+sep+"cards.txt";
  private static String savefile  = cfgdir+sep+"saved.txt";
  private static Config _instance = null;
  private static Object mutex     = new Object();

  private Config() {
    System.getProperties().put("pinochle.dir",    cfgdir);
    System.getProperties().put("pinochle.conf",   cfgfile);
    System.getProperties().put("pinochle.memory", memfile);
    System.getProperties().put("pinochle.scores", hsfile);
    System.getProperties().put("pinochle.cards",  cardfile);
    System.getProperties().put("pinochle.saved",  savefile);

    conf = new Attributes();

    if (! FileUtils.exists(this.cfgdir)) {
      FileUtils.mkdirs(this.cfgdir); 
    }
      
    this.legacyFix();

    Setup.install(Setup.MEMORY);

    try {
      FileInputStream fis = new FileInputStream(new File(this.cfgfile));
      conf.load(fis);
      if (conf.getProperty("Debug") != null && conf.getProperty("Debug").equals("true")) {
        System.getProperties().put("joedog.debug", "true");
      } 

      if (conf.getProperty("Log") != null) {
        System.getProperties().put("log.file", conf.getProperty("Log"));
      }
      /**
       * this is sloppy but we like turing this on for debugging 
       */
      Enumeration e = conf.propertyNames();
      while (e.hasMoreElements()) {
        String key = (String) e.nextElement();
        Debug.print(key + ": " + conf.getProperty(key));
      }
    } catch (Exception e) {
      // catch Config Exception right here
      e.printStackTrace();
    }

    this.legacyFixToo();
  }

  public synchronized static Config getInstance() {
    if (_instance == null) {
      synchronized(mutex) {
        if (_instance == null) {
          _instance = new Config();
        }
      }
    }
    return _instance;
  }

  private void legacyFix() {
    File a = new File(System.getProperty("user.home")+"/.pinochle.properties");
    if (a.exists()) {
      a.renameTo(new File(this.cfgfile));
    }
  }
  
  private void legacyFixToo() {
    conf.remove("MainX");
    conf.remove("MainY");
    conf.remove("DialogX");
    conf.remove("DialogY");
    conf.remove("ConfigX");
    conf.remove("ConfigY");
    if (conf.getProperty("PlayerNorthType") != null && conf.getProperty("PlayerNorthType").equals("1")) 
      conf.setProperty("PlayerNorthType", "computer");
    if (conf.getProperty("PlayerNorthType") != null && conf.getProperty("PlayerNorthType").equals("0")) 
      conf.setProperty("PlayerNorthType", "human");
    if (conf.getProperty("PlayerSouthType") != null && conf.getProperty("PlayerSouthType").equals("1")) 
      conf.setProperty("PlayerSouthType", "computer");
    if (conf.getProperty("PlayerSouthType") != null && conf.getProperty("PlayerSouthType").equals("0")) 
      conf.setProperty("PlayerSouthType", "human");
    if (conf.getProperty("PlayerEastType") != null  && conf.getProperty("PlayerEastType").equals("1")) 
      conf.setProperty("PlayerEastType", "computer");
    if (conf.getProperty("PlayerEastType") != null  && conf.getProperty("PlayerEastType").equals("0")) 
      conf.setProperty("PlayerEastType", "human");
    if (conf.getProperty("PlayerWestType") != null  && conf.getProperty("PlayerWestType").equals("1")) 
      conf.setProperty("PlayerWestType", "computer");
    if (conf.getProperty("PlayerWestType") != null  && conf.getProperty("PlayerWestType").equals("0")) 
      conf.setProperty("PlayerWestType", "human");
    this.save();
  }

  public Enumeration propertyNames() {
    return conf.propertyNames();
  }

  public void save() {
    try {
      conf.store(new FileOutputStream(this.cfgfile), null);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return;
  }

  // get property value by name
  public String getProperty(String key) {
    String value = null;

    if (conf.containsKey(key)) {
      value = (String) conf.get(key);
    } else {
      // the property is absent
    }
    return value;
  }

  public String getProperty(String key, String def) {
    String val = this.getProperty(key);
    return (val == null) ? def : val;
  }

  public void setProperty(String key, String val) {
    conf.setProperty(key, val);
  }

  /**
   * If you have a config file, we'll assume you're config'd
   */
  private boolean isConfigured() {
    return FileUtils.exists(this.cfgfile);
  }

  /**
   * Getters and setters 
   *
   * Tracked properties in the class:
   * MainDimension=(num,num)       // dimensions of the main frame
   * MainPosition=(num,num)        // screen position of the main frame
   * ConfigPosition=(num,num)      // screen position of the configuration frame
   * MinimumBid=num                // minimum pinochle bid
   * WinningScore=num              // score at which the game ends
   * DeckSize=single|double        // one or two deck variation
   * BidVariation=auction|single   // auction or single bid pinochle variation
   * PlayerNorthName=string        // name of the player on the north face
   * PlayerSouthName=string        // name of the player on the south face 
   * PlayerEastName=string         // name of the player on the east side
   * PlayerWestName=string         // name of the player on the west face
   * PlayerNorthType=1|0           // computer (1) or human (0) - default computer
   * PlayerSouthType=0|1           // computer (1) or human (0) - default human
   * PlayerEastType=1|0            // computer (1) or human (0) - default computer
   * PlayerWestType=1|0            // computer (1) or human (0) - default computer
   * Cheat=string                  // a passphrase to set the game in cheat mode
   * TopVariation=1|0              // must top trick (1) or can playoff (0) 
   * Headless=true|false           // run game without GUI (for building AI)
   * Debug=true|false              // print debugging information to screen
   */

  /**
   * Sets the dimensions for the main frame. The default  
   * minimum size 1024x690. The format here is (1024,690)
   * <p>
   * @param  String   In format (num,num) 
   * @return void
   */
  public void setMainDimension(String value) {
    String dim = this.dimensionCleanse(value, 1024, 600);
    this.conf.setProperty("MainDimension", dim);
  }

  /**
   * Returns the default dimensions of the main frame
   * size in the following format: (num,num).
   * @param  void
   * @return String  default value is (1024,690)
   */
  public String getMainDimension() {
    return this.conf.getProperty("MainDimension", "(1024,600)");
  }

  /** 
   * Sets the x,y coordinates of the main frame. It expects 
   * those coordinates in the following format: (num,num)
   * <p>
   * @param  String  default value is (10,10)
   * @return void
   */
  public void setMainPosition(String value) {
    String dim = this.dimensionCleanse(value, 10, 10);
    this.conf.setProperty("MainPosition", dim);
  }

  /** 
   * Returns the x,y coordinates for the main frame in the
   * following format: (num,num)
   * <p>
   * @param  void
   * @return String  default value: (10,10)
   */
  public String getMainPosition() {
    return this.conf.getProperty("MainPosition", "(10,10)");
  }

  /** 
   * Returns the x,y coordinates for the Config frame in the
   * following format: (num,num)
   * <p>
   * @param  void
   * @return String  default value: (10,10)
   */
  public void setConfigPosition(String value) {
    String dim = this.dimensionCleanse(value, 10, 10);
    this.conf.setProperty("ConfigPosition", dim);
  }

  /**
   * Returns the x,y coordinates for the Config frame in
   * the following format: (num,num)
   * <p>
   * @param  void
   * @return String default value: (10,10)
   */
  public String getConfigPosition() {
    return this.conf.getProperty("ConfigPosition", "(10,10)");
  }

  /** 
   * Returns the x,y coordinates for the Dialog frame in the
   * following format: (num,num)
   * <p>
   * @param  void
   * @return String  default value: (10,10)
   */
  public void setDialogPosition(String value) {
    String dim = this.dimensionCleanse(value, 10, 10);
    this.conf.setProperty("DialogPosition", dim);
  }

  /**
   * Returns the x,y coordinates for the Dialog frame in
   * the following format: (num,num)
   * <p>
   * @param  void
   * @return String default value: (X+690,Y+420)
   */
  public String getDialogPosition() {
    return this.conf.getProperty("DialogPosition", "(700,430)");
  }

  /**
   * Sets the deck size for the game, either single or double deck
   * <p>
   * @param  String  Either single or double
   * @return void
   */
  public void setDeckSize(String value) {
    String def = "single";
    if (value == null) {
      this.conf.setProperty("DeckSize", def);
      return;
    }

    if (value.equals("single") || value.equals("double")) {
      this.conf.setProperty("DeckSize", value);
      return;
    }
    
    //WTF? 
    this.conf.setProperty("DeckSize", def);
  }

  /**
   * Returns the size of the deck based on the string
   * value stored in the properties file. 
   * <p>
   * @param  void
   * @return String  "single" returns 1, "double" returns 2
   */
  public String getDeckSize() {
    String tmp = this.conf.getProperty("DeckSize", "single");

    if (tmp.equals("single")) {
      return "1";
    }

    if (tmp.equals("double")) {
      return "2";
    }

    return "1";
  }

  /**
   * Sets the minimum pinochle bid to value. The default
   * minimum is 16 for single deck and 50 for double deck
   * <p>
   * @param  String  the value of the minimum bid
   * @return void
   */
  public void setMinimumBid(String value) {
    int def = (conf.getProperty("DeckSize", "single")).equals("double") ? 50 : 16;

    if (value == null) {
      this.conf.setProperty("MinimumBid", ""+def);
      return;
    }

    if (! NumberUtils.isNumeric(value)) {
      this.conf.setProperty("MinimumBid", ""+def);
      return;
    } 
    this.conf.setProperty("MinimumBid", ""+def);
  }

  /**
   * Returns the minimum bid for a pinochle game
   * <p>
   * @param  void
   * @return String default value is "16"
   */
  public String getMinimumBid() {
    String ret = this.conf.getProperty("MinimumBid", "16");
    if (! NumberUtils.isNumeric(ret)) {
      return "16";
    }
    return ret;
  }

  /**
   * Set the winning score for a Pinochle game.
   * The default values are 300 (for single deck)
   * and 500 for double deck.
   * <p>
   * @param String  the winning score value
   * @return void
   */
  public void setWinningScore(String value) {
    int def = (conf.getProperty("DeckSize", "single")).equals("single") ? 300 : 500;
    if (NumberUtils.isNumeric(value)) {
      this.conf.setProperty("WinningScore", value);
    }
    this.conf.setProperty("WinningScore", ""+def);
  }

  /**
   * Returns the winning score for a pinochle game - 
   * single deck default is 300, double deck is 500
   * <p>
   * @param  void
   * @return String
   */
  public String getWinningScore() {
    int    def = (conf.getProperty("DeckSize", "single")).equals("single") ? 300 : 500;
    String ret = this.conf.getProperty("WinningScore", "300");
    if (NumberUtils.isNumeric(ret)) {
      return ""+ret;
    }
    return ""+def; 
  }

  /**
   * Sets the bid variation for a pinochle game. 
   * The options include single bid and auction.
   * The default value is auction
   * <p>
   * @param  String  Either "single" or "auction"
   * @return void
   */
  public void setBidVariation(String value) {
    String tmp = value.toLowerCase();
    if (tmp.equals("auction") || tmp.equals("single")) {
      this.conf.setProperty("BidVariation", tmp);
      return;
    }
    this.conf.setProperty("BidVariation", "auction");
  }

  /**
   * Gets the bid variation for a pinochle game.
   * The options include single bid and auction.
   * <p>
   * @param  void
   * @return String Default value is "auction"
   */
  public String getBidVariation() {
    String tmp = this.conf.getProperty("BidVariation", "auction").toLowerCase();
    if (tmp.equals("auction") || tmp.equals("single")) {
      return tmp;
    }
    return "auction";
  }

  /**
   * Sets the name of the player in the North seat
   * <p>
   * @param  String   The name of the player
   * @return void     
   */
  public void setPlayerNorthName(String value) {
    if (value != null && value.length() > 0) {
      this.conf.setProperty("PlayerNorthName", value); 
      firePropertyChange(Constants.NAMES, "names", value);
      return;
    }
    this.conf.setProperty("PlayerNorthName", "North"); 
  }

  /**
   * Returns the name of the player at the North seat
   * <p>
   * @param  void
   * @return String  The player's name; default North
   */ 
  public String getPlayerNorthName() {
    return this.conf.getProperty("PlayerNorthName", "North");
  }
 
  /**
   * Sets the name of the player in the South seat
   * <p>
   * @param  String   The name of the player
   * @return void     
   */
  public void setPlayerSouthName(String value) {
    if (value != null && value.length() > 0) {
      this.conf.setProperty("PlayerSouthName", value); 
      firePropertyChange(Constants.NAMES, "names", value);
      return;
    }
    this.conf.setProperty("PlayerSouthName", "South");
  }

  /**
   * Returns the name of the player at the South seat
   * <p>
   * @param  void
   * @return String  The player's name; default South
   */
  public String getPlayerSouthName() {
    return this.conf.getProperty("PlayerSouthName", "South");
  }

  /**
   * Sets the name of the player in the East seat
   * <p>
   * @param  String   The name of the player
   * @return void     
   */
  public void setPlayerEastName(String value) {
    if (value != null && value.length() > 0) {
      this.conf.setProperty("PlayerEastName", value); 
      firePropertyChange(Constants.NAMES, "names", value);
      return;
    }
    this.conf.setProperty("PlayerEastName", "East");
  }

  /**
   * Returns the name of the player at the East seat
   * <p>
   * @param  void
   * @return String  The player's name; default East
   */
  public String getPlayerEastName() {
    return this.conf.getProperty("PlayerEastName", "East");
  }

  /**
   * Sets the name of the player in the West seat
   * <p>
   * @param  String   The name of the player
   * @return void     
   */
  public void setPlayerWestName(String value) {
    if (value != null && value.length() > 0) {
      this.conf.setProperty("PlayerWestName", value); 
      firePropertyChange(Constants.NAMES, "names", value);
      return;
    }
    this.conf.setProperty("PlayerWestName", "West");
  }

  /**
   * Returns the name of the player at the West seat
   * <p>
   * @param  void
   * @return String  The player's name; default West
   */
  public String getPlayerWestName() {
    return this.conf.getProperty("PlayerWestName", "West");
  }

  /**
   * Sets the temperament of the player in the North seat
   * <p>
   * @param  String   The temperament of the player
   * @return void     
   */
  public void setPlayerNorthTemperament(String value) {
    if (value != null && value.length() > 0) {
      this.conf.setProperty("PlayerNorthTemperament", value);
      return;
    }
    this.conf.setProperty("PlayerNorthTemperament", "melancholic");
  }

  /**
   * Returns the temperament of the player at the North seat
   * <p>
   * @param  void
   * @return String  The player's name; default melancholic
   */
  public String getPlayerNorthTemperament() {
    return this.conf.getProperty("PlayerNorthTemperament", "melancholic");
  }

  /**
   * Sets the temperament of the player in the North seat
   * <p>
   * @param  String   The temperament of the player
   * @return void     
   */
  public void setPlayerSouthTemperament(String value) {
    if (value != null && value.length() > 0) {
      this.conf.setProperty("PlayerSouthTemperament", value);
      return;
    }
    this.conf.setProperty("PlayerSouthTemperament", "phlegmatic");
  }

  /**
   * Returns the name of the player at the South seat
   * <p>
   * @param  void
   * @return String  The player's name; default phlegmatic
   */
  public String getPlayerSouthTemperament() {
    return this.conf.getProperty("PlayerSouthTemperament", "phlegmatic");
  }

  /**
   * Sets the temperament of the player in the East seat
   * <p>
   * @param  String   The temperament of the player
   * @return void     
   */
  public void setPlayerEastTemperament(String value) {
    if (value != null && value.length() > 0) {
      this.conf.setProperty("PlayerEastTemperament", value);
      return;
    }
    this.conf.setProperty("PlayerEastTemperament", "sanguine");
  }

  /**
   * Returns the name of the player at the East seat
   * <p>
   * @param  void
   * @return String  The player's name; default phlegmatic
   */
  public String getPlayerEastTemperament() {
    return this.conf.getProperty("PlayerEastTemperament", "sanguine");
  }

  /**
   * Sets the temperament of the player in the West seat
   * <p>
   * @param  String   The temperament of the player
   * @return void     
   */
  public void setPlayerWestTemperament(String value) {
    if (value != null && value.length() > 0) {
      this.conf.setProperty("PlayerWestTemperament", value);
      return;
    }
    this.conf.setProperty("PlayerWestTemperament", "choleric");
  }

  /**
   * Returns the name of the player at the West seat
   * <p>
   * @param  void
   * @return String  The player's name; default choleric
   */
  public String getPlayerWestTemperament() {
    return this.conf.getProperty("PlayerWestTemperament", "choleric");
  }

  /**
   * Sets the player type for the North seat. 
   * Choices are human or computer. Default: computer
   * <p>
   * @param  String  "human" or "computer"
   * @return void  
   */
  public void setPlayerNorthType(String value) {
    if (value == null || value.length() < 1) {
      this.conf.setProperty("PlayerNorthType", "computer");
      return;
    }

    String tmp = value.toLowerCase();
     
    if (tmp.equals("computer") || tmp.equals("human")) {
      this.conf.setProperty("PlayerNorthType", tmp);
      return;
    }
    // WTF?? 
    this.conf.setProperty("PlayerNorthType", "computer");
  }

  /**
   * Returns the player type for the North seat. While we
   * store these properties in human-readable "computer" 
   * or "human" we return a numeric representation for 
   * each value. 1=computer and 0=human
   * <p>
   * @param  void
   * @return String  1=computer, 0=human (default 1)
   */
  public String getPlayerNorthType() {
    String tmp = this.conf.getProperty("PlayerNorthType", "computer").toLowerCase();
    if (tmp.equals("human")) {
      return "0";
    }
    return "1";
  }

  /**
   * Sets the player type for the South seat. 
   * Choices are human or computer. Default human
   * <p>
   * @param  String  "human" or "computer"
   * @return void  
   */
  public void setPlayerSouthType(String value) {
    if (value == null || value.length() < 1) {
      this.conf.setProperty("PlayerSouthType", "human");
      return;
    }

    String tmp = value.toLowerCase();
    if (tmp.equals("computer") || tmp.equals("human")) {
      this.conf.setProperty("PlayerSouthType", tmp);
      return;
    }
    // WTF?? 
    this.conf.setProperty("PlayerSouthType", "human");  
  }

  /**
   * Returns the player type for the South seat. While we
   * store these properties in human-readable "computer" 
   * or "human" we return a numeric representation for 
   * each value. 1=computer and 0=human
   * <p>
   * @param  void
   * @return String  1=computer, 0=human (default 0)
   */
  public String getPlayerSouthType() {
    String tmp = this.conf.getProperty("PlayerSouthType").toLowerCase();
    if (tmp.equals("computer")) {
      return "1";
    }
    return "0";
  }

  /**
   * Sets the player type for the East seat. 
   * Choices are human or computer. Default: computer
   * <p>
   * @param  String  "human" or "computer"
   * @return void  
   */
  public void setPlayerEastType(String value) {
    if (value == null || value.length() < 1) {
      this.conf.setProperty("PlayerEastType", "computer");
      return;
    }

    String tmp = value.toLowerCase();
  
    if (tmp.equals("computer") || tmp.equals("human")) {
      this.conf.setProperty("PlayerEastType", tmp);
      return;
    }
    // WTF?? 
    this.conf.setProperty("PlayerEastType", "computer");
  }

  /**
   * Returns the player type for the East seat. While we
   * store these properties in human-readable "computer" 
   * or "human" we return a numeric representation for 
   * each value. 0=computer and 1=human
   * <p>
   * @param  void
   * @return String  1=computer, 0=human (default 1)
   */
  public String getPlayerEastType() {
    String tmp = this.conf.getProperty("PlayerEastType", "computer").toLowerCase();
    if (tmp.equals("human")) {
      return "0";
    }
    return "1";
  }

  /**
   * Sets the player type for the West seat. 
   * Choices are human or computer. Default: computer
   * <p>
   * @param  String  "human" or "computer"
   * @return void  
   */
  public void setPlayerWestType(String value) {
    if (value == null || value.length() < 1) {
      this.conf.setProperty("PlayerWestType", "computer");
      return;
    }

    String tmp = value.toLowerCase();

    if (tmp.equals("computer") || tmp.equals("human")) {
      this.conf.setProperty("PlayerWestType", tmp);
      return;
    }
    // WTF?? 
    this.conf.setProperty("PlayerWestType", "computer");
  }

  /**
   * Returns the player type for the West seat. While we
   * store these properties in human-readable "computer" 
   * or "human" we return a numeric representation for 
   * each value. 1=computer and 0=human
   * <p>
   * @param  void
   * @return String  1=computer, 0=human (default 1)
   */
  public String getPlayerWestType() {
    String tmp = this.conf.getProperty("PlayerWestType", "computer").toLowerCase();
    if (tmp.equals("human")) {
      return "0";
    }
    return "1";
  }

  /**
   * This will probably never be called. The programmer sets 
   * the cheat in his .pinochle/game.conf file
   */
  public void setCheat(String value) {
    this.conf.setProperty("Cheat", value);
  }

  /**
   * Returns the cheat from the .pinochle/game.conf file. The
   * string is then hashed and must match a hardcoded cheat.
   * Hint: I used a popular computer game cheat code
   */
  public String getCheat() {
    String key = MD5((String)conf.getProperty("Cheat"));

    if (key==null || key.length() < 2) {
      return "false";
    } 
    if (key.equals("91924358dbfa1ad602a7eecad53c99f1")) {
      return "true";
    }
    return "false";
  }

  /**
   * In some versions of pinochle, if you can top the
   * highest card in the trick, then you must. In other
   * versions, you're allowed to play-off.
   * This sets a game's play variation.  
   * <p>
   * @param  String   true=must top; false=can play-off
   * @return void
   */
  public void setTopVariation(String value) {
    String tmp = value.toLowerCase();
    if (tmp == null || ! tmp.equals("true") || ! tmp.equals("false")) {
      this.conf.setProperty("TopVariation", "true");  
      return;
    }
    this.conf.setProperty("TopVariation", tmp);
  }

  /**
   * Returns the play variation of a game. true indicates
   * you MUST top the highest card in a trick if you're 
   * capable. false indicates you can play-off
   * <p>
   * @param  void
   * @return String
   */
  public String getTopVariation() {
    String tmp = this.conf.getProperty("TopVariation", "true").toLowerCase();
    if (! tmp.equals("true") || ! tmp.equals("false")) {
      return tmp;
    }
    return "true";
  }

  /**
   * Instruct the application as to whether or not it
   * should run headless, i.e., no GUI. true for headless
   * and false for not headless
   * <p>
   * @param  String
   * @return void
   */
  public void setHeadless(String value) {
    String tmp = value.toLowerCase();
    if (tmp == null || ! tmp.equals("true") || ! tmp.equals("false")) {
      this.conf.setProperty("Headless", "false");  
    }
    this.conf.setProperty("Headless", tmp);
  }

  /**
   * Return the headless state for the application -
   * true for headless and false for not headless
   * <p>
   * @param  void
   * @return String  default value is "false"
   */
  public String getHeadless() {
    String tmp = this.conf.getProperty("Headless", "false").toLowerCase();
    if (tmp == null) return "false";

    if (tmp.equals("true") || tmp.equals("false")) {
      return tmp;
    }
    return "false";
  }

  /**
   * Puts the game into simulation mode 
   * <p>
   * @param  String  "true" or "false"
   * @return void
   */
  public void setSimulation(String value) {
    String tmp = value.toLowerCase();
    if (tmp == null || ! tmp.equals("true") || ! tmp.equals("false")) {
      this.conf.setProperty("Simulation", "false");  
    }
    this.conf.setProperty("Simulation", tmp);
  }

  public String getSimulation() {
    String tmp = this.conf.getProperty("Simulation", "false").toLowerCase();
    if (tmp == null) return "false";

    if (tmp.equals("true") || tmp.equals("false")) {
      return tmp;
    }
    return "false";
  }

  /**
   * Instructs the application as to whether or not it should
   * go into debug mode: true or false
   * <p>
   * @param  String  either "true" or "false"
   * @return void
   */
  public void setDebug(String value) {
    String debug = value.toLowerCase();

    if (! debug.equals("true") || ! debug.equals("false")) {
      this.conf.setProperty("Debug", "false");  
    }
    this.conf.setProperty("Debug", debug);
  }

  /**
   * Returns the debugging state for the application,
   * true turns debugging on, false does not.
   * <p>
   * @param  void
   * @return String default value is false
   */
  public String getDebug() {
    String debug = this.conf.getProperty("Debug", "false").toLowerCase();
    if (debug == null) return "false";

    if (! debug.equals("true") || ! debug.equals("false")) {
      return debug;
    }
    return "false";
  }

  /**
   * A helper method used to hash our cheat.
   */
  private String MD5(String md5) {
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

  /**
   * Examine the dimension input and ensure it conforms with the
   * appropriate format, i.e., (num,num). If anything is wrong with
   * the input, this will retrun (defX,defY)
   */
  private String dimensionCleanse(String value, int defX, int defY) {
    String def = "("+defX+","+defY+")"; 
    String ret = value; // store it and use this if value checks out

    if (value == null) {
      return def;
    }

    if ((value = value.replaceAll("\\s", "")).length() < 6) {
      return def;
    }
    String[] tokens = (value = value.substring(1, value.length() - 1)).split(",");
    if (tokens.length != 2) {
      return def;
    }

    int[] tmp = new int[tokens.length];

    try {
      for (int i = 0; i < tokens.length; ++i) {
        tmp[i] = Integer.parseInt(tokens[i]);
      }
    } catch (NumberFormatException ex) {
      return def;
    }
    if (tmp[0] > 0 && tmp[1] > 0) {
      return ret;
    }
    return def;
  }  
}
