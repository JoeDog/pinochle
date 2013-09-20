package org.joedog.pinochle.game;

import java.util.Random;

public class Meld {
  private   int     trump;
  private   boolean select;    // should we select cards for melding?
  private   int     score = 0;
  protected Hand    hand;

  static final int[] suits = new int[]{
    Pinochle.HEARTS,Pinochle.CLUBS,Pinochle.DIAMONDS,Pinochle.SPADES
  };
  static final int[] ranks = new int[]{
    Pinochle.ACE,  Pinochle.TEN, Pinochle.KING,
    Pinochle.QUEEN,Pinochle.JACK,Pinochle.NINE
  };

  public Meld(Hand hand) {
    this.hand = hand;
  }

  public Meld(Hand hand, int trump) {
    this.hand   = hand; 
    this.trump  = trump; 
    this.select = true;
    this.score  = score(this.trump);
  }

  public int getMeld() {
    return this.score; 
  }

  /**
   * Returns an int that represents the total meld
   * for the hand. The trump argument represents the
   * trump suit. (See org.joedog.pinochle.game.Pinochle
   * <p>
   * This method searches a hand for the following:
   * Run (A,10,K,Q,J) 15, 150 (must be in trump)
   * Aces round       10, 100
   * Kings round       8,  80
   * Queens round      6,  60
   * Jacks round       4,  40
   * Nines in trump    1 point each
   * Marriages         4 points in trump, 2 pts otherwise
   * Pinochle Jack of Diamonds & Queen of Spades 4, 30
   * 
   * @param  int trump (i.e., SPADES|CLUBS|HEARTS|DIAMONDS
   * @return int - the total meld score of the hand
   */
  public int score(int trump) {
    int score = 0;

    for (int i = 0; i < suits.length; i++) {
      score += this.marriage(suits[i], trump);
    }
    score += this.pinochle();
    score += this.round(Pinochle.ACE);
    score += this.round(Pinochle.KING);
    score += this.round(Pinochle.QUEEN);
    score += this.round(Pinochle.JACK);
    score += this.run(trump);
    score += this.nines(trump);
    return score;
  }

  /**
   * This function evaluates a hand and returns an
   * Assessment to the player.
   * @param  none
   * @return Assessment
   */
  public Assessment assessment() {
    int cnt;
    int max   = 0;
    int meld  = 0;
    int aces  = 0;
    int taces = 0;
    Assessment a = new Assessment(); 
 
    for (int i = 0; i < suits.length; i++) {
      meld  = this.score(suits[i]);
      taces = this.hand.contains(new Card(Pinochle.ACE, suits[i]));
      aces += taces;
      cnt   = 0;
      for (int x = 0; x < ranks.length; x++) {
        cnt += this.hand.contains(new Card(ranks[x], suits[i]));
      }
      if ((cnt + meld +taces) > (a.getTrumpCount()+a.getTrumpAces()+a.getMeld())) {
        a.setMeld(meld);
        a.setSuit(suits[i]);
        a.setTrumpCount(cnt);
        a.setTrumpAces(taces);
      }
      a.setAces(aces);
    } 
    return a;
  }

  public Deck passables(boolean bidder, int num, int trump) {
    Deck deck = new Deck();
    System.out.println("Trump: "+Pinochle.suitname(trump));
    if (! bidder) {
      /**
       * If our partner took the bid, then we want
       * to pass trump and aces UNLESS we have a run
       * or aces round
       */
      if (run(trump) == 0) {
        Card ace = new Card(Pinochle.ACE, trump);
        while (deck.count() < num && hand.contains(ace) > 0) {
          deck.add(hand.pass(ace));  
          hand.remove(ace);  
        }
        if (marriage(trump, trump) == 0 && round(Pinochle.KING) < 1 && round(Pinochle.QUEEN) < 1) {
          Card king = new Card(Pinochle.KING, trump);
          while (deck.count() < num && hand.contains(king) > 0) {
            deck.add(hand.pass(king));  
            hand.remove(king);  
          }
          Card queen = new Card(Pinochle.QUEEN, trump);
          while (deck.count() < num && hand.contains(queen) > 0) {
            deck.add(hand.pass(queen));  
            hand.remove(queen);  
          }
        } else 
          if (marriage(trump, trump) > 1 && deck.count() < 2 && 
              round(Pinochle.KING) < 1 && round(Pinochle.QUEEN) < 1) {
          // XXX: is my 'else if' check sufficient???
          //System.out.println("Going to pass a marriage...");
          Card king  = new Card(Pinochle.KING, trump);
          Card queen = new Card(Pinochle.QUEEN, trump);
          if (trump == Pinochle.SPADES || trump == Pinochle.DIAMONDS) {
            if (pinochle() < 1) {
              // We don't want to pass pinochle; 
              // XXX: maybe we do but that's for later
              deck.add(hand.pass(king));  
              hand.remove(king);  
              deck.add(hand.pass(queen));  
              hand.remove(queen);  
            }
          }
        } // else if marriage in trump
        Card ten = new Card(Pinochle.TEN, trump);
        while (deck.count() < num && hand.contains(ten) > 0) {
          deck.add(hand.pass(ten));  
          hand.remove(ten);
        }
        if (round(Pinochle.JACK) < 1) {
          Card jack = new Card(Pinochle.JACK, trump);
          while (deck.count() < num && hand.contains(jack) > 0) {
            deck.add(hand.pass(jack));  
            hand.remove(jack);
          }
        }
        Card nine = new Card(Pinochle.NINE, trump);
        while (deck.count() < num && hand.contains(nine) > 0) {
          deck.add(hand.pass(nine));  
          hand.remove(nine);
        }
      } // we're done passing trump...
      if (deck.count() == num) return deck;
      if (round(Pinochle.ACE) < 1) {
        for (int i = 0; i < suits.length && deck.count() < num; i++) {
          Card c = new Card(Pinochle.ACE, suits[i]);
          if (hand.contains(c) > 0) {
            deck.add(hand.pass(c));
            hand.remove(c);
          }
        }
      }
    } else {
      /**
       * We won the bid; we're going to try to short
       * suit ourselves without passing meld
       */
      int suit = shortsuit(trump);
      // We'll start passing meldable cards...
      if (marriage(suit, trump) == 0 && round(Pinochle.KING) < 1 && round(Pinochle.QUEEN) < 1) {
        Card king = new Card(Pinochle.KING, suit);
        while (deck.count() < num && hand.contains(king) > 0) {
          deck.add(hand.pass(king));
          hand.remove(king);
        }
        Card queen = new Card(Pinochle.QUEEN, suit);
        while (deck.count() < num && hand.contains(queen) > 0) {
          deck.add(hand.pass(queen));
          hand.remove(queen);
        }
      } else
        if (marriage(suit, trump) > 1 && deck.count() < 2 &&
            round(Pinochle.KING) < 1 && round(Pinochle.QUEEN) < 1) {
        //System.out.println("Going to pass a marriage...");
        Card king  = new Card(Pinochle.KING, suit);
        Card queen = new Card(Pinochle.QUEEN, suit);
        if (suit == Pinochle.SPADES || suit == Pinochle.DIAMONDS) {
          if (pinochle() < 1) {
            // We don't want to pass pinochle; 
            // XXX: maybe we do but that's for later
            deck.add(hand.pass(king));
            deck.add(hand.pass(queen));
            //System.out.println("Gonna pass: "+king.toString());
            hand.remove(king);
            //System.out.println("Gonna pass: "+queen.toString());
            hand.remove(queen);
          }
        }
      }
      Card ten = new Card(Pinochle.TEN, suit);
      while (deck.count() < num && hand.contains(ten) > 0) {
        deck.add(hand.pass(ten));  
        //System.out.println("Gonna pass: "+ten.toString());
        hand.remove(ten);
      }
      if (round(Pinochle.JACK) < 1) {
        Card jack = new Card(Pinochle.JACK, suit);
        while (deck.count() < num && hand.contains(jack) > 0) {
          deck.add(hand.pass(jack));  
          //System.out.println("Gonna pass: "+jack.toString());
          hand.remove(jack);
        }
      }
      Card nine = new Card(Pinochle.NINE, suit);
      while (deck.count() < num && hand.contains(nine) > 0) {
        deck.add(hand.pass(nine));  
        //System.out.println("Gonna pass: "+nine.toString());
        hand.remove(nine);
      }
    }

    /**
     * Eek! We've gotten this far and we haven't found
     * enough cards to pass; we're just gonna do random
     */
    while (deck.count() < num) {
      Random r  = new Random();
      int lo    = 1;
      int hi    = hand.size();
      int rand  = r.nextInt(hi-lo) + lo; 
      Card card = hand.get(rand);  
      if (bidder) {
        if (card.getSuit() != trump) {
          deck.add(hand.pass(card));
          hand.remove(card);
        }
      } else {
        deck.add(hand.pass(card));
        hand.remove(card);
      }
    }  
    //System.out.println(deck.toString());
    return deck;
  }

  public int run(int trump) {
    Card ace   = new Card(Pinochle.ACE,   trump);
    Card ten   = new Card(Pinochle.TEN,   trump);
    Card king  = new Card(Pinochle.KING,  trump);
    Card queen = new Card(Pinochle.QUEEN, trump);
    Card jack  = new Card(Pinochle.JACK,  trump);
    int a = hand.contains(ace);
    if (a == 0)   return 0;
    int t = hand.contains(ten);
    if (t == 0)   return 0;
    int k = hand.contains(king);
    if (k == 0)  return 0;
    int q = hand.contains(queen);
    if (q == 0) return 0;
    int j = hand.contains(jack);
    if (j == 0)  return 0;
    if (select) {
      int num = ((a+t+k+q+j) == 10) ? 2 : 1;
      hand.meld(ace,   num);
      hand.meld(ten,   num);
      hand.meld(king,  num);
      hand.meld(queen, num);
      hand.meld(jack,  num);
    }
    /** 
     * KLUDGE ALERT
     * Runs are worth 15 and 150 but we'll
     * count them  11 and 142 because this 
     * class will add the marriage values
     */ 
    return ((a+t+k+q+j) == 10) ? 142 : 11; 
  }

  public int nines(int trump) {
    Card nine = new Card(Pinochle.NINE, trump);
    int num = hand.contains(nine);
    if (select) {
      hand.meld(nine, num);
    }
    return num;
  }

  public int marriage(int suit, int trump) {
    int  n  = 0;
    int  r  = 0;
    Card k  = new Card(Pinochle.KING, suit);
    Card q  = new Card(Pinochle.QUEEN, suit);
    int  ks = this.hand.contains(k);
    int  qs = this.hand.contains(q);
    if (ks >= 1 && qs >= 1) {
      if (ks + qs > 3) n = 2; 
      else             n = 1;
    }
    r = (suit == trump) ? n * 4 : n * 2;
    if (select) {
      hand.meld(k, n);
      hand.meld(q, n);
    }    
    return r;
  }
  
  public int pinochle() {
    int  n  = 0;
    int  r  = 0;
    Card j  = new Card(Pinochle.JACK,  Pinochle.DIAMONDS);
    Card q  = new Card(Pinochle.QUEEN, Pinochle.SPADES);
    int  js = this.hand.contains(j);
    int  qs = this.hand.contains(q); 
    if (js >= 1 && qs >= 1) {
      if (js + qs == 4) n = 2; 
      else              n = 1;
    }
    r = (n==2) ? 30 : n*4;
    if (select) {
      hand.meld(j, n);
      hand.meld(q, n);
    }
    return r;
  }

  public int round(int rank) {
    int   res = 0;
    int   ret = 0;
    int[] num = new int[suits.length];
    for (int i = 0; i < suits.length; i++) {
      num[i] = this.hand.contains(new Card(rank, suits[i]));  
      if (num[i] == 0) return 0;
    } 
    for (int i = 0; i < num.length; i++) 
      res += num[i];
    ret = (res==8) ? 10 * this.multiplier(rank) : 1 * this.multiplier(rank); 
    if (select) {
      int y = (res == 8) ? 2 : 1;
      for (int i = 0; i < suits.length; i++) {
        hand.meld(new Card(rank, suits[i]), y);  
      }
    }
    return ret;
  }

  public int shortsuit(int trump) {
    int num  =  0;
    int low  =  100;
    int suit = -1;
    for (int i = 0; i < suits.length; i++) {
      if (trump != i) {
        num = count(i);
        if (num < low) {
          low  = num;    
          suit = i;
        }
      } 
    } 
    return suit;
  }

  public int count(int suit) {
    int cnt = 0;
    for (int x = 0; x < ranks.length; x++) {
      cnt += this.hand.contains(new Card(ranks[x], suit));
    }
    return cnt;
  }

  private int multiplier(int rank) {
    switch (rank) {
      case Pinochle.ACE:
        return 10;
      case Pinochle.KING:
        return 8;
      case Pinochle.QUEEN:
        return 6;
      case Pinochle.JACK:
        return 4;
    }
    return 0;
  }
}

