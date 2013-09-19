package org.joedog.pinochle.view;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager2;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;

public class PositionalLayoutManager implements LayoutManager2 {
  private Map<Component, PositionalConstraints> mapConstraints;

  public PositionalLayoutManager() {
    mapConstraints = new HashMap<Component, PositionalConstraints>(25);
  }

  public PositionalConstraints getConstraintsFor(Component comp) {
    return mapConstraints.get(comp);
  }

  public void setConstraintsFor(Component comp, PositionalConstraints pc) {
    mapConstraints.put(comp, pc);
  }

  @Override
  public void addLayoutComponent(Component comp, Object constraints) {
    System.out.println("addLayoutComponent(Component comp, Object constraints)");
    if (constraints instanceof PositionalConstraints) {
      mapConstraints.put(comp, (PositionalConstraints) constraints);
    } else {
      throw new IllegalArgumentException("Constraints must be PositionalConstraints");
    }
  }

  @Override
  public Dimension maximumLayoutSize(Container target) {
    return preferredLayoutSize(target);
  }

  @Override
  public float getLayoutAlignmentX(Container target) {
    return 0.5f;
  }

  @Override
  public float getLayoutAlignmentY(Container target) {
    return 0.5f;
  }

  @Override
  public void invalidateLayout(Container target) {

  }

  @Override
  public void addLayoutComponent(String name, Component comp) {

  }

  @Override
  public void removeLayoutComponent(Component comp) {
    mapConstraints.remove(comp);
  }

  @Override
  public Dimension preferredLayoutSize(Container parent) {
    return parent.getSize();
  }

  @Override
  public Dimension minimumLayoutSize(Container parent) {
    return preferredLayoutSize(parent);
  }

  @Override
  public void layoutContainer(Container parent) {
    int width = parent.getWidth();
    int height = parent.getHeight();
    for (Component comp : parent.getComponents()) {
      PositionalConstraints con = mapConstraints.get(comp);
      if (con != null) {
        int x = (int)con.getX();
        int y = (int)con.getY();
        comp.setSize(comp.getPreferredSize());
        comp.setLocation(x, y);
      } else {
        comp.setBounds(0, 0, 0, 0);
      }
    }
  }
} // end of class!

