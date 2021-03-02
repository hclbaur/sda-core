package be.baur.sda;

import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * A <code>NodeSet</code> is an collection of {@link Node} objects. It extends a
 * {@link CopyOnWriteArraySet} and defines a convenience methods to get a
 * reference to specific member nodes by index or name.
 */
@SuppressWarnings("serial")
public final class NodeSet extends CopyOnWriteArraySet<Node> {
/*
 * This class should overwrite some of the standard set methods to make them safe! 
 */

	/**
	 * Returns a node by its position in the range [1 .. set.size()],<br>
	 * or <code>null</code> if no such node exists in the set.
	 */
	public Node get(int position) {

		if (this.size() < position || position < 1) return null;
		return (Node)this.toArray()[position-1];
	}
	
	/**
	 * Returns the position of node in the range [1 .. set.size()],<br>
	 * or 0 if no such node exists in the set.
	 */
	public int find(Node node) {
		return Arrays.asList(this.toArray()).indexOf(node) + 1;
	}


	/**
	 * Returns all nodes with a particular name from the set, or an empty set if none is found.
	 */
	public NodeSet get(String name) {

		NodeSet sub = new NodeSet();
		for (Node node : this)
			if (node.name.equals(name)) sub.add(node);
		return sub;
	}


	/**
	 * Returns all nodes of a particular class from the set, or an empty set if none is found.
	 */
	public NodeSet get(Class<?> cls) {

		NodeSet sub = new NodeSet();
		for (Node node : this)
			if (node.getClass() == cls) sub.add(node);
		return sub;
	}


	/** Renders the set as a list of SDA elements. */
	@Override
	public String toString() {

		String s = ""; 
		for (Node node : this) s = s + node.toString() + " ";
		return s;
	}
	
	/** Adds a node to the set. */
	@Override
	public boolean add(Node node) {
		/*
		 * Design choice: override the super method to prevent adding null references,
		 * which may cause null pointer exceptions downstream. There would be no point
		 * of adding "nothing" to a node set anyway.
		 */
		if (node == null) return false;
		return super.add(node);
	}

}
