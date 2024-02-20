package be.baur.sda.util;

import java.util.Objects;

/**
 * A {@code Result} can be used to return an operational result rather than an
 * exception. Results indicate success (OK) or failure (Error), and may hold a
 * typed value as well as a diagnostic (error) message.
 * 
 * @see Results
 */
public class Result <T> {

	private final T value;   		// the value associated with this result
	private final boolean ok;  		// whether this is an OK or error result
	private final String message;	// an informational / diagnostic message

	
	// protected constructor for factory methods or subclasses
	protected Result(boolean ok, T value, String message) {
		this.ok = ok; this.value = value; this.message = message;
	}


	// OK result factories. No values or messages are required.

	/**
	 * Returns an OK result.
	 * 
	 * @return a result, not null
	 */
	public static <T> Result<T> OK() {
		return new Result<T>(true, null, null);
	}


	/**
	 * Returns an OK result with a value.
	 * 
	 * @param value a value
	 * @return a result, not null
	 */
	public static <T> Result<T> OK(T value) {
		return new Result<T>(true, value, null);
	}


	/**
	 * Returns an OK result with an informational message.
	 * 
	 * @param message a message
	 * @return a result, not null
	 */
	public static <T> Result<T> OK(String message) {
		return new Result<T>(true, null, message);
	}

	
	/**
	 * Returns an OK result with a value and an informational message.
	 * 
	 * @param value   a value
	 * @param message a message
	 * @return a result, not null
	 */
	public static <T> Result<T> OK(T value, String message) {
		return new Result<T>(true, value, message);
	}


	// Error result factories. Errors without message are not considered useful.

	/**
	 * Returns an error result with a diagnostic message.
	 * 
	 * @param message a message, not null
	 * @return a result, not null
	 */
	public static <T> Result<T> error(String message) {
		Objects.requireNonNull(message, "message must not be null");
		return new Result<T>(false, null, message);
	}


	/**
	 * Returns an error result with a value and a diagnostic message.
	 * 
	 * @param value   a value
	 * @param message a message, not null
	 * @return a result, not null
	 */
	public static <T> Result<T> error(T value, String message) {
		Objects.requireNonNull(message, "message must not be null");
		return new Result<T>(false, value, message);
	}


	/**
	 * Returns true if this is an OK result, and false otherwise.
	 * 
	 * @return true or false
	 */
	public boolean isOK() {
		return ok;
	}


	/**
	 * Returns true if this is an error result, and false otherwise.
	 * 
	 * @return true or false
	 */
	public boolean isError() {
		return !ok;
	}


	/**
	 * Returns the (possibly null) value for this result.
	 * 
	 * @return a value, may be null
	 */
	public T getValue() {
		return value;
	}


	/**
	 * Returns the informational or diagnostic message for this result.
	 * 
	 * @return a message, not null, may be empty
	 */
	public String getMessage() {
		return message == null ? "" : message;
	}

}
