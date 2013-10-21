package org.joedog.pinochle.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.beans.PropertyChangeEvent;

import org.joedog.pinochle.control.*;
import org.joedog.pinochle.game.*;

public class TrickArea extends JPanel implements View {
  private GameController controller;
  private Card nc = null;
  private Card sc = null;
  private Card ec = null;
  private Card wc = null;

  public TrickArea(GameController controller) {
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
      wc.getIcon().paintIcon(this, g, 5, 10);
    }
    if (nc != null) {
      nc.getIcon().paintIcon(this, g, 25, 0);
    }
    if (sc != null) {
      sc.getIcon().paintIcon(this, g, 45, 20);
    }
    if (ec != null) {
      ec.getIcon().paintIcon(this, g, 70, 10);
    }
  }

  public void setNorthPlay(Card card) {
    System.out.println("NORTH: "+card.toString());
    card.setFaceUp();
    this.nc = card;
    repaint();
  }

  public void setSouthPlay(final Card card) {
    System.out.println("SOUTH: "+card.toString());
    card.setFaceUp();
    this.sc = card;
    repaint();
  }

  public void setEastPlay(Card card) {
    System.out.println("EAST: "+card.toString());
    card.setFaceUp();
    this.ec = card;
    repaint();
  }

  public void setWestPlay(Card card) {
    System.out.println("WEST: "+card.toString());
    card.setFaceUp();
    this.wc = card;
    repaint();
  }

  public void clear() {
    this.wc = null;
    this.nc = null;
    this.ec = null;
    this.sc = null;
    System.out.println("repainting after clear!");
    this.removeAll();
    this.revalidate();
    this.repaint();
  }

  public void modelPropertyChange(PropertyChangeEvent e) {
    if (e.getNewValue() == null) return;
  }
}

