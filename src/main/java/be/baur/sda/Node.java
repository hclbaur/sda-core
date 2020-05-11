package be.baur.sda;

/**
 * A <code>Node</code> represents an SDA element. It has a name and a reference
 * to an (optional) parent node. This is an abstract base class. To understand
 * the SDA package, you should start here. Next, look at a {@link SimpleNode}.
 */
public abstract class Node {

	/**
	 * The (immutable) name has public visibility, and there is no getter for it.
	 * Questionable design choice, I will probably fix this before I go public :)
	 */
	public final String name;
	
	/** A reference to the parent node. */
	private Node parent;

	
	/** Creates a node and sets the name. */
	public Node(String name) {
		/*
		 * Design choice: turn null or empty names into "null" as the actual name, to
		 * prevent null pointer exceptions downstream. Since SDA does not support
		 * anonymous nodes, all nodes must have a name, and "null" will be a clear
		 * indication that no proper name was supplied.
		 */
		this.name = (name == null || name.isEmpty()) ? "null" : name;
	}

	/**
	 * Sets the parent reference. Potentially dangerous, as this method does not
	 * actually update the parent. We call this from <code>ComplexNode</code> when
	 * adding a child node. Do not make this method public!
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