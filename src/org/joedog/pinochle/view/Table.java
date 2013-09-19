package org.joedog.pinochle.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JLabel;

public class Table extends JPanel {
  private Color green = new Color(48,200,126);

  public Table() {
    this.setLayout(new PositionalLayoutManager());
    this.setBackground(green);
  }

  /**
   * Adds a component to the table
   * @param  Component the component to be added
   * @param  x         the x coordinate from the left side of the Table       
   * @param  y         the y coordinate from the top of the Table
   * @return void 
   */
  public void add(Component comp, int x, int y) {
    this.add(comp, new PositionalConstraints(x, y));
  }

  /**
   * Adds a component to the table
   * @param  Component the component to be added
   * @param  x         the x coordinate from the left side of the Table       
   * @param  y         the y coordinate from the top of the Table
   * @param  w         The preferred width of the component
   * @param  h         The preferred height of the component
   * @return void 
   */
  public void add(Component comp, int x, int y, int w, int h) {
    comp.setPreferredSize(new Dimension(w, h));
    this.add(comp, new PositionalConstraints(x, y));
  }
}

