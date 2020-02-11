package be.baur.sda;

/** A <code>SimpleNode</code> represents a simple SDA element; 
 * a node with a value of a simple type and no children - as
 * opposed to a {@link ComplexNode}.
 */
public final class SimpleNode extends Node {

	/** The (immutable) value has public visibility, and there is no getter for it. */
    public final String value;
	
    
	/** Creates a simple node with a name and value. */
	public SimpleNode(String name, String value) {
		super(name); this.value = value;
	}

	
	/** Returns the node value (as-is). */
	public String toString() { return value; }

	
	private final static String ESCAPE = "" + (char)SDA.ESCAPE;
	private final static String QUOTE = "" + (char)SDA.QUOTE;
	
	/** Renders the node as an SDA element, properly escaping backslashes and quotes. */
	public String render() {
		
		String val = value.replace(ESCAPE, ESCAPE + ESCAPE);
		return name + " " + QUOTE + val.replace(QUOTE, ESCAPE + QUOTE) + QUOTE;
	}
}