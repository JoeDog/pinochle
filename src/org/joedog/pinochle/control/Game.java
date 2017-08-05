package org.joedog.pinochle.control;
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

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.joedog.util.*;
import org.joedog.pinochle.model.Arena;
import org.joedog.pinochle.model.Config;
import org.joedog.pinochle.model.Match;
import org.joedog.pinochle.model.HighScoreModel;
import org.joedog.pinochle.game.*;
import org.joedog.pinochle.player.*;
import org.joedog.pinochle.test.*;

public class Game extends AbstractController {
  private Engine engine;
  private Arena  arena;
  private Config config;
  private Match  match;
  private HighScoreModel highs;
  private Thread thread;
  private int    round;
  private Player[]       players;
  private PlayerFactory  factory;
  private Scenarios      scenarios;
  private boolean        meldable = false;
  private boolean        passable = false;
  private AtomicBoolean  hiatus   = new AtomicBoolean(false);

  public Game() {
    this.config    = Config.getInstance();
    this.arena     = Arena.getInstance(); 
    this.match     = Match.getInstance();
    this.highs     = new HighScoreModel();
    this.round     = 1; 
    this.scenarios = new Scenarios();
    this.addModel(config);
    this.addModel(arena);
    this.addModel(match);
    this.addModel(highs);
    this.setModelProperty("Round", ""+this.round);
  }

  public void setEngine(Engine engine) {
    this.engine    = engine;
  }

  public void addThread(Thread thread) {
    this.thread = thread;
  }

  public void setMessage(String message) {
    this.setModelProperty("Message", message);
  }

  public void exit() {
    this.setMessage("Shutting down...");
    this.runModelMethod("save");
    Sleep.milliseconds(500);
    System.exit(0);
  }

  public void pause(boolean pause) {
    hiatus.set(pause);
  }

  public boolean isPaused() {
    return hiatus.get();
  }

  public void setPassable(boolean passable) {
    this.passable = passable;
  }

  public boolean isPassable() {
    return passable;
  }

  public void setMeldable(boolean meldable) {
    this.meldable = meldable;
  }

  public boolean isMeldable() {
    return this.meldable;
  }

  public void select(int x, int y) {
    this.arena.select(x, y);
  }

  public boolean cheatMode() {
    return this.getModelBooleanProperty("Cheat");
  }

  public void addPassButton() {
    this.runViewMethod("addPassButton");
  }

  public void enablePassButton(boolean enable) {
    if (enable) {
      this.runViewMethod("enablePassButton");
    } else {
      this.runViewMethod("disablePassButton");
    }
  }

  public void addMeldButton() {
    this.runViewMethod("addMeldButton");
  }

  public void addPlayButton() {
    this.runViewMethod("addPlayButton");
  }

  /**
   * Invoked by the GameThread this method starts and 
   * conducts the flow of a game.
   * <p>
   * @param  none
   * @return void
   */
  public synchronized void start() {
    int turn     = 0;
    int status   = Pinochle.DEAL;
    this.players = this.getPlayers();
    engine.start();

    while (engine.paused()) {
      Sleep.milliseconds(500);
    }

    while (status != Pinochle.OVER) {
      status = this.getModelIntProperty("Status");
      switch (status) {
        case Pinochle.DEAL:
          this.deal(players);
          break;
        case Pinochle.BID:
          this.bid(players);
          break;
        case Pinochle.PASS:
          this.pass(players);
          break;
        case Pinochle.MELD:
          this.meld(players);
          this.runModelMethod("showScores");
          break;
        case Pinochle.AVOW:
          this.avow(players);
          break;
        case Pinochle.PLAY:
          this.play(players);
          break;
        case Pinochle.DONE:
          this.done(players);
          break;
        case Pinochle.OVER:
          if (this.getModelBooleanProperty("Headless") == true) {
            System.exit(0);
          }
          this.setMessage("Game. Set. Match.");
          this.pause(true);
          while(this.isPaused()); 
          break;
      } 
      Sleep.milliseconds(20);
    } 
  }

  /**
   * Creates and returns the players necessary to play
   * the game; player types are picked up from the game 
   * configuration.
   * <p>
   * @param  none
   * @return Player[]
   */
  public Player [] getPlayers () {
    this.factory      = new PlayerFactoryImpl();
    Player [] players = {
      factory.getPlayer(this, this.getModelIntProperty("PlayerNorthType")),
      factory.getPlayer(this, this.getModelIntProperty("PlayerEastType")),  
      factory.getPlayer(this, this.getModelIntProperty("PlayerSouthType")),
      factory.getPlayer(this, this.getModelIntProperty("PlayerWestType")) 
    };

    for (int i = 0; i < players.length; i++) {
      int    partner = -1;
      String name    = "default";
      Hand   hand    = null;
      switch (i) {
        case Pinochle.NORTH:
          partner = Pinochle.SOUTH;
          name    = this.getModelStringProperty("PlayerNorthName");
          hand    = arena.getHand(Pinochle.NORTH);
          break;
        case Pinochle.EAST:
          partner = Pinochle.WEST;
          name    = this.getModelStringProperty("PlayerEastName");
          hand    = arena.getHand(Pinochle.EAST);
          break;
        case Pinochle.SOUTH:
          partner = Pinochle.NORTH;
          name    = this.getModelStringProperty("PlayerSouthName");
          hand    = arena.getHand(Pinochle.SOUTH);
          break;
        case Pinochle.WEST:
          partner = Pinochle.EAST;
          name    = this.getModelStringProperty("PlayerWestName");
          hand    = arena.getHand(Pinochle.WEST);
          break;
      }
      this.addModel(players[i]);
      players[i].setup(i, partner, name, hand);
    }
    return players;
  }

  /**
   * Resets all game level variables and returns the status to Pinochle.DEAL
   * <p>
   * @param  none
   * @return void
   */
  public void newHand() {
    this.round += 1;
    this.meldable = false;
    this.passable = false;
    this.setMessage("New hand!");
    this.setModelProperty("Round", ""+this.round);
    this.setModelProperty("Trump", "-1");
    this.setModelProperty("Status", ""+Pinochle.DEAL);
    this.runModelMethod("resetGameScore");
  }

  /**
   * Resets everything and returns the status to Pinochle.DEAL
   * <p>
   * @param  none
   * @return void
   */
  public void newGame() {
    this.setMessage("New game!");
    this.round = 1;
    this.setMeldable(false);
    this.setPassable(false);
    this.runViewMethod("reset");
    this.runModelMethod("resetMatchScore");
    this.setModelProperty("Round", ""+this.round);
    this.setModelProperty("Trump", "-1");
    this.setModelProperty("Status", ""+Pinochle.DEAL);
    this.setMessage("Dealing a new hand....");
    if (this.isPaused()) {
      this.pause(false);
    } else {
      this.thread.stop();
    }
  }

  /**
   * Deals the cards to all game participants
   * <p>
   * @param  Player[]
   * @return void
   */
  private void deal(Player[] players) {
    Deck deck = new Deck(1);
    int turn  = this.getModelIntProperty("Dealer");
    int next  = (turn < players.length-1) ? turn + 1 : 0;

    for (Player player : players) {
      player.init();     //XXX: what about the hand?
      player.setText("");
    }

    /**
     * The first part of this if-check is a programmer's
     * helper; it reads cards from $HOME/.pinochle/cards.txt
     * and loads them into the game. The file's format looks
     * like this:
     * north: 10H KH 9H 9H 10C QC JC KD KD 9D AS KS
     * east: 10H KH QH AC AC KC JC 9C QD JD AS QS
     * south: AH QH JH 9C AD JD 9D 10S KS JS 9S 9S
     * west: AH JH 10C KC QC AD 10D 10D QD 10S QS JS
     */
    if (scenarios.loaded()) {
      int id = 0;
      Map<java.lang.Integer,ScenarioList<java.lang.String>> m = scenarios.next();
      for (Player player : players) {
        ScenarioList<String> tmp = m.get(player.getPosition());
        for (String str : tmp) {
          Card c = new Card(id, str);
          player.takeCard(c);
          id++;
        }
      }
    } else {
      // this section handles normal play
      deck.shuffle();
      for (int i = 0; i < deck.count(); i++) {
        players[turn%players.length].takeCard(deck.dealCard(i));
        turn++;
      }
    }

    for (Player player : players) {
      // Let's print in cards.txt friendly format in case we want to capture it
      Debug.print(Pinochle.position(player.getPosition())+": "+player.handToString());
    } Debug.print("");

    this.runModelMethod("relocate");

    this.setModelProperty("Trump",  "-1");
    this.setModelProperty("Status", ""+Pinochle.BID);
    this.setModelProperty("Dealer", ""+next);
    this.setModelProperty("Active", ""+next);
    return;
  }

  public void bid(Player[] players) {
    int bid       = this.getModelIntProperty("MinimumBid")-1;
    int turn      = this.getModelIntProperty("Active");
    int sum       = 0;  // sum of the number of passes
    int mark      = 0;  // a placeholder for the winning bidder
    int limit     = this.getModelProperty("BidVariation").equals("auction") ? (players.length-1) : players.length;
    int [] passes = new int[players.length];

    for (Player player : players) {
      this.setMessage(player.getName()+" is thinking....");
      player.assessHand();
      Debug.print("  "+player.getName()+"'s max bid is: "+player.getMaxBid()+"\t["+player.handToString()+"]");
    }

    while (sum < limit) {
      boolean opponents = false;
      int ind = turn%players.length;
      int prt = players[ind].getPartner();
      for (int i = 0; i < players.length; i++) {
        if (i != ind && i != prt) {
          opponents = (players[i].lastBid() >= 0 || opponents != false) ? true : false;
        }
      } 

      int ret = players[ind].bid(bid, players[prt].lastBid(), opponents);
      if (ret > 1) 
        this.setMessage(players[ind].getName()+" bids "+ret+".");
      else
        this.setMessage(players[ind].getName()+" passes.");
      if (! this.getModelBooleanProperty("Headless")) {
        Sleep.milliseconds(RandomUtils.range(10,40));
      }

      if (ret < bid) {
        passes[turn%players.length] = 1;
      } else {
        bid  = ret;
        mark = turn%players.length;
      }
      turn++;

      if (this.getModelProperty("BidVariation").equals("auction")) {
        /**
         * If we're playing an auction, then we need to count
         * the number of passes to determine the end of bidding
         */
        sum = 0;
        for (int i : passes) sum += i; 
      } else {
        /** If we're playing single bid, then we just increment sum */
        sum++;
      }
    }

    if (this.getModelProperty("BidVariation").equals("auction")) {
      for (int i = 0; i < passes.length; i++) {
        if (passes[i] == 0) mark = i;
      } 
    } // else mark is already set for us....

    String bidstr;
    if (mark == Pinochle.NORTH || mark == Pinochle.SOUTH) {
      bidstr = "("+bid+",0)"; 
    } else {
      bidstr = "(0,"+bid+")";
    }

    int trump = players[mark].nameTrump();
    this.setModelProperty("Bid",    ""+bidstr);
    this.setModelProperty("Bidder", ""+mark);
    this.setModelProperty("Active", ""+mark);
    this.setModelProperty("Trump",  ""+trump);
    this.setModelProperty("Status", ""+Pinochle.PASS);
    Debug.print(
      players[mark].getName()+" won the bid with "+players[mark].lastBid()+" ("+players[mark].getMaxBid()+")"
    );
    this.setMessage(players[mark].getName()+" won the bid with "+players[mark].lastBid());
    return;
  }

  public void pass(Player[] players) {
    //XXX: some variations won't pass
    int bidder = this.getModelIntProperty("Active");
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
    this.setModelProperty("Status", ""+Pinochle.MELD);
  }

  /**
   * Overseas the meld phase for all participating players
   * <p>
   * @param  Player[]
   * @return void
   */
  public void meld(Player[] players) {
    int score   = 0;
    int meld[]  = new int[] {0,0};
    for (int i = 0; i < players.length; i++) {
      if (players[i].getType() == Player.HUMAN) {
        score = players[i].meld();
        if (i == Pinochle.NORTH || i == Pinochle.SOUTH) {
          meld[0] += score;
        } else {
          meld[1] += score;
        }
      }
    }
    for (int i = 0; i < players.length; i++) {
      if (players[i].getType() == Player.COMPUTER) {
        score = players[i].meld();
        if (i == Pinochle.NORTH || i == Pinochle.SOUTH) {
          meld[0] += score;
        } else {
          meld[1] += score;
        }
      }
    }
    this.setModelProperty("Meld", "("+meld[0]+","+meld[1]+")");
    Logger.log("Meld:  "+"("+meld[0]+","+meld[1]+")");
    this.setModelProperty("Status", ""+Pinochle.AVOW);
    this.pause(true);
    return;
  }

  public void avow(Player[] players) {
    if (
      players[0].getType()==Player.COMPUTER &&
      players[1].getType()==Player.COMPUTER &&
      players[2].getType()==Player.COMPUTER &&
      players[3].getType()==Player.COMPUTER ){
      this.pause(false);
      this.setModelProperty("Status", ""+Pinochle.PLAY);
      return;
    } else {
      this.addPlayButton();
    }

    do {
      Sleep.milliseconds(400);
    } while (this.isPaused());

    for (Player player : players) {
      player.clearMeld();
    }

    this.setModelProperty("Status", ""+Pinochle.PLAY);
  }

   /** 
   * Referees the trick taking phase of a pinochle hand.
   * <p>
   * @param  Player[]
   * @return void
   */
  public void play(Player[] players) {
    int count   = (this.getModelStringProperty("DeckSize").equals("double")) ? 80 : 48;
    int turn    = this.getModelIntProperty("Active");
    boolean sim = this.getModelBooleanProperty("Simulation");
    int tricks  = count/players.length;
    
    for (int i = 0; i < tricks; i++) {
      Trick trick   = new Trick(this.getModelIntProperty("Trump"));
      Card [] cards = new Card[4];
      for (Player player : players) {
        Debug.print(player.getName()+"("+player.getHand().size()+"): "+player.handToString());
        player.setText("");
      }

      for (int j = 0; j < players.length; j++) {
        Card card = null;
        if (trick.isEmpty()) {
          this.runModelMethod("clearTrick");
          this.setMessage(
            players[turn%players.length].getName()+" it's your turn. You have the lead..."
          );
          Debug.print(players[turn%players.length].getName()+" it's your turn. You have the lead...");
        } else {
          this.setMessage(
            players[turn%players.length].getName()+" it's your turn. " +
            Pinochle.suitname(trick.getLeadingSuit())+" was led."
          );
          Debug.print(
            players[turn%players.length].getName()+" it's your turn. " +
            Pinochle.suitname(trick.getLeadingSuit())+" was led."
          );
        }
        card = players[turn%players.length].playCard(trick);
        cards[j] = card;
         
        /**
         * Each computer player will commit this play to memory...
         */
        for (Player player : players) {
          player.remember(card);
        }
        trick.add(players[turn%players.length], card);
        this.arena.play(players[turn%players.length].getPosition(), card);

        int lo = (sim==true) ?  20 : 300;
        int hi = (sim==true) ?  80 : 700;
        if (! this.getModelBooleanProperty("Headless")) {
          Sleep.milliseconds(RandomUtils.range(lo, hi));
        }
        turn++;
      }

      if (! this.getModelBooleanProperty("Headless")) {
        Sleep.milliseconds(RandomUtils.range((sim==true)?20:300,(sim==true)?80:700));
      } 
      turn = trick.winner();

      if (turn % 2 == 0) {
        // Us took the trick
        this.setModelProperty("Take", "("+trick.counters()+",0)");
        org.joedog.util.Logger.log("Trick: ("+trick.counters()+",0)");
      } else {
        // Them took the trick
        this.setModelProperty("Take", "(0,"+trick.counters()+")");
        org.joedog.util.Logger.log("Trick: (0,"+trick.counters()+")");
      }
      Debug.print(trick.toString());
      Debug.print("");
    }
    // Award the last trick
    if (turn % 2 == 0) {
      this.setModelProperty("Take", "(1,0)");
      Logger.log("Last:  (1,0)");
    } else {
      this.setModelProperty("Take", "(0,1)");
      Logger.log("Last:  (0,1)");
    }

    this.runModelMethod("addItUp"); // a homage to Violent Femmes
    if (! this.getModelBooleanProperty("Headless")) {
      Sleep.seconds(2);
    }
   
    // we pass this information to all players but only the bidder stores it 
    players[Pinochle.NORTH].remember(this.getModelIntProperty("NSMeld"), this.getModelIntProperty("NSTake"));
    players[Pinochle.EAST].remember (this.getModelIntProperty("EWMeld"), this.getModelIntProperty("EWTake"));
    players[Pinochle.SOUTH].remember(this.getModelIntProperty("NSMeld"), this.getModelIntProperty("NSTake"));
    players[Pinochle.WEST].remember (this.getModelIntProperty("EWMeld"), this.getModelIntProperty("EWTake"));

    this.setModelProperty(
      "HighScore",
      this.getModelProperty("PlayerNorthName")+"/"+
      this.getModelProperty("PlayerSouthName")+"|"+
      this.getModelProperty("NSHand")
    ); 
    this.setModelProperty(
      "HighScore",
      this.getModelProperty("PlayerEastName")+"/"+
      this.getModelProperty("PlayerWestName")+"|"+
      this.getModelProperty("EWHand")
    ); 
    this.setModelProperty("Status", ""+Pinochle.DONE);
  }

  private void done(Player[] players) {
    this.runModelMethod("addItUp"); // a homage to Violent Femmes
    if (! this.getModelBooleanProperty("Headless")) {
      Sleep.seconds(2);
    }

    // we pass this information to all players but only the bidder stores it 
    players[Pinochle.NORTH].remember(this.getModelIntProperty("NSMeld"), this.getModelIntProperty("NSTake"));
    players[Pinochle.EAST].remember (this.getModelIntProperty("EWMeld"), this.getModelIntProperty("EWTake"));
    players[Pinochle.SOUTH].remember(this.getModelIntProperty("NSMeld"), this.getModelIntProperty("NSTake"));
    players[Pinochle.WEST].remember (this.getModelIntProperty("EWMeld"), this.getModelIntProperty("EWTake"));

    this.setModelProperty(
      "HighScore",
      this.getModelProperty("PlayerNorthName")+"/"+
      this.getModelProperty("PlayerSouthName")+"|"+
      this.getModelProperty("NSHand")
    );
    this.setModelProperty(
      "HighScore",
      this.getModelProperty("PlayerEastName")+"/"+
      this.getModelProperty("PlayerWestName")+"|"+
      this.getModelProperty("EWHand")
    );
    for (Player player : players) {
      player.clear();  
      player.setText("");
    }
    if (this.getModelIntProperty("Status") != Pinochle.OVER) {
      this.newHand();
    }
  }
}
