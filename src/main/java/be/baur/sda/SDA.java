package be.baur.sda;

import java.io.IOException;
import java.io.Writer;

/** General class to hold some constants and static convenience methods */
public final class SDA {
	
	/** A left brace (starts a node list). */
	public static final int LBRACE = '{'; 
	/** A right brace (ends a node list). */
	public static final int RBRACE = '}'; 
	/** A quote (encloses simple content). */
	public static final int QUOTE = '"'; 
	/** The escape character: a back slash. */
	public static final int ESCAPE = '\\';

	
	/** Recursively render a <code>Node</code> as an SDA element to an output stream. */
    public static final void Render(Writer output, Node node) throws IOException {
    		
    	output.write( node.toString() ); output.close();
    }
	
}
