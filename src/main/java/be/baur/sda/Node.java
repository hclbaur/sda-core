package be.baur.sda;

/**
 * A <code>Node</code> is the basic building block of an SDA document object
 * model. It has a name and a value (simple content). A node can be a parent
 * node, in which case it contains other nodes (complex content).
 * 
 * @see {@link NodeSet}.
 */
public class Node {

	private String name; 	// name (tag) of this node
	private String value; 	// the value of this node
	private Node parent; 	// reference to parent node
	private NodeSet nodes;	// reference to child nodes
	
	
	/**
	 * Creates a node with the specified name and an empty value.
	 * 
	 * @throws IllegalArgumentException if the name is invalid
	 * @see {@link #setName}
	 */
	public Node(String name) {
		setName(name); this.value = "";
	}

	
	/**
	 * Creates a node with the specified name and value. This method will gracefully
	 * handle a <code>null</code> value.
	 * 
	 * @throws IllegalArgumentException if the name is invalid
	 * @see {@link #setName} and {@link #setValue}
	 */
	public Node(String name, String value) {
		setName(name); setValue(value);
	}

	
	/**
	 * Sets the name of this node. A name must not be null or empty, but more
	 * restrictions apply. Refer to {@link SDA#isName} for details.
	 * 
	 * @param name a valid node name
	 * @throws IllegalArgumentException if the name is invalid
	 */
	public final void setName(String name) {
		if (! SDA.isName(name)) 
			throw new IllegalArgumentException("invalid node name (" + name + ")");
		this.name = name;
	}
	
	
	/** Returns the name of this node. */
	public final String getName() {
		return name;
	}
	
	
	/**
	 * Sets the simple content value of this node. A <code>null</code> value is
	 * turned into an empty string to prevent accidental null pointer exceptions at
	 * a later time. Since SDA does not support explicit nil, there is no valid
	 * reason to supply null other than to set an empty value.
	 * 
	 * @param value a string value, may be null
	 */
	public final void setValue(String value) {
		this.value = (value == null || value.isEmpty()) ? "" : value;
	}
	
	
	/**
	 * Returns the value of this node or an empty string if no value has been set.
	 * This method will never return a <code>null</code> reference.
	 */
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

	
	/**
	 * Returns the parent of this node or a <code>null</code> reference if it has no
	 * parent.
	 */
	public final Node getParent() {
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
	 * Returns the set of child nodes. Will return <code>null</code> for a node with
	 * simple content only (such as <code>node "value"</code>), and an empty set for
	 * a "vacant parent" (like <code>node { }</code>).
	 * 
	 * @return <code>null</code> or a {@link NodeSet}.
	 * @see {@link #isComplex} and {@link #isParent}.
	 */
	public NodeSet getNodes() {
		return nodes;
	}


	/**
	 * Returns <code>true</code> if this node has complex content. Will return
	 * <code>false</code> for a node with simple content <i>only</i> (such as
	 * <code>node "value"</code>), and <code>true</code> for a parent node or a
	 * "vacant parent" with an empty child set (like <code>node { }</code>).
	 * 
	 * @return <code>true</code> if this node has a {@link NodeSet} (empty or not).
	 * @see {@link #getNodes} and {@link #isParent}.
	 */
	public boolean isComplex() {
		return (nodes != null);
	}

	
	/**
	 * Returns <code>true</code> if this node has one or more child nodes. Will
	 * return <code>false</code> for a node with simple content <i>only</i> (such as
	 * <code>node "value"</code>), and for a "vacant parent" with an empty child set
	 * (like <code>node { }</code>).
	 * 
	 * @return <code>true</code> if this node has a non-empty {@link NodeSet}.
	 * @see {@link #getNodes} and {@link #isComplex}.
	 */
	public boolean isParent() {
		return ! (nodes == null || nodes.isEmpty());
	}


	/**
	 * Add a child {@code node} to this node. Adding a node that already has a
	 * parent will not work (no child is automatically detached from its parent).
	 * Adding a {@code null} reference has no effect if this node has complex
	 * content already, but it will turn a node without complex content into a
	 * "vacant parent" (like <code>node { }</code>).
	 * 
	 * @param node the node to be added, may be null
	 * @return true if a node was added, false otherwise
	 * @see {@link #isComplex} and {@link #isParent}
	 */
	public final boolean add(Node node) {
		if (node != null && node.getParent() != null) return false;
		if (nodes == null) nodes = new NodeSet(this);
		return (node == null) ? false : nodes.add(node);
	}
	
	
	/**
	 * Returns the location of this node in X-path style. When a node occurs more than
	 * once in the same context, the position (starting at 1) is indicated in square
	 * brackets, for example: <code>/root/message[3]/text</code> refers to the first
	 * (and only) text node in the third message node beneath the root.
	 */
	public final String path() {
		NodeSet similar = parent != null ? parent.getNodes().get(name) : null;
		int pos = (similar != null && similar.size() > 1) ? similar.find(this) : 0;

		return (parent != null ? parent.path() : "") + "/" + name + (pos > 0 ? "[" + pos + "]" : "");
	}
	
	
	/**
	 * Returns the string representation of this node in SDA notation. For example
	 * 
	 * <pre>
	 * node ""
	 * node "value"
	 * </pre>
	 * 
	 * for nodes without child nodes, and
	 * 
	 * <pre>
	 * node "value" { ... }
	 * node { ... }
	 * </pre>
	 * 
	 * for parent nodes (with or without value).
	 */
	@Override
	public String toString() {

		String str = name;
		
		if (! value.isEmpty() || nodes == null) 
			str += " " + (char) SDA.QUOTE + SDA.encode(value) + (char) SDA.QUOTE;

		if (nodes != null)
			str += " " + (char)SDA.LBRACE + " " + nodes.toString() + (char)SDA.RBRACE;

		return str;
	}
}