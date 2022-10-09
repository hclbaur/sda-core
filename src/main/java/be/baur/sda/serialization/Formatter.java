/**
 * 
 */
package be.baur.sda.serialization;

import java.io.IOException;
import java.io.Writer;

import be.baur.sda.Node;

/**
 * A <code>Formatter</code> (in SDA context) is a <i>serializer</i> that
 * converts a {@link Node} to a character stream (in a format specific to the
 * type of formatter). A sample implementation is the default
 * {@link SDAFormatter}.
 */
public interface Formatter {
	
	/**
	 * Renders a node on the specified output stream.
	 * 
	 * @param output an output stream
	 * @param node the node to be rendered
	 * @throws IOException if an output exception occurs
	 */
	public void format(Writer output, Node node) throws IOException;

}
