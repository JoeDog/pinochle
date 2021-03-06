package org.joedog.pinochle.view;
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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;

import org.joedog.pinochle.game.Hand;
import org.joedog.pinochle.game.Card;
import org.joedog.pinochle.game.Pinochle;
import org.joedog.pinochle.control.Game;

public class Table extends JPanel {
  private Game   control;
  private Color  green   = new Color(48,200,126);
  private String bidval  = "";
  private String bidder  = "";
  private boolean over   = false;
  private int   [] meld  = new int[] {0,0};
  private int   [] take  = new int[] {0,0};
  private int   [] hand  = new int[] {0,0};
  private int   [] total = new int[] {0,0};
  private String[] names = new String[4];
  private BufferedImage trump = null;
  private ScorePad pad;

  public Table(Game control) {
    this.control = control;
    this.setBackground(green);
    this.pad = new ScorePad(control);
    for (int i = 0; i < this.names.length; i++) {
      this.names[i] = "";
    }
  }

  /**
   * Sets the final bid for the hand which is displayed
   * in the upper left corner. 
   * <p>
   * @param  String  A string interpretation of a numeric bid
   * @return void
   */
  public void setBid(String bidval) {
    this.bidval = bidval;
  }

  public void setBidder(String bidder) {
    this.bidder = bidder;
  }

  /**
   * Sets the status text for the player at position;
   * primarily called by View after a ModelPropertyChange.
   * <p>
   * @param  int    Position of the player: NORTH,SOUTH,EAST,WEST 
   * @param  String The status text
   * @return void
   */
  public void setStatus(int position, String status) {
    this.names[position] = status;
  }

  public void over() {
    this.over = true;
  }

  public void reset() {
    this.over = false;
  }

  public void setTrumpImage(int suit) {
     if (suit < 0) { 
       this.trump = null;
       return;
     }
     this.trump = new TrumpImage(suit).getImage();
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    ArrayList<Card> cards = (ArrayList<Card>)control.getModelProperty("Cards"); 
    if (cards != null) { // assume nothing
      for (Card card : cards) {
        g.setFont(new Font("Tahoma", Font.BOLD, 12));
        g.drawString(this.names[Pinochle.NORTH], 310, 145);
        g.drawString(this.names[Pinochle.EAST],  590, 315);
        g.drawString(this.names[Pinochle.SOUTH], 330, 475);
        g.drawString(this.names[Pinochle.WEST],  60,  315);
        if (control.getModelIntProperty("Status") == Pinochle.AVOW) {
          if (card.melded()) {
            card.setFaceUp();
            g.drawImage(card.getImage(), card.getX(), card.getY(), null);    
          } 
        } else {
          if (card.isSelected()) {
            g.drawImage(card.getImage(), card.getX(), card.getY()-20, null);    
          } else {
            g.drawImage(card.getImage(), card.getX(), card.getY(), null);    
          }
        }
      }
    }
    if (this.bidval != null && bidval.length() > 0 && this.over != true) {
      g.setFont(new Font("Tahoma", Font.BOLD, 14));
      g.drawString(this.bidval, 60, 43);
    }
    if (this.trump != null && this.over != true) {
      g.drawImage(this.trump, 20, 20, null);
    }
    g.drawImage(this.pad, 20, 350, this);
    if (this.over) {
      g.setFont(new Font("Tahoma", Font.BOLD, 64));
      g.setColor(new Color(0, 153, 255));
      g.drawString("GAME OVER", 300, 300);
    }
  }
}

