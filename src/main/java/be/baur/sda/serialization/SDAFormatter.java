/**
 * 
 */
package be.baur.sda.serialization;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

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
	
	
	@Override
	public void format(Writer output, DataNode node) throws IOException { 
		StringBuilder sb = new StringBuilder(); formatNode(sb, node, ""); 
		output.write(sb.toString()); output.flush();
	}

	
	/* Private helper to recursively adds to a string builder
	 * 
	 * @param sb the string builder
	 * @param node the node to add
	 * @param indent the indentation
	 * @throws IOException
	 */
	private void formatNode(StringBuilder sb, DataNode node, String indent) throws IOException {
		
		final boolean isLeaf = node.isLeaf();
		
		sb.append(indent).append(node.getName());
		
		if (! node.getValue().isEmpty() || isLeaf)
			sb.append(" ").append((char) SDA.QUOTE)
			  .append(SDA.encode(node.getValue()))
			  .append((char) SDA.QUOTE);
	
		if (! isLeaf) {
			List<DataNode> nodes = node.nodes();
			boolean empty = nodes.isEmpty();
		
			sb.append(" ").append((char)SDA.LBRACE).append((empty ? " " : "\n"));
			if (! empty) for (DataNode child : nodes) 
				formatNode(sb, child, indent + this.indent);
			sb.append(empty ? "" : indent).append((char) SDA.RBRACE);
		}
		
		sb.append("\n");
	}

}
