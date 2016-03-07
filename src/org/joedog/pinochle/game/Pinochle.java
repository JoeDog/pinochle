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

public final class Pinochle {
  public final static int NORTH      = 0;
  public final static int EAST       = 1;
  public final static int SOUTH      = 2;
  public final static int WEST       = 3;
  public final static int [] PLAYERS = new int[] {NORTH, SOUTH, EAST, WEST};

  public final static int HEARTS     = 0;
  public final static int CLUBS      = 1;
  public final static int DIAMONDS   = 2;
  public final static int SPADES     = 3;

  public final static int ACE        = 14;
  public final static int TEN        = 13;
  public final static int KING       = 12;
  public final static int QUEEN      = 11;
  public final static int JACK       = 10;
  public final static int NINE       = 9;

  public final static int DEAL       = 0;  // Deal the cards
  public final static int BID        = 1;  // Bid on the hands
  public final static int PASS       = 2;  // Winners pass cards
  public final static int MELD       = 3;  // Select cards for meld
  public final static int AVOW       = 4;  // Display cards for meld
  public final static int PLAY       = 5;  // Play the hand
  public final static int DONE       = 6;  // The hand is completed
  public final static int OVER       = 7;  // GAME OVER

 
  public final static String suit(int suit) {
    switch(suit) {
      case CLUBS:
        return "C";
      case SPADES:
        return "S";
      case HEARTS:
        return "H";
      case DIAMONDS:
        return "D";
    }
    return null;
  }

  public final static String suitname(int suit) {
    switch(suit) {
      case CLUBS:
        return "Clubs";
      case SPADES:
        return "Spades";
      case HEARTS:
        return "Hearts";
      case DIAMONDS:
        return "Diamonds";
    }
    return null;
  }

  public final static String rank(int rank) {
    switch(rank) {
      case ACE:
        return "A";
      case TEN:
        return "10";
      case KING:
        return "K";
      case QUEEN: 
        return "Q";
      case JACK:
        return "J";
      case NINE:
        return "9";
    }
    return null;
  }

  /**
   * Returns the player's seated position as String
   * <p>
   * @param  int    Pinochle.POSITION
   * @return String Name of the position
   */
  public final static String position(int position) {
    switch (position) {
      case NORTH:
        return "north";
      case EAST:
        return "east";
      case SOUTH:
        return "south";
      case WEST:
        return "west";
    }
    return null;
  }

  /**
   * Programmer's convenience This method helps create
   * a string interpretation of a hand that we can store
   * inside our memory banks....
   */
  public final static String store (int rank, int suit) {
    switch(rank) {
      case ACE:
        return "A";
      case TEN:
        return "T";
      case KING:
        return "K";
      case QUEEN: 
        if (suit == Pinochle.SPADES) 
          return "S";
        else 
          return "Q";
      case JACK:
        if (suit == Pinochle.DIAMONDS) 
          return "D";
        else 
          return "J";
      case NINE:
        return "9";
    }
    return null;
  }
}

