package org.joedog.pinochle.control;

import java.lang.Thread;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Random;
import org.joedog.pinochle.view.*;
import org.joedog.pinochle.game.*;
import org.joedog.pinochle.player.*;

public class GameController extends AbstractController {
  private Thread  thread;
  private AtomicBoolean hiatus           = new AtomicBoolean(false);
  private boolean running                = false;
  private boolean passable               = false;
  private boolean meldable               = false;
  private boolean playable               = false;
  public final static int DEAL           = 0;
  public final static int BID            = 1;
  public final static int PASS           = 2;
  public final static int MELD           = 3;
  public final static int PLAY           = 4;
  public final static int SCORE          = 5;
  public final static int OVER           = 6;
  public final static String RESET       = "NEWHAND";
  public final static String TRUMP       = "TRUMP";
  public final static String HIGH_BID    = "HIBID";
  public final static String OURS        = "OURS";
  public final static String THEIRS      = "THEIRS";
  public final static String MELD_SCORE  = "NSMELD";
  public final static String TAKE_SCORE  = "NSTAKE";
  public final static String HAND_SCORE  = "NSHAND";
  public final static String GAME_SCORE  = "NSGAME";
  public final static String WINNER      = "NONE";

  private boolean over = false;

  public GameController () {
    setModelProperty("GameStatus", DEAL);
  }

  public void resetGame() {
    this.over     = false;
    this.meldable = false;
    this.passable = false;
    setStatus("New game!");
    runModelMethod("resetGame");
    runViewMethod("resetScore");
    setModelProperty("GameStatus", ""+DEAL);
    this.thread.stop();
  }

  public void newHand() {
    this.over     = false;
    this.meldable = false;
    this.passable = false;
    setStatus("New hand!");
    setModelProperty("GameStatus", ""+DEAL);
  }

  public void winner() {
    this.over = true;
    System.out.println("WINNER IS FIRED!!!!!!"); 
    setModelProperty("GameStatus", ""+OVER);
  }

  public synchronized void start () {
    this.running  = true;
    this.over = false;
  }

  public boolean isRunning() {
    return this.running;
  }

  public void setProperty(String key, String value) {
    setModelProperty(key, value);
  }

  public String getProperty(String property) {
    String str = (String)getModelProperty(property); 
    if (str == null) {
      return "";
    } else {
      return str;
    }
  }

  public int getIntProperty(String property) {
    String tmp = (String)getModelProperty(property); 
    try {
      if (tmp != null && tmp.length() > 0) {
        return Integer.parseInt(tmp);
      }
    } catch (final NumberFormatException nfe) {
      return -1;
    }
    return -1;
  }

  public boolean getBooleanProperty(String property) {
    String tmp = (String)getModelProperty(property); 
    if (tmp.equals("true")) {
      return true;
    } 
    return false;
  }

  public int getTrump() {
    return getIntProperty("GameTrump");
  }

  public void setPassable(boolean passable) {
    this.passable = passable;
  }

  public boolean isPassable() {
    return passable;
  }

  public void addPassButton() {
    runViewMethod("addPassButton");
  }
  
  public void enablePassButton() {
    runViewMethod("enablePassButton");
  }
  
  public void disablePassButton() {
    runViewMethod("disablePassButton");
  }

  public void addMeldButton() {
    runViewMethod("addMeldButton");
  }
  
  public void setMeldable(boolean meldable) {
    this.meldable = meldable;
  }

  public boolean isMeldable() {
    return meldable;
  }

  public void setPlayable(boolean playable) {
    this.playable = playable;
  }

  public boolean isPlayable() {
    return playable;
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
    try {
      TimeUnit.SECONDS.sleep(1);
    } catch (Exception e) {}
    System.exit(0);
  }

  public synchronized int gameStatus () {
    int status  = ((Integer)getModelProperty("GameStatus")).intValue();
    switch (status) {
      case DEAL:
      case BID:
      case PASS:
      case MELD:
      case PLAY:
      case SCORE:
        this.over = false;
        break;
      case OVER:
        /**
         * we should loop in this case  
         * until the user selects exit
         * or a new game....
         */
        this.over = true;
        try {
          TimeUnit.SECONDS.sleep(1);
        } catch (Exception e) {}
        break;
    }
    return status;
  }

  public void newDeal(Player[] players) {
    if (this.over) return;

    Deck deck = new Deck(1);
    int turn  = this.getIntProperty("Dealer");
    int next  = (turn < players.length-1)? turn + 1 : 0;

    for (int i = 0; i < players.length; i++) 
      players[i].newHand();

    deck.shuffle();
    for (int i = 0; i < deck.count(); i++) {
      players[turn%players.length].takeCard(deck.dealCard(i));
      turn++;
    }

    /**
     * XXX: when this is commented out, you can't
     * see the cards in order to bid, but selection
     * for pass works....
     */
    setModelProperty("GameTrump",    "-1");
    setModelProperty("GameStatus",   ""+BID);
    setModelProperty("ActivePlayer", ""+next);
    setModelProperty("Dealer",       ""+next);
    return;
  }

  public void getBids(Player[] players) {
    int bid       = (getIntProperty("MinimumBid") - 1);
    int turn      = 0;  
    int sum       = 0;  // sum of the number of passes
    int mark      = 0;
    int [] passes = new int[players.length];
              
    for (Player player : players) { 
      player.refresh();
      player.assessHand();
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
    System.out.println("TRUMP: "+trump);
    setModelProperty("GameBid",      ""+bid);
    setModelProperty("Bidder",       ""+mark);
    setModelProperty("GameTrump",    ""+trump);
    setModelProperty("GameStatus",   ""+PASS);
    return;
  }

  public void passCards(Player[] players) {
    //XXX: some variations won't pass
    int bidder = this.getIntProperty("ActivePlayer");
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
    int ns    = 0;
    int ew    = 0;
    for (int i = 0; i < players.length; i++) {
      if (players[i].getType() == Player.HUMAN) {
        score = players[i].meld();
        if (i == Pinochle.NORTH || i == Pinochle.SOUTH) {
          ns += score;
        } else {
          ew += score;
        }
        players[i].refresh();
      }
    } 
    for (int i = 0; i < players.length; i++) {
      if (players[i].getType() == Player.COMPUTER) {
        score = players[i].meld();
        if (i == Pinochle.NORTH || i == Pinochle.SOUTH) {
          ns += score;
        } else {
          ew += score;
        }
        players[i].refresh();
      }
    } 
    runModelMethod("resetHand");
    setModelProperty("NSMeld", ""+ns);
    setModelProperty("EWMeld", ""+ew);
    setModelProperty("GameStatus", ""+PLAY);
    this.pause(true);
    runViewMethod("addPlayButton");
    return;
  }

  public void playHand(Player[] players) {
    int count  = getIntProperty("CardCount");
    int turn   = getIntProperty("ActivePlayer");
    int tricks = count/players.length;
    while (this.isPaused()) {
      try {
        if (
          players[0].getType()==Player.COMPUTER && 
          players[1].getType()==Player.COMPUTER &&
          players[2].getType()==Player.COMPUTER &&
          players[3].getType()==Player.COMPUTER ) {
          this.pause(false);
        } else {
          Thread.sleep(500);
        }
      } catch (Exception e) {}
    }
    for (Player player : players) {
      player.clearMeld();
      player.refresh();
    }
    for (int i = 0; i < tricks; i++) {
      Trick trick   = new Trick(this.getTrump());
      Card [] cards = new Card[4];
      for (int j = 0; j < players.length; j++) {
        setStatus(players[turn%players.length].getName()+" it's your turn");
        Card card = players[turn%players.length].playCard(trick);
        cards[j] = card;
        trick.add(players[turn%players.length], card);
        //add to GUI
        switch (players[turn%players.length].getPosition()) {
          case Pinochle.NORTH:
            setViewProperty("NorthPlay", card);
            break;
          case Pinochle.SOUTH:
            setViewProperty("SouthPlay", card);
            break;
          case Pinochle.EAST:
            setViewProperty("EastPlay", card);
            break;
          case Pinochle.WEST:
            setViewProperty("WestPlay", card);
            break;
        }
        this.setPlayable(false);
        try {
          int lo = 300;
          int hi = 700;
          // We're going to sleep within a 
          // random range between turns so
          // play is less choppy, more humany
          if (this.getBooleanProperty("Simulation")) {
            lo = 20;
            hi = 80;
          }
          Thread.sleep(randInt(lo, hi));
        } catch (Exception e) {}
        turn++;
      }  
      for (Player player : players) {
        player.remember(trick.getCards());
      }
      turn = trick.winner();
      if (turn % 2 == 0) {
        setModelProperty("NSTake", ""+trick.counters());
      } else {
        setModelProperty("EWTake", ""+trick.counters());
      }
      runViewMethod("clear");
      setViewProperty("DisplayTrick", cards);
    }
    // Award the last trick
    if (turn % 2 == 0) {
      setModelProperty("NSTake", "1");
    } else {
      setModelProperty("EWTake", "1");
    }
    runModelMethod("addScore");
    if (!this.over) {
      newHand();
    }
  }

  public String getName (int player) {
    switch (player) {
      case Pinochle.NORTH:
        return (String)getModelProperty("PlayerNorthName");
      case Pinochle.SOUTH:
        return (String)getModelProperty("PlayerSouthName");
      case Pinochle.EAST:  
        return (String)getModelProperty("PlayerEastName");
      case Pinochle.WEST:  
        return (String)getModelProperty("PlayerWestName");
      default:
        return "";
    }
  }

  public void setStatus (String status) {
    setViewProperty("Status", status);
  }

  public String getGameString () {
    return (String)getModelProperty("GameString");
  }

  public void addThread(Thread thread) {
    this.thread = thread;
  }

  public void pause(boolean b) {
    hiatus.set(b);
  }

  public boolean isPaused() {
    return hiatus.get();
  }

  public boolean cheatMode() {
    String mode = (String)getModelProperty("CheatMode");
    return Boolean.parseBoolean(mode);
  }

  public static int randInt(int min, int max) {
    Random rand = new Random();

    int randomNum = rand.nextInt((max - min) + 1) + min;
    return randomNum;
  }
}
