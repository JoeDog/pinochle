package org.joedog.pinochle.model;

import java.lang.NumberFormatException;
import org.joedog.pinochle.control.*;

public class HighScoreModel extends AbstractModel {
  HighScoreTableModel model = new HighScoreTableModel();
 
  /**
   * This is a convenience class that extends the
   * abstract model for high score management....
   */
  public HighScoreModel() { }

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
    model.add(names, value); 
  }
}

