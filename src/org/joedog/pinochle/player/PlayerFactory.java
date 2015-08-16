package org.joedog.pinochle.player;

import org.joedog.pinochle.control.Game;

public interface PlayerFactory {

  public Player getPlayer(Game controller, int type);
}

