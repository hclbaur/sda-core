package be.baur.sda.parse;

import java.io.IOException;
import java.io.Reader;

import be.baur.sda.ComplexNode;
import be.baur.sda.Node;
import be.baur.sda.SDA;
import be.baur.sda.SimpleNode;


/** The default parser. Reads SDA input and returns a root <code>Node</code>.
 * <br>For example, when processing the following input<br><br>
 * <code>greeting { message "hello" }</code>
 * <br><br>it returns a <code>ComplexNode</code> 'greeting' with a
 * <code>SimpleNode</code> child 'message' with a value of "hello".
 * <br>
 * <br>SDA is parsed according to the following EBNF:
 * <br>SDA = node
 * <br>node = name (simple_content | complex_content)
 * <br>simple_content = '"' string '"'
 * <br>complex_content = '{' node* '}'
 */
public final class Parser {

	private Scanner scanner;

	/** Parse a character stream with SDA content. */
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

		if (scanner.c == SDA.LBRACE) { // complex content ahead, return a complex node

			ComplexNode complexNode = new ComplexNode(name);

			scanner.advance(); scanner.skipwhite(); // skip left brace and whitespace

			while (scanner.c != SDA.RBRACE) {	// until end of complex content...
				complexNode.add( getNode() );	// ...get the next node and add it
			}

			scanner.advance(); scanner.skipwhite(); // skip right brace and whitespace

			return complexNode; 
		}
		else // simple content ahead, return a simple node
			return new SimpleNode(name, scanner.getQuotedString());
	}

}
