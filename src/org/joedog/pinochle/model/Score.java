package org.joedog.pinochle.model;

import java.io.Serializable; 

public class Score implements Serializable {
  private String name;
  private String stamp;
  private int    score = 0;
  static final long serialVersionUID = -3611946473283033478L;

  public Score (String name, int score, String stamp) {
    this.name  = name;
    this.score = score;
    this.stamp = stamp;
  }

  public String getName() {
    return (this.name == null) ? "" : this.name;
  }

  public int getScore() {
    return this.score;
  }

  public String getStamp() {
    return (this.stamp == null) ? "" : this.stamp;
  }

  public void setName(String name) {
    this.name = name;
    return;
  }

  public void setScore(int score) {
    this.score = score;
    return;
  }

  public void setStamp(String stamp) {
    this.stamp = stamp;
    return;
  }

  public int compare (Score score) {
    int x = ((Integer)this.score).compareTo((Integer)score.score);
    return x;
  }
}
