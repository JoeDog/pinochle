package org.joedog.pinochle.view.actions;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import org.joedog.pinochle.control.Game;
import org.joedog.pinochle.view.Configuration;

public class ConfigAction implements ActionListener {
  private Game controller;

  public ConfigAction (Game controller) {
    this.controller = controller;
  }
      
  public void actionPerformed (ActionEvent e) {
    new Configuration(this.controller); 
  } 
} 
