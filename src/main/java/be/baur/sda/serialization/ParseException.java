package be.baur.sda.serialization;

/**
 * A {@code ParseException} is thrown by a {@code Parser} if a parsing error
 * occurs.
 * 
 * @see Parser
 */
@SuppressWarnings("serial")
public abstract class ParseException extends java.text.ParseException {

	/**
	 * Creates a parse exception with an error message and offset (counted in
	 * characters from the start of the input).
	 * 
	 * @param message     an error message
	 * @param errorOffset the position where the error was found
	 */
	public ParseException(String message, int errorOffset) {
		super(message, errorOffset); 
	}


	/**
	 * Returns a pre-formatted message that includes the error position (offset).
	 * 
	 * @return "error at position <i>offset</i>: <i>error text</i>"
	 */
	public String getLocalizedMessage() {
		return "error at position " + this.getErrorOffset() + ": " + getMessage();
	}

}
