package be.baur.sda;

/** General class to hold some constants and static convenience methods */
public final class SDA {
	
	/** A left brace (starts a node list). */
	public static final int LBRACE = '{'; 
	/** A right brace (ends a node list). */
	public static final int RBRACE = '}'; 
	/** A quote (encloses simple content). */
	public static final int QUOTE = '"'; 
	/** The escape character: a back slash. */
	public static final int ESCAPE = '\\';
	
	
	private final static String escape = "" + (char)SDA.ESCAPE;
	private final static String quote = "" + (char)SDA.QUOTE;

	/** Encodes the string as an SDA value, properly escaping backslashes and quotes. */
	public static String escape(String s) {
		return s.replace(escape, escape + escape).replace(quote, escape + quote);
	}
}
