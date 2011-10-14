/* Convert.java
 * CS193J Winter 1999
 */
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;


/** The Convert class contains a few utility conversion routines you may
 * find helpful for your assignment. In Java, all operations must be defined as
 * methods on some class, so that forces us to put these methods into a class.
 * We have just lumped them into this utility class to start you off.
 * It is fine to leave this class as is and not muck with it, but you may find
 * that this functionality fits more cleanly into some of the classes you
 * will add when implementing the rest of the program. (For example, converting
 * between a Time and string representation may fit better in your Time class.)
 * Feel free to edit these routines or move to more appropriate places as you wish.
 *
 * <P>A few of these methods are a little tricky, such as the methods to take 
 * a time or a date in string form and convert to a matching Time or Date object 
 * by using the java.text formatters to parse out the fields. Because formatters
 * use exceptions and since we will not get to exceptions for a while in lecture, 
 * you may not entirely understand all of what goes on in these methods, but
 * as given to you, they will cover the basic needs, so you shouldn't need
 * to change them.
 * 
 * <P>All of the public conversion methods are static, so you just send messages 
 * to the Convert class itself, like this:
 * <PRE>
 *		Date foolsDay = Convert.stringToDate("4/1/99");
 *		Time lectureStart = Convert.stringToTime("12:50pm");
 * </PRE>
 * Read the comments by each method for more individual details.
 *
 * @see       	java.text.DateFormat
 * @see			Time
 * @version    	1.1 01/10/99
 * @author     	Julie Zelenski
 */


public class Convert
{	
	/**
	 * Given a string, attempts to parse out a time from it to set the 
	 * hour and minute field of a newly created Time object. Uses the
	 * built-in DateFormat to parse using several possible common time
	 * formats. If one appears to match and can be parsed, pull out the
	 * the hours and minutes from the temporary Date object and create
	 * a new Time object. If we couldn't parse the string as any recognizable
	 * form of time, null is returned.
	 * @param date a string hopefully containing a time
	 * @return the time object matching the input string if possible, null otherwise
 	 * @see          java.text.SimpleDateFormat
 	 * @see          java.text.DateFormat
	 */
	public static Time stringToTime(String timeString)
	{
		String formatsToTry[] = {"hh:mma" , "hha", "HH:mm", "HH"};
		Date date = null;
		
		timeString = timeString.trim(); // remove any excess white space
		for (int i = 0; date == null && i < formatsToTry.length; i++) {
			try { 
				date = getDateFormatter(formatsToTry[i]).parse(timeString);
			} catch (Exception e) {}	// that one didn't work, try another
		}
		if (date == null) 
			return null; // unable to parse successfully
		else { // parsed successfully, read hour/minutes out of Calendar
			Calendar cal = GregorianCalendar.getInstance();
			cal.setTime(date);
			return new Time(cal.get(GregorianCalendar.HOUR), 
				            cal.get(GregorianCalendar.MINUTE),
				            cal.get(GregorianCalendar.HOUR_OF_DAY) < 12);
		}
	}
	

	/**
	 * Given a string and a format string, will attempt to parse a
	 * Date object out of the string, expecting the format given.
	 * If the string does not match the format string, null is returned,
	 * otherwise, a Date object matching the string is returned.
	 * The format string is somewhat reminsicent of C's printf or strftime. 
	 * It uses letter codes to indicate which pieces of the date (month, day, year, 
	 * etc.) should be shown and whether to use the long or short forms. See the
	 * docs for DateFormat and SimpleDateFormat to get an explanation of 
	 * all the various format codes.
	 * @param date a string hopefully containing a date
	 * @param formatString the string specifying the desired format
	 * @return the date object matching the input string if possible, null otherwise
 	 * @see          java.text.SimpleDateFormat
 	 * @see          java.text.DateFormat
	 */
	public static Date stringToDate(String dateString, String formatString)
	{
		try {				// will throw exception if can't parse
			return getDateFormatter(formatString).parse(dateString.trim()); 
		} catch (Exception e) {	// if trouble pulling out Date, return null 
			return null;
		}
	}
	
	/**
	 * Calls <CODE>stringToDate</CODE> supplying a standard format string
	 * of <CODE>m/d/y</CODE>.
 	 * @see          Convert#stringToDate(java.lang.String, java.lang.String)
	 */
	public static Date stringToDate(String dateString)
	{
		return stringToDate(dateString, "M/d/yy"); 
	}


	/**
	 * Given a date and a format string, returns a string representation
	 * of the date using that format. The format string is somewhat
	 * reminsicent of C's printf or strftime. It uses letter codes to
	 * indicate which pieces of the date (month, day, year, etc.) should
	 * be shown and whether to use the long or short forms. See the
	 * docs for DateFormat and SimpleDateFormat to get an explanation of 
	 * all the various format codes.
	 * @param date a Date to be formatted
	 * @param formatString the string specifying the desired format
	 * @return the formatted date string
 	 * @see          java.text.SimpleDateFormat
 	 * @see          java.text.DateFormat
	 */
	public static String dateToString(Date date, String formatString)
	{
		return getDateFormatter(formatString).format(date);
	}
	
	/**
	 * Calls <CODE>dateToString</CODE> supplying a standard format string
	 * of <CODE>weekday m/d</CODE>.
 	 * @see          Convert#dateToString(java.util.Date, java.lang.String)
	 */
	public static String dateToString(Date date)
	{
    	return dateToString(date, "E M/d");
	}


	
	private static SimpleDateFormat getDateFormatter(String formatString)
	{
		SimpleDateFormat formatter = new SimpleDateFormat(formatString);
		formatter.setTimeZone(java.util.TimeZone.getDefault()); // workaround for bug
		formatter.setLenient(false); // don't alllow things like 1/35/99
		return formatter;
	}

}
	