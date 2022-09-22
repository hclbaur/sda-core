package be.baur.sda.serialization;

import java.io.IOException;
import java.io.Reader;

import be.baur.sda.Node;
import be.baur.sda.SDA;


/**
 * This is the default parser; used to read and parse SDA content to create 
 * a {@code Node}. For example, when processing the following input:
 * 
 * <pre>
 * <code>
 * greeting { message "hello" }
 * </code>
 * </pre>
 * 
 * the parser returns a node named 'greeting', containing another node named
 * 'message' with a value of "hello".<br>
 * <br>
 * SDA is parsed according to the following EBNF:
 * 
 * <pre>
 * <code>
 * SDA = node
 * node = name (simple_content complex_content? | complex_content)
 * simple_content = '"' char* '"'
 * complex_content = '{' node* '}'
 * </code>
 * </pre>
 * 
 * See also {@link Node}.
 */
public final class SDAParser implements Parser {

	private Scanner scanner;

	public Node parse(Reader input) throws IOException, SyntaxException {

		scanner = new Scanner(input);
		
		scanner.advance(true); // advance to the first non-whitespace character
		Node node = getNode(); // and get the root node

		if (scanner.c != Scanner.EOF)
			throw new SyntaxException("excess input after root node", scanner.p);

		return node;
	}

	
	/**
	 * Recursive helper method to get nodes from the input stream, follows straight
	 * from EBNF.
	 */
	private Node getNode() throws SyntaxException, IOException {

		Node node;
		try {
			node = new Node( scanner.getNodeName() ); // create a new node
		} 
		catch (IllegalArgumentException e) {
			throw new SyntaxException(e.getMessage(), scanner.p);
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
				node.add( getNode() );
			}

			scanner.advance(true); // skip right brace and whitespace
		}
		else { // no complex content
			if (value == null) // and no simple content either
				throw new SyntaxException("unexpected character '" + (char)scanner.c + "'", scanner.p);
		}
		
		return node;
	}
}
