package org.joedog.pinochle.test;
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

import java.util.List;
import java.util.Map;

public class Scenarios {
  int current = 0;
  List<Map<Integer, ScenarioList<String>>> array = null;
  
  public Scenarios() {
    ScenarioParser sp = new ScenarioParser();
    if (sp.loaded()) {
      array = sp.getScenarios();
    }
  } 

  public boolean loaded() {
    return false;
    //return (array != null && array.size() > 0);
  } 
 
  public boolean hasNext() {
    return (array.size() > this.current);
  } 
   
  public Map<java.lang.Integer,ScenarioList<java.lang.String>> next() {
    Map<Integer, ScenarioList<String>> map = array.get(this.current);
    this.current++;
    if (this.hasNext() == false) {
      this.current = 0;
    }
    return map;
  }
}
