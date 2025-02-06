package be.baur.sda;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import be.baur.sda.serialization.ParseException;
import be.baur.sda.serialization.SDAFormatter;
import be.baur.sda.serialization.SDAParseException;
import be.baur.sda.serialization.SDAParser;

/**
 * This class defines static constants and utility methods.
 */
public final class SDA {

	/** A left brace (starts a node list). */
	public static final int LBRACE = '{'; 
	
	/** A right brace (ends a node list). */
	public static final int RBRACE = '}'; 
	
	/** A quote (encloses simple content). */
	public static final int QUOTE = '"'; 
	
	/** A back slash (the escape character). */
	public static final int BSLASH = '\\';
	
	/** An underscore (the only non-alphanumeric allowed in node names). */
	public static final int USCORE = '_';
	
	
	private SDA() {} // cannot construct this
	
	
	/**
	 * Check whether a character is a valid SDA digit.
	 * 
	 * @param c a character
	 * @return true or false
	 */
	private static boolean isDigit(int c) {
		return (c >= '0' && c <= '9');
	}


	/**
	 * Check whether a character is a valid SDA letter.
	 * 
	 * @param c a character
	 * @return true or false
	 */
	private static boolean isLetter(int c) {
		return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
	}
	
	
	/**
	 * Check whether a character is a valid start of an SDA node name.
	 * 
	 * @param c a character
	 * @return true or false
	 */
	public static boolean isNameStart(int c) {
		return (isLetter(c) || c == USCORE);
	}
	
	
	/**
	 * Check whether a character is valid in an SDA node name.
	 * 
	 * @param c a character
	 * @return true or false
	 */
	public static boolean isNamePart(int c) {
		return (isLetter(c) || c == USCORE || isDigit(c));
	}
	
	
	/**
	 * Check whether a string is a valid SDA node name. A node name may consist only
	 * of letters, digits and underscores. It cannot start with a digit and must
	 * contain at least one character that is not an underscore. Also, non-English
	 * letters and digits (like Greek symbols) are all excluded.
	 * 
	 * @param name a string
	 * @return true or false
	 */
	public static boolean isName(String name) {
		
		if (name == null || name.isEmpty()) return false;

		int c = name.codePointAt(0);
		if (! isNameStart(c)) return false;
		
		int i = Character.charCount(c);
		int alfanum = (c == USCORE) ? 0 : 1;
		
		while (i < name.length()) {
			c = name.codePointAt(i);
			if (!isNamePart(c))	return false;
			i += Character.charCount(c);
			if (c != USCORE) ++alfanum;
		}
		
		return (alfanum > 0);
	}


/*
	public static void main(String[] args) {

		String s;
		s="@"; System.out.println(s + ": " + isName(s));
		s="2"; System.out.println(s + ": " + isName(s));
		s="_"; System.out.println(s + ": " + isName(s));
		s="_@"; System.out.println(s + ": " + isName(s));
		s="__"; System.out.println(s + ": " + isName(s));
		
		s="a"; System.out.println(s + ": " + isName(s));
		s="aa"; System.out.println(s + ": " + isName(s));
		s="_a"; System.out.println(s + ": " + isName(s));
		s="a_"; System.out.println(s + ": " + isName(s));
		s="_2"; System.out.println(s + ": " + isName(s));
		s="a2"; System.out.println(s + ": " + isName(s));
		s="2a"; System.out.println(s + ": " + isName(s));
		
		s="a a"; System.out.println(s + ": " + isName(s));	
		s="___"; System.out.println(s + ": " + isName(s));	
		
		s="_a_"; System.out.println(s + ": " + isName(s));	
		s="a_a"; System.out.println(s + ": " + isName(s));	
	
		long runs = 30;
		long total = 0, r = runs;
		while (r > 0) {
			long i = 10000000;
			long start = new Date().getTime();
			while (i > 0) {
				isName("_A1b_2C3_");
				--i;
			}
			long duration = new Date().getTime() - start;
			System.out.print(" " + duration);
			total += duration; --r;
		}
		System.out.print(" avg: " + (total/runs));
	}
*/


	private static final String bslash = "" + (char)SDA.BSLASH;
	private static final String quote = "" + (char)SDA.QUOTE;

	/**
	 * Encode a string as an SDA value. This method formats its argument as an SDA
	 * value, with backslashes and quotes properly escaped.
	 * 
	 * @param value a string, for example 'The \ is called a "backslash" in English.'
	 * @return the encoded string, like 'The \\ is called a \"backslash\" in English.'
	 */
	public static String encode(String value) {
		return value.replace(bslash, bslash + bslash).replace(quote, bslash + quote);
	}
	
	
	private static SDAParser PARSER = new SDAParser();  // singleton parser
	
	/**
	 * Creates a data node from a character stream, using the default SDA parser.
	 * 
	 * @param input an input stream
	 * @return a (root) node
	 * @throws IOException       if an I/O operation failed
	 * @throws SDAParseException if an SDA parsing error occurs
	 */
	public static DataNode parse(Reader input) throws IOException, SDAParseException {
		return PARSER.parse(input);
	}
	
	
	/**
	 * Creates a data node from an input file, using the default SDA parser.
	 * 
	 * @param file an input file
	 * @return a (root) node
	 * @throws IOException    if an I/O operation failed
	 * @throws ParseException if an SDA parsing error occurs
	 */
	public static DataNode parse(File file) throws IOException, ParseException {
		return PARSER.parse(file);
	}
	
	
	/**
	 * Creates a data node from a string, using the default SDA parser.
	 * 
	 * @param input an input string
	 * @return a (root) node
	 * @throws IOException    if an I/O operation failed
	 * @throws ParseException if an SDA parsing error occurs
	 */
	public static DataNode parse(String input) throws IOException, ParseException {
		return PARSER.parse(input);
	}
	

	private static SDAFormatter FORMATTER = new SDAFormatter();  // singleton formatter
	
	/**
	 * Writes a formatted data node to a character stream, using the default SDA
	 * formatter.
	 * 
	 * @see SDAFormatter
	 * 
	 * @param output an output stream, not null
	 * @param node   the node to be rendered
	 * @throws IOException if an I/O operation failed
	 */
	public static void format(Writer output, DataNode node) throws IOException {
		FORMATTER.format(output, node);
	}


	/**
	 * Writes a formatted data node to a file, using the default SDA formatter.
	 * 
	 * @see SDAFormatter
	 * 
	 * @param node the node to be rendered
	 * @param file the file to be created or overwritten, not null
	 * @throws IOException if an I/O operation failed
	 */
	public static void format(File file, DataNode node) throws IOException {
		FORMATTER.format(file, node);
	}


	/**
	 * Formats a data node as a string, using the default SDA formatter.
	 * 
	 * @see SDAFormatter
	 * 
	 * @param node the node to be rendered
	 * @return a string representing the node
	 * @throws IOException if an I/O operation failed
	 */
	public static String format(DataNode node) throws IOException {
		return FORMATTER.format(node); // will never throw, but ok
	}
}
