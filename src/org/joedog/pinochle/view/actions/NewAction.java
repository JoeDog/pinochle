package org.joedog.pinochle.view.actions;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import org.joedog.pinochle.controller.GameController;

public class NewAction implements ActionListener {
  private GameController controller;

  public NewAction (GameController controller) {
    this.controller = controller;
  }

  public void actionPerformed (ActionEvent e) {
    //this.controller.newMatch();
  }
}

