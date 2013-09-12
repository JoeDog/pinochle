package org.joedog.pinochle.game;

import java.util.Random;

public class Deck extends Pack {
  public int size  = -1; 

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

  public Deck() {
    this.size = 1;
    //this(1); // default to single deck
  }

  public Deck (int size) {
    int id = 0;
    this.size = size;
    for (int x = 0; x < (this.size*2); x++) {
      for (int i = 0; i < ranks.length; i++) {
        for (int j = 0; j < suits.length; j++) {
          add(new Card(ranks[i], suits[j], id++));
        }
      }
    }
    System.out.println(this.toString());
  }

  public void addCard(Card card) {
    this.add(card);
  }

  public void printIt() {
    System.out.println("card count: "+this.count());
    System.out.println("deck size:  "+this.size);
    System.out.println(this.toString());
  }

  public Card dealCard(int num) {
    return (Card) this.get(num);    
  }

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
    tmp = na[a];
    na[a] = na[b];
    na[b] = tmp;
  }
}

