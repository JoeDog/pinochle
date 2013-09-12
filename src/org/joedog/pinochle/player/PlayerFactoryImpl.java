package org.joedog.pinochle.player;

import org.joedog.pinochle.controller.GameController;

public class PlayerFactoryImpl implements PlayerFactory {
  
  public PlayerFactoryImpl() {
  }

  public Player getPlayer(GameController controller, int type) {
    if (type == Player.HUMAN) {
      return new Human(controller);
    } 
    if (type == Player.COMPUTER) {
      return new Computer(controller);
    }
    return null;
  }
}

