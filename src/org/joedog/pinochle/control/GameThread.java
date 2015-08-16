package org.joedog.pinochle.control;

import java.lang.Thread;

public class GameThread extends Thread {
  private Game game;

  public GameThread(Game game) {
    this.game = game;
    Thread.UncaughtExceptionHandler h = new Thread.UncaughtExceptionHandler() {
      public void uncaughtException(Thread th, Throwable ex) {
        ;; // System.out.println("Uncaught exception in Game Thread: " + ex);
           // ex.printStackTrace();
      }
    };
    this.setUncaughtExceptionHandler(h);
  }

  public void run() {
    game.start();
  }
}
