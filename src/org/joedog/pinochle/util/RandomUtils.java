package org.joedog.pinochle.util;

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

    int randomNum = rand.nextInt((max - min) + 1) + min;
    return randomNum;
  }

  public static int number(int max) {
    Random r = new Random();
    return r.nextInt(max) + 1;
  }
}
