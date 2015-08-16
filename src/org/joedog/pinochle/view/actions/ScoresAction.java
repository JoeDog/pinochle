package org.joedog.pinochle.view.actions;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import org.joedog.pinochle.control.Game;
import org.joedog.pinochle.view.HighScorePanel;

public class ScoresAction implements ActionListener {
  private Game controller;

  public ScoresAction (Game controller) {
    this.controller = controller;
  }

  public void actionPerformed (ActionEvent e) {
    new HighScorePanel(this.controller);
  }
}

