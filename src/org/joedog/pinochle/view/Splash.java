package org.joedog.pinochle.view;

import java.util.concurrent.TimeUnit;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.SplashScreen;

import org.joedog.pinochle.Version;

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
    int w     = 245;
    int h     = 190;

    if (!okay) return;

    g.setComposite(AlphaComposite.Clear);
    g.fillRect(0,0,490,510);
    g.setPaintMode();
    g.setColor(new Color(248,248,248));
    g.setFont(new Font("Helvetica", Font.BOLD, 24));
    g.drawString("Pinochle", 240, 90);
    g.setFont(new Font("Helvetica", Font.BOLD, 12));
    g.drawString("version: "+Version.version, w, 110);
    g.setFont(new Font("Helvetica", Font.BOLD, 10));
    g.drawString(Version.author, w, 124);
    g.drawString("© "+Version.copyright, w, 138);
    g.setColor(new Color(64,64,64));
    g.setFont(new Font("Helvetica", Font.PLAIN, 12));

    if (message.equals("close")) {
      g.drawString("Pinochle is ready ...",  w, h);
    } else {
      g.drawString("Loading "+message+"...", w, h);
    }
    splash.update();
    try {
      TimeUnit.SECONDS.sleep(1);
    } catch (Exception e) {}
  }
}
