package be.baur.sda.parser;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

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
		if (scanner.c != Scanner.EOF) 
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



	/** For unit testing only. Make public to run. */
	static void main(String[] args) throws Exception {

		Node sr = (new Parser()).Parse(new StringReader("greeting{message\"hello world\"}"));
		Node fr = (new Parser()).Parse(new FileReader("D:\\home\\hb\\files\\projects\\SDA\\samples\\sample.sda"));
		System.out.println("sr: " + sr);
		System.out.println("fr: " + fr);

		/* test valid SDA */

		TestParseAndRender("S01", "empty\"\"", "empty \"\"");
		TestParseAndRender("S02", "  empty  \"\"  ", "empty \"\"");
		TestParseAndRender("S03", "empty{}", "empty{ }");
		TestParseAndRender("S04", "  empty  {  }  ", "empty{ }");
		TestParseAndRender("S05", "c1 { s1 \"hello  world\" } ", "c1{ s1 \"hello  world\" }");
		TestParseAndRender("S06", "example \"The \\\\ is called a \\\"backslash\\\" in English.\"", "example \"The \\\\ is called a \\\"backslash\\\" in English.\"");

		/* test error checking */

		String s = "SDA syntax violation at position ";
		TestParseError("F01", "", s + "1: unexpected end of input");
		TestParseError("F02", "  ", s + "3: unexpected end of input");
		TestParseError("F03", "bad", s + "4: unexpected end of input");
		TestParseError("F04", "bad  ", s + "6: unexpected end of input");
		TestParseError("F05", "2bad", s + "1: node name cannot start with '2'");
		TestParseError("F06", "b@d", s + "2: unexpected character '@'");
		TestParseError("F07", "trailing\"", s + "10: unexpected end of input");
		TestParseError("F08", "{", s + "1: node name cannot start with '{'");
		TestParseError("F09", "trailing{", s + "10: unexpected end of input");
		TestParseError("F10", "abc{ { ", s + "6: node name cannot start with '{'");
		TestParseError("F11", "abc{ \"", s + "6: node name cannot start with '\"'");
		TestParseError("F12", " }", s + "2: node name cannot start with '}'");
		TestParseError("F13", "noright {", s + "10: unexpected end of input");
		TestParseError("F14", "noleft }", s + "8: unexpected character '}'");
		TestParseError("F15", "a{ b{}", s + "7: unexpected end of input");
		TestParseError("F16", "a{} b{}", s + "5: excess input after root node");
		TestParseError("F17", "a{ b{} } }", s + "10: excess input after root node");
		TestParseError("F18", "a \"b\" c \"d\"", s + "7: excess input after root node");

	}

	private static Parser parser = new Parser();
	
	private static void TestParseError(String name, String in, String out) {

		try { parser.Parse(new StringReader(in)); }
		catch (Exception e) { 
			if (e.getMessage().equals(out)) 
				System.out.print(name + " ");
			else {
				System.out.println(name + " FAILED!");
				System.out.println("EXPECTED: " + out);
				System.out.println("RETURNED: " + e.getMessage());
			}
			return;
		}
		System.out.println(name + ": FAIL - exception expected");
	}

	private static void TestParseAndRender(String name, String in, String out) 
			throws IOException, SyntaxException {

		Node e = parser.Parse(new StringReader(in));
		StringWriter s = new StringWriter(); 
		SDA.Render(s, e);

		if (s.toString().equals(out)) 
			System.out.print(name + " ");
		else {
			System.out.println(name + " FAILED!");
			System.out.println("EXPECTED: " + out);
			System.out.println("RETURNED: " + s);
		}
		return;
	}
}
