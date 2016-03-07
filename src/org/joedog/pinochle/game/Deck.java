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

import java.util.Random;

public class Deck extends Pack {
  public int size  = -1; 
  public int count = 6;

  static final int ranks[] = {
    Pinochle.ACE,
    Pinochle.TEN,
    Pinochle.KING,
    Pinochle.QUEEN,
    Pinochle.JACK,
    Pinochle.NINE
  };
  static final int suits[]  = {
    Pinochle.CLUBS,
    Pinochle.SPADES,
    Pinochle.HEARTS,
    Pinochle.DIAMONDS
  };

  /**
   * Creates an empty deck; good for creating 
   * groups of cards for passing or tracking
   * <p>
   * @return Deck
   */
  public Deck() {
    this.size = 1;
    //this(1); // default to single deck
  }

  /** 
   * Creates a Pinochle deck of size (generally single or double)
   * <p>
   * @return Deck
   */
  public Deck (int size) {
    int id = 0;
    this.size = size;
    this.count = (this.size==2) ? 5 : 6;
    for (int x = 0; x < (this.size*2); x++) {
      for (int i = 0; i < this.count; i++) {
        for (int j = 0; j < suits.length; j++) {
          add(new Card(ranks[i], suits[j], id++));
        }
      }
    }
  }

  public int contains(Card card) {
    int  count = 0;
    for (Card c: this.getCards()) {
      if (c.matches(card)) count++; 
    }
    return count;
  }

  public int contains (int suit) {
    int num = 0;
    for (Card c: this.getCards()) {
      if (c.getSuit() == suit) {
        num ++;
      }
    }
    return num;
  }

  public void printIt() {
    System.out.println("card count: "+this.count());
    System.out.println("deck size:  "+this.size);
    System.out.println(this.toString());
  }

  public Card dealCard(int num) {
    return (Card) this.get(num);    
  }

  /**
   * Returns a String interpretation of the Deck
   * <p>
   * @param  none
   * @return String
   */
  public String toString() {
    String cards = null;
    for (Card c: this.getCards()) {
      if (cards==null) {
        cards = Pinochle.rank(c.getRank())+Pinochle.suit(c.getSuit())+"("+c.getId()+")";
      } else {
        cards = cards+" "+Pinochle.rank(c.getRank())+Pinochle.suit(c.getSuit())+"("+c.getId()+")";
      }
    }
    return cards;
  }

  /**
   * Places the cards in the deck in random order; while
   * completely unnecessary, this method 'shuffles' the 
   * deck five times just for fun....
   * <p>
   * @param  none
   * @return void
   */
  public void shuffle() {
    int x = 0;
    while (x < 5) { 
      Pack shuffled = new Pack();

      while (this.count() > 0) {
        shuffled.add(remove((int)(count() * java.lang.Math.random())));
      }

      while (shuffled.count() > 0) {
        add(shuffled.remove(0));
      }
      x++;
    } // this outer loop isn't necessary but it's fun....
  }

  public static final int[] randomSet(int n) {
    int na[] = new int[n];
    Random rand = new Random();

    for (int i = 0; i < n; ++i) {
      na[i] = i;
    }
    for (int i = 0; i < n; ++i) {
      swap(na, i, Math.abs(rand.nextInt()) % n);
    }
    return na;
  }

  private static final void swap(int[] na, int a, int b) {
    int tmp;
    tmp   = na[a];
    na[a] = na[b];
    na[b] = tmp;
  }
}

