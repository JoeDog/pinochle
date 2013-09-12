package org.joedog.pinochle.view;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.SplashScreen;

public class Splash {
  private Graphics2D   g;
  private boolean      okay;
  final   SplashScreen splash;

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
    if (!okay) return;
    int sleep = 100;
    int w     = 20;
    int h     = 315;
    g.setComposite(AlphaComposite.Clear);
    g.fillRect(0,0,300,320);
    g.setPaintMode();
    g.setColor(Color.WHITE);
    if (message.equals("close")) {
      sleep = 750;
      g.drawString("Byron is ready ...", w, h);
    } else {
      g.drawString("Loading "+message+"...", w, h);
    }
    splash.update();
    try {
        Thread.sleep(sleep);
    } catch(InterruptedException e) { }
  }
}
