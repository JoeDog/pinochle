package org.joedog.pinochle.game;

public final class Pinochle {
  public final static int NORTH      = 0;
  public final static int EAST       = 1;
  public final static int SOUTH      = 2;
  public final static int WEST       = 3;

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

