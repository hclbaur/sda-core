package be.baur.sda;

/**
 * A <code>Node</code> represents an SDA element. It has a name and a reference
 * to an (optional) parent node. This is an abstract base class.
 * To understand the SDA package, you should start here.
 * Next, have a look at a {@link SimpleNode}.
 */
public abstract class Node {

	/** The (immutable) name has public visibility, and there is no getter for it. */
	public final String name;
	
	/** A reference to the parent node. */
	private Node parent;

	
	/** Creates a node and sets the name. */
	public Node(String name) {
		this.name = name;
	}


	/**
	 * Sets the parent reference. Potentially dangerous, as this method does
	 * not actually update the parent. We call this from <code>ComplexNode</code>
	 * when adding a child node. Do not make this method public!
	 */
	void setParent(Node parent) {
		this.parent = parent;
	}

	/** Returns a reference to the parent. */
	public Node getParent() {
		return parent;
	}

	abstract public String toString();
}