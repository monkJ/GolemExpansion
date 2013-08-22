/**
 * File: DateHandler.java
 * created: 10.01.2013 - 03:30:27
 * by: RoboMat
 */

package com.monkj.mods.calendar.utility;

import java.text.DecimalFormat;

/**
 * Provides several methods for calculation the age of a minecraft world. The
 * standard constructor sets the fields to their real world equivalents - this
 * means each world starts at Mon, 1st Jan 0001.
 * To calculate the current date simply call the {@code updateFields} method
 * with the
 * total ticks of the current world as a parameter. You can then use the return*
 * (Formatted dates and time) and get* (Unformatted single values) methods to
 * read the calculated values.
 */
public class DateHandler
{
	/* ---------------------- FIELDS NEEDED FOR CALCULATION ------------------- */
	/* Constants */
	final private static short TIME_OFFSET = 6000;		// used to fix the offset between ticks and actual time
	final private static short TICKS_PER_DAY = 24000;		// 1 day equals 24000 ticks
	final private static short TICKS_PER_HOUR = 1000;		// 1 hour equals 1000 ticks
	final private static double TICKS_PER_MIN = 1000d / 60; 	// 1 minute equals approximately 16,6 ticks
	/* Used to format the date */
	final private static DecimalFormat FOUR_DIGITS = new DecimalFormat("0000");
	final private static DecimalFormat TWO_DIGITS = new DecimalFormat("00");
	
	/* Names */
	final private String[] dayNames;
	final private String[] dayNamesFull;
	final private String[] monthNames;
	final private String[] monthNamesFull;
	final private String[] eraNames;
	
	/* Lengths */
	final private int[] monthLengths;
	final private int weekLength;
	final private int yearLength;
	final private int eraLength;
	final private int noOfMonths;
	final private int noOfEras;
	
	/* Can be used to adjust the year counter */
	final private int yearOffset;
	
	/* ---------------------- RESULTS OF THE CALCULATIONS --------------------- */
	/* Dates */
	private int totalDays;
	private int currentMonth;
	private int currentDayOfTheMonth;
	private int currentYear;
	
	private String curDayName;
	private String curDayNameFull;
	private String curMonthName;
	private String curMonthNameFull;
	private String ordinalExt;
	/* Eras */
	private int curEra;
	private String curEraName;
	
	/* Time */
	private byte curMin;
	private byte curHour;
	
	/* ---------------------- CONSTRUCTORS ------------------------------------- */
	/**
	 * This constructor initializes the date calculation with real-time formats.<br>
	 * <br>
	 * - 365 Days per year<br>
	 * - 12 Months per year<br>
	 * - 7 Day a week<br>
	 * - names for months and years are based on their real life equivalents.<br>
	 * - 2 years per era<br>
	 * - Standard era names: "Rising Sun Era", "Black Stone Era",
	 * "Seventh Ender Era"<br>
	 * - yearOffset = 0<br>
	 */
	public DateHandler() {
		this.dayNames = new String[] { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun" };
		this.dayNamesFull = new String[] { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };
		this.monthNames = new String[] { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
		this.monthNamesFull = new String[] { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
		this.eraNames = new String[] { "Clouds Fall", "Creepers Retreat", "Seventh Ender", "Drunk Zombie", "Deep Sun" };
		this.monthLengths = new int[] { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		this.eraLength = 2;
		this.yearOffset = 0;
		weekLength = dayNames.length;
		noOfMonths = monthNames.length;
		noOfEras = eraNames.length;
		yearLength = initializeYearLength(monthLengths);
	}
	
	/**
	 * Use this constructor if you want to display custom dates.
	 * 
	 * @param dayNames
	 * @param monthNames
	 * @param eraNames
	 * @param monthLengths
	 * @param eraLength
	 * @param yearOffset
	 */
	public DateHandler(String[] dayNames, String[] dayNamesFull, String[] monthNames, String[] monthNamesFull, String[] eraNames, int[] monthLengths, int eraLength, int yearOffset) {
		/* Directly read from config */
		this.eraNames = eraNames;
		this.eraLength = eraLength;
		this.yearOffset = yearOffset;
		
		/* Make sure the custom config is correct */
		if (dayNames.length != dayNamesFull.length) {
			System.err.println("ERROR: Number of abbreviated and full day names is inconsistent. Standard values will be used instead.");
			this.dayNames = new String[] { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun" };
			this.dayNamesFull = new String[] { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };
		} else {
			this.dayNames = dayNames;
			this.dayNamesFull = dayNamesFull;
		}
		
		if (monthNames.length != monthLengths.length || monthNames.length != monthNamesFull.length || monthNamesFull.length != monthLengths.length) {
			System.err.println("ERROR: Number of month-names and month-lengths is inconsistent. Standard values will be used instead.");
			this.monthNames = new String[] { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
			this.monthNamesFull = new String[] { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
			this.monthLengths = new int[] { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		} else {
			this.monthNames = monthNames;
			this.monthNamesFull = monthNamesFull;
			this.monthLengths = monthLengths;
		}
		
		/* Calculated through other fields */
		weekLength = dayNames.length;
		noOfMonths = monthNames.length;
		noOfEras = eraNames.length;
		yearLength = initializeYearLength(monthLengths);
	}
	
	/* ---------------------- DATE FORMATS ------------------------------------ */
	/**
	 * @return Returns the total Days that have passed ingame.
	 */
	public String returnTotalDays() {
		return "Day: " + this.totalDays;
	}
	
	/**
	 * Returns the current date in standard format.
	 * 
	 * @param totalWorldTime - The total ticks passed since creation of the
	 *            minecraft world.
	 * @return Example: Mon, 1st Jan 0001
	 */
	public String returnStandardDate() {
		final StringBuilder str = new StringBuilder();
		str.append(curDayName);
		str.append(", ");
		str.append(currentDayOfTheMonth);
		str.append(ordinalExt);
		str.append(" ");
		str.append(curMonthNameFull);
		str.append(" ");
		str.append(FOUR_DIGITS.format(currentYear));
		return str.toString();
	}
	
	public String[] returnDateForCalendarBlock() {
		String[] str = new String[7];
		str[0] = curDayNameFull;
		str[1] = currentDayOfTheMonth + ordinalExt + ",";
		str[2] = curMonthNameFull;
		str[3] = FOUR_DIGITS.format(currentYear);
		str[4] = "";
		str[5] = TWO_DIGITS.format(curHour) + ":" + TWO_DIGITS.format(curMin);
		str[6] = "Day " + totalDays;
		return str;
	}
	
	/**
	 * Returns the current date in a short format used in the EU.
	 * 
	 * @param totalWorldTime - The total ticks passed since creation of the
	 *            minecraft world.
	 * @return Mon 31/12/0001
	 */
	public String returnShortDateEU() {
		final StringBuilder str = new StringBuilder();
		str.append(curDayName);
		str.append(" ");
		str.append(TWO_DIGITS.format(currentDayOfTheMonth));
		str.append("/");
		str.append(TWO_DIGITS.format(currentMonth));
		str.append("/");
		str.append(FOUR_DIGITS.format(currentYear));
		return str.toString();
	}
	
	/**
	 * Returns the current date in a short format used in the USA where months
	 * and days are switched.
	 * 
	 * @param totalWorldTime - The total ticks passed since creation of the
	 *            minecraft world.
	 * @return 12/31/0001
	 */
	public String returnShortDateUS() {
		final StringBuilder str = new StringBuilder();
		str.append(curDayName);
		str.append(" ");
		str.append(TWO_DIGITS.format(currentMonth));
		str.append("/");
		str.append(TWO_DIGITS.format(currentDayOfTheMonth));
		str.append("/");
		str.append(FOUR_DIGITS.format(currentYear));
		return str.toString();
	}
	
	/* ---------------------- ERA FORMATS ----------------------------------- */
	public String returnEras() {
		final StringBuilder str = new StringBuilder();
		str.append("E");
		str.append(curEra);
		str.append(" - ");
		str.append(curEraName);
		return str.toString();
	}
	
	/* ---------------------- CLOCK FORMATS ----------------------------------- */
	/**
	 * Returns the time in standard clock format.
	 * 
	 * @param totalWorldTime - The total ticks passed since creation of the
	 *            minecraft world.
	 * @return e.g.: 23:19
	 */
	public String returnStandardClock() {
		return "Time: " + TWO_DIGITS.format(curHour) + ":" + TWO_DIGITS.format(curMin);
	}
	
	/**
	 * Returns the time in military format (0000 - 2300)
	 * 
	 * @param totalWorldTime
	 * @return e.g.: 2200
	 */
	public String returnMilitaryClock() {
		int hour = curHour * 100;
		return "Time: " + FOUR_DIGITS.format(hour);
	}
	
	/**
	 * Returns the time in military format (0000 - 2300)
	 * 
	 * @param totalWorldTime
	 * @return e.g.: 2200
	 */
	public String returnTwelveHourClock() {
		byte min = curMin;
		byte hour = curHour;
		String ext = null;
		
		if (hour >= 12) {
			hour = (byte) (hour % 12);
			
			if (hour == 0) {
				hour = 12;
			}
			
			ext = " p.m.";
		} else {
			if (hour == 0) {
				hour = 12;
			}
			
			ext = " a.m.";
		}
		
		return "Time: " + TWO_DIGITS.format(hour) + ":" + TWO_DIGITS.format(min) + ext;
	}
	
	/* ---------------------- DATE RELATED METHODS ---------------------------- */
	/**
	 * Calculates the total days. First day is 0.
	 * 
	 * @param totalWorldTime - Total amount of ingame ticks passed.
	 * @return
	 */
	private int calcTotalDays(final long totalTicksfixed) {
		return (int) (totalTicksfixed / TICKS_PER_DAY) + 1;
	}
	
	/**
	 * Calculates the current month. Returns months from 0 - 11.
	 * 
	 * @param totalDays - Total amount of ingame days passed.
	 * @return
	 */
	private int calcCurrentMonth(int totalDays) {
		int remDays = totalDays % yearLength;
		int curMonth = 0;
		
		/* Only true at 31dec */
		if (remDays == 0) {
			remDays = yearLength;
		}
		
		for (int i = 0; i < noOfMonths; i++) {
			if (remDays > monthLengths[i]) {
				remDays -= monthLengths[i];
			} else {
				curMonth = i;
				break;
			}
		}
		
		return curMonth;
	}
	
	/**
	 * Calculates the current day of the month.
	 * 
	 * @param totalDays - Total amount of ingame days passed.
	 * @return
	 */
	private int calcDayOfCurMonth(int totalDays) {
		int remDays = totalDays % yearLength;
		
		/* Only true at 31dec */
		if (remDays == 0) {
			remDays = yearLength;
		}
		
		for (int i = 0; i < noOfMonths; i++) {
			if (remDays > monthLengths[i]) {
				remDays -= monthLengths[i];
			} else {
				break;
			}
		}
		
		return remDays;
	}
	
	/**
	 * Calculates the current year.
	 * 
	 * @param totalDays - Total amount of ingame days passed (1- 365)
	 * @return
	 */
	private int calcCurrentYear(int totalDays) {
		int curYear = 0;
		
		while (totalDays > yearLength) {
			totalDays -= yearLength;
			curYear++;
		}
		
		return curYear;
	}
	
	/**
	 * Assign the name of the current day.
	 * 
	 * @param totalDays - Total amount of ingame days passed.
	 * @return - The name of the current day of the week.
	 */
	private String assignDayNames(int totalDays) {
		int days = (totalDays - 1) % weekLength;
		return dayNames[days];
	}
	
	/**
	 * Assign the full name of the current day.
	 * 
	 * @param totalDays - Total amount of ingame days passed.
	 * @return - The name of the current day of the week.
	 */
	private String assignDayNamesFull(int totalDays) {
		int days = (totalDays - 1) % weekLength;
		return dayNamesFull[days];
	}
	
	/**
	 * Returns the name of the current month based on the total of passed days.
	 * 
	 * @param currentMonth - Current Month.
	 * @return The name of the current Month.
	 */
	private String assignMonthNames(int currentMonth) {
		return monthNames[currentMonth];
	}
	
	/**
	 * Returns the full name of the current month based on the total of passed
	 * days.
	 * 
	 * @param currentMonth - Current Month.
	 * @return The name of the current Month.
	 */
	private String assignMonthNamesFull(int currentMonth) {
		return monthNamesFull[currentMonth];
	}
	
	/**
	 * Assigns the ordinal extension of the current day of the month.
	 * 
	 * @param currentDay
	 * @return ordinal Ext - (String) "st", "nd", "rd", "th"
	 */
	private String assignOrdinalExtensions(int currentDayOfTheMonth) {
		/* Exceptions of the rule */
		switch (currentDayOfTheMonth) {
			case 11:
			case 12:
			case 13:
				return "th";
		}
		
		/* calculate the ordinal extensions */
		currentDayOfTheMonth = currentDayOfTheMonth % 10;
		
		switch (currentDayOfTheMonth) {
			case 1:
				return "st";
				
			case 2:
				return "nd";
				
			case 3:
				return "rd";
				
			default:
				return "th";
		}
	}
	
	/* ---------------------- ERA RELATED METHODS ---------------------------- */
	private int calcCurEra(int years) {
		int result = (int) ((years + 1) / eraLength);
		return result;
	}
	
	private String assignEraNames(int curEra) {
		curEra--;	// correct offset
		if (curEra > eraNames.length - 1) {
			curEra = eraNames.length - 1;
		}
		return eraNames[curEra];
	}
	
	/* ---------------------- TIME RELATED METHODS ---------------------------- */
	
	/**
	 * Calculates the current ingame minutes. (0 - 59)
	 * 
	 * @param totalWorldTime
	 * @return
	 */
	private byte calcMinutes(final long totalTicksfixed) {
		return (byte) (((totalTicksfixed % TICKS_PER_DAY) % TICKS_PER_HOUR) / TICKS_PER_MIN);
	}
	
	/**
	 * Calculates the current ingame hours. (0 - 23)
	 * 
	 * @param totalWorldTime
	 * @return
	 */
	private byte calcHours(final long totalTicksfixed) {
		return (byte) ((totalTicksfixed % TICKS_PER_DAY) / TICKS_PER_HOUR);
	}
	
	/* ---------------------- UTILITY METHODS --------------------------------- */
	/**
	 * !IMPORTANT! This needs to be called before any other calculation of the
	 * date. It updates the fields and corrects the output of the methods to
	 * display the correct date (e.g. 1st Jan instead of 0th Jan).
	 * 
	 * @param totalWorldTimeThe - total ticks passed since creation of the
	 *            minecraft world.
	 */
	public void updateFields(final long totalWorldTime) {
		long totalTicksFixed = fixTimeOffset(totalWorldTime);	// fix the offset
		/* Date related */
		this.totalDays = calcTotalDays(totalTicksFixed);
		this.currentMonth = calcCurrentMonth(totalDays) + 1;
		this.currentDayOfTheMonth = calcDayOfCurMonth(totalDays);
		this.currentYear = calcCurrentYear(totalDays) + yearOffset;
		this.curDayName = assignDayNames(totalDays);
		this.curDayNameFull = assignDayNamesFull(totalDays);
		this.curMonthName = assignMonthNames(currentMonth - 1);
		this.curMonthNameFull = assignMonthNamesFull(currentMonth - 1);
		this.ordinalExt = assignOrdinalExtensions(currentDayOfTheMonth);
		/* Era related */
		this.curEra = calcCurEra(currentYear);
		this.curEraName = assignEraNames(curEra);
		/* Time related */
		this.curMin = calcMinutes(totalTicksFixed);
		this.curHour = calcHours(totalTicksFixed);
	}
	
	/**
	 * Fixes the offset between ingame ticks and the actual time of day in a
	 * world.
	 * 
	 * @param totalWorldTime
	 * @return
	 */
	private long fixTimeOffset(final long totalWorldTime) {
		return totalWorldTime + TIME_OFFSET;
	}
	
	/**
	 * Initializes the yearLength field from the length of the monthLengths
	 * array.
	 * 
	 * @param monthLengths
	 * @return
	 */
	private int initializeYearLength(int[] monthLengths) {
		int total = 0;
		
		for (int i : monthLengths) {
			total += i;
		}
		
		return total;
	}
	
	/* ---------------------- GETTERS ----------------------------------------- */
	/**
	 * @return the totalDays
	 */
	public int getTotalDays() {
		return totalDays;
	}
	
	/**
	 * @return the currentMonth
	 */
	public int getCurrentMonth() {
		return currentMonth;
	}
	
	/**
	 * @return the currentDayOfTheMonth
	 */
	public int getCurrentDayOfTheMonth() {
		return currentDayOfTheMonth;
	}
	
	/**
	 * @return the currentYear
	 */
	public int getCurrentYear() {
		return currentYear;
	}
	
	/**
	 * @return the curEraName
	 */
	public String getCurEraName() {
		return curEraName;
	}
	
	/**
	 * @return the curDayName
	 */
	public String getCurDayName() {
		return curDayName;
	}
	
	/**
	 * @return the ordinalExt
	 */
	public String getOrdinalExt() {
		return ordinalExt;
	}
	
	/**
	 * @return the curMonthName
	 */
	public String getCurMonthName() {
		return curMonthName;
	}
	
	/**
	 * @return the curEra
	 */
	public int getCurEra() {
		return curEra;
	}
	
	/**
	 * @return the curMin
	 */
	public byte getCurMin() {
		return curMin;
	}
	
	/**
	 * @return the curHour
	 */
	public byte getCurHour() {
		return curHour;
	}
	
	/**
	 * @return the ticksPerMin
	 */
	public static double getTicksPerMin() {
		return TICKS_PER_MIN;
	}

	/**
	 * @return the yearLength
	 */
	public int getYearLength() {
		return yearLength;
	}
}