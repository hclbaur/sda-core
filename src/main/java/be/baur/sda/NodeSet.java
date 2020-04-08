package be.baur.sda;

import java.util.concurrent.CopyOnWriteArraySet;

/** A <code>NodeSet</code> is an collection of {@link Node} objects. 
 * It extends a CopyOnWriteArraySet and defines a convenience methods
 * to get a reference to specific member nodes by index or name.
 */
@SuppressWarnings("serial")
public final class NodeSet extends CopyOnWriteArraySet<Node> {


	/** Get a single node from a set, by an index in the range 1 .. size(). */
	public Node get(int index) {

		if (this.size() < index || index < 1) return null;
		return (Node)this.toArray()[index-1];
	}


	/** Get one or more nodes with a particular name from a set.*/
	public NodeSet get(String name) {

		NodeSet sub = new NodeSet();
		for (Node node : this)
			if (node.name.equals(name)) sub.add(node);
		return sub;
	}


	/** Renders the set as a list of SDA elements. */
	public String toString() {

		String s = ""; 
		for (Node node : this) s = s + node.toString() + " ";
		return s;
	}
	
	/** Overwrite inherited method to prevent adding null */
	public boolean add(Node node) {
		
		if (node == null) return false;
		return super.add(node);
	}

}
