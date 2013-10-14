package org.joedog.pinochle.game;

import org.joedog.pinochle.controller.*;
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
      factory.getPlayer(this.controller, Player.COMPUTER),
      factory.getPlayer(this.controller, Player.COMPUTER),
      factory.getPlayer(this.controller, Player.HUMAN), 
      factory.getPlayer(this.controller, Player.COMPUTER)
    };
    for (int i = 0; i < players.length; i++) {
      String name = "default";
      if (controller == null) System.out.println("null controller");
      switch (i) {
        case Pinochle.NORTH:
          name = controller.getProperty("PlayerNorthName");
          break;
        case Pinochle.EAST:
          name = controller.getProperty("PlayerEastName");
          break;
        case Pinochle.SOUTH:
          name = controller.getProperty("PlayerSouthName");
          break;
        case Pinochle.WEST:
          name = controller.getProperty("PlayerWestName");
          break;
      }
      players[i].setup(view.getSetting(i), i, name);
    }
    return players;
  }
  
  public synchronized void play() {
    int x      = 0;
    int turn   = 0;
    int status = GameController.DEAL;

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
          try {
            TimeUnit.SECONDS.sleep(90);
          } catch (java.lang.InterruptedException ie) {}
          //System.exit(0); 
          break;
        case GameController.SCORE:
          break;
        case GameController.OVER:
          break;
      }
      /* FOR REFERENCE
      if ((players[turn%2].getType()).equals("HUMAN")) {
        controller.setStatus("Your turn...");
      } else {
        controller.setStatus("My turn...");
        players[turn%2].setEngine(controller.getEngine());
      }
      players[turn%2].takeTurn(); 
      */
      turn++;
    }
  }
}

