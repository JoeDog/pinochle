package org.joedog.pinochle.view.actions;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import org.joedog.pinochle.control.GameController;
import org.joedog.pinochle.view.ConfigView;

public class ConfigAction implements ActionListener {
  private GameController controller;

  public ConfigAction (GameController controller) {
    this.controller = controller;
  }
      
  public void actionPerformed (ActionEvent e) {
    //this.controller.newMatch();
    new ConfigView(this.controller); 
  } 
} 
