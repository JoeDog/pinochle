package org.joedog.util;
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

  public static String monthToString(int month) {
    return DateUtils.monthToString(month, false);
  }

  public static String monthToString(int month, boolean full) {
    switch (month) {
      case 1:
        if (full) return "January";
        return "Jan";
      case 2:
        if (full) return "February";
        return "Feb";
      case 3:
        if (full) return "March";
        return "Mar";
      case 4:
        if (full) return "April";
        return "Apr";
      case 5:
        if (full) return "May";
        return "May";
      case 6:
        if (full) return "June";
        return "Jun";
      case 7:
        if (full) return "July";
        return "Jul";
      case 8:
        if (full) return "August";
        return "Aug";
      case 9:
        if (full) return "September";
        return "Sep";
      case 10:
        if (full) return "October";
        return "Oct";
      case 11:
        if (full) return "November";
         return "Nov";
      case 12:
        if (full) return "December";
         return "Dec";
      default: // WTF?
         return "";
    }
  }
}
