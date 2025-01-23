/**
 * 
 */
package be.baur.sda.serialization;

import java.io.IOException;
import java.io.Writer;

import be.baur.sda.Node;

/**
 * A {@code Formatter} is a <i>serializer</i> that renders a {@code Node} in a
 * format specific to the type of formatter, and writes it to an output stream.
 * A sample implementation is the default SDA formatter.
 * 
 * @see SDAFormatter
 */
public interface Formatter<T extends Node> {
	
	/**
	 * Serialize and write a node to a character output stream.
	 * 
	 * @param output an output stream
	 * @param node the node to be rendered
	 * @throws IOException if an I/O operation failed
	 */
	public void format(Writer output, T node) throws IOException;

}
