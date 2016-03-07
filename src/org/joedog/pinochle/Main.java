package org.joedog.pinochle;
/**
 * Copyright (C) 2013-2016
 * Jeffrey Fulmer - <jeff@joedog.org>, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *--
 */

import java.awt.BorderLayout;
import java.awt.Dimension;

import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentAdapter;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.joedog.pinochle.control.Engine;
import org.joedog.pinochle.control.Game;
import org.joedog.pinochle.control.GameThread;
import org.joedog.pinochle.view.Menu;
import org.joedog.pinochle.view.View;
import org.joedog.pinochle.view.actions.GameActions;

import org.joedog.pinochle.game.Card;
import org.joedog.pinochle.game.Hand;
import org.joedog.pinochle.game.Pinochle;

public class Main extends JPanel implements MouseListener {
  private static final long serialVersionUID = -2666347118493388423L;
  private static View      view    = null;
  private static Engine    engine  = null;
  private static Game      control = null;
  private static Menu      menu;

  public Main(View panel) {
    super(new BorderLayout());
    panel.addMouseListener(this);
    this.addMouseListener(this);
  }

  private static void createAndShowGui(View view, Menu menu) {
    final JFrame frame = new JFrame("Pinochle");
    int [] point = control.getModelArrayProperty("MainPosition");
    int [] scope = control.getModelArrayProperty("MainDimension");
    if (point == null) {
      point = new int[] {10, 10}; 
    }
    if (scope == null) {
      scope = new int [] {1024, 666};
    }
    int dx = (scope[0]  < 1024) ? 1024 : scope[0];
    int dy = (scope[1]  < 690)  ?  690 : scope[1];
    JComponent   panel = new Main(view);
    panel.add(view, BorderLayout.CENTER);
    view.setFocusable(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setContentPane(panel);
    frame.setPreferredSize(new Dimension(dx, dy));
    frame.setSize(dx, dy);
    frame.setJMenuBar(menu);
    frame.pack();
    frame.setLocation(point[0], point[1]);
    frame.addComponentListener(new ComponentAdapter() {
      public void componentMoved(ComponentEvent e) {
        control.setModelProperty("MainPosition", "("+frame.getX()+","+frame.getY()+")");
      }
      public void componentResized(ComponentEvent e) {
        control.setModelProperty("MainDimension", "("+frame.getWidth()+","+frame.getHeight()+")");
      }
    });
    frame.setVisible(true);
  }

  public void mousePressed(MouseEvent e){
    int x = e.getX();
    int y = e.getY();
    //control.select(x, y);
  }
  public void mouseReleased(MouseEvent e){
    int x = e.getX();
    int y = e.getY();
    control.select(x, y);
    //control.slide(x, y);
  }
  public void mouseClicked(MouseEvent e) {}
  public void mouseEntered(MouseEvent e) {}
  public void mouseExited(MouseEvent e) {}

  public static void main(String [] args) {
    if (control == null) {
      control = new Game();
    }

    if (view == null) {
      view = new View(control);
    }

    if (menu == null) {
      menu = new Menu(new GameActions(control));
    }

    if (engine == null) {
      engine = new Engine(view);
    }

    control.setEngine(engine);
    control.addView(view);

    if (! control.getModelBooleanProperty("Headless")) {
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          createAndShowGui(view, menu);
        }
      });
    }

    while (true) {
      GameThread thread = new GameThread(control);
      control.addThread(thread);
      thread.start();
      while (thread.isAlive()) ;
    }
  } 
}
