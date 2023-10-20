/**
 * 
 */
package be.baur.sda.serialization;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import be.baur.sda.Node;
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
 * </pre>
 * 
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
		StringBuilder sb = new StringBuilder(); addNode(sb, node, ""); 
		output.write(sb.toString()); output.flush();
	}

	
	/* Private helper to recursively add the rendered node and children to a builder
	 * 
	 * @param sb the string builder
	 * @param node the node to add
	 * @param indent the indentation
	 * @throws IOException
	 */
	private void addNode(StringBuilder sb, Node node, String indent) throws IOException {
		
		boolean isLeaf = node.isLeaf();
		
		sb.append(indent).append(node.getName());
		
		if (! node.getValue().isEmpty() || isLeaf)
			sb.append(" ").append((char) SDA.QUOTE)
			  .append(SDA.encode(node.getValue()))
			  .append((char) SDA.QUOTE);
	
		if (! isLeaf) {
			List<Node> nodes = node.nodes();
			boolean empty = nodes.isEmpty();
		
			sb.append(" ").append((char)SDA.LBRACE).append((empty ? " " : "\n"));
			if (! empty) for (Node child : nodes) 
				addNode(sb, child, indent + this.indent);
			sb.append(empty ? "" : indent).append((char) SDA.RBRACE);
		}
		
		sb.append("\n");
	}

}
