package org.joedog.pinochle.view;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.SplashScreen;


public class Splash {
  final   SplashScreen splash;

  private Graphics2D   g;
  private boolean      okay;

  public Splash() {
    okay   = true;
    splash = SplashScreen.getSplashScreen();

    if (splash == null) {
      okay = false;
      return;
    }

    g = splash.createGraphics();
    if (g == null) {
      okay = false;
      return;
    }
  }

  public void close() {
    if (!okay) return;
    setMessage("close");
    splash.close();
  }

  public void setMessage(String message) {
    int sleep = 400;
    int w     = 20;
    int h     = 315;

    if (!okay) return;

    g.setComposite(AlphaComposite.Clear);
    g.fillRect(0,0,300,320);
    g.setPaintMode();
    g.setColor(Color.WHITE);

    if (message.equals("close")) {
      sleep = 1000;
      g.drawString("Byron is ready ...", w, h);
    } else {
      g.drawString("Loading "+message+"...", w, h);
    }
    splash.update();
    // A poor man's sleep
    long cT = System.currentTimeMillis();  
    while (System.currentTimeMillis() - cT < sleep);
  }
}
