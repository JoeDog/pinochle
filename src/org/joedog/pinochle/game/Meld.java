package org.joedog.pinochle.game;

import org.joedog.util.*;

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

  /**
   * Returns the score of the hand
   * <p>
   * @param  none
   * @return int
   */
  public int getMeld() {
    return this.score; 
  }

  /**
   * Returns an int that represents the total meld
   * for the hand. The trump argument represents the
   * trump suit. See org.joedog.pinochle.game.Pinochle
   * <p>
   * This method searches a hand for the following:
   * Run (A,10,K,Q,J) 15, 150 (must be in trump)
   * Aces round       10, 100
   * Kings round       8,  80
   * Queens round      6,  60
   * Jacks round       4,  40
   
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
    int max      = 0;
    int meld     = 0;
    int aces     = 0;
    int taces    = 0;
    int power    = 0;
    int matt     = 100;
    Assessment a = new Assessment(); 
 
    for (int i = 0; i < suits.length; i++) {
      meld  = this.score(suits[i]);
      taces = this.hand.contains(new Card(Pinochle.ACE, suits[i]));
      aces += taces;
      cnt   = 0;
      power = 0;
      matt  = 100;
      for (int x = 0; x < ranks.length; x++) {
        cnt += this.hand.contains(new Card(ranks[x], suits[i]));
      }
      if (cnt < matt) matt = cnt;
      if (this.hand.contains(new Card(Pinochle.ACE, suits[i])) == 2) {
        power += 3;
      }  
      if (power == 4 && this.hand.contains(new Card(Pinochle.ACE, suits[i])) == 2) {
        power += 3;
      }
      if (power == 4 && this.hand.contains(new Card(Pinochle.ACE, suits[i])) == 2) {
        power += 2;
      }
      if (cnt >= 5) {
        power += 3;
      }
      if (matt < 2) {
        power += 2;
      }
      if (meld < 6) {
        power -= 4;
      }
      if (run(suits[i]) > 0) {
        power += 5;
      } else {
        switch(shy(suits[i])) {
          case 1: 
            power += 4;
            break;
          case 2:
            power += 1;
            break;
          default:
           break;
        }
      }
      power += powerFactor();
      if ((cnt + meld + taces + power) > (a.getTrumpCount()+a.getTrumpAces()+a.getMeld()+a.getPower())) {
        a.setMeld(meld);
        a.setSuit(suits[i]);
        a.setTrumpCount(cnt);
        a.setTrumpAces(taces);
        a.setPower(power);
      }
      a.setAces(aces);
    } 
    return a;
  }

  public Deck passables(boolean bidder, int num, int trump) {
    Deck deck = new Deck();
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
          Card king  = new Card(Pinochle.KING, trump);
          Card queen = new Card(Pinochle.QUEEN, trump);
          if (! (trump == Pinochle.SPADES && pinochle() > 1)) {
            if (deck.count() < num && hand.contains(king) > 0) {
              deck.add(hand.pass(king));  
              hand.remove(king);  
            }
            if (deck.count() < num && hand.contains(queen) > 0) {
              deck.add(hand.pass(queen));  
              hand.remove(queen);  
            }
          } else if (deck.count() < num && this.hand.contains(queen) == 2 && pinochle() < 30) {
            // we have an extra queen. Let's pass it.
            deck.add(hand.pass(queen));
            hand.remove(queen);
          } 
        } else 
          if (marriage(trump, trump) == 4 && deck.count() < 2 && 
              round(Pinochle.KING) < 1 && round(Pinochle.QUEEN) < 1) {
          Card king  = new Card(Pinochle.KING, trump);
          Card queen = new Card(Pinochle.QUEEN, trump);
          if (! ((trump == Pinochle.SPADES || trump == Pinochle.DIAMONDS) && pinochle() > 1)) {
            // We don't want to pass pinochle; 
            // XXX: maybe we do but that's for later
            deck.add(hand.pass(king));  
            hand.remove(king);  
            deck.add(hand.pass(queen));  
            hand.remove(queen);  
          } else if (deck.count() < num && this.hand.contains(queen) == 2 && pinochle() < 30) {
            // we have an extra queen. Let's pass it.
            deck.add(hand.pass(queen));
            hand.remove(queen);
          }
        } // else if marriage in trump
        Card ten = new Card(Pinochle.TEN, trump);
        while (deck.count() < num && hand.contains(ten) > 0) {
          deck.add(hand.pass(ten));  
          hand.remove(ten);
        }
        if (round(Pinochle.JACK) < 1) {
          if (!(trump == Pinochle.DIAMONDS && pinochle() > 1)) {
            Card jack = new Card(Pinochle.JACK, trump);
            if (deck.count() < num && hand.contains(jack) > 0) {
              deck.add(hand.pass(jack));  
              hand.remove(jack);
            }
          }
        }
      } // we're done passing trump...
      if (deck.count() == num) return deck;
      if (round(Pinochle.ACE) < 1) {
        // loop twice because there's tow of each
        for (int j = 0; j < 2 && deck.count() < num; j++) { 
          for (int i = 0; i < suits.length && deck.count() < num; i++) {
            Card c = new Card(Pinochle.ACE, suits[i]);
            if (hand.contains(c) > 0) {
              deck.add(hand.pass(c));
              hand.remove(c);
            }
          }
        }
      }
      Card nine = new Card(Pinochle.NINE, trump);
      while (deck.count() < num && hand.contains(nine) > 0) {
        deck.add(hand.pass(nine));  
        hand.remove(nine);
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
        Card king  = new Card(Pinochle.KING, suit);
        Card queen = new Card(Pinochle.QUEEN, suit);
        if (! ((suit == Pinochle.SPADES || suit == Pinochle.DIAMONDS) && pinochle() > 1)) {
          // We don't want to pass pinochle; 
          // XXX: maybe we do but that's for later
          deck.add(hand.pass(king));
          deck.add(hand.pass(queen));
          hand.remove(king);
          hand.remove(queen);
        }
      }
      if (round(Pinochle.JACK) < 1) {
        if (!(suit == Pinochle.DIAMONDS && pinochle() > 0)) {
          Card jack = new Card(Pinochle.JACK, suit);
          while (deck.count() < num && hand.contains(jack) > 0) {
            deck.add(hand.pass(jack));  
            hand.remove(jack);
          }
        }
      }
      // Looking for meldables...
      for (int i = 0; i < 4 && deck.count() < num; i++) {
        if (i != trump) {
          Card king = new Card(Pinochle.KING, i);
          if (hand.contains(king) > 0 && deck.count() < num && ! this.hand.isMarried(king)) {
            if ((! this.hand.isMarried(king)) && (round(Pinochle.KING) < 1)) {
              deck.add(hand.pass(king));
              hand.remove(king);    
            }
          } 
        }
      }
      // Looking for meldable queens....
      for (int i = 0; i < 4 && deck.count() < num; i++) {
        if (i != trump && i != Pinochle.SPADES) {
          Card queen = new Card(Pinochle.QUEEN, i);
          if (hand.contains(queen) > 0 && deck.count() < num) {
            if ((! this.hand.isMarried(queen)) && (round(Pinochle.QUEEN) < 1)) {
              deck.add(hand.pass(queen));
              hand.remove(queen);    
            } 
          } 
        }
      }
      Card ten = new Card(Pinochle.TEN, suit);
      while (deck.count() < num && hand.contains(ten) > 0) {
        deck.add(hand.pass(ten));  
        hand.remove(ten);
      }
      Card nine = new Card(Pinochle.NINE, suit);
      while (deck.count() < num && hand.contains(nine) > 0) {
        deck.add(hand.pass(nine));  
        hand.remove(nine);
      }
    }

    /**
     * Eek! We've gotten this far and we haven't found
     * enough cards to pass; we're just gonna do random
     */
    int tries = 0;
    while (deck.count() < num) {
      Random r  = new Random();
      int lo    = 1;
      int hi    = hand.size();
      int rand  = r.nextInt(hi-lo) + lo; 
      Card card = hand.get(rand);  
      if (bidder) {
        if (tries >= 50) {
          // GOD DAMN DO WE HAVE A SPECTACULAR HAND
          deck.add(hand.pass(card));
          hand.remove(card);
        } else if (card.getSuit() != trump && card.getRank() != Pinochle.ACE) {
          if (!(pinochle() > 1 && card.isPinochleMate())) {
            deck.add(hand.pass(card));
            hand.remove(card);
          }
        }
      } else {
        if (tries >= 50) {
          deck.add(hand.pass(card));
          hand.remove(card);
        } else if (!(pinochle() > 1 && card.isPinochleMate())) {
          if (tries < 40 && card.getRank() != Pinochle.NINE && card.getRank() != Pinochle.TEN) {
            // we don't want to break up marriages, rounds or pass trump at this point. We 
            // thoroughly vetted trump above. No reason to buck those rules down here.
            if (this.hand.isMarried(card) == false && round(card.getRank()) < 1 && card.getSuit() != trump) {
              deck.add(hand.pass(card));
              hand.remove(card);
            }
          }
        } 
      }
      tries++;
    }  
    return deck;
  }

  public int powerFactor() {
    int cnt = 0;
    int num = 0;
    int ace = 0;
    for (int i = 0; i < suits.length; i++) {
      cnt = 0; // reset to 0
      ace = this.hand.contains(new Card(Pinochle.ACE, suits[i]));
      cnt += this.hand.contains(suits[i]);
      if (cnt >= 5 && ace > 0) {
        num += 1;
      } else if (cnt >= 4 && ace == 2) {
        num += 1;
      }
    }
    if (num > 1) return 4;
    else return 0;
  }

  public int shy(int trump) {
    int  cnt   = 5;
    Card ace   = new Card(Pinochle.ACE,   trump);
    Card ten   = new Card(Pinochle.TEN,   trump);
    Card king  = new Card(Pinochle.KING,  trump);
    Card queen = new Card(Pinochle.QUEEN, trump);
    Card jack  = new Card(Pinochle.JACK,  trump);
    if (hand.contains(ace)   > 0) cnt --; 
    if (hand.contains(ten)   > 0) cnt --; 
    if (hand.contains(king)  > 0) cnt --; 
    if (hand.contains(queen) > 0) cnt --; 
    if (hand.contains(jack)  > 0) cnt --; 
    return cnt;
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

