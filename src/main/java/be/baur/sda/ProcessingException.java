package be.baur.sda;

import java.io.IOException;

/**
 * A {@code ProcessingException} may be thrown by a class processing a node tree
 * when an error (of any kind) is encountered.
 */
@SuppressWarnings("serial")
public abstract class ProcessingException extends IOException {

	final private Node errorNode;
	
	/**
	 * Creates a processing exception with an error message and node.
	 * 
	 * @param node    the node where the error was found
	 * @param message an error message
	 */
	public ProcessingException(Node node, String message) {
		super(message); this.errorNode = node;
	}


	/**
	 * Returns the node where the error was found.
	 * 
	 * @return a node
	 */
	public Node getErrorNode() {
		return errorNode;
	}
	
	
	/**
	 * Returns a message that includes the location (path) of the error node.
	 * 
	 * @return "error at <i>path</i>: <i>message</i>"
	 * @see Node#path()
	 */
	@Override
	public String getLocalizedMessage() {
		return (errorNode != null ? ("error at " + errorNode.path() + ": ") : "") + getMessage();
	}
}
