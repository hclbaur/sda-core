package be.baur.sda;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import be.baur.sda.parser.Parser;
import be.baur.sda.parser.SyntaxException;

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
	/** The end of file character. */
	public static final int EOF = -1;	
	
	/** Invoke the default parser on an input stream 
	 * @throws SyntaxException 
	 * @throws IOException */
    public static final Node Parse(Reader input) throws IOException, SyntaxException {
    	return new Parser().Parse(input);
    }
    

	/** Recursively render a <code>Node</code> as an SDA element to an output stream. */
    public static final void Render(Writer output, Node node) throws IOException {
    		
    	output.write( node.render() ); output.close();
    }
	
}
