package be.baur.sda;

/** A <code>SimpleNode</code> represents a simple SDA element; 
 * a node with a value of a simple type. <br>Typically, these 
 * nodes are children of a parent {@link ComplexNode}.
 */
public class SimpleNode extends Node {

	/**
	 * The (immutable) value has public visibility, and there is no getter for it.
	 * Questionable design choice, I will probably fix this before I go public :)
	 */
    public final String value;
	
    
	/** Creates a simple node with a name and value. */
	public SimpleNode(String name, String value) {
		/*
		 * Design choice: turn null values into an empty string to prevent null pointer
		 * exceptions downstream. Since SDA does not support explicit nil, there is no
		 * valid reason to supply null other than to create an empty node (and which is
		 * exactly what a value of "" means).
		 */
		super(name); this.value = (value != null) ? value : "";
	}

	
	/** Renders the node as an SDA element. */
	public String toString() {
		return name + " " + (char)SDA.QUOTE + SDA.escape(value) + (char)SDA.QUOTE;
	}
}