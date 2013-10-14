package org.joedog.pinochle.game;

import java.lang.Thread;

public class GameThread extends Thread {
  private Game game;

  public GameThread(Game game) {
    this.game = game;
  }

  public void run() {
    game.play();
  }
}
