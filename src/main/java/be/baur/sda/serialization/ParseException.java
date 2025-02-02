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
	 * number of characters read up to the point where the error occurred).
	 * 
	 * @param message     an error message
	 * @param errorOffset the error position, non-negative integer
	 */
	public ParseException(String message, int errorOffset) {
		super(message, errorOffset < 0 ? 0 : errorOffset); 
	}


	/**
	 * Returns a pre-formatted message that includes the error position (offset).
	 * 
	 * @return "error at position <i>offset</i>: <i>error text</i>"
	 */
	@Override
	public String getLocalizedMessage() {
		return "error at position " + this.getErrorOffset() + ": " + getMessage();
	}

}
