package org.joedog.pinochle.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;

import org.joedog.pinochle.control.Game;
import org.joedog.pinochle.control.Constants;

public class BidDialog extends JOptionPane implements Viewable {
  private Game control;
  private List<JComponent> components;
  private JFrame frame = new JFrame();
  private String title;
  private int messageType;
  private JRootPane rootPane;
  private JDialog  dialog;
  private String[] options;
  private JSlider slider;
  private JButton okay;
  private JButton pass;
  private int     bid;
  private int     x,y;

  static final long serialVersionUID = -2425571907198904444L;

  public BidDialog(Game control, int bid) {
    this.setTitle("Make a bid");
    this.control    = control;
    this.bid        = bid;
    this.components = new ArrayList<>();
    this.slider     = getSlider(this, bid, bid+16);
    this.okay       = getOkayButton("Bid", slider);
    this.pass       = getPassButton("Pass"); 
    this.control.addView(this);
    this.createAndShowGui();
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void addComponent(JComponent component) {
    components.add(component);
  }

  private void createAndShowGui() {
    JPanel panel   = new JPanel();
    JPanel buttons = new JPanel();

    panel.setLayout(new BorderLayout());
    buttons.setLayout(new FlowLayout());
    buttons.add(okay);
    buttons.add(pass);
    panel.add(slider, BorderLayout.CENTER);
    panel.add(buttons, BorderLayout.SOUTH);
    this.addComponent(panel);
    this.setMessage("Submit a bid"); 
    this.setValue("-1");
    this.setMessageType(JOptionPane.PLAIN_MESSAGE);
    this.setOptionType(JOptionPane.OK_CANCEL_OPTION);
    this.setOptions(components.toArray());
    dialog = this.createDialog(frame, this.title);
    dialog.setModal(true);
    dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    int [] point = control.getModelArrayProperty("DialogPosition");
    if (point == null || point.length != 2) {
      point = new int [] {10, 10};
    }
    dialog.setLocation(point[0],point[1]);
    dialog.setVisible(true); 
    if (this.getValue() == null) {
      this.setValue(-1);
    }
    control.setModelProperty("DialogPosition", "("+dialog.getX()+","+dialog.getY()+")");
    dialog.dispose();
  }

  private static JSlider getSlider(final JOptionPane optionPane, int min, int max) {
    JSlider slider = new JSlider();
    slider.setMajorTickSpacing(2);
    slider.setMinimum(min);
    slider.setMaximum(max);
    slider.setValue(min);
    slider.setPaintTicks(true);
    slider.setPaintLabels(true);
    ChangeListener changeListener = new ChangeListener() {
      public void stateChanged(ChangeEvent changeEvent) {
        JSlider theSlider = (JSlider) changeEvent.getSource();
        if (!theSlider.getValueIsAdjusting()) {
          optionPane.setInputValue(new Integer(theSlider.getValue()));
        }
      }
    };
    slider.addChangeListener(changeListener);
    return slider;
  }

  private final JButton getOkayButton(String text, final JSlider slider) {
    final JButton okay = new JButton(text);
    okay.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        int val = (Integer)slider.getValue();
        setValue(val);
      }
    });
    return okay;
  }
 
  private final JButton getPassButton(String text) { 
    final JButton cancel = new JButton(text);
    cancel.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        setValue(-1);
      }
    });
    return cancel;
  }

  public void modelPropertyChange(PropertyChangeEvent e) {
    if (e.getNewValue() == null) return;
    if (e.getPropertyName().equals(Constants.RESET)) {
      this.setValue(-1);
      this.control.removeView(this);
      dialog.setVisible(false);
      dialog.dispose();
    }
  }
}
