package org.joedog.pinochle.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.joedog.pinochle.game.Pinochle;

public class TrumpDialog extends JOptionPane implements View {

  public TrumpDialog() {
    this.setMessage("Select Trump");
    this.setMessageType(JOptionPane.INFORMATION_MESSAGE);
    String [] suits = {"Hearts", "Clubs", "Diamonds", "Spades"};
    Icon icons[] = new ImageIcon[4];
    for (int i = 0; i < 4; i++) {
      icons[i] = new TrumpIcon(i);
    }
    Object objects[] = new Object[4];
    for (int i = 0; i < 4; i++) {
      objects[i] = getButton(this, suits[i], icons[i]);
    }
    this.setOptions(objects);
    final JDialog dialog = this.createDialog(null, "Select Trump");
    dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    dialog.setLocationRelativeTo(null);
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        dialog.setVisible(true);
      }
    });
  }

  public static JButton getButton(final JOptionPane optionPane, final String text, Icon icon) {
    final JButton button = new JButton(icon);
    ActionListener actionListener = new ActionListener() {
      public void actionPerformed(ActionEvent actionEvent) {
        optionPane.setValue(text);
      }
    };
    button.addActionListener(actionListener);
    return button;
  }

  public void modelPropertyChange(PropertyChangeEvent e) {
    if (e.getNewValue() == null) return;
  }
}


