package be.baur.sda;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

/**
 * A {@code Node} is an object in a tree-like structure that stems from a single
 * root node. A node may have child nodes (in which case it is called a parent
 * node), or it may be a leaf node. Nodes usually have information attached, the
 * nature of which depends on the implementation.
 * 
 * @see AbstractNode
 */
public interface Node {


	/**
	 * Returns the name of this node. Typically, a node name is an identifier by
	 * which it can be found or addressed. A node name does not have to be unique.
	 * <p>
	 * This method should not return null. The default method returns the simple
	 * name of the class implementing this interface.
	 * 
	 * @return the node name, not null
	 * @see Class#getSimpleName
	 */
	default String getName() {
		return getClass().getSimpleName();
	}


	/**
	 * Returns the parent of this node or null if it has no parent.
	 * 
	 * @return the parent node, may be null
	 */
	<T extends Node> T getParent();


	/**
	 * Returns the ultimate ancestor of this node. This method returns the node
	 * itself if it has no parent (in which case it <i>is</i> the root node).
	 * 
	 * @return the root node, not null, may be this node
	 */
	@SuppressWarnings("unchecked")
	default <T extends Node> T root() {
		final Node parent = getParent();
		return ((parent != null) ? parent.root() : (T) this);
	}


	/**
	 * Returns an <i>unmodifiable</i> list of child nodes, which may be empty. This
	 * method never returns null.
	 * 
	 * @return a list, not null
	 */
	<T extends Node> List<T> nodes();


	/**
	 * Returns true if this node has no child nodes.
	 * 
	 * @return true or false
	 * @see #isParent
	 */
	default boolean isLeaf() {
		return nodes().isEmpty();
	}


	/**
	 * Returns true if this node has one or more child nodes.
	 * 
	 * @return true or false
	 * @see #isLeaf
	 */
	default boolean isParent() {
		return ! nodes().isEmpty();
	}


	/**
	 * Adds a child node to this node. This method takes after the add method of the
	 * {@code Collection} interface and implementations should strive to comply with
	 * the specified requirements.
	 * 
	 * @see Collection#add(Object)
	 * 
	 * @param node the node to be added
	 * @return true if this node changed as a result of the call
	 */
	boolean add(Node node);


	/**
	 * Removes a child node from this node. Implementations should specify how
	 * special cases like null or non-child nodes (or other anomalies) are handled.
	 * 
	 * @param node the node to be removed
	 * @return true if the node was removed, false otherwise
	 * @see #add
	 */
	boolean remove(Node node);


	/**
	 * Returns the first child node with the specified name, or null if no such node
	 * is found. This method uses the result of {@link #getName} to find a match.
	 * 
	 * @param name a node name
	 * @return a node, may be null
	 */
	default <T extends Node> T get(String name) {
		return get(n -> n.getName().equals(name));
	}


	/**
	 * Returns the first child node that satisfy the given predicate, or null if no
	 * such node is found.
	 * 
	 * @param predicate a boolean valued function of one argument
	 * @return a node, may be null
	 */
	@SuppressWarnings("unchecked")
	default <T extends Node> T get(Predicate<? super Node> predicate) {
		for (Node node : nodes())
			if (predicate.test(node))
				return (T) node;
		return null;
	}


	/**
	 * Returns a list of child nodes with the specified name, or an empty list if no
	 * such nodes are found. This method uses the result of {@link #getName} to find
	 * matching nodes.
	 * 
	 * @param name a node name
	 * @return a list, not null
	 */
	default <T extends Node> List<T> find(final String name) {
		return find(n -> n.getName().equals(name));
	}


	/**
	 * Returns a list of child nodes that satisfy the given predicate, or an empty
	 * list if no such nodes are found.
	 * 
	 * @param predicate a boolean valued function of one argument
	 * @return a list, not null
	 */
	@SuppressWarnings("unchecked")
	default <T extends Node> List<T> find(Predicate<? super Node> predicate) {
		List<T> list = new ArrayList<T>();
		for (Node node : nodes())
			if (predicate.test(node))
				list.add((T) node);
		return list;
	}

	
	/**
	 * Returns a list of descendant nodes that satisfy the given predicate, or an
	 * empty list if no such nodes are found. In the resulting list, matching child
	 * nodes are returned before matching sibling nodes (and their children).
	 * 
	 * @param predicate a boolean valued function of one argument
	 * @return a list, not null
	 */
	@SuppressWarnings("unchecked")
	default <T extends Node> List<T> findDescendant(Predicate<? super Node> predicate) {
		List<T> list = new ArrayList<T>();
		for (Node node : nodes()) {
			if (predicate.test(node))
				list.add((T) node);
			list.addAll(node.findDescendant(predicate));
		}
		return list;
	}

	/**
	 * Returns the location of this node in X-path style. If a node occurs more than
	 * once in the same context, its position is indicated in square brackets.
	 * <p>
	 * The path is constructed from node names returned by {@link #getName}, and may
	 * contain whatever characters are allowed in these names.
	 * 
	 * @return the path to this node, for example {@code /root/message[2]/text[1]}
	 */
	default String path() {
		
		final String name = getName();
		final Node parent = getParent();
		final List<Node> same = (parent != null) ? parent.find(name) : null;
		final int pos = (same != null && same.size() > 1) ? same.indexOf(this)+1 : 0;

		return (parent != null ? parent.path() : "")
			+ "/" + name + (pos > 0 ? "[" + pos + "]" : "");
	}

}