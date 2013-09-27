package org.joedog.pinochle.view;

import java.awt.Color;
import javax.swing.JPanel;
import java.beans.PropertyChangeEvent;

import org.joedog.pinochle.controller.*;

class TrickArea extends JPanel implements View {
  private GameController controller;

  public TrickArea(GameController controller) {
    this.controller = controller;
    this.setBackground(Color.red);
  }

  public void modelPropertyChange(PropertyChangeEvent e) {
    if (e.getNewValue() == null) return;
  }
}

