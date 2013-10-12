package org.joedog.pinochle;

import org.joedog.pinochle.controller.*;
import org.joedog.pinochle.model.*;
import org.joedog.pinochle.game.*;
import org.joedog.pinochle.player.*;
import org.joedog.pinochle.view.*;
import org.joedog.pinochle.view.actions.*;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.concurrent.TimeUnit;
import java.lang.InterruptedException;
import javax.swing.WindowConstants;

import javax.swing.UIManager;
import javax.swing.SwingUtilities;

/**
 * @author Jeffrey Fulmer
 */
public class Main {
  private static GameController controller;
  private static GameActions    actions;
  private static GameView       view;
  private static GameModel      model;
  private static GameMenu       menu;
  private Splash         splash;
  private PlayerFactory  factory;

  public Main() {
   /**
    this.splash     = new Splash();
    splash.setMessage("Game controller");
    splash.setMessage("Game view");
    splash.setMessage("Game data");
    splash.setMessage("Initializing actions");
    splash.close();
    */
  }

  public Player [] getPlayers (GameController controller) {
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
  
  public synchronized void play(GameController controller, Player[] players) {
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

  private static void createAndShowGui(GameController controller, GameView view) {
    JFrame   frame = new JFrame("Pinochle");
    GameMenu menu  = new GameMenu(new GameActions(controller));
    frame.setJMenuBar(menu);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setContentPane(view.createContentPane());
    frame.setPreferredSize(new Dimension(970,630));
    frame.setSize(970, 630);
    Dimension dim  = Toolkit.getDefaultToolkit().getScreenSize();
    int x = controller.getIntProperty("MainX");
    int y = controller.getIntProperty("MainY");
    if (x == 0 && y == 0) {
      int w = frame.getSize().width;
      int h = frame.getSize().height;
      x = (dim.width-w)/2;
      y = (dim.height-h)/2;
    }
    frame.pack();
    frame.setLocation(x, y);
    frame.setVisible(true);
    frame.setVisible(true);
  }

  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch (javax.swing.UnsupportedLookAndFeelException e) {
      System.err.println("Pinochle requires java-1.6 or higher");
    }
    catch (ClassNotFoundException e) {
      System.err.println("Pinochle requires java-1.6 or higher");
    }
    catch (InstantiationException e) {
    }
    catch (IllegalAccessException e) {
    }
    if (controller == null) {
      controller = new GameController();
    }
    if (model == null) {
      model = new GameModel();
    }
    if (view == null) {
      view = new GameView(controller);
    }
    controller.addView(view);
    controller.addModel(model);
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        createAndShowGui(controller, view);
      }
    });
    Main main = new Main();
    Player[] players = main.getPlayers(controller);
    main.play(controller, players);
  }
}

