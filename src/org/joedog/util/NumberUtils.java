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

import java.text.DecimalFormatSymbols;

public final class NumberUtils {
  public static boolean isNumeric(String str) {
    DecimalFormatSymbols currentLocaleSymbols = DecimalFormatSymbols.getInstance();
    char localeMinusSign = currentLocaleSymbols.getMinusSign();
    if (!Character.isDigit(str.charAt(0)) && str.charAt(0) != localeMinusSign) {
      return false;
    }

    boolean isDecimalSeparatorFound = false;
    char localeDecimalSeparator = currentLocaleSymbols.getDecimalSeparator();

    for (char c : str.substring(1).toCharArray()) {
      if (!Character.isDigit(c)) {
        if (c == localeDecimalSeparator && !isDecimalSeparatorFound) {
          isDecimalSeparatorFound = true;
          continue;
        }
        return false;
      }
    }
    return true;
  }
}
