package be.baur.sda;

/**
 * A <code>Node</code> represents an SDA element. It has a name and a reference
 * to an (optional) parent. This a an abstract base class.
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
	 * not actually update the parent. To be called from <code>ComplexNode</code>
	 * when adding or removing a child node. Do not make this method public.
	 */
	void setParent(Node parent) {
		this.parent = parent;
	}

	/** Returns a reference to the parent. */
	public Node getParent() {
		return parent;
	}

	abstract public String render();

	abstract public String toString();
}