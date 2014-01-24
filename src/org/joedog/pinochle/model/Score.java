package org.joedog.pinochle.model;

import java.io.Serializable; 

public class Score implements Serializable {
  private String name;
  private int    score;
  static final long serialVersionUID = -3611946473283033478L;

  public Score (String name, int score) {
    this.name  = name;
    this.score = score;
  }

  public String getName() {
    return this.name;
  }

  public int getScore() {
    return this.score;
  }

  public void setName(String name) {
    this.name = name;
    return;
  }

  public void setScore(int score) {
    this.score = score;
    return;
  }

  public int compare (Score score) {
    int x = ((Integer)this.score).compareTo((Integer)score.score);
    return x;
  }
}
