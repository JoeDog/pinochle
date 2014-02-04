package org.joedog.pinochle.util;

import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public final class DateUtils {
  public static String now() {
    DateFormat df = new SimpleDateFormat("MM-dd-yyyy HH:mm");
    Date today = Calendar.getInstance().getTime();        
    return df.format(today);
  }
}
