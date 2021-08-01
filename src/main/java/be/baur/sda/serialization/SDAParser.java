package be.baur.sda.serialization;

import java.io.IOException;
import java.io.Reader;

import be.baur.sda.ComplexNode;
import be.baur.sda.Node;
import be.baur.sda.SDA;
import be.baur.sda.SimpleNode;


/**
 * This is the default parser; used to read and parse SDA content into a
 * <code>Node</code> tree. For example, when processing the following input:
 * 
 * <pre>
 * greeting { message "hello" }
 * </pre>
 * 
 * it returns a <code>ComplexNode</code> 'greeting' containing a
 * <code>SimpleNode</code> 'message' with a value of "hello".<br>
 * <br>
 * SDA is parsed according to the following EBNF:
 * 
 * <pre>
 * SDA = node
 * node = name (simple_content | complex_content)
 * simple_content = '"' string '"'
 * complex_content = '{' node* '}'
 * </pre>
 */
public final class SDAParser implements Parser {

	private Scanner scanner;

	public Node parse(Reader input) throws IOException, SyntaxException {

		scanner = new Scanner(input);
		
		Node node = getNode();

		scanner.skipwhite();
		if (scanner.c != Scanner.EOF)
			throw new SyntaxException("excess input after root node", scanner.p);

		return node;
	}

	
	/** Recursive helper method to get nodes from the input stream, follows straight from BNF. */
	private Node getNode() throws SyntaxException, IOException {

		String name = scanner.getNodeName();  // get the name of the new node

		if (scanner.c == SDA.LBRACE) { // complex content ahead, create a complex node

			ComplexNode complexNode;
			try {
				complexNode = new ComplexNode(name);
			} catch (IllegalArgumentException e) {
				throw new SyntaxException(e.getMessage(), scanner.p);
			}
			
			scanner.advance(); scanner.skipwhite();			// skip left brace and whitespace
			while (scanner.c != SDA.RBRACE) {				// until end of complex content...
				complexNode.getNodes().add( getNode() );	// ... get the next node and add it
			}

			scanner.advance(); scanner.skipwhite(); // skip right brace and whitespace
			return complexNode; 
		}
		else try { // simple content ahead, create a simple node
			return new SimpleNode(name, scanner.getQuotedString());
		} catch (IllegalArgumentException e) {
			throw new SyntaxException(e.getMessage(), scanner.p);
		}
	}
}
