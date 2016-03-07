package org.joedog.util;
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
