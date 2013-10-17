package org.joedog.pinochle.view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import org.joedog.pinochle.view.OverlapLayout;
import org.joedog.pinochle.game.*;
import org.joedog.pinochle.control.GameController;

public class Setting extends JPanel implements MouseListener {
  private String         name;
  private Hand           hand       = null;
  private Card           card       = null;
  private int            size       = 0;
  private OverlapLayout  layout     = null;
  private GameController controller = null;
  private JPanel setting = null;
  private JLabel notice  = null;

  public Setting(GameController controller) {
    this.name = "";
    this.controller = controller;
    this.setLayout(new BorderLayout());
    this.setBackground(new Color(48,200,126));
    this.notice = new JLabel("");
    this.add(notice, BorderLayout.SOUTH);
    Point overlap = new Point(20,0);
    layout = new OverlapLayout(overlap, true);
    Insets popupInsets = new Insets(20, 0, 0, 0);
    layout.setPopupInsets(popupInsets);
    layout.setIncludeInvisible(true);
    createPanel();
  }

  public void display(Hand hand) {
    this.hand = hand;
    createPanel();
  }

  public void setText(final String text) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        notice.setText(name+" -- "+text);
      }
    });
  }

  public void refresh() {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        for (Component c: setting.getComponents()) {
          c.invalidate();
          c.validate();
          c.repaint();
        }
        setting.invalidate();
        setting.validate();
        setting.repaint();
      }
    });
  }

  public void refresh(Hand hand) {
    int status = this.controller.gameStatus();
    if (status == GameController.MELD) {
      Hand tmp = new Hand();
      for (Card c: hand.getCards()) {
        if (c.melded()/* || c.isFaceUp()*/) {
          tmp.add(c);
        }
      }
      this.hand = tmp;
    } else {
      this.hand = hand;
    }
    this.setting.removeAll();
    this.setting = null;
    createPanel();
  }
  
  private void createPanel() {
    if (setting == null) {
      setting = new JPanel(layout);
    }
    setting.setBackground(new Color(48,200,126));
    if (this.hand != null) {
      hand.sort();
      for (Card card: hand.getCards()) {
        if (card.melded() == true) {
          card.setFaceUp();
        }
        CardPanel cp = new CardPanel(setting, card);
        cp.addMouseListener(this);
      }
    } 
    try {
      int index = layout.convertIndex(1);
      int count = setting.getComponentCount();
      if (index < 1) {
        return;
      }
      index = (count == 1) ? 0 : index; // kludge
      Component c = setting.getComponent(index);
      c.invalidate();
      c.validate();
      layout.addLayoutComponent(c, OverlapLayout.POP_DOWN);
    } catch (java.lang.ArrayIndexOutOfBoundsException ex){ 
      /* error! bad input */
      ex.printStackTrace();
    }
    setting.revalidate();
    this.add(setting, BorderLayout.NORTH);
    this.revalidate();
  }

  public void setName(String name) {
    this.name = name;
    this.refresh();
  }

  public Card getCard() {
    return this.card;
  }

  public Dimension size() {
    return new Dimension(400, 400);
  }

  public void mousePressed(MouseEvent e) {
    int status   = this.controller.gameStatus();
    this.refresh();
    CardPanel cp = (CardPanel)e.getComponent();
    if (status < GameController.PLAY) {
      cardAction(cp);
    }
    if (status == GameController.PLAY) {
      cardAction(cp);
      this.card = cp.getCard();
      this.controller.setPlayable(true);
    }
  }

  public void mouseMoved(MouseEvent e) {}
  public void mouseClicked(MouseEvent e) {}
  public void mouseEntered(MouseEvent e) {}
  public void mouseExited(MouseEvent e) {}
  public void mouseReleased(MouseEvent e) {}


  private void cardAction(final CardPanel cp) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        Boolean constraint = layout.getConstraints(cp);
        try {
          if (constraint == null || constraint == OverlapLayout.POP_DOWN) {
            layout.addLayoutComponent(cp, OverlapLayout.POP_UP);
            cp.select(true);
          } else {
            layout.addLayoutComponent(cp, OverlapLayout.POP_DOWN);
            cp.select(false);
          }
        } catch (java.lang.ArrayIndexOutOfBoundsException ex){ /* error! bad input to the function*/
          System.err.println(ex.toString());
        }
        cp.getParent().invalidate();
        cp.getParent().validate();
        revalidate();
      }
    });
  }
}

