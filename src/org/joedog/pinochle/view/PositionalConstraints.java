package org.joedog.pinochle.view;

public class PositionalConstraints {

  private float x;
  private float y;

  public PositionalConstraints(float x, float y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Returns the X coordinate
   * <p>
   * @return  float 
   */
  public float getX() {
    return x;
  }

  /**
   * Returns the Y coordinate
   * <p>
   * @return  float 
   */
  public float getY() {
    return y;
  }

  /**
   * Sets the X coordinate
   * <p>
   * @param float The X coordinate
   */
  public void setX(float x) {
    if (x > 1f) {
      x = 1f;
    } else if (x < -0f) {
      x = 0f;
    }
    this.x = x;
  }

  /**
   * Sets the Y coordinate
   * <p>
   * @param float The y coordinate
   */
  public void setY(float y) {
    if (y > 1f) {
      y = 1f;
    } else if (y < -0f) {
      y = 0f;
    }
    this.y = y;
  }
}
