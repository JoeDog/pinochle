package org.joedog.pinochle.view.actions;

import javax.swing.JFrame;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import org.joedog.pinochle.control.Game;
import org.joedog.pinochle.model.HighScoreTableModel;

public class HighScoreCloseAction implements ActionListener {
  private JFrame frame;
  private HighScoreTableModel model;
  private Game control;
  public final static String NAME = "Okay";

  public HighScoreCloseAction (JFrame frame, Game control, HighScoreTableModel model) {
    this.frame   = frame;
    this.control = control;
    this.model   = model;
  }

  public void actionPerformed (ActionEvent e) {
    this.model.save();
    this.control.setModelProperty("ConfigX",  Integer.toString(this.frame.getX()));
    this.control.setModelProperty("ConfigY",  Integer.toString(this.frame.getY()));
    this.frame.setVisible(false); 
    this.frame.dispose();
  }
}

