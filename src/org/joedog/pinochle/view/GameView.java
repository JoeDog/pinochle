package org.joedog.pinochle.view;

import org.joedog.pinochle.control.*;
import org.joedog.pinochle.model.*;
import org.joedog.pinochle.view.actions.*;
import org.joedog.pinochle.player.*;
import org.joedog.pinochle.game.*;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.Action;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.WindowConstants;
import javax.swing.SwingUtilities;

public class GameView extends JPanel implements View, MouseListener {
  private JPanel         main        = new JPanel();
  private Table          table       = new Table();
  private JPanel         bottom      = new JPanel();
  private JPanel         buttons     = new JPanel();
  private JPanel         msgbox;
  private JLabel         trump       = null;
  private JLabel[]       spacer      = new JLabel[3];
  public  Setting[]      setting     = new Setting[4]; 
  public  TrickArea      trick       = null;
  public  LastTrick      last        = null;
  private StatusBar      status      = new StatusBar(); 
  private ScorePad       pad         = null;
  private JButton        passButton  = null;
  private JButton        meldButton  = null;
  private JButton        playButton  = null;
  private GameController controller;
  private int width;
  private int height;

  public GameView (GameController controller) {
    this.controller = controller;
  }

  public void setStatus(String message) {
    status.setMessage(message);
  }

  public void setTrumpLabel() {
    if (trump == null) {
      trump = new JLabel(" * ");
    }
  }

  public Container createContentPane() {
    int id = 0;
    for (int i=0; i < 3; i++)
      spacer[i] = new JLabel("    ");
    if (main  == null) {
      main = new JPanel();
    }
    if (trick == null) {
      trick = new TrickArea(this.controller);
      this.controller.addView(trick);
    }
    if (last == null) {
      last = new LastTrick(this.controller);
      this.controller.addView(last);
    }

    table.add(getMsgBox(), 0, 0, 84, 42);
    table.add(getSetting(Pinochle.NORTH), 290, 10,  355, 132);
    table.add(getSetting(Pinochle.EAST),  570, 180, 355, 132);
    table.add(getSetting(Pinochle.SOUTH), 290, 340, 355, 132);
    table.add(getSetting(Pinochle.WEST),   20, 180, 355, 132);
    table.add(trick, 380, 180, 180, 155);
    table.add(last,  650, 350, 200, 155);
    table.add(getScorePad(),  4, 350, 240, 130);
    buttons.setLayout(new FlowLayout());
    bottom.setLayout(new BorderLayout());
    bottom.add(buttons, java.awt.BorderLayout.CENTER);
    bottom.add(status,  java.awt.BorderLayout.SOUTH);
    main.setLayout(new BorderLayout());
    main.add(spacer[0], BorderLayout.NORTH);
    main.add(spacer[1], BorderLayout.EAST);
    main.add(spacer[2], BorderLayout.WEST);
    main.add(table,     BorderLayout.CENTER);
    main.add(bottom,    BorderLayout.SOUTH);
    return main;
  }

  public JPanel getMsgBox() {
    if (trump == null) {
      trump = new JLabel("", null, JLabel.CENTER);
    }
    if (msgbox == null) {
      msgbox = new JPanel();
      msgbox.setLayout(new FlowLayout());
      msgbox.setBackground(new Color(48,200,126));
      msgbox.add(trump);
    }
    return msgbox;
  }

  public ScorePad getScorePad() {
    if (this.pad == null) {
      this.pad = new ScorePad(this.controller);
    }
    this.controller.addView(this.pad);
    return this.pad;
  }

  public Setting getSetting(Integer position) {
    return this.getSetting((int)position);
  }

  public Setting getSetting(int position) {
    if (setting[position] == null) {
      switch (position) {
        case Pinochle.NORTH:
          setting[position] = new Setting(this.controller);
          //setting[position].setBackground(Color.yellow);
          setting[position].setName(controller.getName(position));
          setting[position].show();
          break;
        case Pinochle.SOUTH:
          setting[position] = new Setting(this.controller);
          setting[position].setName(controller.getName(position));
          //setting[position].setBackground(Color.yellow);
          break;
        case Pinochle.EAST:
          setting[position] = new Setting(this.controller);
          setting[position].setName(controller.getName(position));
          //setting[position].setBackground(Color.yellow);
          break;
        case Pinochle.WEST:
          setting[position] = new Setting(this.controller);
          setting[position].setName(controller.getName(position));
          //setting[position].setBackground(Color.yellow);
          break;
      }
    }
    return setting[position];
  }

  public void addPassButton() {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        if (passButton == null) {
          passButton = new JButton(new PassAction(controller));
        }  
        passButton.setEnabled(false);
        buttons.add(passButton);
        invalidate();
        validate();
        repaint(); 
        main.revalidate();
        main.repaint();
        for (int i = 0; i < setting.length; i++) {
          setting[i].refresh();
        }
      }
    });
    this.setStatus("Pass 3 cards to your partner...");
  }

  public void enablePassButton() {
    if (SwingUtilities.isEventDispatchThread()) {
      passButton.setEnabled(true);
    } else {
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          passButton.setEnabled(true);
        }
      });
    }
  }

  public void disablePassButton() {
    if (SwingUtilities.isEventDispatchThread()) {
      passButton.setEnabled(false);
    } else {
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          passButton.setEnabled(false);
        }
      });
    }
  }

  public void addMeldButton() {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        if (meldButton == null) {
          meldButton = new JButton(new MeldAction(controller));
        }  
        buttons.removeAll();
        buttons.add(meldButton);
        for (int i = 0; i < setting.length; i++) {
          setting[i].refresh();
        }
        buttons.invalidate();
        buttons.validate();
        buttons.repaint();
      }
    });
    this.setStatus("Select cards to meld...");
  }

  public void addPlayButton() {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        if (playButton == null) {
          playButton = new JButton(new PlayAction(controller));
        }  
        buttons.removeAll();
        buttons.add(playButton);
        for (int i = 0; i < setting.length; i++) {
          setting[i].refresh();
        }
        buttons.invalidate();
        buttons.validate();
        buttons.repaint();
      }
    });
    this.setStatus("Push 'Play' to begin the hand...");
  }

  public synchronized void resetButtons() {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        buttons.removeAll();
        buttons.invalidate();
        buttons.validate();
        buttons.repaint();
      }
    });
    for (int i = 0; i < setting.length; i++) {
      setting[i].refresh();
    }
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
      System.out.println("controller.TRUMP! "+e.getPropertyName());
      trump.setText(controller.getProperty("GameBid"));
      if (e.getNewValue().equals("0")) {
        trump.setIcon(new TrumpIcon(Pinochle.HEARTS));
      }
      if (e.getNewValue().equals("1")) {
        trump.setIcon(new TrumpIcon(Pinochle.CLUBS));
      }
      if (e.getNewValue().equals("2")) {
        trump.setIcon(new TrumpIcon(Pinochle.DIAMONDS));
      }
      if (e.getNewValue().equals("3")) {
        trump.setIcon(new TrumpIcon(Pinochle.SPADES));
      }
    }
    if (e.getPropertyName().equals(controller.OURS)) {
      System.out.println("controller.OURS! "+e.getPropertyName());
      System.out.println("Cher and Jeff now have: "+e.getNewValue());
    }
    if (e.getPropertyName().equals(controller.THEIRS)) {
      System.out.println("controller.THEIRS! "+e.getPropertyName());
      System.out.println("Limey and Pommie now have: "+e.getNewValue());
    }
  }

  private class PassAction extends AbstractAction {
    private GameController controller;

    public PassAction(GameController controller) {
      putValue(NAME, "Pass");
      this.controller = controller;
    }

    public void actionPerformed(ActionEvent ae) {
      this.controller.setPassable(true);
    } 
  }

  private class MeldAction extends AbstractAction {
    private GameController controller;

    public MeldAction(GameController controller) {
      putValue(NAME, "Meld");
      this.controller = controller;
    }

    public void actionPerformed(ActionEvent ae) {
      this.controller.setMeldable(true);
    } 
  }

  private class PlayAction extends AbstractAction {
    private GameController controller;

    public PlayAction(GameController controller) {
      putValue(NAME, "Play");
      this.controller = controller;
    }

    public void actionPerformed(ActionEvent ae) {
      this.controller.pause(false);
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
