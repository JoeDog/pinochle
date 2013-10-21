package org.joedog.pinochle.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.beans.PropertyChangeEvent;

import org.joedog.pinochle.control.*;
import org.joedog.pinochle.game.*;

public class LastTrick extends JPanel implements View {
  private GameController controller;
  private Card nc = null;
  private Card sc = null;
  private Card ec = null;
  private Card wc = null;

  public LastTrick(GameController controller) {
    this.controller = controller;
    this.setBackground(new Color(48,200,126));
  }
 
  @Override
  public void paint(Graphics g) {
    super.paint(g);
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g); 
    if (wc != null) {
      wc.getIcon().paintIcon(this, g, 5, 0);
    }
    if (nc != null) {
      nc.getIcon().paintIcon(this, g, 25, 0);
    }
    if (sc != null) {
      sc.getIcon().paintIcon(this, g, 45, 0);
    }
    if (ec != null) {
      ec.getIcon().paintIcon(this, g, 70, 0);
    }
  }

  public void setDisplayTrick(Card[] cards){
    cards[0].setFaceUp();
    cards[1].setFaceUp();
    cards[2].setFaceUp();
    cards[3].setFaceUp();
    this.wc = cards[0];
    this.nc = cards[1];
    this.sc = cards[2];
    this.ec = cards[3];
    repaint();
  }

  public void clear() {
    this.wc = null;
    this.nc = null;
    this.ec = null;
    this.sc = null;
    this.removeAll();
    this.revalidate();
    this.repaint();
  }

  public void modelPropertyChange(PropertyChangeEvent e) {
    if (e.getNewValue() == null) return;
  }
}

