package org.joedog.pinochle.view;

import java.awt.Color;
import java.awt.Container;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class StatusBar extends JPanel implements LayoutManager {
  private   int     gap;
  private   int     pad;
  private   String  message; 
  protected HashMap constraints;

  public StatusBar() {
    this.setLayout(this);
    this.gap = 10;
    this.pad =  8;
    this.setPreferredSize(new Dimension(490, 24));
    this.constraints = new HashMap(8);
    this.message     = new String("Ready...");
  }
  
  public void setMessage (String message) {
    this.message = message;
    this.repaint();
  } 

  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    int y = 0;
    g.setColor(new Color(156, 154, 140));
    g.drawLine(0, y, getWidth(), y);
    y++;
    g.setColor(new Color(196, 194, 183));
    g.drawLine(0, y, getWidth(), y);
    g.setColor(Color.BLACK);
    g.setFont(new Font("Helvetica", Font.PLAIN, 10));
    g.drawString(message, pad, getHeight()-4);
  }

  public void layoutContainer(Container container) {
    Insets insets = container.getInsets();
    int top       = insets.top;
    int bottom    = container.getSize().height - insets.bottom;
    int right     = container.getSize().width - insets.right;
    int left      = right;
    int height    = bottom - top;
    int count     = container.getComponentCount();
    Dimension d   = new Dimension(0,0);
    for (int i = 0; i < count; i++) {
      Component comp = container.getComponent(i);
      Dimension dim  = new Dimension(0,0);
      if (comp.isVisible()) {
        dim = comp.getPreferredSize();
        int w = dim.width;
        int h = dim.height;
        int o = height - h; // offset
        left -= (w + gap);
        right = left+w;
        comp.setBounds(left, top + o, w, h);
      }
    } 
  }

  public Dimension preferredLayoutSize(Container container) {
    return (new Dimension(0,0));
  }

  public Dimension minimumLayoutSize(Container container) {
    return (new Dimension());  // constructor sets to (0, 0)
  }
 
  public void addLayoutComponent(String name, Component comp) {
    constraints.put(comp, name);
  }

  public void removeLayoutComponent(Component comp) {
    constraints.remove(comp);
  }
}
