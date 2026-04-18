package be.baur.sda;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * An {@code AbstractNode} provides the bare-bones implementation of a node
 * object with optional child nodes. Its primary concern is to manage the tree
 * structure and keep it in a consistent state (maintaining parent-child
 * relationships and synchronization). Concrete subclasses are responsible for
 * adding semantic content (such as names, values, or other data).
 */
public abstract class AbstractNode<T extends AbstractNode<T>> implements Node<T> {

	private T parent; // reference to a parent, null if this is not a child node
	private List<T> nodes; // reference to child nodes, initially null (no children)
	private final List<T> EMPTY_LIST = Collections.emptyList(); // an empty node list

	
	/**
	 * This <i>protected</i> method sets the parent. It is called internally to
	 * maintain parent-child integrity when adding or removing child nodes.
	 */
	protected final void setParent(T node) {
		this.parent = node;
	}
	
	
	/**
	 * This <i>package-private</i> method allows direct access to the internal list
	 * of child nodes, and may return a null reference.
	 * <p>
	 * <strong>Warning:</strong> direct modification of this list bypasses
	 * synchronization and parent-child integrity checks. Use only in a controlled,
	 * package-internal context.
	 * 
	 * @return a <i>modifiable</i> list of nodes, may be null
	 */
	final List<T> getNodeList() {
		return nodes;
	}


	/**
	 * This <i>package-private</i> method is called for lazy initialization of the
	 * internal list that holds any child nodes.
	 * 
	 * @apiNote this method is thread safe. Calling it more than once has no effect
	 *          (it returns false if the nodes list had already been initialized).
	 * 
	 * @return true if the child list was initialized by this call.
	 */
	final boolean initNodeList() {
	
		if (nodes == null) {
			synchronized (this) {
				if (nodes == null) {
					nodes = new ArrayList<T>();
					return true;
				}
			}
		}
		return false;
	}


	@Override
	public List<T> nodes() {
		if (nodes == null) return EMPTY_LIST;
		return Collections.unmodifiableList(nodes);
	}

	
	@Override
	public final T getParent() {
		return parent;
	}


	@Override
	public boolean isParent() {
		// more efficient than the default method
		return !(nodes == null || nodes.isEmpty());
	}


	/**
	 * Adds a child node. This method returns false if the supplied node already was
	 * a child of this node; the invariant being that after successful completion
	 * this node contains the supplied node.
	 * 
	 * @apiNote this method is thread safe.
	 * 
	 * @param node the node to be added, not null
	 * @return true if the supplied node was added as a child
	 * @throws UnsupportedOperationException if add() is not supported by this node
	 * @throws IllegalArgumentException      if the supplied node already is a child
	 *                                       of <i>another</i> node
	 */
	public boolean add(T node) {

		Objects.requireNonNull(node, "child node must not be null");

		if (node.getParent() != null) {
			if (node.getParent() != this)
				throw new IllegalArgumentException("node '" + node.getName() + "' already has a parent");
			return false;
		}

		if (nodes == null) // minor optimization
			initNodeList();

		if (nodes.add(node)) {
			/*
			 * Safe cast assuming proper F-bounded polymorphism usage (T bound to
			 * implementing type). See Javadoc of Node.getParent() and Node.root()
			 */ @SuppressWarnings("unchecked")
			T self = (T) this;
			node.setParent(self);
			return true;
		}

		return false; // should never reach this
	}
	

	/**
	 * Removes a child node from this node. This method ignores null references and
	 * nodes that are not children of this node. It returns true only if this node
	 * was changed as a result of calling the method; the invariant being that after
	 * successful completion this node will not contain the (alleged) child node.
	 * 
	 * @apiNote this method is thread safe.
	 * 
	 * @param node the node to be removed from this node
	 * @return true if this node contained the child node
	 * @throws UnsupportedOperationException if remove() is not supported
	 */
	public boolean remove(T node) {

		if (nodes != null && node != null) {
			// nodes can never be set back to null so no extra check required
			synchronized (this) {
				if (nodes.remove(node)) {
					node.setParent(null);
					return true;
				}
			}
		}
		return false;
	}

}