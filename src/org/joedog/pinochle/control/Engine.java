package org.joedog.pinochle.control;

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
