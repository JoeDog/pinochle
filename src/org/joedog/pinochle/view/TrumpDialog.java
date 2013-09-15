package org.joedog.pinochle.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.joedog.pinochle.game.Pinochle;

public class TrumpDialog extends JOptionPane {

  public TrumpDialog() {
    JFrame frame = new JFrame();
    this.setMessage("Select Trump");
    this.setMessageType(JOptionPane.INFORMATION_MESSAGE);
    String [] suits = {"Hearts", "Clubs", "Diamonds", "Spades"};
    Icon icons[] = new ImageIcon[4];
    for (int i = 0; i < 4; i++) {
      icons[i] = new TrumpIcon(i);
    }
    //icons[0] = new ImageIcon("../images/cards/hearts.png");
    //icons[1] = new ImageIcon("../images/cards/clubs.png");
    //icons[2] = new ImageIcon("../images/cards/diamonds.png");
    //icons[3] = new ImageIcon("../images/cards/spades.png");
    Object objects[] = new Object[4];
    for (int i = 0; i < 4; i++) {
      objects[i] = getButton(this, suits[i], icons[i]);
    }
    this.setOptions(objects);
    JDialog dialog = this.createDialog(frame, "Select Trump");
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

  /*public static void main (String args[]) {
    TrumpDialog td = new TrumpDialog();
    Object value   = td.getValue();
    System.out.println((String)value);
  }*/
}
