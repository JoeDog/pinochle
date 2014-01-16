package org.joedog.pinochle.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import org.joedog.pinochle.game.*;

public class CardPanel extends JPanel {
  private int         id;
  private Card        card;
  private JPanel      setting;
  private JLabel      face;

  public CardPanel(JPanel setting, Card card) {
    this.setting = setting;
    this.card    = card;
    this.face    = new JLabel(card.getIcon());
    this.setLayout(new BorderLayout()); 
    this.add(face);
    this.setName(card.toString());
    this.id      = this.card.getId();
    if (! included()) {
      setting.add(this);
    }
  }

  /**
   * 
   */
  public boolean included() {
    for (Component c: this.setting.getComponents()) {
      if (c != null && this.id == ((CardPanel)c).id) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean equals(Object object) {
    boolean same = false;

    if (object != null && object instanceof CardPanel) {
      same = (this.id == ((CardPanel)object).id);
    }
    return same;
  }

  public Card getCard() {
    return this.card;
  }

  public int getSuit() {
    return card.getSuit();
  }

  public int getRank() {
    return card.getRank();
  }

  public int getId() {
    return card.getId();
  }

  public void select(boolean select) {
    this.card.select(select);
  }
}
