package org.joedog.pinochle.view.actions;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import org.joedog.pinochle.control.GameController;

public class BidButtonListener implements ActionListener {
  private GameController controller;

  public BidButtonListener(GameController controller) {
    this.controller = controller;
  }
  public void actionPerformed(ActionEvent e) {
    controller.setProperty("BidType", e.getActionCommand());
  }
}
