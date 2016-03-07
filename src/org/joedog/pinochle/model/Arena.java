package org.joedog.pinochle.model;
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

import java.util.HashMap;
import java.util.ArrayList;
import javax.swing.event.ListDataEvent;

import org.joedog.pinochle.control.Constants;
import org.joedog.util.NumberUtils;
import org.joedog.pinochle.game.Card;
import org.joedog.pinochle.game.Hand;
import org.joedog.pinochle.game.Pinochle;

public class Arena extends AbstractModel {
  private int    cols;
  private int    rows;
  private int    width;
  private int    height;
  private int    csize;
  private String message;
  private int    status = Pinochle.DEAL;

  private ArrayList<Card> trick = new ArrayList<Card>(); 
  private ArrayList<Card> last  = new ArrayList<Card>();
  private HashMap<Integer, Hand> hands = new HashMap<Integer, Hand>();
  private static Arena  _instance = null;
  private static Object mutex     = new Object();
  private Location nt = new Location(410,195);
  private Location st = new Location(435,235);
  private Location et = new Location(460,215);
  private Location wt = new Location(385,215);
  private int lastX   = 700;
  private int lastY   = 400;
  

  private Arena() {
    this.width  = 1024;
    this.height = 690;
    this.csize  = (this.width  / 32);
    this.cols   = (this.width  / this.csize);
    this.rows   = (this.height / this.csize);
    this.addHand(Pinochle.NORTH, new Hand());
    this.addHand(Pinochle.SOUTH, new Hand());
    this.addHand(Pinochle.EAST,  new Hand());
    this.addHand(Pinochle.WEST,  new Hand());
  }

  public synchronized static Arena getInstance() {
    if (_instance == null) {
      synchronized(mutex) {
        if (_instance == null) {
          _instance = new Arena();
        }
      }
    }
    return _instance;
  }

  public void save() {}

  /**
   * Arena subscribes to status for internal purposes. 
   * It's getter is private
   * <p>
   * @param  String
   * @return
   */
  public void setStatus(String status) {
    int s;
    if (! NumberUtils.isNumeric(status)) {
      this.status = Pinochle.DEAL;
      return;
    }

    s = Integer.parseInt(status);
    if (s < Pinochle.DEAL || s > Pinochle.OVER) {
      this.status = Pinochle.DEAL;
      return;
    }
    this.status = Integer.parseInt(status);
  }

  private int getStatus() {
    return this.status;
  }

  /**
   * Status the status message and fires a property change
   * <p>
   * @param  String  the status message
   * @return void
   */
  public void setMessage(String message) {
    this.message = message;
    firePropertyChange(Constants.MESSAGE, "message", message);
  }

  /**
   * Returns a status message
   * <p>
   * @param  void
   * @return String  The status message
   */
  public String getMessage() {
    return this.message;
  }

  public synchronized void select(int x, int y) {
    ArrayList<Card> south = (ArrayList<Card>)((this.getHand(Pinochle.SOUTH)).getHand());
    if (this.getStatus() == Pinochle.PLAY) {
      for (Card card : south) {
        if (! card.isSelected(x, y)) {
          // we can only select one card at a time in play. 
          // So if this is not the clicked card it goes false
          card.select(false);
        }
      }
    } 
    for (Card card : south) {
      if (card.isSelected(x, y)) {
        card.select(! card.isSelected());
        break;
      }
    }
    return;
  }

  /**
   * Returns the grid column count in integers
   * <p>
   * @param  none
   * @return int
   */
  public int getCols() {
    return this.cols;
  }

  /**
   * Returns the grid row count in integers
   * <p>
   * @param  none
   * @return int
   */
  public int getRows() {
    return this.rows;
  }

  /**
   * Returns the width of the grid in pixels
   * <p>
   * @param  none
   * @return int
   */
  public int getWidth() {
    return this.width;
  }

  /**
   * Returns the height of the grid in pixels
   * <p>
   * @param  none
   * @return int
   */
  public int getHeight() {
    return this.height;
  }

  /**
   * Returns the cell size of the grid in pixels
   * <p>
   * @param  none
   * @return int
   */
  public int getCellSize() {
    return this.csize;
  }

  /**
   * Add a hand to the Area at a given position
   * <p>
   * @param  int   The player's position at the table
   * @param  Hand  The player's hand
   * @return void
   */
  public void addHand(int position, Hand hand) {
    hand.addListDataListener(new HandChangeListener());
    switch (position) {
      // XXX: need to make these dynamic once we have our size
      case Pinochle.NORTH:
        hand.setLayout(new Location(320,30), 372);
        break;
      case Pinochle.EAST:
        hand.setLayout(new Location(580,200), 372);
        break;
      case Pinochle.SOUTH:
        hand.setLayout(new Location(320,360), 372);
        break;
      case Pinochle.WEST:
        hand.setLayout(new Location(50,200), 372);
        break;
    }
    this.hands.put(position, hand);
  }

  /**
   * Given a position on the table return the Hand
   * <p>
   * @param  int   The player's position at the table
   * @return Hand  The player's hand
   */
  public Hand getHand(int position) {
    return this.hands.get(position);
  }

  /**
   * Position the card in the play area 
   * and ensure that it's lying face up.
   * <p>
   * @param  int  The player's position at the table
   * @param  Card The card we're moving to the play area
   */
  public void play(int position, Card card) {
    switch(position) {
      case Pinochle.NORTH: 
        card.setLocation(nt); 
        card.setFaceUp();
        break;
      case Pinochle.SOUTH: 
        card.setLocation(st);
        card.setFaceUp(); 
        break;
      case Pinochle.EAST:
        card.setLocation(et);
        card.setFaceUp();
        break;
      case Pinochle.WEST:
        card.setLocation(wt);
        card.setFaceUp();
        break;
    }
    trick.add(card);
  }

  /**
   * Clears the completed trick from the play 
   * area and repositions it in the take area
   * <p>
   * @param  none
   * @return void
   */
  public void clearTrick() {
    if (trick != null && trick.size() == 4) {
      int newX = this.lastX;
      int newY = this.lastY;
      last.clear();
      for (Card c : this.trick) {
        c.setLocation(newX, newY);
        last.add(c);
        newX += 20;
        newY += 10;
      }
      trick.clear();
    }
  }

  /**
   * Returns all the cards that are displayed in the Arena
   * <p>
   * @param  none
   * @return ArrayList
   * @see    java.util.ArrayList
   */
  public ArrayList<Card> getCards() {
    ArrayList<Card> cards = new ArrayList<Card>();
    if (this.getStatus() != Pinochle.PLAY) {
      if (trick != null) trick.clear();
      if (last  != null) last.clear();
    }

    for(int key : this.hands.keySet()){
      hands.get(key).relocate(); // XXX
      cards.addAll(this.hands.get(key).getCards());
    }
    if (this.trick != null) cards.addAll(this.trick);
    if (this.last  != null) cards.addAll(this.last);
    return cards;
  }

  private class HandChangeListener implements javax.swing.event.ListDataListener {
    public void intervalAdded(ListDataEvent e)   {}

    public void intervalRemoved(ListDataEvent e) {}

    public void contentsChanged(ListDataEvent e) {
      for (int key : hands.keySet()) {
        hands.get(key).relocate();
      }
    }
  }
}
