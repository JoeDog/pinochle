package org.joedog.pinochle.controller;

import org.joedog.pinochle.view.*;
import org.joedog.pinochle.util.*;
import org.joedog.pinochle.game.*;
import org.joedog.pinochle.player.*;

public class GameController extends AbstractController {
  public  boolean alive             = true;
  private boolean running           = false;
  public final static String TRUMP  = "0";
  public final static int DEAL      = 0;
  public final static int BID       = 1;
  public final static int PASS      = 2;
  public final static int MELD      = 3;
  public final static int PLAY      = 4;
  public final static int SCORE     = 5;
  public final static int OVER      = 6;

  private boolean over = false;

  public GameController () {
    setModelProperty("GameStatus", DEAL);
  }

  public synchronized void start () {
    this.running  = true;
  }

  public boolean isRunning() {
    return this.running;
  }

  public void setProperty(String key, String value) {
    setModelProperty(key, value);
  }

  public String getProperty(String property) {
    return (String)getModelProperty(property); 
  }

  public int getIntProperty(String property) {
    String tmp = (String)getModelProperty(property); 
    try {
      if (tmp != null && tmp.length() > 0) {
        return Integer.parseInt(tmp);
      }
    } catch (final NumberFormatException nfe) {
      return 0;
    }
    return 0;
  }

  public int getTrump() {
    return getIntProperty("GameTrump");
  }
  
  public int save() {
    runViewMethod("save");
    runModelMethod("save");
    return 1;
  }

  // ExitAction
  public void exit() {
    setStatus("Shutting down...");
    this.save();
    this.alive = false;
    try {
      Thread.sleep(500);
    } catch (Exception e) {
      e.printStackTrace();
    }
    System.exit(0);
  }

  public synchronized int gameStatus () {
    int status  = ((Integer)getModelProperty("GameStatus")).intValue();
    switch (status) {
      case DEAL:
        this.over = false;
        break;
      case BID:
        this.over = false;
        break;
      case PASS:
        this.over = false;
        break;
      case MELD:
        this.over = false;
        break;
      case PLAY:
        this.over = false;
        break;
      case SCORE:
        this.over = false;
        break;
      case OVER:
        this.over = false;
        break;
    }
    return status;
  }

  public void newDeal(Player[] players) {
    int turn = 0;
    Deck deck = new Deck(1);
    deck.shuffle();
    while (turn < deck.count()) {
      players[turn%players.length].takeCard(deck.dealCard(turn));
      turn++;
    }
    /**
     * XXX: when this is commented out, you can't
     * see the cards in order to bid, but selection
     * for pass works....
     */
    //for (int i = 0; i < players.length; i++) {
    //  players[i].refresh();
    //}
    setModelProperty("GameTrump",  "-1");
    setModelProperty("GameStatus", ""+BID);
    return;
  }

  public void getBids(Player[] players) {
    int bid       = 15; // XXX: Need to get this from the model - (min bid - 1)
    int turn      = 0;  // XXX: Need to pass in an incrementing value
    int sum       = 0;  // sum of the number of passes
    int mark      = 0;
    int [] passes = new int[players.length];
              
    for (int i = 0; i < players.length; i++) {
      players[i].refresh();
      players[i].assessHand();
    }
    
    while (sum < (players.length - 1)) {
      int ret = players[turn%players.length].bid(bid);
      if (ret < bid) {
        passes[turn%players.length] = 1;  
      } else {
        bid  = ret;
      }
      turn++;
      sum = 0;
      for (int i : passes) sum += i; // get the total passes
    }

    for (int i = 0; i < passes.length; i++) {
      if (passes[i] == 0) mark = i;
    }
    int trump = players[mark].nameTrump();
    String t  = ""+trump;
    setModelProperty("GameBid",    ""+bid);
    setModelProperty("GameBidder", ""+mark);
    setModelProperty("GameTrump",  ""+trump);
    setModelProperty("GameStatus", ""+PASS);
    return;
  }

  public void passCards(Player[] players) {
    //XXX: some variations won't pass
    int bidder = this.getIntProperty("GameBidder");
    Deck a     = null;
    Deck b     = null;
    switch (bidder) {
      case Pinochle.NORTH:
        a = players[Pinochle.SOUTH].passCards(false);
        players[Pinochle.NORTH].takeCards(a);
        b = players[Pinochle.NORTH].passCards(true);
        players[Pinochle.SOUTH].takeCards(b);
        break;
      case Pinochle.SOUTH:
        a = players[Pinochle.NORTH].passCards(false);
        players[Pinochle.SOUTH].takeCards(a);
        b = players[Pinochle.SOUTH].passCards(true);
        players[Pinochle.NORTH].takeCards(b);
        break;
      case Pinochle.EAST:
        a = players[Pinochle.WEST].passCards(false);
        players[Pinochle.EAST].takeCards(a);
        b = players[Pinochle.EAST].passCards(true);
        players[Pinochle.WEST].takeCards(b);
        break;
      case Pinochle.WEST:
        a = players[Pinochle.EAST].passCards(false);
        players[Pinochle.WEST].takeCards(a);
        b = players[Pinochle.WEST].passCards(true);
        players[Pinochle.EAST].takeCards(b);
        break;
    }
    setModelProperty("GameStatus", ""+MELD);
  }

  public void getMeld(Player[] players) {
    int score = 0;
    for (int i = 0; i < players.length; i++) {
      score = players[i].meld();
      System.out.println(players[i].getName()+" melded: "+score);
      players[i].refresh();
    } 
    return;
  }

  public void setStatus (String status) {
    setViewProperty("Status", status);
  }

  public String getGameString () {
    return (String)getModelProperty("GameString");
  }

}
