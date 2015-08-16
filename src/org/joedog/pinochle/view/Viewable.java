package org.joedog.pinochle.view;

import java.beans.PropertyChangeEvent;

public interface Viewable {
  public void modelPropertyChange(PropertyChangeEvent evt);
}
