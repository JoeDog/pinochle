package org.joedog.pinochle.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Enumeration;

public class Configuration {
  private Properties conf       = null;
  private static String cfgfile = System.getProperty("user.home")+"/.pinochle.properties";
  private static Configuration  _instance = null;

  private Configuration() {
    conf = new Properties();
    try {
      FileInputStream fis = new FileInputStream(new File(this.cfgfile));
      conf.load(fis);
      /**
       * this is sloppy but we like turing this on for debugging */
      Enumeration e = conf.propertyNames();
      while (e.hasMoreElements()) {
        String key = (String) e.nextElement();
        //System.out.println(key + " -- " + conf.getProperty(key));
      }
       /**/
    } catch (Exception e) {
      // catch Configuration Exception right here
    }
  }

  public synchronized static Configuration getInstance() {
    if (_instance == null)
      _instance = new Configuration();
    return _instance;
  }

  public Enumeration propertyNames() {
    return conf.propertyNames();
  }

  public void setProperty(String key, String val) {
    conf.setProperty(key, val);
  }

  // get property value by name
  public String getProperty(String key) {
    String value = null;
    if (conf.containsKey(key)) {
      value = (String) conf.get(key);
    } else {
      // the property is absent
    }
    return value;
  }

  public void save() {
    try {
      conf.store(new FileOutputStream(this.cfgfile), null);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return;
  }
}
