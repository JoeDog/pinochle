package org.joedog.pinochle.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.security.*;
import javax.swing.table.*;

public class HighScoreTableModel extends AbstractTableModel {
  private String[]   headers = {"Name", "Score"};
  private File       file;
  private String     path;
  private HighScores scores; 
  static final long serialVersionUID = -3611946473283033478L;

  public HighScoreTableModel () {
    AccessController.doPrivileged(new PrivilegedAction<Object>() {
      public Object run() {
        path = System.getProperty("pinochle.scores");
        file = new File(path);
        return null;
      }
    });
    try {
      scores = this.read();
    } catch (Exception e) {} 
  } 

  public void add(String name, int score) {
    if ((score > 0.0) && (scores.isHighScore(score))) {
      scores.add(new Score(name, score));
      this.save();
    } 
  } 

  public int save() {
    try {
      this.write(scores);
    } catch (Exception e) {}
    return 1;
  }
  
  public boolean isCellEditable(int row, int col) {
    Score score = (Score)this.scores.get(row);
    switch (col) {
    case 0:
      if (score.getName().equals("Your Name")) {
        return true;
      } else {
        return false;
      }
    case 1:
       return false;
    }
    throw new IllegalArgumentException("Bad selection ("+row+", "+col+")");
  } 

  public void setValueAt(Object o, int row, int col) {
    Score score = (Score)this.scores.get(row);
    switch (col) {
      case 0:
        score.setName((String)o);
        break;  
      case 1:
        break;
    }
    fireTableDataChanged();
    this.save();
  }
  
  public Object getValueAt(int row, int col) {
    Score score = (Score)this.scores.get(row);
    switch(col) {
    case 0: 
      return new String(score.getName());
    case 1:
      return new Double(score.getScore());
    }
    throw new IllegalArgumentException("Bad selection ("+row+", "+col+")");
  } 

  public String getColumnName(int col) { return headers[col]; }
  public int    getRowCount()          { return scores.size(); }
  public int    getColumnCount()       { return headers.length; }

  private HighScores read () throws IOException, ClassNotFoundException {
    SecurityManager sm = System.getSecurityManager();
    if (sm != null) {
      sm.checkPermission(new HighScoresPermission("byron"));
    }
    if (!file.exists()) {
      System.out.println("Error: "+path+" does not exist.");
      return new HighScores();
    }

    HighScores scores = null;

    try {
      scores = (HighScores) AccessController.doPrivileged(new PrivilegedExceptionAction() {
        public Object run() throws IOException, ClassNotFoundException {
          HighScores tmp = null;
          FileInputStream fis = new FileInputStream(file);
          ObjectInputStream ois = new ObjectInputStream(fis);
          tmp = (HighScores) ois.readObject();
          return tmp;
        }
      });
    } catch (PrivilegedActionException pae) {
      Exception e = pae.getException();
      e.printStackTrace();
      if (e instanceof IOException)
        throw (IOException) e;
      else
        throw (ClassNotFoundException) e;
    }
    return scores;
  }

  private void write (final HighScores scores) throws IOException {
    SecurityManager sm = System.getSecurityManager();
    if (sm != null) {
      sm.checkPermission(new HighScoresPermission("byron"));
    }

    try {
      AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
        public Object run() throws IOException {
          FileOutputStream fos   = new FileOutputStream(file);
          ObjectOutputStream oos = new ObjectOutputStream(fos);
          oos.writeObject(scores);
          oos.close();
          return null;
        }
      });
    } catch (PrivilegedActionException pae) {
      throw (IOException) pae.getException();
    }
  }

}
