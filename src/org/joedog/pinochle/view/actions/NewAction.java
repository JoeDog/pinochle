package org.joedog.pinochle.view.actions;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingUtilities;

import org.joedog.pinochle.control.Game;

public class NewAction implements ActionListener {
  private Game controller;

  public NewAction (Game controller) {
    this.controller = controller;
  }

  public void actionPerformed (ActionEvent e) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        System.out.println("controller.newGame()");
        controller.newGame();
      }
    });
  }
}

