package org.joedog.pinochle.player;

import org.joedog.pinochle.controller.GameController;

public interface PlayerFactory {

  public Player getPlayer(GameController controller, int type);
}

