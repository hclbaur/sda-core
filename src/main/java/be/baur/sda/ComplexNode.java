package be.baur.sda;

/**
 * A <code>ComplexNode</code> represents an SDA element that has no value, but
 * contains zero or more child nodes (of complex or simple type). Any child
 * nodes are held in a {@link NodeSet}.
 */
public class ComplexNode extends Node {

	/**
	 * A public, immutable reference to the set of child <code>nodes</code>; the
	 * reference cannot be changed after creation, but the set can be changed (add
	 * or remove child nodes).
	 */
	public final NodeSet nodes;

	/**
	 * Creates a complex node with the specified <code>name</code>.
	 * @throws IllegalArgumentException if <code>name</code> is invalid.
	 */
	public ComplexNode(String name) {
		super(name); nodes = new NodeSet(this);
	}
	

	public String toString() {
		return getName() + (char)SDA.LBRACE + " " + nodes.toString() + (char)SDA.RBRACE;
	}
}