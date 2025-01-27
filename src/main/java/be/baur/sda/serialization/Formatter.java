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
	 * <i>Note:</i> mplementations may flush() but must not close() the output
	 * stream.
	 * 
	 * @param output an output stream
	 * @param node   the node to be rendered
	 * @throws IOException if an I/O operation failed
	 */
	public void format(Writer output, T node) throws IOException;

	
	/**
	 * Serializes a node and writes it to a file in UTF-8 encoding.
	 * 
	 * @param node the node to be rendered
	 * @param file the file to be created or overwritten
	 * @throws IOException if an I/O operation failed
	 */
	default void format(File file, T node) throws IOException {

		BufferedWriter output = new BufferedWriter(
			new OutputStreamWriter(new FileOutputStream(file), "UTF-8")
		);
		format(output, node);
		output.close();
	}	
		
	
	/**
	 * Serializes a node and returns a string.
	 * 
	 * @param node the node to be rendered
	 * @return a string representing the node
	 * @throws IOException if an I/O operation failed
	 */
	default String format(T node) throws IOException {

		Writer output = new StringWriter();
		format(output, node);
		return output.toString();
	}
	
}
