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

import java.util.Random;

public final class RandomUtils {
  
  /**
   * Returns a psuedo random int between min and max
   * <p>
   * @param  int    the minimum number in the range
   * @param  int    the maximum number in the range
   * @return int    a psuedo random number between min and max
   */
  public static int range(int min, int max) {
    Random rand = new Random();

    int randomNum = rand.nextInt((max+1) - min) + min;
    return randomNum;
  }

  public static int number(int max) {
    Random r = new Random();
    return r.nextInt(max);
  }

  public static float delta() {
    return range(-999, 999) / 1000;
  }

  public static float weight() {
    return range(-2000, 2000) / 1000;
  }
}
