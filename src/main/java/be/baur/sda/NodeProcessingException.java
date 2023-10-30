package be.baur.sda;

import java.io.IOException;

/**
 * A {@code NodeProcessingException} may be thrown by a class processing a node
 * tree when an error (of any kind) is encountered.
 */
@SuppressWarnings("serial")
public abstract class NodeProcessingException extends IOException {

	final private Node errorNode;
	
	/**
	 * Creates a processing exception with an error message and node.
	 * 
	 * @param node    the node where the error was found
	 * @param message an error message
	 */
	public NodeProcessingException(Node node, String message) {
		super(message); this.errorNode = node;
	}

	
//	/**
//	 * Creates a NodeProcessingException caused by another exception.
//	 * 
//	 * @param node  the node where the exception was thrown
//	 * @param cause the exception causing this exception to be thrown
//	 */
//	public NodeProcessingException(Node node, Throwable cause) {
//		super(cause); this.errorNode = node;
//	}
	
	
	/**
	 * Creates a NodeProcessingException with an error message, a node and a root
	 * cause.
	 * 
	 * @param node    the node where the error was found
	 * @param message an error message
	 * @param cause   the exception causing this exception to be thrown
	 */
	public NodeProcessingException(Node node, String message, Throwable cause) {
		super(message, cause); this.errorNode = node;
	}
	
	
	/**
	 * Returns the node where the error was found.
	 * 
	 * @return a node
	 */
	public Node getErrorNode() {
		return errorNode;
	}
}
