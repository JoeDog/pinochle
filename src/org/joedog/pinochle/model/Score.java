package org.joedog.pinochle.model;

import java.util.List;
import java.util.ArrayList;
import java.io.Serializable; 

public class Score implements Serializable {
  public static final int ONE   = 0;
  public static final int TWO   = 1;
  public static final int THREE = 2;
  public static final int FOUR  = 3;
  public static final int FIVE  = 4;

  private int size;
  private ArrayList<Integer> scores = new ArrayList<Integer>();
  private ArrayList<String>  names  = new ArrayList<String>();
  private ArrayList<String>  stamps = new ArrayList<String>();

  public Score() {
    this.size = 2;
  }

  public Score(int size) {
    this.size = 2;
  }

  /**
   * Convenience constructor for the HighScores list; 
   * each score is, by necessity, single-columned
   * <p>
   * @param  String  Name of the team that posted it
   * @param  int     Score value
   * @param  String  A timestamp in String form
   * @return Score
   */
  public Score(String name, int score, String stamp) {
    this.names.add (Score.ONE, name);  
    this.scores.add(Score.ONE, score);  
    this.stamps.add(Score.ONE, stamp);  
  }

  public Score(int [] arr) {
    this.size = arr.length;
    int[] ints = {1, 2, 3};
    List<Integer> list = new ArrayList<Integer>();
    for (int i = 0; i < arr.length; i++) {
      list.add(arr[i]);
    }
    this.scores.addAll(list);    
  }
	
  public Score(int s1, int s2) {
    this.size = 2;
    this.scores.add(Score.ONE, s1);
    this.scores.add(Score.TWO, s2);
  }

  public int size() {
    return this.size;
  }

  public void reset() {
    this.scores.set(Score.ONE, 0);
    this.scores.set(Score.TWO, 0);
  }

  public void add(Score s) {
    if (s.size() != this.size()) {
      throw new ScoreMismatchException();
    }
    for (int i = 0; i < this.size(); i++) {
      int t1 = s.getScore(i);
      int t2 = this.getScore(i);
      this.setScore(i, (t1+t2)); 
    }
  }

  public void setScore(int col, int score) {
    if (col > this.size()) {
      new ScoreMismatchException("Assigning score to unknown column");
    } 
    this.scores.set(col, score); 
  }

  public int getScore(int col) {
    if (col > this.size) {
      throw new ScoreMismatchException("Request for an unknown scoring column");
    }
    return this.scores.get(col);
  } 

  public String getName(int col) {
    if (col > this.size()) {
      throw new ScoreMismatchException("Retrieving name from unknown column");
    } 
    return this.names.get(col); 
  }

  public void setName(int col, String name) {
    if (col > this.size()) {
      throw new ScoreMismatchException("Assigning name to unknown column");
    } 
    this.names.set(col, name); 
  }

  public void setStamp(int col, String stamp) {
    if (col > this.size()) {
      throw new ScoreMismatchException("Assigning stamp to unknown column");
    } 
    this.stamps.set(col, stamp); 
  }

  public String getStamp(int col) {
    if (col > this.size()) {
      throw new ScoreMismatchException("Retrieving stamp from unknown column");
    } 
    return this.stamps.get(col); 
  }

  /**
   * Compares the scores of two single column Scores
   * <p> 
   * @param  Score  The Score against which we compare this one
   * @return int
   */
  public int compare (Score score) {
    int x = ((Integer)this.getScore(Score.ONE)).compareTo((Integer)score.getScore(Score.ONE));
    return x;
  } 

  @Override
  public String toString() {
    String str = "(";
    for (int i = 0; i < this.size(); i++) {
      str += this.getScore(i);
      if (i < (this.size() - 1)) {
        str += ",";
      }
    }
    str += ")";
    return str;
  }

  class ScoreMismatchException extends RuntimeException {
    private static final long serialVersionUID = 6529685098267757690L;
    public ScoreMismatchException() {
      super("The scoring columns do not align");
    }
    public ScoreMismatchException(Throwable throwable) {
      super("The scoring columns do not align", throwable);
    }
    public ScoreMismatchException(String message) {
      super(message);
    }
    public ScoreMismatchException(String message, Throwable throwable) {
      super(message, throwable);
    }
  }
}
