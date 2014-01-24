package org.joedog.pinochle.view.actions;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import org.joedog.pinochle.control.GameController;
import org.joedog.pinochle.view.HighScorePanel;

public class ScoresAction implements ActionListener {
  private GameController controller;

  public ScoresAction (GameController controller) {
    this.controller = controller;
  }

  public void actionPerformed (ActionEvent e) {
    new HighScorePanel();
  }
}

