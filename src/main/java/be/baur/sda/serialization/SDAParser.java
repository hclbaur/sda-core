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
 * SDA is parsed according to the following BNF (simplified):
 * 
 * <pre>
 * SDA = data_node
 * data_node = node_name (simple_content complex_content? | complex_content)
 * simple_content = '"' char* '"'
 * complex_content = '{' data_node* '}'
 * </pre>
 * 
 * @see DataNode
 */
public final class SDAParser implements Parser<DataNode> {

	private Scanner scanner;

	/**
	 * @throws SDAParseException if an SDA parsing error occurs
	 */
	public DataNode parse(Reader input) throws IOException, SDAParseException {

		scanner = new Scanner(input);
		
		scanner.advance(true); // advance to the first non-whitespace character
		DataNode node = parseNode(); // and get the root node

		if (scanner.c != Scanner.EOF)
			throw new SDAParseException("excess input after root node", scanner.p);

		return node;
	}

	
	// Recursive helper to get nodes from the input, follows straight from the EBNF.
	private DataNode parseNode() throws SDAParseException, IOException {

		final DataNode node;
		try {
			node = new DataNode( scanner.getNodeName() ); // create a new node
		} 
		catch (IllegalArgumentException e) { // should not happen
			throw new SDAParseException(e.getMessage(), scanner.p);
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
				node.add( parseNode() );
			}

			scanner.advance(true); // skip right brace and whitespace
		}
		else { // no complex content
			if (value == null) // and no simple content either
				throw new SDAParseException("unexpected character '" + (char)scanner.c + "'", scanner.p);
		}
		
		return node;
	}
}
