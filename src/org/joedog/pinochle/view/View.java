package org.joedog.pinochle.view;

import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.beans.PropertyChangeEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Action;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JDesktopPane;
import javax.swing.SwingUtilities;

import org.joedog.util.TextUtils;
import org.joedog.pinochle.game.Pinochle;
import org.joedog.pinochle.control.Game;
import org.joedog.pinochle.control.Constants;

public class View extends JDesktopPane implements Viewable {
  private Game      control;
  private int       width;
  private int       height;
  private Table     table;
  private JPanel    bbar;
  private StatusBar sbar;
  private JButton   passButton = null; 
  private JButton   meldButton = null; 
  private JButton   playButton = null; 

  public View (Game control) {
    this.control = control;
    this.table   = new Table(control);
    this.bbar    = new JPanel();
    this.sbar    = new StatusBar();
    this.build();
  }

  public void action() {
    table.repaint();
  }

  public void reset() {
    table.reset();
  }

  private void build() {
    this.setLayout(new GridBagLayout());
    bbar.setLayout(new FlowLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx =  0;
    gbc.gridy = 0;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.anchor = GridBagConstraints.NORTHWEST;
    gbc.weightx = gbc.weighty = 95;
    gbc.insets = new Insets(2, 2, 2, 2);
    this.add(table, gbc);
 
    gbc.gridy = 1;
    gbc.weightx = gbc.weighty = 3;
    gbc.insets  = new Insets(0,0,0,0);
    JButton pin = new JButton("Pinochle");
    pin.setEnabled(false);
    bbar.add(pin);
    this.add(bbar, gbc);

    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = 1;
    gbc.weightx = gbc.weighty = 2;
    this.add(sbar, gbc); 
  }   

  public void addPassButton() {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        if (passButton == null) {
          passButton = new JButton(new PassAction(control));
        }  
        passButton.setEnabled(false);
        bbar.removeAll();
        bbar.repaint();
        bbar.add(passButton);
        invalidate();
        validate();
      }
    });
    this.sbar.setMessage("Pass 3 cards to your partner...");
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
          meldButton = new JButton(new MeldAction(control));
        }  
        bbar.removeAll();
        bbar.repaint();
        bbar.add(meldButton);
        bbar.invalidate();
        bbar.validate();
      }
    });
    this.sbar.setMessage("Select cards to meld...");
  }

  public void addPlayButton() {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        if (playButton == null) {
          playButton = new JButton(new PlayAction(control));
        }  
        playButton.setEnabled(true);
        bbar.removeAll();
        bbar.repaint();
        bbar.add(playButton);
        invalidate();
        validate();
      }
    });
    this.sbar.setMessage("Press 'Play' to start the hand...");
  }

  public void modelPropertyChange(PropertyChangeEvent e) {
    if (e.getNewValue() == null) return;
    if (e.getPropertyName().equals(Constants.MESSAGE)) {
      this.sbar.setMessage((String)e.getNewValue());
    }
    if (e.getPropertyName().equals(Constants.BID)) {
      this.table.setBid((String)e.getNewValue());
    }
    if (e.getPropertyName().equals(Constants.BIDDER)) {
      this.table.setBidder((String)e.getNewValue());
    }
    if (e.getPropertyName().equals(Constants.BIDDER)) {
      String tmp   = (String)e.getNewValue(); 
      int    trump = Integer.parseInt(tmp);
      this.table.setTrumpImage(trump);
    }
    if (e.getPropertyName().equals(Constants.NORTH)) {
      this.table.setStatus(Pinochle.NORTH, (String)e.getNewValue());
    }
    if (e.getPropertyName().equals(Constants.EAST)) {
      this.table.setStatus(Pinochle.EAST,  (String)e.getNewValue());
    }
    if (e.getPropertyName().equals(Constants.SOUTH)) {
      this.table.setStatus(Pinochle.SOUTH, (String)e.getNewValue());
    }
    if (e.getPropertyName().equals(Constants.WEST)) {
      this.table.setStatus(Pinochle.WEST,  (String)e.getNewValue());
    }
    if (e.getPropertyName().equals(Constants.WINNER)) {
      this.table.over();
    }
  }

  private class PassAction extends AbstractAction {
    private Game control;

    public PassAction(Game control) {
      putValue(NAME, "Pass");
      this.control = control;
    }

    public void actionPerformed(ActionEvent ae) {
      this.control.setPassable(true);
    } 
  }

  private class MeldAction extends AbstractAction {
    private Game control;

    public MeldAction(Game control) {
      putValue(NAME, "Meld");
      this.control = control;
    }

    public void actionPerformed(ActionEvent ae) {
      this.control.setMeldable(true);
    } 
  }

  private class ResetAction extends AbstractAction {
    private Game control;

    public ResetAction(Game control) {
      putValue(NAME, "Reset");
      this.control = control;
    }

    public void actionPerformed(ActionEvent ae) {
      control.newGame();
    } 
  }

  private class PlayAction extends AbstractAction {
    private Game control;

    public PlayAction(Game control) {
      putValue(NAME, "Play");
      this.control = control;
    }

    public void actionPerformed(ActionEvent ae) {
      this.control.pause(false);
    } 
  }
}
