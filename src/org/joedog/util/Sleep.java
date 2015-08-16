package org.joedog.util;

public final class Sleep {
 
  /**
   * Puts the thread to sleep (cease operation) 
   * for n milliseconds, subject to the precision 
   * and accuracy of system timers and schedulers.
   * <p>
   * @param  long   A duration in milliseconds.
   * @return void   
   * @see    Thread.sleep
   */ 
  public static void milliseconds(long n) {
    nap(n);
  }

  /**
   * Puts the thread to sleep (cease operation)
   * for n seconds, subject to the precision
   * and accuracy of system timers and schedulers.
   * <p>
   * @param  long   A duration in seconds.
   * @return void
   * @see    Thread.sleep
   */
  public static void seconds(long n) {
    nap(n*1000);
  }

  /**
   * Puts the thread to sleep (cease operation)
   * for n minutes, subject to the precision
   * and accuracy of system timers and schedulers.
   * <p>
   * @param  long   A duration in minutes.
   * @return void
   * @see    Thread.sleep
   */
  public static void minutes(long n) {
    nap(n*60000);
  }

  /**
   * Puts the thread to sleep (cease operation)
   * for n hours, subject to the precision
   * and accuracy of system timers and schedulers.
   * <p>
   * @param  long   A duration in hours.
   * @return void
   * @see    Thread.sleep
   */
  public static void hours(long n) {
    nap(n*3600000);
  }

  private static void nap(long n) {
    try {
      Thread.sleep(n);
    } catch (Exception e) {
      // It's an interrupted exception; no one cares....
    }
  }
}
