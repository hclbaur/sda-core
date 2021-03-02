package be.baur.sda;

/**
 * A <code>Node</code> represents an SDA element. It has a name and a reference
 * to an (optional) parent node. This is an abstract base class. To understand
 * the SDA package, you should start here. Next, look at a {@link SimpleNode}.
 */
public abstract class Node {

	/** The (mutable) name has public visibility. Must fix! */
	public String name;
	
	/** A reference to the parent node (by definition a complex node). */
	private ComplexNode parent;

	
	/** Creates a node with a specified name. */
	public Node(String name) {
		/*
		 * Design choice: turn null or empty names into "null" as the actual name, to
		 * prevent null pointer exceptions downstream. Since SDA does not support
		 * anonymous nodes, all nodes must have a name, and "null" will be a clear
		 * indication that no proper name was supplied.
		 */
		this.name = (name == null || name.isEmpty()) ? "null" : name;
	}

	/** Sets the name of this node. */
	public void setName(String name) {
		/*
		 * This method should check if the name is valid according to SDA syntax rules
		 * and throw a runtime exception otherwise! See also constructor comments.
		 */
		this.name = name;
	}
	
	/** Returns the name of this node. */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the parent node. Potentially dangerous, as this method does not
	 * actually update the parent. We call this from a <code>ComplexNode</code> 
	 * when adding a child node. Do not make this method public!
	 */
	void setParent(ComplexNode parent) {
		this.parent = parent;
	}

	/** Returns the parent of this node or <code>null</code>. */
	public ComplexNode getParent() {
		return parent;
	}
	
	/** Returns the ultimate parent (or root) of this node or itself if it is <em>is</em> the root. */
	public Node getRoot() {
		return ((parent != null) ? parent.getRoot() : this);	
	}

	/**
	 * Returns the "pathname" of this node in X-path style. When a node occurs more
	 * than once in the same context, its position is indicated in square brackets,
	 * for example: <code>/root/message[3]/text</code> refers to the first (and
	 * only) text node in the third message node.
	 */
	public String path() {
		NodeSet similar = parent != null ? parent.get(name) : null;
		int pos = (similar != null && similar.size() > 1) ? similar.find(this) : 0;
		return (parent != null ? parent.path() : "") + "/" 
			+ name + (pos > 0 ? "[" + pos + "]" : "");	
	}
	
	abstract public String toString();
}