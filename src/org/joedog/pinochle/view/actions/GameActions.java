package org.joedog.pinochle.view.actions;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import org.joedog.pinochle.control.GameController;

public class GameActions {
  private GameController controller;

  public GameActions (GameController controller) {
    this.controller = controller;
  }

  public ActionListener getAction(String item) {
    if (item.equals("New"))
      return new NewAction(controller);
    else if (item.equals("Exit"))
      return new ExitAction(controller);
    else if (item.equals("Configure..."))
      return new ConfigAction(controller);
    else 
      return null;
  }
}


