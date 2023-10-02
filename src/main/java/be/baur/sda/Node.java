package be.baur.sda;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import be.baur.sda.serialization.SDAFormatter;

/**
 * A <code>Node</code> is the basic building block of an SDA document. It has a
 * name and a value (simple content). A node can be a parent node, in which case
 * it contains other nodes (complex content).
 */
public class Node {

	private String name; 		// name of this node, never null
	private String value; 		// the value of this node, never null
	private Node parent; 		// reference to parent node, null if this is not a child node
	private List<Node> nodes;	// reference to child nodes, null if this is a leaf node
	
	
	/**
	 * Creates a node with the specified name and an empty value.
	 * 
	 * @param name a valid node name
	 * @throws IllegalArgumentException if the name is invalid
	 * @see #setName
	 */
	public Node(String name) {
		setName(name); this.value = "";
	}

	
	/**
	 * Creates a node with the specified name and value. This method will treat a
	 * null value as if an empty string was supplied.
	 * 
	 * @param name  a valid node name
	 * @param value a string value, may be null
	 * @throws IllegalArgumentException if the name is invalid
	 * @see #setName
	 * @see #setValue
	 */
	public Node(String name, String value) {
		setName(name); setValue(value);
	}

	
	/**
	 * Sets the name (tag) of this node. A name cannot be null or empty, but other
	 * restrictions apply as well. Refer to {@link SDA#isName} for details.
	 * 
	 * @param name a valid node name
	 * @throws IllegalArgumentException if the name is invalid
	 */
	public final void setName(String name) {
		if (! SDA.isName(name)) 
			throw new IllegalArgumentException("invalid node name (" + name + ")");
		this.name = name;
	}
	
	
	/**
	 * Returns the name (tag) of this node.
	 * 
	 * @return a node name, not null or empty
	 */
	public final String getName() {
		return name;
	}
	
	
	/**
	 * Sets the simple content value of this node. A null value is turned into an
	 * empty string to prevent accidental null pointer exceptions at a later time.
	 * Since SDA does not support explicit nil, there is no valid reason to supply
	 * null other than to set an empty value.
	 * 
	 * @param value a string value, may be null
	 */
	public final void setValue(String value) {
		this.value = (value == null || value.isEmpty()) ? "" : value;
	}
	
	
	/**
	 * Returns the value of this node. This method returns an empty string if no
	 * value has been set.
	 *
	 * @return the string value, never null, may be empty
	 */
	public final String getValue() {
		return value;
	}
	
	
	/*
	 * Sets the parent. This is called internally to maintain parent-child
	 * integrity when adding or removing child nodes. Do not make public !
	 */
	private final void setParent(Node parent) {
		this.parent = parent;
	}

	
	/**
	 * Returns the parent of this node or null if it has none.
	 * 
	 * @return the parent node, may be null
	 */
	public final Node getParent() {
		return parent;
	}
	
	
	/**
	 * Returns the ultimate ancestor (root) of this node. This method returns the
	 * node itself if it has no parent (in which case it <i>is</i> the root).
	 * 
	 * @return the root node, may be this node
	 */
	public final Node root() {
		return ((parent != null) ? parent.root() : this);
	}


	private final List<Node> EMPTY_LIST = Collections.emptyList();
	
	/**
	 * Returns an unmodifiable list of child nodes. This will be an empty list if
	 * this node has no children.
	 * 
	 * @see #isLeaf
	 * @see #isParent
	 * @return a list of nodes, never null
	 */
	public List<Node> nodes() {
		if (nodes == null) return EMPTY_LIST;
		return Collections.unmodifiableList(nodes);
	}


	/**
	 * Returns true if this node has no child list (empty or not). This method
	 * returns false both for a parent node and for a "vacant parent" with an <i>empty</i>
	 * child set, which would be the SDA equivalent of "<code>node { }</code>".
	 * 
	 * @return true if this is a leaf node
	 * @see #isParent
	 */
	public boolean isLeaf() {
		return (nodes == null);
	}


	/**
	 * Returns true if this node has at least one child node, and false otherwise.
	 * 
	 * @return true if this is a parent node
	 * @see #isLeaf
	 */
	public boolean isParent() {
		return ! (nodes == null || nodes.isEmpty());
	}


	/**
	 * Adds a child node to this node. Attempting to add a node that already has a
	 * parent will cause an exception. Adding a null reference will turn a leaf node
	 * into a "vacant parent" (this is a feature) but has no effect otherwise.
	 * 
	 * @param node a node to be added, may be null
	 * @return true if the node was added
	 * @throws IllegalStateException if the node has a parent already
	 * @see #isLeaf
	 * @see #isParent
	 */
	public final synchronized boolean add(Node node) {
		if (node != null && node.getParent() != null)
			throw new IllegalStateException("node '" + node.getName() + "' already has a parent");
		if (nodes == null) // this is why we need synchronization
			nodes = new ArrayList<Node>();
		if (node != null && nodes.add(node)) {
			node.setParent(this);
			return true;
		}
		return false;
	}

	
	/**
	 * Returns the first child node with the specified name, or null if no such node
	 * is found.
	 * 
	 * @param name a node name
	 * @return a node, may be null
	 */
	public Node get(String name) {
		if (nodes != null) {
			for (Node node : nodes) 
				if (node.getName().equals(name)) return node;
		}
		return null;
	}
	
	
	/**
	 * Returns a list of child nodes with the specified name, or an empty list if no
	 * such nodes are found.
	 * 
	 * @param name a node name
	 * @return a node list, may be empty
	 */
	public List<Node> find(String name) {
		List<Node> list = new ArrayList<Node>();
		if (nodes != null) {
			for (Node node : nodes)
				if (node.getName().equals(name)) list.add(node);
		}
		return list;
	}

	
	/**
	 * Returns a list of child nodes that satisfy the given predicate, or an empty
	 * list if no such nodes are found.
	 * 
	 * @param predicate a boolean valued function of one argument
	 * @return a node list, may be empty
	 */
	public List<Node> find(Predicate<? super Node> predicate) {
		if (nodes == null) return new ArrayList<Node>();
		return nodes.stream().filter(predicate).collect(Collectors.toCollection(ArrayList::new));
	}


	/**
	 * Returns the location of this node in X-path style. When a node occurs more than
	 * once in the same context, the position (starting at 1) is indicated in square
	 * brackets, for example: <code>/root/message[3]/text</code> refers to the first
	 * (and only) text node in the third message node beneath the root.
	 * 
	 * @return the path to this node
	 */
	public final String path() {
		List<Node> same = (parent != null) ? parent.find(name) : null;
		int pos = (same != null && same.size() > 1) ? same.indexOf(this)+1 : 0;

		return (parent != null ? parent.path() : "") 
			+ "/" + name + (pos > 0 ? "[" + pos + "]" : "");
	}
	
	
	/**
	 * Returns the string representing this node in SDA notation. For example:
	 * 
	 * <pre>
	 * node ""
	 * node "value"
	 * </pre>
	 * 
	 * for leaf nodes, and
	 * 
	 * <pre>
	 * node "value" { ... }
	 * node { ... }
	 * </pre>
	 * 
	 * for parent nodes (with or without value). Note that the returned string is
	 * formatted as a single line of text. For a pretty (more readable) format, 
	 * refer to {@link SDAFormatter}.
	 * 
	 * @return an SDA representation of this node
	 */
	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder(name);
		
		if (! value.isEmpty() || nodes == null) 
			sb.append(" ").append((char) SDA.QUOTE)
				.append(SDA.encode(value)).append((char) SDA.QUOTE);

		if (nodes != null) {
			sb.append(" ").append((char)SDA.LBRACE).append(" ");
			for (Node node : nodes) 
				sb.append(node.toString()).append(" ");
			sb.append((char)SDA.RBRACE);
		}

		return sb.toString();
	}

}