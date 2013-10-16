package org.joedog.pinochle;

import org.joedog.pinochle.control.*;
import org.joedog.pinochle.model.*;
import org.joedog.pinochle.game.*;
import org.joedog.pinochle.player.*;
import org.joedog.pinochle.view.*;
import org.joedog.pinochle.view.actions.*;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.WindowConstants;

import javax.swing.UIManager;
import javax.swing.SwingUtilities;

/**
 * @author Jeffrey Fulmer
 */
public class Main {
  private Splash splash;
  private static GameController controller;
  private static GameActions    actions;
  private static GameView       view;
  private static GameModel      model;
  private static GameMenu       menu;

  public Main() { }

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

    while (true) {
      Game game = new Game(controller, view);
      GameThread thread = new GameThread(game);
      controller.addThread(thread);
      thread.start();
      while (thread.isAlive()) ;
    }
  }
}

