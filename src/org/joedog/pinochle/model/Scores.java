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
import java.util.Map;
import java.util.ArrayList;

public class Scores {
  private Score  sum;
  private Score  bid;
  private Score  winner;
  private ArrayList<Map<String, Score>> scores;
  private Map<String, Score> data;
   
  /**
   * Constructor 
   */
  public Scores() {
    this.bid    = new Score(0,0); 
    this.sum    = new Score(0,0);
    this.winner = new Score(0,0);
    this.scores = new ArrayList<Map<String, Score>>();
    this.data   = new HashMap<String, Score>();
  } 

  /**
   * Deletes all entries in the ArrayList and resets
   * the score back to (0,0). 
   * <p>
   * @param  none
   * @return void
   */
  public void clear() {
    this.scores.removeAll(scores);
    this.bid.reset();
    this.sum.reset();
    this.winner.reset();
  }

  private Score getter(String key, int round) {
    if ((round - 1) > this.scores.size()) return new Score(0,0);

    HashMap<String, Score> data = (HashMap<String, Score>)scores.get(round-1);
    Score s = data.get(key);
    return (s == null) ? new Score (0,0) : s;
  }

  private void adder(String key, int round, Score s) {
    HashMap<String, Score> tmp;
    if (this.scores.size() < round) {
      tmp = new HashMap<String, Score>();
      this.scores.add(round-1, tmp); 
    } else {
      tmp = (HashMap<String, Score>)this.scores.get(round-1);
    }
    tmp.put(key, s);
  }

  /**
   * Returns the meld as a Score object
   * <p>
   * @param  int   the round of play for which we want a result
   * @return Score
   */
  public Score getMeld(int round) {
    return this.getter("meld", round);
  }

  /**
   * Returns the take as a Score object
   * <p>
   * @param  int   the round of play for which we want a result
   * @return Score
   */
  public Score getTake(int round) {
    return this.getter("take", round);
  }

  /**
   * Returns the round total as a Score object
   * <p>
   * @param  int   the round of play for which we want a result
   * @return Score 
   */
  public Score getTotal(int round) {
    return this.getter("total", round);
  }

  /**
   * Returns the round total as a Score object
   * <p>
   * @param  void    
   * @return Score  Returns the running total
   */
  public Score getScore() {
    return this.sum;
  }

  /**
   * Returns true if we have a winner, false if we do not
   * <p>
   * @param  void
   * @return boolean
   */
  public boolean hasWinner() {
    return (this.winner.getScore(Score.ONE) > 0 || this.winner.getScore(Score.TWO) > 0);
  }

  /**
   * Add a new bid for the round; used to calculate 
   * the hand total. 
   * <p>
   * @param  Score  A score object representing the bid
   *                Only the bidder has a positive number
   *                The non-bidder is represented as zero
   *                The bid applies only to the current round
   * @return void 
   */
  public void addBid(Score bid) {
    this.bid = bid;
  }

  /**
   * Add the meld score for the round.
   * <p>
   * @param  int    The round for which we're adding meld
   * @param  Score  A score object which represents each
   *                team's meld score: (us, them)
   * @return void
   */
  public void addMeld(int round, Score score) {
    this.adder("meld", round, score);
  }

  /**
   * Add the take for the round. A call to this method
   * triggers a calculation which scores the hand.
   * <p>
   * @param  int   The round for which we're adding the take
   * @param  Score A score object representing each team's take
   * @return void
   */
  public void addTake(int round, Score score) {
    Score t = this.getTake(round);
    int s1  = t.getScore(Score.ONE)+score.getScore(Score.ONE);
    int s2  = t.getScore(Score.TWO)+score.getScore(Score.TWO);
    this.adder("take", round, new Score(s1, s2));
    this.total(round, -1); // keep a running total...
    this.sum();
  }

  /**
   * Tallies the complete score for the round (hand)
   * <p>
   * @param  int     round/hand number for which we want a total
   * @return boolean true=winner, false=no winner
   */
  public boolean total(int round, int win) {
    Score m  = this.getMeld(round);
    Score t  = this.getTake(round);
    int   s1 = m.getScore(Score.ONE)+t.getScore(Score.ONE);
    int   s2 = m.getScore(Score.TWO)+t.getScore(Score.TWO);
    int   b1 = this.bid.getScore(Score.ONE);
    int   b2 = this.bid.getScore(Score.TWO);
    if (b1 > 0) {
      s1 = (s1 >= b1) ? s1 : (b1 *= -1);
    }
    if (b2 > 0) {
      s2 = (s2 >= b2) ? s2 : (b2 *= -1);
    }
    this.adder("total", round, new Score(s1, s2));
    this.sum();

    // we pass a negative number when we keep a running total
    if (win < 0) return false;
   
    /**
     * Determine if we have a winner.
     */ 
    if (this.sum.getScore(Score.ONE) > win && this.sum.getScore(Score.TWO) > win) {
      // if both are over win, then the bidder wins out
      this.winner = (this.bid.getScore(Score.ONE) > this.bid.getScore(Score.TWO)) ? new Score(1,0) : new Score(0,1);
    } else if (this.sum.getScore(Score.ONE) > win || this.sum.getScore(Score.TWO) > win) {
      // if just one is greater than win, the high score wins out
      this.winner = (this.sum.getScore(Score.ONE) > win) ? new Score(1,0) : new Score(0,1);
    }
    return (this.winner.getScore(Score.ONE) > 0 || this.winner.getScore(Score.TWO) > 0);
  }

  /**
   * Tallies the game total score; sum of all hands
   * <p>
   * @param  void
   * @return void
   */
  public void sum() {
     int s1 = 0;
     int s2 = 0;
     for (int i = 0; i < this.scores.size(); i++) {
       HashMap<String, Score> tmp = (HashMap<String, Score>) this.scores.get(i);
       Score s = (Score)tmp.get("total");
       if (s == null) {
         return;
       }
       s1 += s.getScore(Score.ONE);
       s2 += s.getScore(Score.TWO);
     }
     this.sum.setScore(Score.ONE, s1);
     this.sum.setScore(Score.TWO, s2);
   }

   /**
    * Returns a String interpretation of game scoring in score pad format
    * <p>
    * @param  void
    * @return String   In score pad format 
    */
   @Override
   public String toString() {
     String sep = System.getProperty("line.separator");

     this.sum();
     StringBuffer sb = new StringBuffer();
     sb.append("      |   Us  |  Them "+sep);
     sb.append("------+-------+-------"+sep);
     for (int i = 0; i < this.scores.size(); i++) {
       HashMap<String, Score> tmp = (HashMap<String, Score>) this.scores.get(i);
       String meld = String.format("%5s ", "Meld");
       String take = String.format("%5s ", "Take");
       String hand = String.format("%5s ", "Hand");
       Score  mscr = (Score)tmp.get("meld");
       Score  tscr = (Score)tmp.get("take");
       Score  hscr = (Score)tmp.get("total");
       sb.append(meld +"|"+String.format("%6s ", mscr.getScore(Score.ONE))+"|"+String.format("%6s", mscr.getScore(Score.TWO))+sep);
       sb.append(take +"|"+String.format("%6s ", tscr.getScore(Score.ONE))+"|"+String.format("%6s", tscr.getScore(Score.TWO))+sep);
       sb.append(hand +"|"+String.format("%6s ", hscr.getScore(Score.ONE))+"|"+String.format("%6s", hscr.getScore(Score.TWO))+sep);
       sb.append("------+-------+-------"+sep);
     }
     String total= String.format("%5s ", "Total");
     sb.append(total+"|"+String.format("%6s ", sum.getScore(Score.ONE)) +"|"+String.format("%6s", sum.getScore(Score.TWO))+sep);
     return sb.toString();
   }
}
