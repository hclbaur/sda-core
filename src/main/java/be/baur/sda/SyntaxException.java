package be.baur.sda;

/** Thrown if the SDA syntax is violated. */
@SuppressWarnings("serial")
public final class SyntaxException extends Exception {
    
	SyntaxException(long p, String message) { 
        super("SDA syntax violation at position " + p + ": " + message); 
    }
}
