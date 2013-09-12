package org.joedog.pinochle.view;

import java.awt.Toolkit;
import javax.swing.KeyStroke;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButtonMenuItem;
import org.joedog.pinochle.view.actions.*;

public class MenuView extends JMenuBar {
  private String fileItems[]  = new String [] {"New", "Exit"};
  private String prefItems[]  = new String [] {"Configure...", "Guru"};
  char    fileShorts[] = {'N', 'X'};
  char    prefShorts[] = {'C', 'G'};
  private JMenu  fileMenu; 
  private JMenu  prefMenu;
  private GameActions actions;
  
  public MenuView (GameActions actions) {
    this.actions  = actions;    
    this.fileMenu = new JMenu("File");
    this.prefMenu = new JMenu("Preferences");
    this.setup();
  }
 
  public void setup () {
    // File Menu
    for (int i = 0; i < fileItems.length; i++) {
      JMenuItem item = new JMenuItem(fileItems[i]);
      item.setAccelerator(
        KeyStroke.getKeyStroke(fileShorts[i], Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(), false)
      );
      item.addActionListener(actions.getAction(fileItems[i]));
      fileMenu.add(item);
    }
    
    for (int i = 0; i < prefItems.length; i++) {
      JMenuItem item = new JMenuItem(prefItems[i]);
      item.setAccelerator(
        KeyStroke.getKeyStroke(prefShorts[i], Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(), false)
      );
      item.addActionListener(actions.getAction(prefItems[i]));
      prefMenu.add(item);
    }
    
    this.add(fileMenu);
    this.add(prefMenu);
  } 
}
