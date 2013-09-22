package org.joedog.pinochle.model;

public class Score  {
  private int a;
  private int b;

  public Score (int a, int b) {
    this.a = a;
    this.b = b;
  }

  public Integer getA() {
    return (Integer)this.a;
  }

  public Integer getB() {
    return (Integer)this.b;
  }
}
