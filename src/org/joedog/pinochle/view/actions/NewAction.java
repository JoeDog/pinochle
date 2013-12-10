package org.joedog.pinochle.view.actions;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingUtilities;

import org.joedog.pinochle.control.GameController;

public class NewAction implements ActionListener {
  private GameController controller;

  public NewAction (GameController controller) {
    this.controller = controller;
  }

  public void actionPerformed (ActionEvent e) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        controller.resetGame();
      }
    });
  }
}

