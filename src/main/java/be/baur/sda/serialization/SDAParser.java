package be.baur.sda.serialization;

import java.io.IOException;
import java.io.Reader;

import be.baur.sda.DataNode;
import be.baur.sda.SDA;


/**
 * The default SDA parser; used to read and parse SDA content to create a
 * {@code DataNode}. For example, when processing the following input:
 * 
 * <pre>
 * greeting { message "hello" }
 * </pre>
 * 
 * the parser returns a node named 'greeting', containing another node named
 * 'message' with a value of "hello".<br>
 * <br>
 * SDA is parsed according to the following (simplified) grammar:
 * 
 * <pre>
 * SDA = data_node
 * data_node = node_name (simple_content | (simple_content? complex_content) )
 * simple_content = '"' char* '"'
 * complex_content = '{' data_node* '}'
 * </pre>
 * 
 * @see DataNode
 */
public final class SDAParser implements Parser<DataNode> {

	/**
	 * Creates a data node from a character input stream in SDA format. The parser
	 * is re-usable and thread-safe, and can be run as a singleton instance.
	 * 
	 * @throws SDAParseException if an SDA parsing error occurs
	 */
	@Override
	public DataNode parse(Reader input) throws IOException, SDAParseException {

		final Scanner scanner = new Scanner(input);
		
		scanner.advance(true); // advance to the first non-whitespace character
		DataNode node = parseNode(scanner); // and get the root node

		if (scanner.c != Scanner.EOF)
			throw exception(scanner.p, "excess input after root node");

		return node;
	}

	
	// Recursive helper to get nodes from the input, follows straight from the EBNF.
	private static DataNode parseNode(final Scanner scanner) throws SDAParseException, IOException {

		final DataNode node;
		try {
			node = new DataNode( scanner.getNodeName() ); // create a new node
		} 
		catch (IllegalArgumentException e) { // should not happen
			throw exception(scanner.p, e.getMessage());
		}

		String value = null;
		if (scanner.c == SDA.QUOTE) {  // simple content ahead
			value = scanner.getQuotedString();
			node.setValue( value );
		}

		if (scanner.c == SDA.LBRACE) { // complex content ahead
	
			scanner.advance(true);  // skip left brace and whitespace
			
			node.add(null);  // initialize child set, recursively add nodes
			while (scanner.c != SDA.RBRACE) {
				node.add( parseNode(scanner) );
			}

			scanner.advance(true); // skip right brace and whitespace
		}
		else { // no complex content
			if (value == null) // and no simple content either
				throw exception(scanner.p, "unexpected character '%c'", scanner.c);
		}
		
		return node;
	}


	/**
	 * Returns an SDA parse exception with a message that includes the error position.
	 * 
	 * @param offset position where the error was found
	 * @param format a format message, and
	 * @param args   arguments, as in {@link String#format}
	 * @return SDAParseException
	 */
	private static final SDAParseException exception(int offset, String format, Object... args) {
		return new SDAParseException(String.format(format, args), offset);
	}
	

	/**
	 * Inner {@code Scanner} class for the {@code SDAParser}.
	 */
	private final class Scanner {
	    
	    private Reader input; // the input stream
		private int c; // current character in the stream
		private int p; // current position in the stream
		
	    
	    /** Create and initialize a scanner at position 0. */
	    Scanner(Reader input) {
	    	this.input = input; p=0;
	    }

		
		/**
		 * Advance the scanner to the next character in the input stream.
		 * If <code>skipWhite</code> is true, whitespace will be skipped.
		 */
	    void advance(boolean skipWhite) throws IOException {
	    	do {
	    		c = input.read(); ++p;
	    	} 
	    	while (skipWhite && Character.isWhitespace(c));
	    }
	    

	    private static final int EOF = -1;
	    /** Check and abort when EOF is reached. */
	    void checkEOF() throws SDAParseException {
	    	if (c == EOF) throw exception(p-1, "unexpected end of input");
	    }
	    
	    
	    /** Look for a valid node name and return it. */
	    String getNodeName() throws SDAParseException, IOException  {
	    	
	    	String s = "";
	    	
	    	checkEOF();
	    	if (! SDA.isNameStart(c)) 
	    		throw exception(p, "node name cannot start with '%c'", c);
	    	
	    	do { // add to result until we get something that is not part of a node name
	    		s = s + (char)c; advance(false);
	    	} while (SDA.isNamePart(c));

	    	if (Character.isWhitespace(c)) advance(true);
	    	checkEOF();  // dangling node names are not allowed
	 
	    	return s;
	    }
	    
	    
	    /** Look for a quoted string and return it (without quotes). */
	    String getQuotedString() throws SDAParseException, IOException  {
	    	
	    	String s = ""; boolean escape = false;
	    	
	    	if (c != SDA.QUOTE)  // must start with quote
	    		throw exception(p, "unexpected character '%c'", c);
	    	
	    	// add to result until we get the end quote or EOF, handle escaped characters  	
	    	while (true) {
	    		advance(false); checkEOF();
	    		if (!escape && c == SDA.BSLASH) {escape = true; continue; };
	    		if (escape) { s = s + (char)c; escape = false; continue; }
	    		if (c == SDA.QUOTE) break;
	    		s = s + (char)c;
	    	}
	    	
	    	advance(true); // skip over the end quote and white-space that follows
	    	return s;
	    }   
	}
}
