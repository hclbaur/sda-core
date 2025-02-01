package be.baur.sda.serialization;

/**
 * A {@code FileParseException} is thrown by a {@code Parser} if a parsing error
 * occurs while processing character input read from a file.
 * 
 * @see Parser
 */
@SuppressWarnings("serial")
public class FileParseException extends ParseException {

	private String fileName; // name of the file that is being parsed
	
	/**
	 * Creates a file parse exception caused by a parse exception.
	 * 
	 * @param fileName name of the file being parsed
	 * @param cause    the exception causing this exception to be thrown
	 */
	public FileParseException(String fileName, ParseException cause) {
		super(cause.getMessage(), cause.getErrorOffset());
		initCause(cause); this.fileName = fileName;
	}


	/**
	 * Returns a pre-formatted message that includes the filename and error position (offset).
	 * 
	 * @return "error parsing <i>filename</i> at position <i>offset</i>: <i>error text</i>"
	 */
	@Override
	public String getLocalizedMessage() {
		return "error parsing " + fileName + " at position " + getErrorOffset() + ": " + getMessage();
	}

}
