package org.joedog.pinochle.view.actions;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import org.joedog.pinochle.control.Game;

public class ExitAction implements ActionListener {
  private Game controller;

  public ExitAction (Game controller) {
    this.controller = controller;
  }

  public void actionPerformed (ActionEvent e) {
    this.controller.exit();
  }
}

