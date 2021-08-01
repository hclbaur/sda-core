package be.baur.sda;

/**
 * A <code>Node</code> represents an SDA element. It has a <code>name</code> and
 * an (optional) reference to a <code>parent</code> node. To understand the SDA
 * package, you should start at this abstract base class. Next, have a look at a
 * {@link SimpleNode}.
 */
public abstract class Node {

	private String name; 		// the name tag of this node
	private ComplexNode parent; // a reference to a parent
	
	
	/**
	 * Creates a node with the specified <code>name</code>, or throws an exception
	 * in case of an invalid node name. See {@link SDA#isName}.
	 * @throws IllegalArgumentException
	 */
	public Node(String name) {
		this.setName(name);
	}
	
	/* Note that most of the methods in this class are final! */
	
	/**
	 * Sets the <code>name</code> of this node.
	 * @throws IllegalArgumentException if <code>name</code> is invalid.
	 */
	public final void setName(String name) {
		if (! SDA.isName(name)) 
			throw new IllegalArgumentException("invalid node name ("+ name + ")");
		this.name = name;
	}
	
	
	/** Returns the <code>name</code> of this node. */
	public final String getName() {
		return name;
	}
	
	
	/*
	 * Sets the parent. This is called from a NodeSet to maintain parent-child
	 * integrity when adding or removing child nodes. Do not make this public!
	 */
	final void setParent(ComplexNode parent) {
		this.parent = parent;
	}

	
	/** Returns the <code>parent</code> of this node or <code>null</code>. */
	public final ComplexNode getParent() {
		return parent;
	}
	
	
	/**
	 * Returns the ultimate ancestor (root) of this node or the node itself if it
	 * has no parent.
	 */
	public final Node root() {
		return ((parent != null) ? parent.root() : this);	
	}
	
	
	/**
	 * Returns the "pathname" of this node in X-path style. When a node occurs more
	 * than once in the same context, its position is indicated in square brackets,
	 * for example: <code>/root/message[3]/text</code> refers to the first (and
	 * only) text node in the third message node.
	 */
	public final String path() {
		NodeSet similar = parent != null ? parent.getNodes().get(name) : null;
		int pos = (similar != null && similar.size() > 1) ? similar.find(this) : 0;
		return (parent != null ? parent.path() : "") + "/" 
			+ name + (pos > 0 ? "[" + pos + "]" : "");	
	}
	
	
	/** Returns the string representation of this node in SDA syntax. */
	@Override 
	abstract public String toString();
}