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
import org.joedog.pinochle.control.GameController;


public class TrumpDialog extends JOptionPane implements View {
  GameController controller;

  /** 
   * This object displays a dialog from which a human player
   * can select a trump suit from one of four buttons. Human
   * can circumvent the dialog by closing the window. If that
   * happens, player.Human will default to Pinochle.SPADES
   */
  public TrumpDialog(GameController controller) {
    this.controller = controller;
    this.setMessage("Select Trump");
    this.setMessageType(JOptionPane.INFORMATION_MESSAGE);
    this.setValue("Spades");
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
    // We rely on the same coordinates captured by our BidDialog
    int xpos = (controller.getIntProperty("DialogX") - 50);
    int ypos = (controller.getIntProperty("DialogY") - 50);
 
    final JDialog dialog = this.createDialog(null, "Select Trump");
    dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    dialog.setLocation(xpos, ypos);
    dialog.setVisible(true);
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


