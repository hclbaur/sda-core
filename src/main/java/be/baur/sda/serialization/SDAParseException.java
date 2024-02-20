package be.baur.sda.serialization;

/**
 * An {@code SDAParseException} is thrown by an {@code SDAParser} if the SDA
 * syntax is violated.
 * 
 * @see SDAParser
 */
@SuppressWarnings("serial")
public final class SDAParseException extends ParseException {

	/**
	 * Creates an SDA parse exception with an error message and offset.
	 * 
	 * @param message     an error message
	 * @param errorOffset the position where the error was found
	 */
	public SDAParseException(String message, int errorOffset) {
		super(message, errorOffset);
	}

}
