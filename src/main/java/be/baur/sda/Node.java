package be.baur.sda;

/**
 * A <code>Node</code> is the building block of an SDA document object model. It
 * has a <code>name</code>, a <code>value</code> and an (optional) reference to
 * a <code>parent</code> node. To understand the SDA package, you should start
 * here. Next, have a look at a {@link NodeSet}.
 */
public abstract class Node {

	private String name; 	// name (tag) of this node
	private String value; 	// the value of this node
	private Node parent; 	// reference to parent node
	private NodeSet nodes;	// reference to child nodes
	
	
	/**
	 * Creates a node with the specified <code>name</code> and an empty value.
	 * 
	 * @throws IllegalArgumentException see also {@link #setName}.
	 */
	public Node(String name) {
		this.setName(name);
	}

	
	/**
	 * Creates a node with the specified <code>name</code> and <code>value</code>.
	 * Accepts a <code>null</code> value, refer to {@link #setValue} for details.
	 * 
	 * @throws IllegalArgumentException see also {@link #setName}.
	 */
	public Node(String name, String value) {
		this.setName(name); this.setValue(value);
	}

	
	/* Note that most of the methods in this class are final! */
	
	/**
	 * Sets the <code>name</code> of this node.
	 * 
	 * @throws IllegalArgumentException if <code>name</code> is invalid. See
	 *                                  {@link SDA#isName}.
	 */
	public final void setName(String name) {
		if (! SDA.isName(name)) 
			throw new IllegalArgumentException("invalid node name (" + name + ")");
		this.name = name;
	}
	
	
	/** Returns the <code>name</code> of this node. */
	public final String getName() {
		return name;
	}
	
	
	/**
	 * Sets the <code>value</code> of this node. A <code>null</code> value is turned
	 * into an empty string to prevent null pointer exceptions at a later time.
	 * Since SDA does not support explicit nil, there is no valid reason to supply
	 * null other than to set an empty value.
	 */
	public final void setValue(String value) {
		this.value = (value == null) ? "" : value;
	}
	
	
	/** Returns the <code>value</code> of this node. */
	public final String getValue() {
		return value;
	}
	
	
	/*
	 * Sets the parent. This is called from a NodeSet to maintain parent-child
	 * integrity when adding or removing child nodes. Do not make this public!
	 */
	final void setParent(Node parent) {
		this.parent = parent;
	}

	
	/** Returns the <code>parent</code> of this node or <code>null</code>. */
	public final Node getParent() {
		return parent;
	}
	
	
	/**
	 * Returns the ultimate ancestor of this node, or the node itself if it has no
	 * parent.
	 */
	public final Node root() {
		return ((parent != null) ? parent.root() : this);	
	}


	/**
	 * Returns the set of child nodes. May return a <code>null</code> reference if
	 * the node is not a parent node!
	 */
	public NodeSet getNodes() {
		return nodes;
	}
	
	
	/**
	 * Adds a child <code>node</code> provided it has no parent yet. Adding a
	 * <code>null</code> reference turns this node in a "vacant parent" with an
	 * empty set of child nodes.
	 * 
	 * @return true if the set was modified.
	 */
	public boolean add(Node node) {
		if (node != null && node.getParent() != null) return false;
		if (nodes == null) nodes = new NodeSet(this);
        return nodes.add(node);
	}
	
	
	/**
	 * Returns the "path" to this node in X-path style. When a node occurs more than
	 * once in the same context, the position (with offset 1) is indicated in square
	 * brackets, for example: <code>/root/message[3]/text</code> refers to the first
	 * (and only) text node in the third message node beneath the root.
	 */
	public final String path() {
		NodeSet similar = parent != null ? parent.getNodes().get(name) : null;
		int pos = (similar != null && similar.size() > 1) ? similar.find(this) : 0;
		return (parent != null ? parent.path() : "") + "/" 
			+ name + (pos > 0 ? "[" + pos + "]" : "");	
	}
	
	
	/** Returns the string representation of this node in SDA syntax. */
	@Override
	public String toString() {
		return name + " " + (char)SDA.QUOTE + SDA.encode(value) + (char)SDA.QUOTE;
	}
}