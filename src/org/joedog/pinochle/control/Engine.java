package org.joedog.pinochle.control;
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

import org.joedog.pinochle.view.View;
import org.joedog.util.Sleep;

public class Engine {
  private View  view   = null;
  private GameRenderer  engine;

  public Engine(View view) {
    this.view   = view;
    this.engine = new GameRenderer();
  }

  public void start() {
    this.engine.start();
  }

  public boolean paused() {
    return false;
  }

  private class GameRenderer implements Runnable {
    private Thread  thread;
    private boolean running = false;

    public void start() {
      if (!running) {
         this.running = true;
         thread = new Thread(this);
         thread.start();
      }
    }

    public void stop() {
      thread.interrupt();
      running = false;
    }

    public void run() {
      while (running) {
        long delta = 0l;
  
        while (view == null) {
          System.out.println("NULL");
          Sleep.milliseconds(100);
        }

        view.requestFocus();

        while (true) {
          long lastTime = System.nanoTime();

          view.action();
          delta = System.nanoTime() - lastTime;
          
          if (delta < 10000000L) {
            Sleep.milliseconds((10000000L - delta) / 1000000L);
          }
          //if (delta < 20000000L) {
          //  Sleep.milliseconds((20000000L - delta) / 1000000L);
          //}
        }
      }
    }
  }
}
