package be.baur.sda;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import be.baur.sda.serialization.SDAFormatter;

/**
 * A <code>Node</code> is the basic building block of an SDA document. It has a
 * name and a value (simple content). A node can be a parent node, in which case
 * it contains other nodes (complex content).
 */
public class Node {

	private String name; 		// name (tag) of this node, never null
	private String value; 		// the value of this node, never null
	private Node parent; 		// reference to parent node, null if this is not a child node
	private List<Node> nodes;	// reference to child nodes, null if this is a leaf node
	
	
	/**
	 * Creates a node with the specified name and an empty value.
	 * 
	 * @param name a valid node name, see {@link #setName}
	 * @throws IllegalArgumentException if the name is invalid
	 */
	public Node(String name) {
		setName(name); this.value = "";
	}

	
	/**
	 * Creates a node with the specified name and value. This method will gracefully
	 * handle a null value.
	 * 
	 * @param name a valid node name, see {@link #setName}
	 * @param value a string value, may be null, see {@link #setValue}
	 * @throws IllegalArgumentException if the name is invalid
	 */
	public Node(String name, String value) {
		setName(name); setValue(value);
	}

	
	/**
	 * Sets the name (tag) of this node. A name cannot be null or empty, but other
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
	 * @return the string value, not null, may be empty
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
	 * Returns the parent of this node or null if has none.
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
	 * this node is a leaf node, or a "vacant parent" (for lack of a better term)
	 * which actually has an empty list of children.
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
	 * Returns true if this node has no child set. This method returns false for a
	 * parent node or for a "vacant parent" with an <i>empty</i> child set, like
	 * <code>node { }</code>.
	 * 
	 * @return true if this node has no child set (empty or not)
	 * @see isParent
	 * @see getNodes
	 */
	public boolean isLeaf() {
		return (nodes == null);
	}


	/**
	 * Returns true if this node has at least one child node. This method returns
	 * false for a leaf node or for a "vacant parent" with an <i>empty</i> child
	 * list, like <code>node { }</code>..
	 * 
	 * @return true if this node has a non-empty child set
	 * @see isLeaf
	 * @see getNodes
	 */
	public boolean isParent() {
		return ! (nodes == null || nodes.isEmpty());
	}


	/**
	 * Adds a child node to {@code this} node. Adding a node that already has a
	 * parent will not work (no child is automatically detached from its parent).
	 * Adding a null reference has no effect if this node has complex content
	 * already, but it will turn a node without complex content into a "vacant
	 * parent" (like <code>node { }</code>). See also {@link #isLeaf} and
	 * {@link #isParent}.
	 * 
	 * @param node a node to be added, may be null
	 * @return true if the node was added
	 */
	public final boolean add(Node node) {
		if (nodes == null) nodes = new CopyOnWriteArrayList<Node>();
		if (node == null) return false; node.setParent(this); 
		return nodes.add(node);
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

		String str = name;
		
		if (! value.isEmpty() || nodes == null) 
			str += " " + (char) SDA.QUOTE + SDA.encode(value) + (char) SDA.QUOTE;

		if (nodes != null) {
			str += " " + (char)SDA.LBRACE + " ";
			for (Node node : nodes) 
				str += node.toString() + " ";
			str += (char)SDA.RBRACE;
		}

		return str;
	}
}