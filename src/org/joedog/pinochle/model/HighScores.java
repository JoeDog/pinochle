package org.joedog.pinochle.model;

import java.util.List;
import java.util.ArrayList;

public class HighScores extends ArrayList {
  private final static int LIMIT     = 9;
  static final long serialVersionUID = -3611946473283033478L;

  /**
   * Constructor - HighScores is a list of single column Score objects
   */
  public HighScores () {
  }

  @Override 
  public boolean add (Object score) {
    if (this.size() >= LIMIT) {
      int d = ((Score)this.get(this.size()-1)).getScore(Score.ONE);
      if (((Score)score).getScore(Score.ONE) < d) {
        return false;
      } else {
        for (int i = LIMIT; i < this.size(); i++) {
          this.remove(i);
        }
      }
    }
    super.add(score);
    this.sort();
    return true;
  } 

  public boolean isHighScore (int score) {
    if (this.size() < LIMIT) return true;

    int min = ((Score)this.get(0)).getScore(Score.ONE);
    for (int i = 0; i < this.size(); i ++) {
      int tmp = ((Score)this.get(i)).getScore(Score.ONE);
      if (tmp < min) {
        min = tmp;
      }
    }
    return (score > min);
  }

  public void sort () {
    for (int i = 0; i < this.size(); i++) {
      Score val = (Score)this.get(i);
      int j;
      for (j = i-1; j > -1; j--) {
        Score tmp = (Score)this.get(j);
        if (tmp.compare(val) >= 0) {
          break; 
        }
        this.set(j+1, tmp);
      }
      this.set(j+1, val);
    }
  }
}
