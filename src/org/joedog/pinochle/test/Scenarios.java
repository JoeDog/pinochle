package org.joedog.pinochle.test;

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
    return (array != null && array.size() > 0);
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
