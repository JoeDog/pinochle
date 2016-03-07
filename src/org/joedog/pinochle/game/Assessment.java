package org.joedog.pinochle.game;
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

public class Assessment {
  private int suit;
  private int meld;
  private int aces;
  private int taces;
  private int index;
  private int trump;
  private int power;
  private int [] hearts;
  private int [] clubs;
  private int [] diamonds;
  private int [] spades;
  private final static int limit = 3; // this should be provided by the controller
  
  public Assessment() {
    hearts   = new int[limit];
    clubs    = new int[limit];
    diamonds = new int[limit];
    spades   = new int[limit];
  }

  public int getMeld() {
    return this.meld;
  }

  public int getTrumpCount() {
    return this.trump;
  }

  public int getTrumpAces() {
    return this.taces;
  }

  public void setSuit(int suit) {
    this.suit = suit;
  } 

  public void setMeld(int meld) {
    this.meld = meld;
  }

  public void setAces(int aces) {
    this.aces = aces;
  }

  public int getAces() {
    return this.aces;
  }

  public void setTrumpCount(int trump) {
    this.trump = trump;
  }

  public void setPower(int power) {
    this.power = power;
  }

  public void setTrumpAces(int aces) {
    this.taces = aces;
  }

  public int getTrump() {
    return this.suit;
  }

  public int getPower() {
    return this.power;
  }

  public int maxBid() {
    int bid = this.meld;
    bid += this.aces*2;
    bid += (4-this.trump>0) ? (4-this.trump)*2 : 2;
    bid += 3;
    bid += this.power;
    return bid;
  }
  
  public void add(int index, int suit) {
    switch (suit) {
      case Pinochle.HEARTS:
        if (hearts.length < limit) hearts[hearts.length-1] = index;
        break;
      case Pinochle.CLUBS:
        if (clubs.length < limit) clubs[clubs.length-1] = index;
        break;
      case Pinochle.DIAMONDS:
        if (diamonds.length < limit) diamonds[diamonds.length-1] = index;
        break;
      case Pinochle.SPADES:
        if (spades.length < limit) spades[spades.length-1] = index;
        break;
    }
    return;
  }

  public String toString() {
    String suit = "";
    switch (this.suit) {
      case Pinochle.HEARTS:
        suit = "Hearts";
        break;
      case Pinochle.CLUBS:
        suit = "Clubs";
        break;
      case Pinochle.DIAMONDS:
        suit = "Diamonds";
        break;
      case Pinochle.SPADES:
        suit = "Spades";
        break;
    }
    return "Trump: "+suit+"; Meld: "+this.meld+"; Trump Aces: "+this.taces+"; Trump cards: "+this.trump;
  }
}
