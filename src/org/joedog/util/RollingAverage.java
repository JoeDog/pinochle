package org.joedog.util;

import java.util.LinkedList;
import java.util.Queue;

public class RollingAverage {
  private final  Queue<Double> queue = new LinkedList<Double>();
  private double total;
  private int    window; 

  public RollingAverage() {
    this.window = 50;
  }
 
  public RollingAverage(int window) {
    this.window = window;
  }
 
  public void add(double num) {
    total += num;
    queue.add(num);
    if (queue.size() > this.window) {
      total -= queue.remove();
    }
  }

  public boolean isEmpty() {
    return (queue.isEmpty());
  }

  public boolean removeAll() {
    this.total        = 0;
    Queue<Double> tmp = queue;
    return queue.removeAll(tmp);    
  }

  public double average() {
    if (queue.isEmpty()) return 0;
    return (total / queue.size());
  }
}
