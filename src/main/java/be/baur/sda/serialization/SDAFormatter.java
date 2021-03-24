/**
 * 
 */
package be.baur.sda.serialization;

import java.io.IOException;
import java.io.Writer;

import be.baur.sda.ComplexNode;
import be.baur.sda.Node;
import be.baur.sda.NodeSet;
import be.baur.sda.SDA;
import be.baur.sda.SimpleNode;

/**
 * This is the default formatter, that renders a <code>Node</code> (and its
 * child nodes) as SDA content in a pretty - or at least human readable - way.
 */
public final class SDAFormatter implements Formatter {
	
	private final String indent;  // the string used for indentation
	
	
	/** Creates a (default) formatter which uses a tab character for indentation. */
	public SDAFormatter() {
		this.indent = "\t";
	}
	
	
	/**
	 * Creates a formatter with the specified indentation <code>depth</code>, using
	 * consecutive blanks for indentation.
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

	
	/** Private helper to recursively render the node and its children */
	private void writeIndented(Writer output, Node node, String indent) throws IOException {
		
		if (node instanceof SimpleNode) 
			output.write(indent + node.toString() + "\n"); 
		
		else if (node instanceof ComplexNode) {
			
			NodeSet childnodes = ((ComplexNode)node).nodes; 
			boolean children = childnodes.size() > 0;
			
			output.write(indent + node.getName() + " " + (char)SDA.LBRACE + (children ? "\n" : ""));
			for (Node child : childnodes) 
				writeIndented(output, child, indent + this.indent);
			output.write((children ? indent : "") + (char)SDA.RBRACE + "\n");
		}
	}

}
