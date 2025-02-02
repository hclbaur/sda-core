/**
 * 
 */
package be.baur.sda.serialization;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Objects;

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
	 * Serializes and writes a node to a character output stream.
	 * <p>
	 * <i>Note:</i> Implementations should typically flush() but must not close()
	 * the output stream.
	 * 
	 * @param output an output stream, not null
	 * @param node   the node to be rendered
	 * @throws IOException if an I/O operation failed
	 */
	public void format(Writer output, T node) throws IOException;

	
	/**
	 * Serializes a node and writes it to a file in UTF-8 encoding.
	 * 
	 * @param node the node to be rendered
	 * @param file the file to be created or overwritten, not null
	 * @throws IOException if an I/O operation failed
	 */
	default void format(final File file, T node) throws IOException {

		Objects.requireNonNull(file, "input file must not be null");
		try (
			FileOutputStream fs = new FileOutputStream(file);
			Writer os = new OutputStreamWriter(fs, "UTF-8");
			Writer bw = new BufferedWriter(os);
		) {
			format(bw, node);
		} catch (Exception e) {
			throw new IOException("error writing to " + file, e);
		}
	}
		
	
	/**
	 * Serializes a node and returns a string.
	 * 
	 * @param node the node to be rendered
	 * @return a string representing the node
	 * @throws IOException if an I/O operation failed
	 */
	default String format(T node) throws IOException {

		Writer sw = new StringWriter();
		try {
			format(sw, node);
		} catch (Exception e) {
			throw new IOException("error formatting node " + node.getName(), e);
		}
		return sw.toString();
	}

}
