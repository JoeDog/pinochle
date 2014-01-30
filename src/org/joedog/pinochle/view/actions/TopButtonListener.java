package org.joedog.pinochle.view.actions;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import org.joedog.pinochle.control.GameController;

public class TopButtonListener implements ActionListener {
  private GameController controller;

  public TopButtonListener(GameController controller) {
    this.controller = controller;
  }
  public void actionPerformed(ActionEvent e) {
    controller.setProperty("TopVariation", e.getActionCommand());
  }
}
