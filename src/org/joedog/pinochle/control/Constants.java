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

public class Constants {
	
  /** 
   * Match model constants
   */

  /** The status bar message */
  public static final String MESSAGE = "message";

  /** A signal to re-render score pad */
  public static final String NAMES   = "names";

  /** The current bid of the current hand */
  public static final String BID     = "setBid";

  /** The player who took the hand */
  public static final String BIDDER  = "setBidder";

  /** The numeric ID of the trump suit */
  public static final String TRUMP   = "setTrump";

  /** A Score formatted version of meld (num,num)  */
  public static final String MELD    = "setMeld";

  /** A Score formatted version of take (num,num)  */
  public static final String TAKE    = "setTake";

  /** A Score formatted version of total (num,num)  */
  public static final String TOTAL   = "setTotal"; 

  /** A Score formatted version of score (num,num)  */
  public static final String SCORE   = "setScore";

  /** A Score formatted version of score (num,num)  */
  public static final String WINNER  = "setWinner";

  /** A Score formatted version of score (num,num)  */
  public static final String RESET   = "reset";

  /** A status message for the NORTH player         */
  public static final String NORTH   = "northStatus";
  
  /** A status message for the SOUTH player         */
  public static final String SOUTH   = "southStatus";

  /** A status message for the EAST player         */
  public static final String EAST   = "eastStatus";

  /** A status message for the WEST player         */
  public static final String WEST   = "westStatus";

}  
  
