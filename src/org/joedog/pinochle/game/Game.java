package org.joedog.pinochle.game;

import org.joedog.pinochle.control.*;
import org.joedog.pinochle.model.*;
import org.joedog.pinochle.game.*;
import org.joedog.pinochle.player.*;
import org.joedog.pinochle.view.*;
import org.joedog.pinochle.view.actions.*;
import org.joedog.pinochle.util.*;

import java.util.concurrent.TimeUnit;
import javax.swing.UIManager;
import javax.swing.SwingUtilities;

/**
 * @author Jeffrey Fulmer
 */
public class Game {
  private GameController controller;
  private GameView       view;
  private Player[]       players;
  private PlayerFactory  factory;


  public Game(GameController controller, GameView view) {
    this.controller = controller;
    this.view       = view;
    this.players    = this.getPlayers();
  }

  /**
   * Creates all the players necessary to play the game; player
   * types are picked up from the game configuration.
   * <p>
   * @param  none
   * @return Player[] 
   */
  public Player [] getPlayers () {
    this.factory      = new PlayerFactoryImpl();
    Player [] players = {
      factory.getPlayer(this.controller, controller.getIntProperty("PlayerNorthType")),
      factory.getPlayer(this.controller, controller.getIntProperty("PlayerEastType")),
      factory.getPlayer(this.controller, controller.getIntProperty("PlayerSouthType")), 
      factory.getPlayer(this.controller, controller.getIntProperty("PlayerWestType"))
    };

    for (int i = 0; i < players.length; i++) {
      int    partner = -1;
      String name    = "default";
      switch (i) {
        case Pinochle.NORTH:
          partner = Pinochle.SOUTH;
          name    = controller.getProperty("PlayerNorthName");
          break;
        case Pinochle.EAST:
          partner = Pinochle.WEST;
          name    = controller.getProperty("PlayerEastName");
          break;
        case Pinochle.SOUTH:
          partner = Pinochle.NORTH;
          name    = controller.getProperty("PlayerSouthName");
          break;
        case Pinochle.WEST:
          partner = Pinochle.EAST;
          name    = controller.getProperty("PlayerWestName");
          break;
      }
      players[i].setup(view.getSetting(i), i, partner, name);
    }
    return players;
  }
 
  /**
   * Invoked by the GameThread this method starts and conducts
   * the flow of a pinochle game.
   * <p>
   * @param  none
   * @return void
   */ 
  public synchronized void start() {
    int x      = 0;
    int turn   = 0;
    int status = GameController.DEAL;
   
    controller.checkConfig();
    while (controller.isPaused()) {
      try {
        Thread.sleep(500);
      } catch (Exception e) {}
    }
    controller.setProperty("GameWinningScore", controller.getProperty("WinningScore"));

    while (status != GameController.OVER) {
      status = controller.gameStatus();
      switch (status) {
        case GameController.DEAL:
          this.deal(players);
          break;
        case GameController.BID:
          this.bid(players);
          break;
        case GameController.PASS:
          this.pass(players);
          break;
        case GameController.MELD:
          this.meld(players);
          break;
        case GameController.PLAY:
          this.play(players);
          break;
        case GameController.SCORE:
          break;
        case GameController.OVER:
          if (controller.getHeadless() == true) {
            System.exit(0);
          }
          controller.pause(true);
          break;
      }
      turn++;
    }
  }

  /**
   * Deals the cards to all game participants
   * <p>
   * @param  Player[] 
   * @return void
   */
  private void deal(Player[] players) {
    if (controller.over()) return;

    Deck deck = new Deck(1);
    int turn  = controller.getIntProperty("Dealer");
    int next  = (turn < players.length-1) ? turn + 1 : 0;

    for (Player player : players) {
      player.newHand();
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
    CustomHands ch = new CustomHands();
    if (ch.hasCustomHands()) {
      int id = 0;
      for (Player player : players) {
        CustomHandsList<String> tmp = ch.get(player.getPosition());
        for (String s : tmp) {
          Card c = new Card(id, s);
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
      player.refresh();
    }

    controller.store("GameTrump",    "-1");
    controller.store("GameStatus",   ""+GameController.BID);
    controller.store("ActivePlayer", ""+next);
    controller.store("Dealer",       ""+next);
    return;
  }

  /**
   * Referees the bidding phase of a pinochle game
   * <p>
   * @param  Player[] 
   * @return void
   */
  public void bid(Player[] players) {
    int bid       = (controller.getIntProperty("MinimumBid") - 1);
    int turn      = controller.getIntProperty("ActivePlayer");
    int sum       = 0;  // sum of the number of passes
    int mark      = 0;
    int limit     = (controller.getProperty("BidVariation").equals("auction")) ? 
                    (players.length - 1) : players.length;
    int [] passes = new int[players.length];

    for (Player player : players) {
      player.assessHand();
      player.refresh();
      Debug.print(player.getName()+"'s max bid is: "+player.getMaxBid()+"\t["+player.handToString()+"]");
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

      if (ret < bid) {
        passes[turn%players.length] = 1;
      } else {
        bid  = ret;
        mark = turn%players.length;
      }
      turn++;

      if (controller.getProperty("BidVariation").equals("auction")) {
        // If we're playing an auction, then we need to count
        // the number of passes to determine the end of bidding
        sum = 0;
        for (int i : passes) sum += i; // get the total passes
      } else {
        //If we're playing single bid, then we just increment sum
        sum++;
      }
    }

    if (controller.getProperty("BidVariation").equals("auction")) {
      for (int i = 0; i < passes.length; i++) {
        if (passes[i] == 0) mark = i;
      } 
    } // else mark is already set for us....

    int trump = players[mark].nameTrump();
    controller.store("GameBid",    ""+bid);
    controller.store("Bidder",     ""+mark);
    controller.store("GameTrump",  ""+trump);
    controller.store("GameStatus", ""+GameController.PASS);
    Debug.print(
      players[mark].getName()+" won the bid with "+players[mark].lastBid()+" ("+players[mark].getMaxBid()+")"
    );
    return;
  }
 
  /**
   * Facilitates the pass between the parters who won the bid
   * <p>
   * @param  Player[] 
   * @return void
   */
  public void pass(Player[] players) {
    //XXX: some variations won't pass
    int bidder = controller.getIntProperty("ActivePlayer");
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
    controller.store("GameStatus", ""+GameController.MELD);
  }

  /**
   * Overseas the meld phase for all participating players
   * <p>
   * @param  Player[]
   * @return void
   */
  public void meld(Player[] players) {
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
    controller.resetHand();
    controller.store("Meld",       "NS|"+ns);
    controller.store("Meld",       "EW|"+ew);
    controller.store("GameStatus", ""+GameController.PLAY);
    controller.pause(true);
    controller.addPlayButton();
    return;
  }
 
  /** 
   * Referees the trick taking phase of a pinochle hand.
   * <p>
   * @param  Player[]
   * @return void
   */
  public void play(Player[] players) {
    int count   = controller.getIntProperty("CardCount");
    int turn    = controller.getIntProperty("ActivePlayer");
    boolean sim = controller.getBooleanProperty("Simulation");
    int tricks  = count/players.length;
    while (controller.isPaused()) {
      try {
        if (
          players[0].getType()==Player.COMPUTER &&
          players[1].getType()==Player.COMPUTER &&
          players[2].getType()==Player.COMPUTER &&
          players[3].getType()==Player.COMPUTER ) {
          controller.pause(false);
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
      Trick trick   = new Trick(controller.getTrump());
      Card [] cards = new Card[4];
      Debug.print("Trump: "+Pinochle.suitname(controller.getTrump()));
      for (Player player : players) {
        Debug.print(player.getName()+": "+player.handToString());
      }
      for (int j = 0; j < players.length; j++) {
        Card card = null;
        if (trick.isEmpty()) {
          controller.setStatus(
            players[turn%players.length].getName()+" it's your turn. You have the lead..."
          );
        } else {
          controller.setStatus(
            players[turn%players.length].getName()+" it's your turn." +
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
        switch (players[turn%players.length].getPosition()) {
          case Pinochle.NORTH:
            controller.display("NorthPlay", card);
            break;
          case Pinochle.SOUTH:
            controller.display("SouthPlay", card);
            break;
          case Pinochle.EAST:
            controller.display("EastPlay", card);
            break;
          case Pinochle.WEST:
            controller.display("WestPlay", card);
            break;
        }
        try {
          int lo = (sim==true) ?  20 : 300;
          int hi = (sim==true) ?  80 : 700;
          Thread.sleep(RandomUtils.range(lo, hi));
        } catch (Exception e) {}
        turn++;
      }
      turn = trick.winner();
      Debug.print("    TRICK: "+trick.toString());
      if (turn % 2 == 0) {
        controller.store("Take", "NS|"+trick.counters());
      } else {
        controller.store("Take", "EW|"+trick.counters());
      }
      controller.clear();
      controller.display("DisplayTrick", cards);
      Debug.print("");
    }
    // Award the last trick
    if (turn % 2 == 0) {
      controller.store("Take", "NS|1");
    } else {
      controller.store("Take", "EW|1");
    }
    controller.addScore();
   
    // we pass this information to all players but only the bidder stores it 
    players[Pinochle.NORTH].remember(controller.getIntProperty("NSMeld"), controller.getIntProperty("NSTake"));
    players[Pinochle.EAST].remember (controller.getIntProperty("EWMeld"), controller.getIntProperty("EWTake"));
    players[Pinochle.SOUTH].remember(controller.getIntProperty("NSMeld"), controller.getIntProperty("NSTake"));
    players[Pinochle.WEST].remember (controller.getIntProperty("EWMeld"), controller.getIntProperty("EWTake"));

    controller.store(
      "HighScore",
      controller.getProperty("PlayerNorthName")+"/"+
      controller.getProperty("PlayerSouthName")+"|"+
      controller.getProperty("NSHand")
    ); 
    controller.store(
      "HighScore",
      controller.getProperty("PlayerEastName")+"/"+
      controller.getProperty("PlayerWestName")+"|"+
      controller.getProperty("EWHand")
    ); 
    if (controller.gameStatus() != GameController.OVER || ! controller.over()) {
      controller.store("GameTrump", "-1");
      controller.newHand();
    }
  }
}

