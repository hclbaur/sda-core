package be.baur.sda.io;

import java.io.IOException;
import java.io.Reader;
import java.util.Objects;

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
	 * is state-less and thread-safe, and can be run as a singleton instance.
	 * 
	 * @throws ParseException if an SDA parsing error occurs
	 */
	@Override
	public DataNode parse(Reader input) throws IOException, ParseException {

		Objects.requireNonNull(input, "input reader must not be null");
		final Scanner scanner = new Scanner(input);
		
		scanner.advanceSkipWhite();
		DataNode node = parseNode(scanner);

		if (scanner.c != Scanner.EOF)
			throw exception(scanner.p, "excess input after root node");

		return node;
	}

	
	// Recursive helper to get nodes from the input, follows straight from the EBNF.
	private static DataNode parseNode(Scanner scanner) throws ParseException, IOException {

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
	
			scanner.advanceSkipWhite();  // skip left brace and whitespace
			
			node.expand();  // initialize as vacant, recursively add nodes
			while (scanner.c != SDA.RBRACE) {
				node.add( parseNode(scanner) );
			}

			scanner.advanceSkipWhite(); // skip right brace and whitespace
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
	 * @return ParseException
	 */
	private static final ParseException exception(int offset, String format, Object... args) {
		return new ParseException(String.format(format, args), offset);
	}
	

	/**
	 * Inner {@code Scanner} class for the {@code SDAParser}.
	 */
	private static final class Scanner {
	    
	    private static final int EOF = -1;
	    
	    private Reader input; // the input stream
		private int c; // current character in the stream
		private int p; // current position in the stream
		
	    
	    /** Create and initialize a scanner at position 0. */
	    Scanner(Reader input) {
	    	this.input = input; p=0;
	    }

		
		/**
		 * Advance the scanner to the next character.
		 */
		void advance() throws IOException {
			c = input.read(); ++p;
		}
	    
	    
		/**
		 * Advance the scanner to the next non-white space character.
		 */
		void advanceSkipWhite() throws IOException {
			do {
				c = input.read(); ++p;
			} while (Character.isWhitespace(c));
		}


		/**
		 * Check and abort when EOF is reached.
		 */
		void checkEOF() throws ParseException {
			if (c == EOF)
				throw exception(p - 1, "unexpected end of input");
		}
	    
	    
	    /** Look for a valid node name and return it. */
	    String getNodeName() throws ParseException, IOException  {
	    	
	    	StringBuilder s = new StringBuilder(); 
	    	
	    	checkEOF();
	    	if (! SDA.isNodeNameStart(c)) 
	    		throw exception(p, "node name cannot start with '%c'", c);
	    	
	    	do { // add to result until we get something that is not part of a node name
	    		s.append((char) c); advance();
	    	} while (SDA.isNodeNamePart(c));

	    	if (Character.isWhitespace(c)) advanceSkipWhite();
	    	checkEOF();  // dangling node names are not allowed
	 
	    	return s.toString();
	    }
	    
	    
	    /** Look for a quoted string and return it (without quotes). */
	    String getQuotedString() throws ParseException, IOException  {
	    	
	    	StringBuilder s = new StringBuilder(); 
	    	boolean escape = false;
	    	
	    	if (c != SDA.QUOTE)  // must start with quote
	    		throw exception(p, "unexpected character '%c'", c);
	    	
	    	// add to result until we get the end quote or EOF, handle escaped characters  	
			while (true) {
				advance(); checkEOF();
				if (!escape && c == SDA.BSLASH) {
					escape = true; continue;
				}
				if (escape) {
					s.append((char) c);
					escape = false; continue;
				}
				if (c == SDA.QUOTE) break;
				s.append((char) c);
			}
	    	
			advanceSkipWhite(); // skip the end quote and any white-space that follows
	    	return s.toString();
	    }   
	}
}
