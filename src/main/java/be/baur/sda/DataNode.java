package be.baur.sda;

import java.util.List;

import be.baur.sda.serialization.SDAFormatter;

/**
 * A data node is the basic building block of an SDA document. It has a name
 * (tag) and a string value (simple content). A data node can be a parent node,
 * in which case it contains other data nodes (complex content).
 */
public class DataNode extends AbstractNode {

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
		super(); setName(name); this.value = "";
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
		super(); setName(name); setValue(value);
	}


	/**
	 * Sets the name (tag) of this node. There are restrictions as to what names are
	 * acceptable. Refer to {@link SDA#isName} for details.
	 * 
	 * @param name a valid node name
	 * @throws IllegalArgumentException if the name is invalid
	 */
	public final void setName(String name) {
		if (! SDA.isName(name)) 
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
	 * Returns true if this node has no child list. This method returns false for a
	 * parent node <i>and</i> for a "vacant parent" with an empty child list (as in
	 * <code>node{ }</code> for example).
	 */
	@Override
	public boolean isLeaf() {
		return (getNodeList() == null);
	}


	/**
	 * Returns a string representing this node in SDA notation. For example:
	 * 
	 * <pre>
	 * node ""
	 * node { ... }
	 * node "a value"
	 * node "a value" { ... }
	 * </pre>
	 * 
	 * Where {@code ...} are (optional) child nodes. Note that the result is
	 * formatted as a single line of text. For a more readable result, use an
	 * {@link SDAFormatter}.
	 * <p>
	 * 
	 * @return the SDA representation of this node
	 */
	@Override
	public String toString() {

		final List<AbstractNode> nodes = getNodeList();
		final StringBuilder sb = new StringBuilder(name);
		
		if (! value.isEmpty() || nodes == null) 
			sb.append(" ").append((char) SDA.QUOTE)
				.append(SDA.encode(value)).append((char) SDA.QUOTE);

		if (nodes != null) {
			sb.append(" ").append((char)SDA.LBRACE).append(" ");
			for (Node node : nodes) 
				sb.append(node.toString()).append(" ");
			sb.append((char)SDA.RBRACE);
		}

		return sb.toString();
	}

}