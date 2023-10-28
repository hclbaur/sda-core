package be.baur.sda;

/**
 * An {@code SDAException} is the abstract root of all SDA related exceptions,
 * and may wrap other exceptions.
 */
@SuppressWarnings("serial")
public abstract class SDAException extends Exception {

	/**
	 * Creates an SDA exception with a detail message.
	 * 
	 * @param message an error message
	 */
	public SDAException(String message) {
		super(message);
	}

	
	/**
	 * Creates an SDA exception caused by another exception.
	 * 
	 * @param cause the exception causing this exception to be thrown
	 */
	public SDAException(Throwable cause) {
		super(cause);
	}

	
	/**
	 * Creates an SDA exception with a detail message and a root cause.
	 * 
	 * @param message an error message
	 * @param cause   the exception causing this exception to be thrown
	 */
	public SDAException(String message, Throwable cause) {
		super(message, cause);
	}

}
