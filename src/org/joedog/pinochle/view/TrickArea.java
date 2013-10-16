package org.joedog.pinochle.view;

import java.awt.Color;
import javax.swing.JPanel;
import java.beans.PropertyChangeEvent;

import org.joedog.pinochle.control.*;

class TrickArea extends JPanel implements View {
  private GameController controller;

  public TrickArea(GameController controller) {
    this.controller = controller;
    this.setBackground(new Color(48,200,126));
  }

  public void modelPropertyChange(PropertyChangeEvent e) {
    if (e.getNewValue() == null) return;
  }
}

