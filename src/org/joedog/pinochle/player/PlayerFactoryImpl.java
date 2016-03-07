package org.joedog.pinochle.player;
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

import org.joedog.pinochle.control.Game;

public class PlayerFactoryImpl implements PlayerFactory {
  
  public PlayerFactoryImpl() {
  }

  public Player getPlayer(Game controller, int type) {
    if (type == Player.HUMAN) {
      return new Human(controller);
    } 
    if (type == Player.COMPUTER) {
      return new Computer(controller);
    }
    return null;
  }
}

