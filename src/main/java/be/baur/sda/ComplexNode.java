package be.baur.sda;


/** A <code>ComplexNode</code> represents an SDA element with complex content;
 * a node that has no value but contains one or more child nodes, each of which 
 * can be complex, or {@link SimpleNode} objects. Children are a {@link NodeSet}.
 */
public final class ComplexNode extends Node {

	private final NodeSet childnodes;
	
	
    /** Creates a complex node and sets the name. */
	public ComplexNode(String name) {
		super(name); childnodes = new NodeSet();
	}
	
	
    /** Adds a child node (if it has no parent already).
     * @return true on success, false otherwise.
     */
	public boolean add(Node child) {

        if ( child != null && child.getParent() == null ) {
			if ( childnodes.add(child) ) {
				child.setParent(this); return true;
			}
        }
		return false;
	}
	
	
	/** Adds all nodes from an input set as children. */
	public void addAll(NodeSet children) {
		for (Node child : children) this.add(child);
	}

	
	/** Returns a reference to the child node set. */
	public NodeSet children() { return childnodes; }
	
	
	/** Removes a child node (if it is a child).
     * @return true on success, false otherwise.
     *
	public boolean removeChild(Node child) {

        if ( child != null && children.remove(child) ) {
			child.setParent(null); return true;
        }
		return false;
	}*/

	
	/** Renders the node as an SDA element (including any children). */
	public String toString() {
		return name + (char)SDA.LBRACE + " " + childnodes.toString() + (char)SDA.RBRACE;
	}

}