/**
 * 
 */
package be.baur.sda.serialization;

import java.io.IOException;
import java.io.Writer;

import be.baur.sda.Node;
import be.baur.sda.NodeSet;
import be.baur.sda.SDA;

/**
 * This is the default formatter, which renders a {@code Node} and any child
 * nodes as SDA content in a human readable way, using Kernighan and Ritchie
 * style for indentation. For example:
 * 
 * <pre>
 * <code>
 * addressbook {
 *     contact "1" {
 *         firstname "Alice"
 *         phonenumber "06-11111111"
 *     }
 * }
 * </code>
 * 
 * <pre>
 * See also {@link Node}.
 */
public final class SDAFormatter implements Formatter {
	
	private final String indent;  // the string used for indentation
	
	
	/** Creates a formatter which uses a tab character for indentation. */
	public SDAFormatter() {
		this.indent = "\t";
	}
	
	
	/**
	 * Creates a formatter which uses consecutive blanks for indentation.
	 * 
	 * @param depth the indentation depth
	 * @throws IllegalArgumentException if depth is less than 0.
	 */
	public SDAFormatter(int depth) {
		if (depth < 0) throw 
			new IllegalArgumentException("invalid indentation depth (" + depth + ")");
		this.indent = new String(new char[depth]).replace("\0", " ");
	}
	
	
	public void format(Writer output, Node node) throws IOException {
		writeIndented(output, node, ""); output.flush();
	}

	
	// Private helper to recursively render the node and its children
	private void writeIndented(Writer output, Node node, String indent) throws IOException {
		
		NodeSet nodes = node.getNodes();
		
		output.write(indent + node.getName());
		
		if (! node.getValue().isEmpty() || nodes == null)
			output.write(" " + (char) SDA.QUOTE + SDA.encode(node.getValue()) + (char) SDA.QUOTE);
	
		if (nodes != null) {	
			boolean empty = nodes.isEmpty();
		
			output.write(" " + (char)SDA.LBRACE + (empty ? " " : "\n"));
			for (Node child : nodes) 
				writeIndented(output, child, indent + this.indent);
			output.write((empty ? "" : indent) + (char) SDA.RBRACE);
		}
		
		output.write("\n");
	}

}
