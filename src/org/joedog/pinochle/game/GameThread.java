package org.joedog.pinochle.game;

import java.lang.Thread;

public class GameThread extends Thread {
  private Game game;

  public GameThread(Game game) {
    this.game = game;
    Thread.UncaughtExceptionHandler h = new Thread.UncaughtExceptionHandler() {
      public void uncaughtException(Thread th, Throwable ex) {
        System.out.println("Uncaught exception: " + ex);
      }
    };
    this.setUncaughtExceptionHandler(h);
  }

  public void run() {
    game.start();
  }
}
