package be.baur.sda.io;

/**
 * A {@code FileParseException} is thrown by a {@code Parser} if a parsing error
 * occurs while processing character input read from a file. The 
 * 
 * @see Parser
 */
@SuppressWarnings("serial")
public class FileParseException extends ParseException {

	private String filename; // name of the file that is being parsed
	
	/**
	 * Creates a file parse exception caused by a parse exception.
	 * 
	 * @param filename the name of the file being parsed
	 * @param cause    the exception causing this exception to be thrown
	 */
	public FileParseException(String filename, ParseException cause) {
		super(cause.getMessage(), cause.getErrorOffset());
		initCause(cause); this.filename = filename;
	}


	/**
	 * Returns the name of the file the error occurred in.
	 * 
	 * @return a file name
	 */
	public String getErrorFilename() {
		return filename;
	}
	
	
	/**
	 * Returns a pre-formatted message that includes the filename.
	 * 
	 * @return "error parsing <i>filename</i>: <i>error text</i>"
	 */
	@Override
	public String getLocalizedMessage() {
		return "error parsing " + filename + ": " + getMessage();
	}

}
