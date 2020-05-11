/**
 * 
 */
package be.baur.sda.render;

import java.io.IOException;
import java.io.Writer;

import be.baur.sda.ComplexNode;
import be.baur.sda.Node;
import be.baur.sda.NodeSet;
import be.baur.sda.SDA;
import be.baur.sda.SimpleNode;

/** Render class to pretty-print an SDA node and its children.
 */

public final class Renderer {
	
	private Writer output;
	private String indstr = "\t";
	
	/** Render with default indentation (tab) */
	public void render(Writer output, Node node) throws IOException {
		
		this.output = output; writeIndented(node, 0); output.flush();
	}

	/** Render with indentation size <code>indent</code> */
	public void Render(Writer output, Node node, int indent) throws IOException {
		
		this.indstr = new String(new char[indent]).replace("\0", " "); 
		render(output, node);
	}
	
	/* private helper to recursively render the node and its children */
	private void writeIndented(Node node, int level) throws IOException {
		
		String indent = (level == 0) ? "" :
			new String(new char[level]).replace("\0", indstr);
		
		if (node instanceof SimpleNode) 
			output.write(indent + node.toString() + "\n"); 
		
		else if (node instanceof ComplexNode) {
			
			NodeSet childnodes = ((ComplexNode)node).get(); 
			boolean children = childnodes.size() > 0;
			
			output.write(indent + node.name + " " + (char)SDA.LBRACE + (children ? "\n" : ""));
			for (Node child : childnodes) writeIndented(child, level + 1);
			output.write((children ? indent : "") + (char)SDA.RBRACE + "\n");
		}
		
	}

}
