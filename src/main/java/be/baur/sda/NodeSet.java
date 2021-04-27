package be.baur.sda;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A <code>NodeSet</code> is an collection of {@link Node} objects. It extends a
 * {@link CopyOnWriteArraySet} and defines several convenience methods to find
 * or manipulate member nodes. Its use includes housing the child nodes of a
 * ComplexNode, but is not limited to that.
 */
@SuppressWarnings("serial")
public final class NodeSet extends CopyOnWriteArraySet<Node> {

	/*
	 * The parent is the node for which this set is holding any child nodes. This
	 * reference is kept to maintain parent-child integrity when adding or removing
	 * child nodes from the set.
	 */
	private final ComplexNode parent;

	
	/** Creates an empty node set. */
	public NodeSet() {
		super(); this.parent = null;
	}

	
	/*
	 * Creates an empty node set with a parent reference. It is called upon creation
	 * of a ComplexNode to establish the unbreakable double bond between this set
	 * and the parent for which it holds any child nodes. Do not make this public!
	 */
	NodeSet(ComplexNode parent) {
		super(); this.parent = parent;
	}


	/**
	 * @deprecated Since 1.5.1 for removal in 1.6. Use {@link NodeSet#of} instead.
	 */
	@Deprecated
	public static NodeSet from(Node node) {
		return NodeSet.of(node);
	}


	// NOTE: some inherited methods are overwritten to maintain parent-child integrity. 


	/**
	 * Adds a <code>node</code> to this set. In case this is a parent set (e.g. it is
	 * holding child nodes for a particular parent) the node is added only if it has 
	 * no parent yet. Also, this method will never add a <code>null</code> reference.
	 * @return true if the set was modified.
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
	 * Adds all nodes in the specified <code>collection</code> to this set if they
	 * are not already present. Will ignore a <code>null</code> argument.
	 * @return true if the set was modified.
	 */
	@Override
	public boolean addAll(Collection<? extends Node> collection) {
		boolean result = false;
		if (collection != null) 
			for (Node n : collection) result &= this.add(n);
		return result;
	}


	/**
	 * Removes all nodes from this set. If this is a parent set, all nodes will be
	 * detached from the parent. The set will be empty after this call returns.
	 */
	@Override
	public void clear() {
		for (Node node : this) this.remove(node);
	}

	
	/**
	 * Returns the position of a <code>node</code> in the range [1 .. set.size()],
	 * or 0 if no such node exists in this set.
	 */
	public int find(Node node) {
		return Arrays.asList(this.toArray()).indexOf(node) + 1;
	}
	
	
	/**
	 * Returns the node at the specified <code>position</code> in the range [1 ..
	 * set.size()], or <code>null</code> if no such node exists in this set.
	 */
	public Node get(int position) {
		if (this.size() < position || position < 1) return null;
		return (Node)this.toArray()[position-1];
	}
	
	
	/**
	 * Returns a sub-set of all nodes with the specified <code>name</code> in this
	 * set, or an empty set if none are found.
	 */
	public NodeSet get(String name) {
		NodeSet sub = new NodeSet();
		for (Node node : this)
			if (node.getName().equals(name)) sub.add(node);
		return sub;
	}

	
	/**
	 * Returns a sub-set of all nodes of the specified class in the set, or an empty
	 * set if none are found.
	 */
	public NodeSet get(Class<?> cls) {
		NodeSet sub = new NodeSet();
		for (Node node : this)
			if (node.getClass() == cls) sub.add(node);
		return sub;
	}

	
	/**
	 * Returns a new set containing the specified <code>node</code> or an empty set
	 * in case of a <code>null</code> argument.
	 */
	public static NodeSet of(Node node) {
		NodeSet set = new NodeSet(); set.add(node); return set;
	}
	
	
	/**
	 * Removes the specified <code>object</code> from this set if it is present. If
	 * this is a parent set and the object is a child node of that parent, this will
	 * detach the node from its parent. Will ignore a <code>null</code> argument.
	 * @return true if the set was modified.
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
	 * Removes all of the elements in the specified <code>collection</code> from
	 * this set if they are present. Will ignore a <code>null</code> argument.
	 * @return true if the set was modified.
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
