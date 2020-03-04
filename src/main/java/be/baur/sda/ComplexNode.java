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
	
	
	/** Adds all nodes from an input set as children. 
     * @return true if all are added, false otherwise.
     */
	public boolean addAll(NodeSet children) {

		boolean result = true;
		for (Node child : children) result &= this.add(child);
		return result;
	}

	
	/** Returns a reference to the child node set. */
	public NodeSet children() { return childnodes; }
	
	
	/** Renders the node as an SDA element (including any children). */
	public String toString() {
		return name + (char)SDA.LBRACE + " " + childnodes.toString() + (char)SDA.RBRACE;
	}

}