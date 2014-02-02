package org.joedog.pinochle.view.actions;

import javax.swing.JFrame;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import org.joedog.pinochle.control.GameController;
import org.joedog.pinochle.model.HighScoreTableModel;

public class HighScoreCloseAction implements ActionListener {
  private JFrame frame;
  private HighScoreTableModel model;
  private GameController controller;
  public final static String NAME = "Okay";

  public HighScoreCloseAction (JFrame frame, GameController controller, HighScoreTableModel model) {
    this.frame      = frame;
    this.controller = controller;
    this.model      = model;
  }

  public void actionPerformed (ActionEvent e) {
    this.model.save();
    this.controller.setProperty("ConfigX",  Integer.toString(this.frame.getX()));
    this.controller.setProperty("ConfigY",  Integer.toString(this.frame.getY()));
    this.frame.setVisible(false); 
    this.frame.dispose();
  }
}

