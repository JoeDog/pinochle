package org.joedog.pinochle.view.actions;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import org.joedog.pinochle.controller.GameController;

public class DeckButtonListener implements ActionListener {
  private GameController controller;

  public DeckButtonListener(GameController controller) {
    this.controller = controller;
  }
  public void actionPerformed(ActionEvent e) {
    controller.setProperty("DeckSize", e.getActionCommand());
  }
}
