package org.joedog.pinochle.player;
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

import org.joedog.util.*;
import java.net.URL;
import java.util.LinkedList;

import org.joedog.pinochle.game.*;
import org.joedog.pinochle.control.Constants;
import org.joedog.pinochle.model.AbstractModel;

public abstract class Player extends AbstractModel {
  public static final   int HUMAN    = 0;
  public static final   int COMPUTER = 1;
  protected Hand        hand   = null;
  protected Brain       brain  = null;
  protected Meld        meld;
  public   int          partner;
  public   int          position;
  public   int          type;
  public   String       name;
  public   int          maxBid = 0;
  public   int          myBid  = 0;
  public   int          pBid   = 0; 
  public   int          ptops  = 0;
  public   Assessment   assessment;
  public   boolean      bidder = false;
  public   String       memory;
  public   String       memtxt = System.getProperty("pinochle.memory");
  public   Knowledge    knowledge   = null;
  public   int          distance;

  public Player () {
    this.knowledge = knowledge.getInstance();
  }

  public void addHand(Hand hand) {
    this.hand = hand;
  }

  public void takeCard(Card c) {
    this.hand.add(c);
  }

  public synchronized void init() {
    this.myBid     = 0;
    this.maxBid    = 0;
    this.pBid      = 0;
    this.ptops     = 0;
    if (this.hand != null) {
      this.hand.reset();
    }
    if (this.brain == null) {
      this.brain = new Brain();
    }
    this.brain.forget();
  }

  public void assessHand() {
    meld    = new Meld(this.hand);
    assessment   = meld.assessment(); 
    this.maxBid  = assessment.maxBid();
    // we want to rely on experience but that's 
    // stored in a file that can be removed. If 
    // the file is purged, experience returns the
    // programmatic maxBid
    this.maxBid  = experience(this.hand.asRanks());
    this.maxBid += guts();

    Debug.print("  "+this.name+"'s assessment: "+assessment.toString());

    // We need to shave some bid if we're lacking aces 
    // We'll shave even more down below if we don't have
    // adequate meld....
    if (this.distance >= 2) {
      Debug.print("  Too much distance to trust experience....");
      if (assessment.getAces() < 3) {
        Debug.print("  Less than three aces, shaving 3 points");
        this.maxBid -= 3;
      }
   
      // adjust down a mediocre meld low power hand
      if (assessment.getAces() < 3 && assessment.getMeld() < 15) {
        Debug.print("  Mediocre meld, low power.");
        this.maxBid = (assessment.getTrumpCount() >= 5) ? this.maxBid : (this.maxBid - 4);
      }

      if (assessment.getAces() < 3) {
        Debug.print("  Less than three aces");
        this.maxBid -= 2;
      }

      if (this.maxBid >= 30 && assessment.getMeld() < 10) {
        Debug.print("  Too little meld to bid more than 30");
        this.maxBid -= 2;
      }

      // no good ever came from a hand with no aces...
      if (assessment.getAces() == 0) {
        Debug.print("  NO ACES (meld+8)");
        this.maxBid = assessment.getMeld() + 8;
      }
 
      // conversely, good things come to those with aces 
      if (assessment.getAces() >= 3 && this.maxBid < 16) {
        Debug.print("  More than three acess (at least 24)");
        this.maxBid = (assessment.getAces() >= 4) ? 28 : 24;
      }

      // Bids in the thirties with 4 cards in trump are nearly 
      // impossible to achieve without a shitload of meld
      if (assessment.getTrumpCount() < 5) {
        Debug.print("  Inadequate trump");
        this.maxBid -= 2;
      }

      if (assessment.getTrumpCount() >= 6) {
        Debug.print("  TONS of trump! bumping by six...");
        this.maxBid += 6;
      }

      if (assessment.getTrumpCount() > 5 && this.maxBid < 16) {
        Debug.print("  Lot's of trump! We have to make a few bids..");
        this.maxBid = 29;
      }

      if (assessment.getTrumpCount() >= 5 && assessment.getAces() >= 3 && this.maxBid < 16) {
        // I'm not sure how this scenario occurs but I've seen it
        Debug.print("  Weirdo scenario....");
        this.maxBid = 30;
      }
    }
    
    if (this.maxBid >= 40 && assessment.getMeld() < 10) {
      this.maxBid = 36;
    }

    if (this.maxBid >= 50 && assessment.getMeld() < 25) {
      this.maxBid = 38;
    }

    // This is for experience generation. By forcing a high
    // maxBid we're ensured of capturing a lot of different
    // hand combinations...
    if (this.name.equals("Limey")) {
      if (this.maxBid < 28) 
        this.maxBid = 28;
    } 
  }

  private int guts() {
    int num = RandomUtils.number(100);

    if (num == 100) {
      return 10;
    }
    if (num >= 90) {
      return 5;
    }
    if (num >= 80) {
      return 4;
    }
    if (num >= 70) {
      return 3;
    }
    if (num >= 60) {
      return 2;
    }
    if (num < 11) {
      return -2;
    }
    return 0;
  }

  public void setup(int position, int partner, String name, Hand hand) {
    this.position = position;
    this.partner  = partner;
    this.name     = name;
    this.hand     = hand;
    this.setText("");
  } 

  public void setText(String text) {
    String status = this.name+" "+text;
    switch (this.position) {
      case Pinochle.NORTH:
        firePropertyChange(Constants.NORTH, "northStatus", status);  
        break;
      case Pinochle.SOUTH:
        firePropertyChange(Constants.SOUTH, "southStatus", status);  
        break;
      case Pinochle.EAST: 
        firePropertyChange(Constants.EAST,  "eastStatus", status);  
        break;
      case Pinochle.WEST: 
        firePropertyChange(Constants.WEST,  "westStatus", status);  
        break;
      default:
        break;
    }
  }

  public void showHand() {
    System.out.println(this.name+" "+this.hand.toString());
  }

  public String handToString() {
    return this.hand.toString();
  }

  public boolean wonBid() {
    return this.bidder;
  }

  public int getPosition() {
    return this.position;
  }

  public int getPartner() {
    return this.partner;
  }

  public int getMaxBid() {
    return this.maxBid;
  }

  public int lastBid() {
    return this.myBid;
  }

  /**
   * This is a programmer's helper; it will
   * never be called in the final product....
   */
  /*public void clearHand() {
    while (this.hand.size() > 0) {
      this.hand.remove(0);
    }  
  }*/

  public String getName() {
    return this.name;
  }

  public int getType () {
    return this.type;
  }

  public Hand getHand() {
    return this.hand;
  }

  private int experience (String hand) {
    int bid = -1;
    int low = 1000;
    RollingAverage avg = new RollingAverage();

    if (! FileUtils.exists(this.memtxt)) {
      // Somebody chucked his memory file;
      // we'll revert to Old School guessing
      return this.maxBid;  
    }

    String a [] = new String[2];

    for (String line: knowledge.getMemory()) {
      String[] array = line.split("\\|",-1);
      /** 
       * Suits 2 and 3 are diamonds and spades they
       * are treated as constants because of pinochle
       * suits 0 and 1 are hearts and clubs and they
       * are treated interchangeably...
       */
      a[0] = array[0]+array[1]+array[2]+array[3];
      a[1] = array[1]+array[0]+array[2]+array[3];

      for (String s: a) {
        int d = computeDistance(hand, s);
        //int d = (int)sift4(hand, s, 15);
        if (d < low) {
          low = d;
          /**
           * Each time d hits a new low; we
           * have to reset the rolling average
           */
          avg.removeAll();
          if (array.length == 5) {
            bid = Integer.parseInt(array[4]);
          }
        }
        if (d == low) {
          if (array.length == 5) {
            int tmp  = Integer.parseInt(array[4]);
            avg.add(tmp);
            bid = (int)avg.average();
          }
        }
      }
    }
    Debug.print(this.name+" is thinking now....");
    Debug.print("  Distance of the match:    "+low);
    Debug.print("  Average bid returned bid: "+bid);
    this.distance = low;
    // We'll adjust down high bids with high distances
    if (bid >= 35 && low >= 4) return (bid - 6);
    if (bid >= 35 && low >= 3) return (bid - 4);
    if (bid >= 35 && low >= 2) return (bid - 2);
    return bid;
  }

  private int computeDistance(String s1, String s2) {
    int[] costs = new int[s2.length() + 1];
    for (int i = 0; i <= s1.length(); i++) {
      int lastValue = i;
      for (int j = 0; j <= s2.length(); j++) {
        if (i == 0)
          costs[j] = j;
        else {
          if (j > 0) {
            int newValue = costs[j - 1];
            if (s1.charAt(i - 1) != s2.charAt(j - 1))
              newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
            costs[j - 1] = lastValue;
            lastValue = newValue;
          }
        }
      }
      if (i > 0)
        costs[s2.length()] = lastValue;
    }
    return costs[s2.length()];
  }

  /**
   * Sift4 - common version
   * online algorithm to compute the distance between two strings in O(n)
   * Algorithm by siderite, java port by Nathan Fischer 2016
   * https://siderite.blogspot.com/2014/11/super-fast-and-accurate-string-distance.html
   * @param s1
   * @param s2
   * @param maxOffset the number of characters to search for matching letters
   * @return
   */
  public static double sift4(String s1, String s2, int maxOffset) {
    class Offset{
        int c1;
        int c2;
        boolean trans;

        Offset(int c1, int c2, boolean trans) {
            this.c1 = c1;
            this.c2 = c2;
            this.trans = trans;
        }
    }

    if(s1 == null || s1.isEmpty())
        return s2 == null ? 0 : s2.length();

    if(s2 == null || s2.isEmpty())
        return s1.length();

    int l1=s1.length();
    int l2=s2.length();

    int c1 = 0;  //cursor for string 1
    int c2 = 0;  //cursor for string 2
    int lcss = 0;  //largest common subsequence
    int local_cs = 0; //local common substring
    int trans = 0;  //number of transpositions ('ab' vs 'ba')
    LinkedList<Offset> offset_arr=new LinkedList<>();  //offset pair array, for computing the transpositions

    while ((c1 < l1) && (c2 < l2)) {
        if (s1.charAt(c1) == s2.charAt(c2)) {
            local_cs++;
            boolean isTrans=false;
            //see if current match is a transposition
            int i=0;
            while (i<offset_arr.size()) {
                Offset ofs=offset_arr.get(i);
                if (c1<=ofs.c1 || c2 <= ofs.c2) {
                    // when two matches cross, the one considered a transposition is the one with the largest difference in offsets
                    isTrans=Math.abs(c2-c1)>=Math.abs(ofs.c2-ofs.c1);
                    if (isTrans) {
                        trans++;
                    } else {
                        if (!ofs.trans) {
                            ofs.trans=true;
                            trans++;
                        }
                    }
                    break;
                } else {
                    if (c1>ofs.c2 && c2>ofs.c1) {
                        offset_arr.remove(i);
                    } else {
                        i++;
                    }
                }
            }
            offset_arr.add(new Offset(c1, c2, isTrans));
        } else {
            lcss+=local_cs;
            local_cs=0;
            if (c1!=c2) {
                c1=c2=Math.min(c1,c2);  //using min allows the computation of transpositions
            }
            //if matching characters are found, remove 1 from both cursors (they get incremented at the end of the loop)
            //so that we can have only one code block handling matches
            for (int i = 0; i < maxOffset && (c1+i<l1 || c2+i<l2); i++) {
                if ((c1 + i < l1) && (s1.charAt(c1 + i) == s2.charAt(c2))) {
                    c1+= i-1;
                    c2--;
                    break;
                }
                if ((c2 + i < l2) && (s1.charAt(c1) == s2.charAt(c2 + i))) {
                    c1--;
                    c2+= i-1;
                    break;
                }
            }
        }
        c1++;
        c2++;
        // this covers the case where the last match is on the last token in list, so that it can compute transpositions correctly
        if ((c1 >= l1) || (c2 >= l2)) {
            lcss+=local_cs;
            local_cs=0;
            c1=c2=Math.min(c1,c2);
        }
    }
    lcss+=local_cs;
    return Math.round(Math.max(l1,l2)- lcss +trans); //add the cost of transpositions to the final result
  }

  public void remember(int meld, int take) {
    if (!this.bidder) return;
    if (this.memory == null || this.memory.length() < 2) return;

    int score  = (meld+take);
    this.memory += "|"+score;
    Logger.remember(memtxt, memory);
    this.memory = new String("");
  }

  public abstract void clear();

  public abstract void remember(Deck cards);
  
  public abstract void remember(Card card);
  
  public abstract Card playCard(Trick trick);

  public abstract int bid(int bid); 

  public abstract int bid(int bid, int pbid, boolean opponents);

  public abstract int nameTrump();

  public abstract Deck passCards(boolean bidder);

  public abstract void takeCards(Deck d);

  public abstract int meld();

  public abstract void clearMeld();
}
