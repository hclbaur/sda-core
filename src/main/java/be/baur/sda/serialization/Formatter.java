/**
 * 
 */
package be.baur.sda.serialization;

import java.io.Writer;

import be.baur.sda.Node;

/**
 * A <code>Formatter</code> (in SDA context) is a <em>serializer</em> that can
 * convert (render) an SDA document in memory to a character stream, in a format
 * specific to the type of formatter.
 */
public interface Formatter {
	
	/**
	 * Renders a <code>node</code> on the specified <code>output</code> stream.
	 * @throws Exception
	 */
	public void format(Writer output, Node node) throws Exception;

}
