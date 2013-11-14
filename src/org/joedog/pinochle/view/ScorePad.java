package org.joedog.pinochle.view;

import org.joedog.pinochle.control.*;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import javax.swing.Action;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;

public class ScorePad extends JPanel implements View {
  static final long serialVersionUID = -196491492884005033L;
  private String[] headers = {"Us", "Them"};
  private Cell[][] cell    = new Cell[5][3];
  private String[] labels  = new String[] {"Meld", "Counters", "Total", "Score"};
  private GameController controller;

  public ScorePad (GameController controller) {
    this.controller = controller;
    this.setup();
    this.reset();
  }

  public void reset () {
    String east  = controller.getProperty("PlayerEastName");
    String west  = controller.getProperty("PlayerWestName");
    String north = controller.getProperty("PlayerNorthName");
    String south = controller.getProperty("PlayerSouthName");

    if (north != null || north.length() > 0 || south != null || south.length() > 1) {
      headers[0] = south.substring(0, 1)+"/"+north.substring(0,1);  
    }
    if (east != null || east.length() > 1 || west != null || west.length() > 1) {
      headers[1] = east.substring(0, 1)+"/"+west.substring(0,1);
    }
    cell[0][1].setValue(headers[0]);
    cell[0][2].setValue(headers[1]);
    for (int i = 1, j = 0; i < labels.length+1; i++, j++) 
      cell[i][0].setValue(labels[j]);
    for (int i = 1; i < cell.length; i++) {
      for (int j = 1; j < cell[i].length; j++) {
        cell[i][j].setValue("0");
      }
    } 
  }

  public void resetHand() {
    for (int i = 1; i < cell.length-1; i++) {
      for (int j = 1; j < cell[i].length; j++) {
        cell[i][j].setValue("0");
      }
    } 
  }

  private void setup() {
    int id = 0;
    BorderLayout bl = new BorderLayout();
    GridLayout   gl = new GridLayout(5, 3);
    gl.setVgap(1);
    gl.setHgap(1);
    this.setLayout(gl);
    this.setBackground(new Color(123,145,83));
    for (int i = 0; i < cell.length; i++) {
      for (int j = 0; j < cell[i].length; j++) {
        id++;
        cell[i][j] = new Cell(id);
        this.add(cell[i][j]);
        if (i == 0) 
          cell[i][j].setBold();
        if (j == 0) 
          cell[i][j].setLabel();
      }
    }
  }

  public void modelPropertyChange(PropertyChangeEvent e) {
    if (e.getNewValue() == null) return;
    if (e.getPropertyName().equals(controller.MELD_SCORE)) {
      if (e.getOldValue().equals("NSMELD")) {
        cell[1][1].setValue((String)e.getNewValue()); 
      } else {
        cell[1][2].setValue((String)e.getNewValue()); 
      }
    }
    if (e.getPropertyName().equals(controller.TAKE_SCORE)) {
      if (e.getOldValue().equals("NSTAKE")) {
        cell[2][1].setValue((String)e.getNewValue()); 
      } else {
        cell[2][2].setValue((String)e.getNewValue()); 
      }
    }
    if (e.getPropertyName().equals(controller.HAND_SCORE)) {
      if (e.getOldValue().equals("NSHAND")) {
        cell[3][1].setValue((String)e.getNewValue()); 
      } else {
        cell[3][2].setValue((String)e.getNewValue()); 
      }
    }
    if (e.getPropertyName().equals(controller.GAME_SCORE)) {
      if (e.getOldValue().equals("NSGAME")) {
        cell[4][1].setValue((String)e.getNewValue()); 
      } else {
        cell[4][2].setValue((String)e.getNewValue()); 
      }
    }
    if (e.getPropertyName().equals(controller.WINNER)) {
      this.controller.winner();
      if (e.getNewValue().equals("NORTH_SOUTH")) {
        cell[4][1].drawWinner(); 
      } else {
        cell[4][2].drawWinner(); 
      }
    }
    if (e.getPropertyName().equals(controller.RESET)) {
      if (e.getNewValue().equals("game")) {
        this.reset();
      } else {
        this.resetHand();
      }
    }
  }

  

  private class Cell extends Canvas {
    private int     id;
    private int     wr     = 0;
    private int     hr     = 0;
    private int     weight = Font.PLAIN;
    private boolean winner = false;
    private String  value  = "";
    private Color   black  = Color.BLACK;
    private Color   red    = Color.RED;
    static final long serialVersionUID = -200243434234218974L; 

    public Cell(int id) {
      this.id = id;
      this.wr = -10 + (int)(Math.random()*2);
      this.hr =  2  + (int)(Math.random()*6);
      this.setBackground(new Color(239,236,157));
    }

    public synchronized void setValue(String value) {
      this.value = value;
      repaint();
    }

    public int getId() {
      return this.id;
    }
  
    public void setBold() {
      this.weight = Font.BOLD;
    }

    public void setLabel() {
      this.wr = -25;
    }

    public void drawWinner() {
      this.winner = true;
      System.out.println("WINNER IS TRUE!!");
      this.repaint();
    }

    public void paint(Graphics g) {
      Graphics2D g2 = (Graphics2D)g;
      int w   = this.getWidth();
      int h   = this.getHeight();
      Font font = new Font("Helvetica", weight, 12);
      g2.setFont(font);
      if (this.value.startsWith("-", 1)) {
        g2.setColor(red);
      } else {
        g2.setColor(black);
      }
      g2.drawString(this.value, (w/2)+this.wr, (h/2)+this.hr);
      if (this.winner) {
        Font f = new Font("Helvetica", Font.ITALIC, 24);
        g2.setFont(f);
        g2.setColor(Color.GREEN);
        g2.drawString("0", (w/2)+this.wr-5, (h/2)+this.hr+4);
      }
    }
  }
}
