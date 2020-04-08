package be.baur.sda.parse;

/** To be thrown if the SDA syntax is violated. */
@SuppressWarnings("serial")
public final class SyntaxException extends Exception {
    
	public SyntaxException(long position, String message) { 
        super("SDA syntax violation at position " + position + ": " + message); 
    }
}
