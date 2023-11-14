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
}
