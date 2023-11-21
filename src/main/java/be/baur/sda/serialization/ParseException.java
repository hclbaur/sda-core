package be.baur.sda.serialization;

import java.io.IOException;

/**
 * A {@code ParseException} is thrown by a {@code Parser} if a parsing error
 * occurs.
 * 
 * @see Parser
 */
@SuppressWarnings("serial")
public abstract class ParseException extends IOException {

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
	 * Returns the position where the error was found (counted in characters from
	 * the start of the input).
	 * 
	 * @return the error offset
	 */
	public int getErrorOffset() {
		return errorOffset;
	}
	
	
	/**
	 * Returns a parse exception with a message that includes the error position.
	 * 
	 * @return "error at position <i>position</i>: <i>message</i>"
	 */
	public String getLocalizedMessage() {
		return "error at position " + errorOffset + ": " + getMessage();
	}

}
