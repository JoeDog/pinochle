package org.joedog.util;

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
