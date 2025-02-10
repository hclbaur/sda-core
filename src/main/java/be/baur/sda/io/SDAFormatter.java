/**
 * 
 */
package be.baur.sda.io;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Objects;

import be.baur.sda.DataNode;
import be.baur.sda.SDA;

/**
 * The default SDA formatter, which renders a {@code DataNode} and any child
 * nodes as SDA content in a human readable way, using Kernighan and Ritchie
 * style for indentation. For example:
 * 
 * <pre>
 * addressbook {
 *     contact "1" {
 *         firstname "Alice"
 *         phonenumber "06-11111111"
 *     }
 * }
 * </pre>
 * 
 * Once created, this formatter is stateless and reusable.
 * <p>
 * @see DataNode
 */
public final class SDAFormatter implements Formatter<DataNode> {
	
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
	
	
	/**
	 * Serialize a data node in SDA format and write it to a character output
	 * stream. This method will ignore a null node reference (and write nothing).
	 */
	@Override
	public void format(final Writer output, DataNode node) throws IOException {

		Objects.requireNonNull(output, "output writer must not be null");
		if (node != null) {
			format(output, node, "");
			output.flush();
		}
	}


	/* Private helper to create SDA content. 
	 * */
	private void format(Writer output, DataNode node, String indent) throws IOException {
		
		final boolean isLeaf = node.isLeaf();
		
		output.append(indent).append(node.getName());
		
		if (! node.getValue().isEmpty() || isLeaf)
			output.append(" ").append((char) SDA.QUOTE)
			  .append(SDA.encode(node.getValue()))
			  .append((char) SDA.QUOTE);
	
		if (! isLeaf) {
			List<DataNode> nodes = node.nodes();
			boolean empty = nodes.isEmpty();
		
			output.append(" ").append((char)SDA.LBRACE).append((empty ? " " : "\n"));
			if (! empty) for (DataNode child : nodes) 
				format(output, child, indent + this.indent); // recursive call
			output.append(empty ? "" : indent).append((char) SDA.RBRACE);
		}
		
		output.append("\n");
	}

}
