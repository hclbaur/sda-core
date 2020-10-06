package be.baur.sda.parse;

import java.text.ParseException;

/**
 * To be thrown if the SDA syntax is violated. The offset (in number of
 * characters from the start of the input) is the position where the error
 * occurred and can be accessed by <code>getErrorOffset()</code>.
 */
@SuppressWarnings("serial")
public final class SyntaxException extends ParseException {

	public SyntaxException(String message, int offset) {
		super("SDA syntax violation at position " + offset + ": " + message, offset);
	}
}
