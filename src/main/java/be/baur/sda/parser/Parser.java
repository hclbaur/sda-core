package be.baur.sda.parser;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import be.baur.sda.ComplexNode;
import be.baur.sda.Node;
import be.baur.sda.NodeSet;
import be.baur.sda.SDA;
import be.baur.sda.SimpleNode;


/** Expects an (SDA) input stream and returns a root <code>Node</code>.
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

public final class Parser {
		
	private Scanner scanner;
	
    public Node Parse(Reader input) 
        throws IOException, SyntaxException {
    	
    	scanner = new Scanner(input); 
    	Node node = getNode(); scanner.skipwhite(); 
    	if (scanner.c != SDA.EOF) 
    		throw new SyntaxException(scanner.p, "excess input after root node");
    	return node;
    }
    
    /** Recursive helper method to get nodes from the input stream, follows straight from BNF. */
    private Node getNode() throws SyntaxException, IOException {
    	
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
	
	   
    /** For unit testing only. */
    public static void main(String[] args) throws Exception {
    	
    	Parser parser = new Parser();
        Node s = parser.Parse(new StringReader("greeting{message\"hello world\"}"));
        Node f = parser.Parse(new FileReader("D:\\home\\hb\\files\\projects\\SDA\\samples\\sample.sda"));
        System.out.println("s: " + s.render());
        System.out.println("f: " + f.render());
    }
}
