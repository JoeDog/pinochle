package org.joedog.pinochle.model;
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

import java.lang.NumberFormatException;
import org.joedog.pinochle.control.*;
import org.joedog.util.DateUtils;

public class HighScoreModel extends AbstractModel {
  HighScoreTableModel model = new HighScoreTableModel();
 
  /**
   * This is a convenience class that extends the
   * abstract model for high score management....
   */
  public HighScoreModel() { }

  public void save() {}

  /**
   * Rather than instantiate a score somewhere
   * else, we'll just ask the caller to send us
   * names|score and we'll do the parsing here...
   */
  public void setHighScore(String line) {
    String   names = null;
    int      value = 0;
    String[] array = line.split("\\|",-1);  
    try {
      names = array[0];
      value = Integer.parseInt(array[1]);
    } catch (NumberFormatException e) {
      value = 0;
    }
    if (value == 0) return;
    model.add(names, value, DateUtils.now()); 
  }
}

