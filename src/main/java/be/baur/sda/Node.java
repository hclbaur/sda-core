package be.baur.sda;

/** A <code>Node</code> represents an SDA element. It has a name and 
 * a reference to an (optional) parent. This a an abstract base class.
 */
public abstract class Node {
	
	/** The name of this node has package visibility for convenience. */
	final String name;
	/** A reference to the parent node. */
	private Node parent;
	
	
	/** Creates a node and sets the name. */
	public Node(String name) {
		this.name = name;
	}
	
	
	/** Returns the node name. */
	public String getName() { return name; }
	
	
    /** Sets the parent reference. This is potentially dangerous, as this
     * method does not actually update the parent. It should be called by 
	 * a <code>ComplexNode</code> when adding or removing a child node.
     */
	protected void setParent(Node parent) {
        this.parent = parent;
	}
        
	
	/** Returns a reference to the parent. */
	public Node getParent() { return parent; }
	

	abstract public String render();
	abstract public String toString();
}