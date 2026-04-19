package be.baur.sda;

import java.util.List;

import be.baur.sda.io.SDAFormatter;

/**
 * A {@code DataNode} is the basic building block of an SDA document. It has a
 * name (tag) and a string value (simple content). A data node can be a parent
 * node, in which case it contains other data nodes (complex content).
 * 
 * @see AbstractNode
 */
public class DataNode extends AbstractNode<DataNode> {

	private String name;  // the name tag, never null or empty
	private String value; // a value, never null, may be empty


	/**
	 * Creates a node with the specified name and an empty value.
	 * 
	 * @param name a node name
	 * @throws IllegalArgumentException if the name is invalid
	 * @see #setName
	 */
	public DataNode(String name) {
		setName(name); this.value = "";
	}

	
	/**
	 * Creates a node with the specified name and value. This method will treat a
	 * null value as if an empty string was supplied.
	 * 
	 * @param name a node name
	 * @param value a string value, may be null
	 * @throws IllegalArgumentException if the name is invalid
	 * @see #setName
	 * @see #setValue
	 */
	public DataNode(String name, String value) {
		setName(name); setValue(value);
	}


	/**
	 * Sets the name (tag) of this node. There are restrictions as to what names are
	 * acceptable. Refer to {@link SDA#isName} for details.
	 * 
	 * @param name a valid node name
	 * @throws IllegalArgumentException if the name is invalid
	 */
	public final void setName(String name) {
		if (! SDA.isNodeName(name)) 
			throw new IllegalArgumentException("invalid node name (" + name + ")");
		this.name = name;
	}


	/**
	 * Returns the name (tag) of this node.
	 * 
	 * @return the node name, not null or empty
	 */
	@Override
	public final String getName() {
		return name;
	}
	
	
	/**
	 * Sets the value of this node. A null value is turned into an empty string to
	 * prevent accidental null pointer exceptions at a later time. Since SDA does
	 * not support explicit nil, there is no valid reason to supply null other than
	 * to set an empty value.
	 * 
	 * @param value a string value, may be null or empty
	 */
	public final void setValue(String value) {
		this.value = (value == null || value.isEmpty()) ? "" : value;
	}
	
	
	/**
	 * Returns the value of this node. This method returns an empty string if no
	 * value has been set.
	 *
	 * @return the string value, not null, may be empty
	 */
	public final String getValue() {
		return value;
	}


	/**
	 * Turns a leaf node into a vacant (empty) parent. For instance, calling this
	 * method on a node like
	 * <p>
	 * {@code value "42"}
	 * <p>
	 * will turn it into
	 * <p>
	 * {@code value "42" { }}
	 * 
	 * @return true if this node was expanded, or false if it already was
	 */
	public final boolean expand() {
		return initNodeList();
	}
	
	
	/**
	 * Returns true if this node has no child list (not even an empty one). This
	 * method returns false for a parent node <i>and</i> for a vacant parent with
	 * an empty node list, like <code>emptyNode{}</code>.
	 * <p>
	 * <strong>Warning</strong>: this method is not the logical opposite of the
	 * {@code isParent()} method, which returns false for a vacant parent as well.
	 * 
	 * @return true or false
	 * @see AbstractNode#isParent
	 */
	public final boolean isLeaf() {
		return (getNodeList() == null);
	}


	/**
	 * Returns a deep copy of this node.
	 * 
	 * @return a new node
	 */
	public final DataNode copy() {
		DataNode cp = new DataNode(this.getName(), this.getValue());
		if (! this.isLeaf()) {
			cp.expand();
			for (DataNode child : this.nodes()) 
				cp.add(child.copy());
		}
		return cp;
	}
	
	
	/**
	 * Returns a string representing this node in SDA notation. For example:
	 * 
	 * <pre>
	 * greeting { message "hello" }
	 * </pre>
	 * 
	 * @apiNote the result is formatted as a single line of text. For a more
	 *          readable presentation, use an {@link SDAFormatter}.
	 * 
	 * @return the SDA representation of this node
	 */
	@Override
	public final String toString() {

		final var nodes = (List<DataNode>) getNodeList();
		final StringBuilder sb = new StringBuilder(name);
		
		if (! value.isEmpty() || nodes == null) 
			sb.append(" ").append((char) SDA.QUOTE)
				.append(SDA.encode(value)).append((char) SDA.QUOTE);

		if (nodes != null) {
			sb.append(" ").append((char)SDA.LBRACE).append(" ");
			for (DataNode node : nodes) 
				sb.append(node.toString()).append(" ");
			sb.append((char)SDA.RBRACE);
		}

		return sb.toString();
	}

}