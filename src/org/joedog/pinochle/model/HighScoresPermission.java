package org.joedog.pinochle.model;

import java.security.BasicPermission;

public final class HighScoresPermission extends BasicPermission {
  public static final long serialVersionUID = 3L;

  public HighScoresPermission (String name) {
    super(name);
  }

  public HighScoresPermission(String name, String actions) {
    super(name, actions);
  }
}
