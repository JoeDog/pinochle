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
public class Main extends JFrame {
  private GameController controller;
  private GameActions    actions;
  private GameView       view;
  private GameModel      model;
  private GameMenu       menu;
  private Splash         splash;
  private PlayerFactory  factory;

  public Main() {
     super("Pinochle");
    this.splash     = new Splash();
    splash.setMessage("Game controller");
    this.controller = new GameController();
    splash.setMessage("Game data");
    this.model      = new GameModel();
    splash.setMessage("Game view");
    this.view       = new GameView(controller);
    splash.setMessage("Initializing actions");
    this.actions    = new GameActions(this.controller);
    this.menu       = new GameMenu(this.actions);

    controller.addView(view);
    controller.addModel(model);

    splash.close();

    view.createPanel();
    this.display();

    Player [] players = this.getPlayers();
    for ( ;; ) {
      while (controller.alive){this.play(players);}
      view.close();
      System.exit(0);
    }
  }

  public void display() {
    this.setPreferredSize(new Dimension(970,630));

    Dimension dim  = Toolkit.getDefaultToolkit().getScreenSize();
    this.getContentPane().add(view, BorderLayout.CENTER);
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    this.setJMenuBar(menu);
    this.pack();
    int x = controller.getIntProperty("MainX");
    int y = controller.getIntProperty("MainY");
    if (x == 0 && y == 0) {
      int w = this.getSize().width;
      int h = this.getSize().height;
      x = (dim.width-w)/2;
      y = (dim.height-h)/2;
    }
    this.setLocation(x, y);
    this.setVisible(true);
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
  
  public synchronized void play(Player[] players) {
    int x      = 0;
    int turn   = 0;
    int status = GameController.DEAL;
  
    while (status != GameController.OVER) {
      status = controller.gameStatus();
      System.out.println("STATUS: "+status+"!!!!!!!!!!!!!!!!");
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
          try {
            TimeUnit.SECONDS.sleep(90);
          } catch (java.lang.InterruptedException ie) {}
          System.exit(0);
          break;
        case GameController.PLAY:
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
    Main pinochle = new Main();
    /**
     Commented out because this results in a thread-locked mess:
     Exception in thread "AWT-EventQueue-0" java.lang.OutOfMemoryError: Java heap space
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        Main pinochle = new Main();
      }
    });
    */
  }
}

