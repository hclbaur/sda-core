package be.baur.sda;

/**
 * A <code>SimpleNode</code> represents a simple SDA element; a node with a
 * value of a simple content type, represented as a string. Typically, these
 * nodes are children of a parent {@link ComplexNode}.
 */
@Deprecated
public class SimpleNode extends Node {

//    private String value; // the value of this node 
    
	/**
	 * Creates a simple node with the specified <code>name</code> and
	 * <code>value</code>.
	 * @throws IllegalArgumentException if <code>name</code> is invalid.
	 */
	public SimpleNode(String name, String value) {
		super(name, value);
	}
	
	/* Note that most of the methods in this class are final! */
	
//	/**
//	 * Sets the <code>value</code> of this node. A value of <code>null</code> is
//	 * turned into an empty string to prevent null pointer exceptions downstream.
//	 * Since SDA does not support explicit nil, there is no valid reason to supply
//	 * null other than to create an empty value.
//	 */
//	public final void setValue(String value) {
//		this.value = (value != null) ? value : "";
//	}


//	/** Returns the <code>value</code> of this node. */
//	public final String getValue() {
//		return value;
//	}


//	public String toString() {
//		return getName() + " " + (char)SDA.QUOTE + SDA.encode(getValue()) + (char)SDA.QUOTE;
//	}
}