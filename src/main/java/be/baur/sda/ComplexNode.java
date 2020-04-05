package be.baur.sda;


/** A <code>ComplexNode</code> represents an SDA element that has no value,
 * but contains one or more child nodes (of complex or simple type).
 * Child nodes are implemented as a {@link NodeSet}.
 */
public final class ComplexNode extends Node {

	private final NodeSet children;


    /** Creates a complex node and sets the name. */
	public ComplexNode(String name) {
		super(name); children = new NodeSet();
	}


    /** Adds a child node (if it has no parent already).
     * @return true on success, false otherwise.
     */
	public boolean add(Node child) {

        if ( child != null && child.getParent() == null ) {
			if ( children.add(child) ) {
				child.setParent(this); return true;
			}
        }
		return false;
	}


	/** Adds all nodes from an input set as children. 
     * @return true if all are added, false otherwise.
     */
	public boolean add(NodeSet children) {

		boolean result = true;
		for (Node child : children) result &= this.add(child);
		return result;
	}


	/** Get a single child, by an index in the range 1 .. size(). */
	public Node get(int index) { return children.get(index); }


	/** Get one or more children with a particular name.*/
	public NodeSet get(String name) { return children.get(name); }

	
	/** Returns a reference to all child nodes. */
	public NodeSet get() { return children; }
	
	
	/** Renders the node as an SDA element (including any children). */
	public String toString() {
		return name + (char)SDA.LBRACE + " " + children.toString() + (char)SDA.RBRACE;
	}

}