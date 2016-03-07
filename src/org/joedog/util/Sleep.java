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
