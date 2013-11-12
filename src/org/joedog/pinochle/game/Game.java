package org.joedog.pinochle.game;

import org.joedog.pinochle.control.*;
import org.joedog.pinochle.model.*;
import org.joedog.pinochle.game.*;
import org.joedog.pinochle.player.*;
import org.joedog.pinochle.view.*;
import org.joedog.pinochle.view.actions.*;

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
      if (controller == null) System.out.println("null controller");
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
  
  public synchronized void play() {
    int x      = 0;
    int turn   = 0;
    int status = GameController.DEAL;
    controller.setProperty("GameWinningScore", controller.getProperty("WinningScore"));

    while (status != GameController.OVER) {
      status = controller.gameStatus();
      switch (status) {
        case GameController.DEAL:
          controller.newDeal(players);
          break;
        case GameController.BID:
          controller.getBids(players);
          break;
        case GameController.PASS:
          controller.passCards(players);
          break;
        case GameController.MELD:
          controller.getMeld(players);
          break;
        case GameController.PLAY:
          controller.playHand(players);
          System.out.println("HAND IS OVER; now what????");
          break;
        case GameController.SCORE:
          break;
        case GameController.OVER:
          controller.pause(true);
          break;
      }
      turn++;
    }
  }
}

