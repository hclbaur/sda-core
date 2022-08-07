package be.baur.sda;

/**
 * A <code>ComplexNode</code> represents an SDA element that has no value, but
 * contains zero or more child nodes (of complex or simple type). Any child
 * nodes are held in a {@link NodeSet}.
 */
public class ComplexNode extends Node {

	// An immutable reference to the set of child <code>nodes</code>; the reference
	// cannot change after creation, but the set itself can (add or remove nodes).
//	private final NodeSet nodes;

	/**
	 * Creates a complex node with the specified <code>name</code>.
	 * @throws IllegalArgumentException if <code>name</code> is invalid.
	 */
	public ComplexNode(String name) {
		super(name); this.add(null);
	}


//	/**
//	 * Returns the child nodes. The set could be empty, but is never <code>null</code>.
//	 */
//	public NodeSet getNodes() {
//		return nodes;
//	}

	
	public String toString() {
		return getName() + (char)SDA.LBRACE + " " + getNodes().toString() + (char)SDA.RBRACE;
	}
}