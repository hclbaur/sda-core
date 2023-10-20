package be.baur.sda;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An abstract node provides the bare-bones implementation of a node object with
 * optional child nodes. It can be extended to implement a concrete class, such
 * as a {@link DataNode}.
 */
public abstract class AbstractNode implements Node {

	private AbstractNode parent; // reference to a parent, null if this is not a child node
	private List<AbstractNode> nodes; // reference to child nodes, null if this is a leaf node
	private final List<AbstractNode> EMPTY_LIST = Collections.emptyList(); // an empty node list

	
	/*
	 * Sets the parent. This is called internally to maintain parent-child
	 * integrity when adding or removing child nodes. Do not make public !
	 */
	private final void setParent(AbstractNode parent) {
		this.parent = parent;
	}
	
	
	/**
	 * This <i>protected</i> method allows direct access to the internal list of child
	 * nodes, and may return a null reference.
	 */
	protected final List<AbstractNode> getNodeList() {
		return nodes;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public final <T extends Node> T getParent() {
		return (T) parent;
	}


	@SuppressWarnings("unchecked")
	@Override
	public <T extends Node> List<T> nodes() {
		if (nodes == null) return (List<T>) EMPTY_LIST;
		return (List<T>) Collections.unmodifiableList(nodes);
	}
//	public List<AbstractNode> nodes() {
//		if (nodes == null) return EMPTY_LIST;
//		return Collections.unmodifiableList(nodes);
//	}

	
	@Override
	// possibly somewhat more efficient than the default method
	public boolean isLeaf() {
		return (nodes == null || nodes.isEmpty());
	}
	
	
	@Override
	// possibly somewhat more efficient than the default method
	public boolean isParent() {
		return !(nodes == null || nodes.isEmpty());
	}


	/**
	 * Adds a child node to this node. Adding a node that already has a parent will
	 * cause a run-time exception. Adding null to a leaf node will create an empty
	 * child list for that node, but has no effect otherwise.
	 * 
	 * @throws IllegalStateException if the provided node already has a parent
	 */
	@Override
//	public final <T extends Node> boolean add(T node) {
//		if (node != null && node.getParent() != null)
//			throw new IllegalStateException("node '" + node.getName() + "' already has a parent");
//		if (nodes == null) {
//			synchronized (this) { // prevent re-assignment by another thread
//				if (nodes == null) nodes = new ArrayList<AbstractNode>();
//			}
//		}
//		if (node != null && (node instanceof AbstractNode) && nodes.add((AbstractNode) node)) {
//			((AbstractNode) node).setParent(this);
//			return true;
//		}
//		return false;
//	}
	public final <T extends Node> boolean add(T node) {
		if (nodes == null) {
			synchronized (this) { // prevent re-assignment by another thread
				if (nodes == null) nodes = new ArrayList<AbstractNode>();
			}
		}
		if (node != null) {
			if (node.getParent() != null)
				throw new IllegalStateException("node '" + node.getName() + "' already has a parent");
			if (node instanceof AbstractNode && nodes.add((AbstractNode) node)) {
				((AbstractNode) node).setParent(this);
				return true;
			}
		}
		return false;
	}


	/**
	 * Removes a child node from this node. This method will ignore null, nodes not
	 * extending this abstract class, and nodes that are not children of this node.
	 */
	@Override
	public final boolean remove(Node node) {
		// if nodes ever can change back to null, we will need synchronization
		if (node != null && nodes != null && node instanceof AbstractNode && nodes.remove(node)) {
			((AbstractNode) node).setParent(null);
			return true;
		}
		return false;
	}

}