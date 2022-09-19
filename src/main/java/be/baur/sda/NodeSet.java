package be.baur.sda;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A {@code NodeSet} is an collection of {@code Node} objects. It extends a
 * {@link CopyOnWriteArraySet} and defines several convenience methods to find
 * or manipulate member nodes. Internally, a NodeSet is used to hold the child
 * nodes of a parent node, in which case it is referred to as a "parent set".
 * All supplied methods are "safe", e.g. parent-child integrity is maintained.
 * 
 * @see {@link Node}
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
	 * the parent which child nodes it holds. Do not make this public!
	 */
	NodeSet(Node parent) {
		super(); this.parent = parent;
	}


	// NOTE: some inherited methods are overwritten to maintain parent-child integrity. 


	/**
	 * Adds a node to this set. A null reference is silently ignored. If the set is
	 * a parent set, nodes that already have a parent will not be added.
	 * 
	 * @param node the node to be added, may be null
	 * @return true if this set was modified
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
	 * argument, and nodes that already present in the set. If the set is a parent
	 * set, nodes that already have a parent will not be added.
	 * 
	 * @param node the node collection to be added, may be null
	 * @return true if this set was modified
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
	 * [1 .. {@link #size()}], or 0 if the specified node does not exist in this set.
	 * 
	 * @param node the node to find, may be null
	 * @return the position in this set
	 */
	public int find(Node node) {
		return Arrays.asList(this.toArray()).indexOf(node) + 1;
	}
	
	
	/**
	 * Returns the node at the specified position. The position is an integer index
	 * in the range [1 .. set.size()]. This method returns a null reference if there
	 * is no node at the specified position in this set.
	 * 
	 * @param position the position in the set
	 * @return a node, may be null
	 */
	public Node get(int position) {
		if (this.size() < position || position < 1) return null;
		return (Node)this.toArray()[position-1];
	}
	
	
	/**
	 * Returns a sub-set of all nodes with the specified name. This method returns
	 * an empty set if no such nodes are found.
	 * 
	 * @param name a node name
	 * @return a node set, may be empty
	 */
	public NodeSet get(String name) {
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
	public NodeSet get(Predicate<? super Node> predicate) {
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
	 * Removes the specified object from this set. This method ignores a null
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
	 * Removes all nodes in the specified collection from this set. This method
	 * ignores a null argument, objects that are not nodes, and nodes not contained
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
	 * @return true if the set was modified.
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
	 * @return true if the set was modified.
	 */
	@Override
	public boolean retainAll(Collection<?> collection) {
		return this.removeAll(
			this.stream().filter(n -> !collection.contains(n)).collect(Collectors.toCollection(NodeSet::new))
		);
	}
	
	
	/** Renders this set as a list of SDA elements. */
	@Override
	public String toString() {
		String result = ""; 
		for (Node node : this) 
			result = result + node.toString() + " ";
		return result;
	}
}
