package org.joedog.pinochle.view;

import java.util.concurrent.TimeUnit;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
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
    int w     = 20;
    int h     = 500;

    if (!okay) return;

    g.setComposite(AlphaComposite.Clear);
    g.fillRect(0,0,490,510);
    g.setPaintMode();
    g.setColor(new Color(223,0,0));
    g.setFont(new Font("Helvetica", Font.BOLD, 24));
    g.drawString("Let's Play Pinochle", 20, 450);
    g.drawString("with Your JoeDog!!", 20, 475);
    g.setColor(Color.BLACK);
    g.setFont(new Font("Helvetica", Font.PLAIN, 12));

    if (message.equals("close")) {
      g.drawString("Pinochle is ready ...", w, h);
    } else {
      g.drawString("Loading "+message+"...", w, h);
    }
    splash.update();
    try {
      TimeUnit.SECONDS.sleep(1);
    } catch (Exception e) {}
  }
}
