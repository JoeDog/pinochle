package org.joedog.pinochle.view;

import org.joedog.pinochle.controller.*;
import org.joedog.pinochle.view.actions.*;
import org.joedog.pinochle.util.*;
import org.joedog.pinochle.player.*;
import org.joedog.pinochle.game.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Canvas;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.io.File;
import java.net.URL;
import javax.imageio.*;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.Action;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

public class GameView extends JFrame implements View, MouseListener {
  private JPanel         main        = new JPanel();
  private JPanel         table       = new JPanel();
  private JPanel         bottom      = new JPanel();
  private JPanel         buttons     = new JPanel();
  private JPanel         msgbox;
  private JLabel         trump       = null;
  private JLabel[]       spacer      = new JLabel[3];
  public  Setting[]      setting     = new Setting[4]; 
  private StatusBar      status      = new StatusBar(); 
  private Action         resetAction = new ResetAction();
  private MenuView       menu;
  private GameController controller;
  private GameActions    actions;
  private int width;
  private int height;

  public GameView (GameController controller) {
    super("Pinochle");
    this.controller = controller;
    this.actions    = new GameActions(this.controller);
    this.menu       = new MenuView(this.actions);
  }

  public void setStatus(String message) {
    status.setMessage(message);
  }

  public void setTrumpLabel() {
    if (trump == null) {
      trump = new JLabel(" * ");
    }
  }

  public void display() {
    int id = 0;
    this.setPreferredSize(new Dimension(1024,638));
    BorderLayout bl = new BorderLayout();
    GridLayout   gl = new GridLayout(3, 3);
    FlowLayout   fl = new FlowLayout();
    for (int i=0; i < 3; i++)
      spacer[i] = new JLabel("    ");
    gl.setVgap(1);
    gl.setHgap(1);
    table.setLayout(null);
    table.setBackground(new Color(48,200,126));
    table.add(getMsgBox(), null);
    table.add(getSetting(Pinochle.NORTH), null);
    table.add(getSetting(Pinochle.EAST),  null);
    table.add(getSetting(Pinochle.SOUTH), null);
    table.add(getSetting(Pinochle.WEST),  null);
    buttons.setLayout(fl);
    buttons.add(new JButton(resetAction));
    bottom.setLayout(new BorderLayout());
    bottom.add(buttons, java.awt.BorderLayout.CENTER);
    bottom.add(status,  java.awt.BorderLayout.SOUTH);
    main.setLayout(bl);
    main.add(spacer[0], BorderLayout.NORTH);
    main.add(spacer[1], BorderLayout.EAST);
    main.add(spacer[2], BorderLayout.WEST);
    main.add(table,     BorderLayout.CENTER);
    main.add(bottom,    BorderLayout.SOUTH);

    Dimension dim  = Toolkit.getDefaultToolkit().getScreenSize();
    this.getContentPane().add(main, BorderLayout.CENTER);
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    this.setJMenuBar(menu);
    this.pack();
    int x = controller.getIntProperty("MainX");
    int y = controller.getIntProperty("MainY");
    if (x == 0 && y == 0) {
      int w = this.getSize().width;
      int h = this.getSize().height;
      x = (dim.width-w)/2;
      y = (dim.height-h)/2;
    }
    this.setLocation(x, y);
    this.setVisible(true);
  }

  public JPanel getMsgBox() {
    if (trump == null) {
      trump = new JLabel("", null, JLabel.CENTER);
    }
    if (msgbox == null) {
      msgbox = new JPanel();
      msgbox.setLayout(new FlowLayout());
      msgbox.setBounds(new Rectangle (0, 0, 84, 42));
      msgbox.setBackground(new Color(48,200,126));
      msgbox.add(trump);
    }
    return msgbox;
  }

  public Setting getSetting(int position) {
    if (setting[position] == null) {
      switch (position) {
        case Pinochle.NORTH:
          setting[position] = new Setting(this.controller);
          // x- from the left, y- from the top, w- width, h- height
          setting[position].setBounds(new Rectangle(300, 10, 450, 132));
          setting[position].show();
          break;
        case Pinochle.SOUTH:
          setting[position] = new Setting(this.controller);
          // x- from the left, y- from the top, w- width, h- height
          setting[position].setBounds(new Rectangle(300, 350, 420, 132));
          break;
        case Pinochle.EAST:
          setting[position] = new Setting(this.controller);
          // x- from the left, y- from the top, w- width, h- height
          setting[position].setBounds(new Rectangle(580, 190, 420, 132));
          break;
        case Pinochle.WEST:
          setting[position] = new Setting(this.controller);
          // x- from the left, y- from the top, w- width, h- height
          setting[position].setBounds(new Rectangle(20, 190, 420, 132));
          break;
      }
    }
    return setting[position];
  }

  public void close() {
    this.controller.setProperty("MainX",  Integer.toString(this.getX()));
    this.controller.setProperty("MainY",  Integer.toString(this.getY()));
    this.controller.save();
    this.setVisible(false);
  }
  
  public void mousePressed (MouseEvent e) {
    int id;
  }

  public void mouseReleased (MouseEvent e) {}
  public void mouseEntered (MouseEvent e) {}
  public void mouseExited (MouseEvent e) {}
  public void mouseClicked (MouseEvent e) { }

  public void modelPropertyChange(PropertyChangeEvent e) {
    if (e.getNewValue() == null) return;
    if (e.getPropertyName().equals(controller.TRUMP)) {
      trump.setText(controller.getProperty("GameBid"));
      if (e.getNewValue().equals("0")) {
        System.out.println("Setting HEARTS");
        trump.setIcon(new TrumpIcon(Pinochle.HEARTS));
      }
      if (e.getNewValue().equals("1")) {
        System.out.println("Setting CLUBS");
        trump.setIcon(new TrumpIcon(Pinochle.CLUBS));
      }
      if (e.getNewValue().equals("2")) {
        System.out.println("Setting DIAMONDS");
        trump.setIcon(new TrumpIcon(Pinochle.DIAMONDS));
      }
      if (e.getNewValue().equals("3")) {
        System.out.println("Setting SPADES");
        trump.setIcon(new TrumpIcon(Pinochle.SPADES));
      }
    }
  }

  private class ResetAction extends AbstractAction {
    public ResetAction() {
      putValue(NAME, "Reset");
      putValue(SHORT_DESCRIPTION, "Reset Game");
      putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_ENTER));
    }
    public void actionPerformed(ActionEvent ae) {
      //if (controller.isGameOver()) {
      //  controller.newGame();
      //  return;
      //}
    }
  }
}
