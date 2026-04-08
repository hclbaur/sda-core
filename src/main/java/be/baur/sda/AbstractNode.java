package be.baur.sda;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
	final List<T> nodeList() {
		return nodes;
	}

	
	@Override
	public final T getParent() {
		return parent;
	}


	@Override
	public List<T> nodes() {
		if (nodes == null) return EMPTY_LIST;
		return Collections.unmodifiableList(nodes);
	}


	@Override
	public boolean isParent() {
		// more efficient than the default method
		return !(nodes == null || nodes.isEmpty());
	}


	/**
	 * Adds a child node to this node. Adding null to a leaf node will create an
	 * empty child list for that node, but has no effect otherwise. It returns false
	 * only if the supplied node is already a child of this node; the invariant
	 * being that after successful completion this node will contain the supplied
	 * node.
	 * 
	 * @apiNote this method is thread safe.
	 * 
	 * @param node the node to be added to this node
	 * @return true if the supplied node was added as a child
	 * @throws IllegalArgumentException if the supplied node already has a different
	 *                                  parent
	 */
	public boolean add(T node) {

		boolean changed = false; // whether this node was changed

		if (nodes == null) { // initialize a node list if we have none yet
			synchronized (this) { // prevent re-assignment by another thread
				if (nodes == null) {
					nodes = new ArrayList<T>();
					changed = true;
				}
			}
		}

		if (node != null) {

			if (node.getParent() != null) {
				if (node.getParent() != this)
					throw new IllegalArgumentException("node '" + node.getName() + "' already has a parent");
				return changed;
			}

			if (nodes.add(node)) {
				/* Safe cast assuming proper F-bounded polymorphism usage (T bound to
				 * implementing type). See Javadoc of Node.getParent() and Node.root()
				 */ @SuppressWarnings("unchecked")
				T self = (T) this;
				node.setParent(self);
				return true;
			}
		}

		return changed;
	}

// possible improvement
	/**
	 * Adds a child node to this node. It returns true if the supplied node was
	 * added as a child, false if it was already a child of this node. The invariant
	 * is that after successful completion this node will contain the supplied node.
	 * 
	 * @apiNote this method is thread safe.
	 * 
	 * @param node the node to be added to this node
	 * @return true if the supplied node was added as a child
	 * @throws NullPointerException     if the supplied node is null
	 * @throws IllegalArgumentException if the supplied node already has a different
	 *                                  parent
	 */
//@SuppressWarnings("unchecked")
//public boolean add(T node) {
//
//	if (node == null)
//		throw new NullPointerException("node cannot be null");
//
//	if (node.getParent() != null) {
//		if (node.getParent() != this)
//			throw new IllegalArgumentException("node '" + node.getName() + "' already has a parent");
//		return false;
//	}
//
//	ensureVacant();
//
//	if (nodes.add(node)) {
//		node.setParent((T) this);
//		return true;
//	}
//
//	return false;
//}


/**
 * Ensures this node has an initialized (possibly empty) child list, making it a
 * vacant parent node. This is useful for rendering nodes with an empty
 * structure notation (e.g., {@code mynode "myvalue" { }}).
 * <p>
 * Calling this method multiple times is safe; if the child list is already
 * initialized, this method has no effect.
 * 
 * @apiNote this method is thread safe.
 * 
 * @return true if the child list was initialized by this call, false if it was
 *         already initialized
 */
//public boolean ensureVacant() {
//
//	if (nodes == null) {
//		synchronized (this) {
//			if (nodes == null) {
//				nodes = new ArrayList<T>();
//				return true;
//			}
//		}
//	}
//	return false;
//}
	

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