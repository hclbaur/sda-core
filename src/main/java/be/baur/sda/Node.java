package be.baur.sda;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * A {@code Node} is an object in a tree-like structure that stems from a single
 * root node. A node may have child nodes (in which case it is called a parent
 * node). Nodes usually have information attached, the nature of which depends
 * on the implementation.
 */
public interface Node<T extends Node<T>> {

	/**
	 * Returns an <i>unmodifiable</i> list of child nodes, which may be empty. This
	 * method never returns null.
	 * 
	 * @return a list of nodes, not null
	 */
	List<T> nodes();

	/**
	 * Returns the parent of this node or null if it has no parent.
	 * 
	 * @implSpec assumes proper F-bounded polymorphism usage; subclasses should bind
	 *           T to their own type, or this method may not return an object of
	 *           type T (in which case the caller should verify type safety).
	 * 
	 * @return the parent node, may be null
	 */
	T getParent();

	// Default methods below this line

	/**
	 * Returns the name by which this node can be found or addressed. A node name
	 * does not have to be unique and can be empty, but should not be null.
	 * 
	 * @implNote the default method returns the simple name of the class
	 *           implementing this interface, e.g. the result of
	 *           {@link Class#getSimpleName}.
	 * 
	 * @return the node name, not null
	 */
	default String getName() {
		return getClass().getSimpleName();
	}


	/**
	 * Returns the ultimate ancestor of this node. This method returns the node
	 * itself if it has no parent (in which case it <i>is</i> the root node).
	 * 
	 * @implNote relies on proper F-bounded polymorphism usage; subclasses should
	 *           bind T to their own type, or this method may not return an object
	 *           of type T (in which case the caller should verify type safety).
	 * 
	 * @return the root node, not null, may be this
	 */
	default T root() {
	    Node<?> current = this;
		for (Node<?> p; (p = current.getParent()) != null; ) {
	        current = p;
	    }
	    @SuppressWarnings("unchecked")
	    T root = (T) current;
	    return root;
	}


	/**
	 * Returns true if this node has one or more child nodes.
	 * 
	 * @return true or false
	 */
	default boolean isParent() {
		return ! nodes().isEmpty();
	}


	/**
	 * Returns the first child node with the specified name, or null if no such node
	 * is found.
	 * 
	 * @implNote this method uses the result of {@link #getName} to find a match.
	 * 
	 * @param name a node name
	 * @return a node, may be null
	 */
	default T get(String name) {
		return get(n -> n.getName().equals(name));
	}


	/**
	 * Returns the first child node that satisfy the given predicate, or null if no
	 * such node is found.
	 * 
	 * @param predicate a boolean valued function of one argument
	 * @return a node, may be null
	 */
	default T get(Predicate<? super T> predicate) {
		for (T node : nodes())
			if (predicate.test(node))
				return node;
		return null;
	}


	/**
	 * Returns a list of child nodes with the specified name, or an empty list if no
	 * such nodes are found.
	 * 
	 * @implNote this method uses the result of {@link #getName} to find a match.
	 * 
	 * @param name a node name
	 * @return a list, not null
	 */
	default List<T> getAll(String name) {
		return getAll(n -> n.getName().equals(name));
	}


	/**
	 * Returns a list of child nodes that satisfy the given predicate, or an empty
	 * list if no such nodes are found.
	 * 
	 * @param predicate a boolean valued function of one argument
	 * @return a list, not null
	 */
	default List<T> getAll(Predicate<? super T> predicate) {
		var list = new ArrayList<T>();
		for (T node : nodes())
			if (predicate.test(node))
				list.add(node);
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
	default List<T> find(Predicate<? super T> predicate) {
		var list = new ArrayList<T>();
		for (T node : nodes()) {
			if (predicate.test(node))
				list.add(node);
			list.addAll(node.find(predicate));
		}
		return list;
	}


	/**
	 * Returns the location of this node in X-path style. If a node occurs more than
	 * once in the same context, its position is indicated in square brackets.
	 * 
	 * @implNote the resulting path is constructed from node names returned by
	 *           {@link #getName}, and may contain any character allowed in these
	 *           names. If name is empty, an asterisk (*) is used instead.
	 * 
	 * @return the path to this node, for example {@code /root/message[2]/text[1]}
	 */
	default String path() {
		
		T parent = getParent(); 
		
		String name = getName();
		if (name.isEmpty()) name = "*"; // not necessarily a DataNode
		
		if (parent == null) // start of the path, so return
			return "/" + name;
				
		var sameNodes = parent.getAll(name);
		// position in list of nodes with the same name
		final int pos = sameNodes.size() > 1 ? position(sameNodes) : 0;

		return parent.path() + "/" + name + (pos > 0 ? "[" + pos + "]" : "");
	}


	/*
	 * Private helper that returns the position of this node in the provided list,
	 * in the range {@code 1 .. list.size()} or 0 if the list does not contain it.
	 * The reason we use this helper rather then list.indexOf() is because we want
	 * an identity compare rather than equals(), which might be overridden.
	 * Precondition: the list must not be null.
	 */
	private int position(List<T> list) {
		for (int i = 0, n = list.size(); i < n; ++i)
			if (list.get(i) == this)
				return ++i;
		return 0;
	}

}