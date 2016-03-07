package org.joedog.pinochle.control;
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

import org.joedog.util.TextUtils;
import org.joedog.pinochle.view.Viewable;
import org.joedog.pinochle.model.AbstractModel;

import java.util.ArrayList;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;

public abstract class AbstractController implements PropertyChangeListener {
  private ArrayList <Viewable>      registeredViews;
  private ArrayList <AbstractModel> registeredModels;
 
  public AbstractController () {
    this.registeredViews  = new ArrayList<Viewable>();
    this.registeredModels = new ArrayList<AbstractModel>();
  } 

  public void addView (Viewable view) {
    this.registeredViews.add(view);
  }

  public void addModel (AbstractModel model) {
    this.registeredModels.add(model);
    model.addPropertyChangeListener(this);
  }

  public void removeView (Viewable view) {
    registeredViews.remove(view);
  }

  public void removeModel (AbstractModel model) {
    registeredModels.remove(model);
  }

  public void propertyChange(PropertyChangeEvent evt) {
    for (Viewable view: registeredViews) {
      view.modelPropertyChange(evt);
    }
  }

  public int save() {
    for (AbstractModel model: registeredModels) {
      model.save();
    }
    return 1;
  }

  public String getModelStringProperty(String key) {
    String val = (String)getModelProperty(key);
    if (val == null) {
      return "null";
    }
    return val;
  }

  public int getModelIntProperty(String key) {
    Object obj = getModelProperty(key);
    if (obj == null) {
      return 0;
    }
    if (obj instanceof String) {
      return Integer.parseInt((String) obj);
    } else if (obj instanceof Integer) {
      return ((Integer) obj).intValue();
    } else {
      String toString = obj.toString();
      if (toString.matches("-?\\d+")) {
        return Integer.parseInt(toString);
      }
      return 0;
    }
  }

  public boolean getModelBooleanProperty(String key) {
    boolean result = false;
    String  value  = ((String)getModelProperty(key)).trim();

    if (value.matches("^true|^false"))  {
      result = Boolean.parseBoolean(value);  
    }
    return result;
  }

  public int[] getModelArrayProperty(String key) {
    String value = (String)getModelProperty(key);

    if (value == null) {
      // the model is designed to return a default value but shit happens
      return null;
    }
   
    int [] ret = TextUtils.toArray(value); 
    return ret;
  }

  public Object getModelProperty(String propertyName) {
    for (AbstractModel model: registeredModels) {
      try {
        Method method = model.getClass().getMethod("get"+propertyName);
        return method.invoke(model);
      } catch (Exception ex) {
        // ex.printStackTrace();
        // No warning; some models won't have the requested method
      }
    }
    return null;
  }

  public void setModelProperty (String propertyName, Object newValue) {
    for (AbstractModel model: registeredModels) {
      try {
        Method method = model.getClass().getMethod("set"+propertyName, new Class[] {
          newValue.getClass()
        });
        method.invoke(model, newValue);
      } catch (Exception ex) {
        // ex.printStackTrace();
        // No warning; some models won't have the requested method
      }
    }
  }

  protected Object getViewProperty(String propertyName) {
    for (Viewable view: registeredViews) {
      try {
        Method method = view.getClass().getMethod("get"+propertyName);
        return method.invoke(view);
      } catch (Exception ex) {
        // No warning; some views won't have the requested method
      }
    }
    return null;
  }

  protected Object getViewProperty (String propertyName, Object param) {
    for (Viewable view: registeredViews) {
      try {
        Method method = view.getClass().getMethod("get"+propertyName, new Class[] {
          param.getClass()
        });
        return method.invoke(view, param);
      } catch (Exception ex) {
        // No warning; some views won't have the requested method
      }
    }
    return null;
  }

  protected void setViewProperty (String propertyName, Object newValue) {
    for (Viewable view: registeredViews) {
      try {
        Method method = view.getClass().getMethod("set"+propertyName, new Class[] {
          newValue.getClass()
        }); 
        method.invoke(view, newValue);
      } catch (Exception ex) {
        // No warning; some views won't have the requested method
        //ex.printStackTrace();
      }
    }
  }

  protected void runModelMethod (String name) {
    for (AbstractModel model: registeredModels) {
      try {
        Method method = model.getClass().getMethod(name);
        method.invoke(model);
      } catch (Exception ex) {
        // No warning; some models won't have the requested method
        //ex.printStackTrace();
      }
    }
  }

  protected void runViewMethod (String name) {
    for (Viewable view: registeredViews) {
      try {
        Method method = view.getClass().getMethod(name);
        method.invoke(view);
      } catch (Exception ex) {
        // No warning; some views won't have the requested method
        //ex.printStackTrace();
      }
    }
  }
}
