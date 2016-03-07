package org.joedog.pinochle.view;
/**
 * Copyright (C) 2013-2016
 * Jeffrey Fulmer - <jeff@joedog.org>, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *--
 */

import java.awt.Toolkit;
import javax.swing.KeyStroke;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButtonMenuItem;
import org.joedog.pinochle.view.Menu;
import org.joedog.pinochle.view.actions.*;

public class Menu extends JMenuBar {
  private String fileItems[]  = new String [] {"New", "Exit"};
  private String prefItems[]  = new String [] {"Configure...", "Scores..."};
  char    fileShorts[] = {'N', 'X'};
  char    prefShorts[] = {'C', 'S'};
  private JMenu  fileMenu;
  private JMenu  prefMenu;
  private GameActions actions;

  public Menu (GameActions actions) {
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

