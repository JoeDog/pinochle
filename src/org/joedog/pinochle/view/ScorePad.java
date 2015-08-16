package org.joedog.pinochle.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;

import org.joedog.util.TextUtils;
import org.joedog.util.RandomUtils;
import org.joedog.pinochle.control.Game;
import org.joedog.pinochle.control.Constants;

/**
 *
 * @author J. Fulmer <jeff@joedog.org>
 */
public class ScorePad extends BufferedImage implements Viewable {
  private Game control;
  private int width   = 240;
  private int height  = 130;
  private int winner  = 0;
  private String ns   = "Us";
  private String ew   = "Them";
  private String[][] cell  = new String[][] {
    {"",      ns,    ew}, // [0][0] blank  [0][1] Us,      [0][2] Them
    {"Meld",  "0",  "0"}, // [1][0] meld   [1][1] us meld  [1][2] them meld
    {"Take",  "0",  "0"}, // [2][0] take   [2][1] us take  [2][2] them take
    {"Total", "0",  "0"}, // [3][0] total  [3][1] us total [3][2] them total
    {"Score", "0",  "0"}  // [4][0] score  [4][1] us score [4][2] them score
  };

  public ScorePad(Game control) {
    super(240, 130, BufferedImage.TYPE_INT_RGB);
    this.control = control;
    this.control.addView(this);
    render();
  }

  public void reset() {
    this.winner = 0;
  }
    
  public void render(){
    int third    = (this.width/3);
    int fifth    = (this.height/5);
    String east  = this.control.getModelStringProperty("PlayerEastName");
    String west  = this.control.getModelStringProperty("PlayerWestName");
    String north = this.control.getModelStringProperty("PlayerNorthName");
    String south = this.control.getModelStringProperty("PlayerSouthName");

    if (north != null && north.length() > 0 && south != null && south.length() > 0) {
      cell[0][1] = south.substring(0, 1)+"/"+north.substring(0,1);
    } else {
      cell[0][1] = "Us";
    }
    if (east != null && east.length() > 0 && west != null && west.length() > 0) {
      cell[0][2] = east.substring(0, 1)+"/"+west.substring(0,1);
    } else {
      cell[0][2] = "Them";
    }

    Graphics2D g2 = (Graphics2D)this.getGraphics();
    g2.setColor(new Color(239,236,157));
    g2.fillRect(0, 0, this.width, this.height);
    g2.setColor(new Color(123,145,83)); 
    for (int i = third;  i < this.width; i+= third) {
      g2.drawLine(i, 0, i, this.height);
    }
    for (int i = fifth;  i < this.height; i+= fifth) {
      g2.setColor(new Color(123,145,83)); 
      g2.drawLine(0, i, this.width, i);
    }
    int cpad = 0;
    int vpad = 0;
    g2.setColor(new Color(33, 33, 33));
    for (int i = 0; i < cell[0].length; i++) {
      for (int j = 0; j < cell.length; j++) {
        if (i == 0 || j == 0) {
          cpad = 10; // label padding
          g2.setFont(new Font("Tahoma", Font.BOLD, 12));
        } else {
          cpad = (cell[j][i]).length()*5; // text padding based on string length
          cpad += RandomUtils.range(-1,1);
          vpad =  RandomUtils.range(-1,1);
          g2.setFont(new Font("Tahoma", Font.PLAIN, 12));
        }
        g2.drawString(cell[j][i], (i*third)+(third/2)-cpad, (j*fifth)+(fifth/2)+5+vpad);
        /** draw a circle around the winner XXX: need to reset this.winner on new game */
        if (this.winner == 1 && (j == 4 && i == 1)) {
          Font f = new Font("Helvetica", Font.ITALIC, 32);
          g2.setFont(f);
          //g2.setColor(new Color(46, 184, 46));
          g2.setColor(new Color(0, 153, 255));
          g2.drawString("0", (i*third)+(third/2)-(cpad/2)-15, (j*fifth)+(fifth/2)+5+(vpad/2)+8); 
          g2.setColor(new Color(33, 33, 33));
        }
        if (this.winner == 2 && (j == 4 && i == 2)) {
          Font f = new Font("Helvetica", Font.ITALIC, 32);
          g2.setFont(f);
          //g2.setColor(new Color(46, 184, 46));
          g2.setColor(new Color(0, 153, 255));
          g2.drawString("0", (i*third)+(third/2)-(cpad/2)-15, (j*fifth)+(fifth/2)+5+(vpad/2)+8); 
          g2.setColor(new Color(33, 33, 33));
        }
        /** end of draw circle around the winner */
      }
    }
  }

  public void modelPropertyChange(PropertyChangeEvent e) {
    if (e.getNewValue() == null) return;
    if (e.getPropertyName().equals(Constants.MELD)) {
      int [] meld = TextUtils.toArray((String)e.getNewValue());
      this.cell[1][1] = ""+meld[0];  
      this.cell[1][2] = ""+meld[1];
      this.render();
    }
    if (e.getPropertyName().equals(Constants.TAKE)) {
      int [] take = TextUtils.toArray((String)e.getNewValue());
      this.cell[2][1] = ""+take[0];  
      this.cell[2][2] = ""+take[1];
      this.render();
    }
    if (e.getPropertyName().equals(Constants.TOTAL)) {
      int [] total = TextUtils.toArray((String)e.getNewValue());
      this.cell[3][1] = ""+total[0];  
      this.cell[3][2] = ""+total[1];
      this.render();
    }
    if (e.getPropertyName().equals(Constants.SCORE)) {
      int [] score = TextUtils.toArray((String)e.getNewValue());
      this.cell[4][1] = ""+score[0];  
      this.cell[4][2] = ""+score[1];
      this.render();
    }
    if (e.getPropertyName().equals(Constants.WINNER)) {
      int [] score = TextUtils.toArray((String)e.getNewValue());
      if (score[0] > score[1]) this.winner = 1;
      if (score[0] < score[1]) this.winner = 2;
      this.render(); 
    }
    if (e.getPropertyName().equals(Constants.NAMES)) {
      this.render();
    }
  }
}


