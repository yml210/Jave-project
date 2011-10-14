/* SimpleInput.java
 * CS193J Winter 1999
 */
  
import java.io.*;
import java.util.Date;

/**
 * SimpleInput provides a wrapper around <CODE>System.in</CODE> that allows you 
 * to more easily read input in a formatted manner.  It offers public methods like 
 * like <CODE>readLine</CODE>, <CODE>readInteger</CODE>, <CODE>readDouble</CODE>,
 * etc., each in two versions: one that allows you to specify a default response 
 * and one without it. If the default response is non-null, it is printed in []  
 * at the end of the prompt to indicate to the user what the default value is.
 * If the user enters no response, the default response is substituted
 * and processed instead.  All of the methods are static, so you just send
 * messages to the SimpleInput class itself, like this:
 * <PRE>
 *		String name = SimpleInput.readLine("What is your name?");
 *		Date date = SimpleInput.readDate("Please enter a date", "1/1/99");
 * </PRE>
 * <P>For those methods that require a particular type of input (integer, 
 * Y/N, etc.) the method will read a full line and attempt to parse the 
 * correct type out, if ill-formatted, the user is chastised and prompted
 * to enter again.
 *
 * <P>You are free to edit or extend this class, but we don't expect that
 * you should need to make any changes.
 *
 * @see          java.io.InputStream
 * @see          java.io.Reader
 * @see          java.io.BufferedReader
 * @version      1.1 01/10/99
 * @author       Julie Zelenski
 */
public class SimpleInput
{
	
	/**
	 * Prompts user by printing prompt (without adding a newline) and
	 * reads entire line of input, up to, but not including the newline.
	 * If the default response is non-null, it is printed in [] at the
	 * end of the prompt to indicate to the user what the default value
	 * is. If the user enters no response, the default response is substituted
	 * and returned instead. Returns null if I/O failure. Also available 
	 * below in an overloaded version with no default response that returns
	 * exactly what the user typed, even if the empty string. If you want
	 * no prompt printed, just pass the "" as the prompt.
	 * @param prompt string to print to System.out to instruct user
	 * @param defaultResponse string to substitute for empty response.
	 * @return  the response entered by user (or default if present and
	 * substituted because user entered nothing.)
	 */
	 public static String readLine(String prompt, String defaultResponse)
	{
		return promptedReadLine(prompt, defaultResponse);
	}
	
	/**
	 * Calls <CODE>readLine</CODE>, passing null for default response.
	 * @see SimpleInput#readLine(java.lang.String, java.lang.String)
	 */
	 public static String readLine(String prompt)
	{
		return readLine(prompt, null);
	}
	

	/**
	 * Prompts user by printing prompt (without trailing newline) and
	 * reads line of input and tries to parse an int out of it by
	 * using the Integer wrapper class. If Integer couldn't parse it, 
	 * prints an error message and prompts the user to re-enter.
	 * Does this until user enters a valid integer. The defaultResponse
	 * in this case should be a string to substitute if the user doesn't
	 * enter any text. Also available in a version with no parameters without
	 * a default response. Valid integer format is a sequence of digits 
	 * with optional sign at front.
	 * @param prompt string to print to System.out to instruct user
	 * @param defaultResponse string to substitute for empty response.
	 * @return  the integer entered by user (or default if present and
	 * substituted because user entered nothing.)
	 */
	public static int readInteger(String prompt, String defaultResponse)
	{
		while (true) {
			String response = promptedReadLine(prompt, defaultResponse);
			try {			// throws exception if can't parse
				return Integer.valueOf(response.trim()).intValue(); 
			}
	 		catch(NumberFormatException e) { 
	 			System.out.println("Not an integer. Please re-enter. ");
			}
		}
	}
	
	/**
	 * Calls <CODE>readInteger</CODE>, passing null for default response.
	 * @see SimpleInput#readInteger(java.lang.String, java.lang.String)
	 */
	public static int readInteger(String prompt)
	{	
		return readInteger(prompt, null);
	}


	/**
	 * Prompts user by printing prompt (without trailing newline) and
	 * reads line of input and tries to parse a double out of it by
	 * using the Double wrapper class. If Double couldn't parse it, 
	 * prints an error message and prompts the user to re-enter.
	 * Does this until user enters a valid double. The defaultResponse
	 * in this case should be a string to substitute if the user doesn't
	 * enter any text. Also available in a version with no parameters without
	 * a default response. Double class accepts most reasonable formats 
	 * (with or without decimal leading zero, sign, etc.) but rejects those 
	 * with non-numeric components, extra decimal points, etc.
	 * @param prompt string to print to System.out to instruct user
	 * @param defaultResponse string to substitute for empty response.
	 * @return  the double entered by user (or default if present and
	 * substituted because user entered nothing.)
	 */
	public static double readDouble(String prompt, String defaultResponse)
	{
		while (true) {
			String response = promptedReadLine(prompt, defaultResponse);
			try {	// throws exception if can't parse
				return Double.valueOf(response.trim()).doubleValue(); 
			}
	 		catch(NumberFormatException e) { 
	 			System.out.println("Not a double value. Please re-enter. ");
			}
		}
	}
	
	/**
	 * Calls <CODE>readDouble</CODE>, passing null for default response.
	 * @see SimpleInput#readDouble(java.lang.String, java.lang.String)
	 */
	public static double readDouble(String prompt)
	{	
		return readDouble(prompt, null);
	}


	/**
	 * Prompts user by printing prompt (without trailing newline) and
	 * reads line of input and checks if first character is Y or N
	 * and returns boolean accordingly. If response doesn't match, 
	 * prints an error message and prompts the user to re-enter.
	 * Does this until user enters something that begins with Y or N. 
	 * Also available in a version with no default response. 
	 * @param prompt string to print to System.out to instruct user
	 * @param defaultResponse string to substitute for empty response.
	 * @return  true if user entered Y, false for N
	 */
	 public static boolean readYesOrNo(String prompt, String defaultResponse)
	{
		while (true) {
			String response = promptedReadLine(prompt, defaultResponse);
			if (response.length() > 0) {
				switch (Character.toUpperCase(response.charAt(0))) {
					case 'Y': return true;
					case 'N': return false;
		 			default: System.out.println("Not a valid response. Please enter Y or N.");
				}
			}
		}
	}
	
	/**
	 * Calls <CODE>readYesOrNo</CODE>, passing null for default response.
	 * @see SimpleInput#readYesOrNo(java.lang.String, java.lang.String)
	 */
	public static boolean readYesOrNo(String prompt)
	{
		return readYesOrNo(prompt, null);
	}


	
	
	/** 
	 * Prompts user by printing prompt (without trailing newline) and
	 * reads line of input and tries to parse a data out of it by
	 * using the Convert class. If string cannot be converted into
	 * a valid date, we print an error message and prompt the user to re-enter.
	 * Does this until user enters a valid date. Also available in a 
	 * version with no default response. The expected format for dates 
	 * is <CODE>m/d/y</CODE>.
	 * @param prompt string to print to System.out to instruct user
	 * @param defaultResponse string to substitute for empty response.
	 * @return  the date entered by user (or default if present and
	 * substituted because user entered nothing.)
	 * @see Convert
	 */
	public static Date readDate(String prompt, String defaultResponse)
	{
		while (true) {
			String response = promptedReadLine(prompt, defaultResponse);
			Date d =  Convert.stringToDate(response, "M/d/y"); 
			if (d != null) return d;
			System.out.println("Improper date, please re-enter (m/d/y expected). ");
		}
	}
	
	/**
	 * Calls <CODE>readDate</CODE>, passing null for default response.
	 * @see SimpleInput#readDate(java.lang.String, java.lang.String)
	 */
	public static Date readDate(String prompt)
	{
		return readDate(prompt, null);
	}
	
	
	/**
	 * Main internal helper to print prompt and read entire line of
	 * text from BufferedReader operating on System.in. On an 
	 * I/O exception, it returns null.
	 */
	 private static String promptedReadLine(String prompt, String defaultResponse)
	{
		try {
			System.out.print(prompt);
			if (defaultResponse != null) // append default in brackets
				System.out.print(" [" + defaultResponse + "]: ");
			String response = reader.readLine().trim();
			if ((response == null || response.equals("")) && defaultResponse != null)
				response = defaultResponse; // substitute for empty response
			return response;
		}
		catch(java.io.IOException e) {
			return null;
		}
	}	


	private static BufferedReader reader; 
	
	static {
		reader = new BufferedReader(new InputStreamReader(System.in));
	}


}