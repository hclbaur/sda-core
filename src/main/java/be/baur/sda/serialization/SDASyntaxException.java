package be.baur.sda.serialization;

/**
 * An {@code SDASyntaxException} is thrown by an {@code SDAParser} if the SDA
 * syntax is violated.
 * 
 * @see SDAParser
 */
@SuppressWarnings("serial")
public final class SDASyntaxException extends ParseException {

	/**
	 * Creates a SDA syntax exception with an error message and offset.
	 * 
	 * @param message     an error message
	 * @param errorOffset the position where the error was found
	 */
	public SDASyntaxException(String message, int errorOffset) {
		super("SDA syntax violation at position " + errorOffset + ": " + message, errorOffset);
	}

}
