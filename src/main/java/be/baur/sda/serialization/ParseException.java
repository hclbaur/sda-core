package be.baur.sda.serialization;

/**
 * A {@code ParseException} is thrown by a {@code Parser} if a parsing error
 * occurs.
 * 
 * @see Parser
 */
@SuppressWarnings("serial")
public class ParseException extends Exception {

	final int errorOffset; // position where the error was found

	/**
	 * Creates a parse exception with an error message and offset.
	 * 
	 * @param message     an error message
	 * @param errorOffset the position where the error was found
	 */
	public ParseException(String message, int errorOffset) {
		super(message); this.errorOffset = errorOffset;
	}

	/**
	 * Returns the offset where the error occurred during parsing (counted in
	 * characters from the start of the input).
	 * 
	 * @return the error offset
	 */
	public int getErrorOffset() {
		return errorOffset;
	}

}
