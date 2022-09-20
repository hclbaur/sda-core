package be.baur.sda.serialization;

import java.text.ParseException;

/**
 * An exception thrown by a <code>Parser</code> if input cannot be deserialized
 * to create an SDA node. The offset (in characters counted from the start of
 * the input) where the error occurred, can be accessed from
 * {@link #getErrorOffset()}.
 */
@SuppressWarnings("serial")
public final class SyntaxException extends ParseException {

	/**
	 * Creates an SDA syntax error.
	 * 
	 * @param message an error message
	 * @param offset  the position where the error was found
	 */
	public SyntaxException(String message, int offset) {
		super("SDA syntax violation at position " + offset + ": " + message, offset);
	}
}
