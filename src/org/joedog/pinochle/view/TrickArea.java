package org.joedog.pinochle.view;

import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.beans.PropertyChangeEvent;

import org.joedog.pinochle.control.*;
import org.joedog.pinochle.game.*;

public class TrickArea extends JPanel implements View {
  private GameController controller;
  private JPanel         table  = null; 

  public TrickArea(GameController controller) {
    this.controller = controller;
    this.setBackground(new Color(48,200,126));
    this.setLayout(new BorderLayout());
    this.table = new JPanel();
    RowLayout layout = new RowLayout(
      RowLayout.HORIZONTAL, 2, 
      RowLayout.TRAILING, 
      RowLayout.ADJUST_NONE
    );
    layout.setGap(0);
    this.table.setLayout(layout);
    this.table.setBackground(new Color(48,200,126));
    this.add(table, BorderLayout.CENTER);
  }

  public void setNorthPlay(Card card) {
    System.out.println("NORTH: "+card.toString());
    card.setFaceUp();
    CardPanel cp = new CardPanel(table, card);
    this.table.add(cp, RowLayout.LEFT);
  }

  public void setSouthPlay(final Card card) {
    System.out.println("SOUTH: "+card.toString());
    card.setFaceUp();
    CardPanel cp = new CardPanel(table, card);
    this.table.add(cp, RowLayout.LEFT);
  }

  public void setEastPlay(Card card) {
    System.out.println("EAST: "+card.toString());
    card.setFaceUp();
    CardPanel cp = new CardPanel(table, card);
    this.table.add(cp, RowLayout.LEFT);
  }

  public void setWestPlay(Card card) {
    System.out.println("WEST: "+card.toString());
    card.setFaceUp();
    CardPanel cp = new CardPanel(table, card);
    this.table.add(cp, RowLayout.LEFT);
  }

  public void clear() {
    System.out.println("clear!1!!!!!!");
    this.table.removeAll();
    this.table.invalidate();
    this.table.validate();
  }

  public void modelPropertyChange(PropertyChangeEvent e) {
    if (e.getNewValue() == null) return;
  }
}

