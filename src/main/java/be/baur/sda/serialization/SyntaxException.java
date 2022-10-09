package be.baur.sda.serialization;

import java.text.ParseException;

/**
 * A {@code SyntaxException} is thrown by the <code>SDAParser</code> if the SDA
 * syntax is violated. The offset where the error occurred during parsing
 * (counted in characters from the start of the input) is available from
 * {@link #getErrorOffset()}.
 */
@SuppressWarnings("serial")
public final class SyntaxException extends ParseException {

	/**
	 * Creates an SDA syntax exception.
	 * 
	 * @param message an error message
	 * @param offset  the position where the error was found
	 */
	public SyntaxException(String message, int offset) {
		super("SDA syntax violation at position " + offset + ": " + message, offset);
	}
}
