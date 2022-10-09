package be.baur.sda;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A {@code NodeSet} is an collection of {@code Node} objects. It has several
 * convenience methods to find and manipulate member nodes. A node set is
 * (amongst others) used to hold the child nodes of a parent node, in which case
 * it is referred to as a "parent set". All supplied methods are "safe" in the
 * sense that they maintain parent-child integrity. <br>
 * See also {@link Node}.
 */
@SuppressWarnings("serial")
public final class NodeSet extends CopyOnWriteArraySet<Node> {

	/*
	 * The parent is the node for that the set is holding child nodes for. This
	 * reference is kept to maintain parent-child integrity when adding or removing
	 * nodes from the set. If the parent is null, the set is not a parent set.
	 */
	private final Node parent;

	
	/** Creates an empty node set. */
	public NodeSet() {
		super(); this.parent = null;
	}

	
	/*
	 * Creates an empty node set with a parent reference. It is called upon creation
	 * of a parent set to establish the unbreakable double bond between this set and
	 * the parent which child nodes it holds. Do not make this method public!
	 */
	NodeSet(Node parent) {
		super(); this.parent = parent;
	}


	// NOTE: some inherited methods are overwritten to maintain parent-child integrity. 


	/**
	 * Adds a node to this set. A null reference is silently ignored and if the set
	 * is a parent set, a node that already has a parent will be ignored. It is possible
	 * but not recommended to use this method to add a child to a parent node, like
	 * 
	 * <pre>
	 * {@code node.getNodes().add(childNode) }
	 * </pre>
	 * 
	 * as getNodes() may return a null pointer. Use {@link Node#add(childNode)} instead.
	 * 
	 * @param node a node to be added, may be null
	 * @return true if the set was modified
	 */
	@Override
	public boolean add(Node node) {
        if (node != null) {
        	if (parent == null) return super.add(node);
        	if (node.getParent() == null && super.add(node) ) {
        		node.setParent(parent); return true;
        	}
        }
		return false;
	}
	
	
	/**
	 * Adds all nodes in the specified collection to this set. Will ignore a null
	 * argument, and nodes that are already contained in the set. If the set is a parent
	 * set, nodes that already have a parent will be ignored.
	 * 
	 * @param collection a node set to be added, may be null
	 * @return true if the set was modified
	 */
	@Override
	public boolean addAll(Collection<? extends Node> collection) {
		boolean result = false;
		if (collection != null) 
			for (Node n : collection) result &= this.add(n);
		return result;
	}


	/**
	 * Removes all nodes from this set. If this is a parent set, removed nodes will
	 * be detached from their parent. The set will be empty after this call returns.
	 */
	@Override
	public void clear() {
		for (Node node : this) this.remove(node);
	}

	
	/**
	 * Returns the position of a node. The position is an integer index in the range
	 * [1 .. {@code size()}], or 0 if the specified node does not exist in this set.
	 * 
	 * @param node a node to find, may be null
	 * @return the position in the set, a non-negative integer
	 */
	public int locate(Node node) {
		return Arrays.asList(this.toArray()).indexOf(node) + 1;
	}
	
	
	/**
	 * Returns the node at the specified position. The position is an integer index
	 * in the range [1 .. set.size()]. This method returns a null reference if the
	 * position is out of range, or if there is no node at the specified position in
	 * this set.
	 * 
	 * @param position a position in the set, a non-negative integer
	 * @return a node, may be null
	 */
	public Node get(int position) {
		if (this.size() < position || position < 1) return null;
		return (Node)this.toArray()[position-1];
	}
	
	
	/**
	 * Returns the first node with the specified name. This method returns
	 * null if no such node is found in the set.
	 * 
	 * @param name a node name, may be null
	 * @return a node, may be null
	 */
	public Node get(String name) {
		for (Node node : this)
			if (node.getName().equals(name)) return node;
		return null;
	}


	/**
	 * Returns a sub-set of all nodes with the specified name. This method returns
	 * an empty set if no such nodes are found.
	 * 
	 * @param name a node name
	 * @return a node set, may be empty
	 */
	public NodeSet find(String name) {
		NodeSet sub = new NodeSet();
		for (Node node : this)
			if (node.getName().equals(name)) sub.add(node);
		return sub;
	}


	/**
	 * Returns a sub-set of all nodes that satisfy the given predicate. This method
	 * returns an empty set if no such nodes are found.
	 * 
	 * @param predicate a boolean valued function of one argument
	 * @return a node set, may be empty
	 */
	public NodeSet find(Predicate<? super Node> predicate) {
		return this.stream().filter(predicate).collect(Collectors.toCollection(NodeSet::new));
	}

	
	/**
	 * Returns a new set containing the specified node. This method returns an empty
	 * set if a null argument was supplied.
	 * 
	 * @param node a node, may be null
	 * @return a node set, may be empty
	 */
	public static NodeSet of(Node node) {
		NodeSet set = new NodeSet(); set.add(node); return set;
	}
	
	
	/**
	 * Removes the specified object from this set. This method will ignore a null
	 * argument, an object that is not a {@code Node} and a node not contained in
	 * this set. If this is a parent set and the node is a child of that parent,
	 * it will detach the node from the parent.
	 * 
	 * @param object a node
	 * @return true if this set was modified
	 */
	@Override
	public boolean remove(Object object) {
		
		boolean result = false;
        if (object != null && object instanceof Node) {
        	Node node = (Node) object;
        	result = super.remove(node);	
        	if (result && node.getParent() == parent)
        		node.setParent(null); 
        }
		return result;
	}
	
	
	/**
	 * Removes all nodes in the specified collection from this set. This method will
	 * ignore a null argument, objects that are not nodes, and nodes not contained
	 * in this set.
	 * 
	 * @param collection a node set, may be null
	 * @return true if this set was modified
	 */
	@Override
	public boolean removeAll(Collection<?> collection) {
		boolean result = false;
		if (collection != null) 
			for (Object o : collection) result &= this.remove(o);
		return result;
	}
	
	
	/**
	 * Removes all nodes that satisfy the given <code>predicate</code> from this set.
	 * 
	 * @return true if the set was modified
	 */
	@Override
	public boolean removeIf(Predicate<? super Node> predicate) {
		return this.removeAll(
			this.stream().filter(predicate).collect(Collectors.toCollection(NodeSet::new))
		);
	}
	
	
	/**
	 * Retains only the nodes in this set that are contained in the specified
	 * <code>collection</code>. In other words, removes from this set all nodes
	 * <em>not</em> contained in the specified collection. Will ignore a
	 * <code>null</code> argument.
	 * 
	 * @return true if the set was modified
	 */
	@Override
	public boolean retainAll(Collection<?> collection) {
		return this.removeAll(
			this.stream().filter(n -> !collection.contains(n)).collect(Collectors.toCollection(NodeSet::new))
		);
	}
	
	
	/**
	 * Returns the string representing this set in SDA notation. For example:
	 * 
	 * <pre>
	 * { node1 "value" node2 "value" { ... } node3 { ... } }
	 * </pre>
	 * 
	 * Note that the returned string is formatted as a single line of text.
	 * 
	 * @return an SDA representation of this set
	 */
	@Override
	public String toString() {
		String result = (char)SDA.LBRACE + " "; 
		for (Node node : this) 
			result = result + node.toString() + " ";
		return result + (char)SDA.RBRACE;
	}
}
