package be.baur.sda;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;

/** General class to hold some constants and static Parse/Render methods */
public final class SDA {
	
	/** A left brace (starts a node list). */
	static final int LBRACE = '{'; 
	/** A right brace (ends a node list). */
	static final int RBRACE = '}'; 
	/** A quote (encloses simple content). */
	static final int QUOTE = '"'; 
	/** The escape character: a back slash. */
	static final int ESCAPE = '\\';
	/** The end of file character. */
	static final int EOF = -1;	
	
	
    /** Parse SDA input and return a top level <code>Node</code>.
	 * For example, when associated with the following SDA input<br><br>
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
	
	private static Scanner scanner;
	
    public static final Node Parse(Reader input) 
        throws IOException, SyntaxException {
    	
    	scanner = new Scanner(input); 
    	Node node = getNode(); scanner.skipwhite(); 
    	if (scanner.c != SDA.EOF) 
    		throw new SyntaxException(scanner.p, "excess input after root node");
    	return node;
    }
    
    /** Recursive helper method to get nodes from the input stream, follows straight from BNF. */
    private static final Node getNode() throws SyntaxException, IOException {
    	
    	String name = scanner.getNodeName();  // get the name of the new node

    	if (scanner.c == SDA.LBRACE) { // complex content ahead, return a complex node
    		
    		ComplexNode parent = new ComplexNode(name);
    		NodeSet children = new NodeSet();
    		
    		scanner.advance(); scanner.skipwhite(); // skip left brace and whitespace
    		
    		while (scanner.c != SDA.RBRACE) { // until end of complex content...
    			children.add(getNode());  // ... add a node to the set of children
    		}
    		
    		scanner.advance(); scanner.skipwhite(); // skip right brace and whitespace
    		
    		if (children.size()>0) parent.addAll(children);
    		return parent; 
    	}
    	else // simple content ahead, return a simple node
    		return new SimpleNode(name, scanner.getQuotedString());

    }
	
	   
	/** Recursively render a <code>Node</code> as an SDA element to an output stream. */
    public static final void Render(Writer output, Node node) throws IOException {
    		
    	output.write( node.render() ); output.close();
    }
	
    
    /** For unit testing only. */
    public static final void main(String[] args) throws Exception {
    	
        Node e = Parse(new StringReader("greeting{message\"hello world\"}"));
        Node f = Parse(new FileReader("D:\\home\\hb\\files\\projects\\SDA\\sample.sda"));
        System.out.println("string: " + e.render());
        System.out.println("file: " + f.render());
    }
}
