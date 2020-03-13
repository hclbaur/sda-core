
package examples.parsers.sda;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Stack;

import be.baur.sda.ComplexNode;
import be.baur.sda.Node;
import be.baur.sda.SDA;
import be.baur.sda.SimpleNode;
import be.baur.sda.parser.SyntaxException;

public final class Parser {

	/** Parse SDA input and return a top level <code>Node</code>.
	 * Internally, this method uses a {@link Tokenizer} to obtain and validate 
	 * input {@link Token} objects and reduced them to {@link Node} objects. 
	 * <br>For example, when associated with the following SDA input<br><br>
	 * <code>greeting { message "hello" }</code>
	 * <br><br>it returns a <code>ComplexNode</code> 'greeting' with a
	 * <code>SimpleNode</code> child 'message' that has a value of "greeting".
	 */
	public Node Parse(Reader input) 
			throws IOException, SyntaxException {

		Tokenizer lexer = new Tokenizer(input);
		Stack<Token> stack = new Stack<Token>();
		Token token;	// the context token

		/* The context node is where we recursively addChild elements from
		 * the input. When a complex node is created, it becomes the 
		 * new context node. When all input has been parsed, the context
		 * node (representing the root element) is returned.
		 */
		Node context = null;

		do {
			token = lexer.getToken();
			//System.out.println("parseInput() " + lexer.getPos(token.toString()));

			// we got an identifier, must be a new element
			if (token.type == Tokenizer.IDENTIFIER) {

				if ( stack.empty() ) {
					// a root element, check whether it is the first (and only)
					if (context != null) 
						throw new SyntaxException(lexer.getPos(), "too many root elements");
					stack.push(token); continue;
				}

				if (stack.peek().type == Tokenizer.BLOCK_START) {
					stack.push(token); continue; // identifier of a child element, put on the stack
				}
				// cannot have identifiers without a context (except the root)
				throw new SyntaxException(lexer.getPos(), "unexpected identifier \"" + token.value + "\"");
			}

			// we got a string, must be value to an element
			if (token.type == Tokenizer.STRING) {

				if ( !stack.empty() ) {

					if (stack.peek().type == Tokenizer.IDENTIFIER) {

						// pop identifier, create a simple node
						Node e = new SimpleNode(stack.pop().value, token.value);

						if (context == null) context = e;
						else { 
							// if we get here, context should be complex!
							ComplexNode c = (ComplexNode) context;
							c.add(e); // addChild to the context context
						}                		
						continue;
					}
				}
				// cannot have a value without preceding identifier
				throw new SyntaxException(lexer.getPos(), "value \"" + token.value + "\" has no identifier");
			}

			// we got the start of a block, must be a complex element then
			if (token.type == Tokenizer.BLOCK_START) {

				if ( !stack.empty() ) {

					if (stack.peek().type == Tokenizer.IDENTIFIER) {

						// pop identifier, create a complex node
						Node c = new ComplexNode(stack.pop().value);
						stack.push(token); // push block start on the stack

						if (context != null) {
							ComplexNode p = (ComplexNode) context;
							p.add(c); // addChild to the context context
						}
						context = c;  // new becomes context context
						continue;
					}
				}
				// cannot start a block without preceding identifier
				throw new SyntaxException(lexer.getPos(), "block has no identifier");
			}

			// we got the end of a block, parent of context becomes context
			if (token.type == Tokenizer.BLOCK_END) {

				if ( !stack.empty() ) {

					if (stack.peek().type == Tokenizer.BLOCK_START) {

						Node parent = context.getParent();
						if (parent != null)	context = parent;
						stack.pop(); continue;
					}
				}
				// cannot start a block without preceding identifier
				throw new SyntaxException(lexer.getPos(), "unexpected block end");
			}

			// no more input, check if we are done
			if (token.type == Tokenizer.EOF) {

				if ( !stack.empty() ) // still tokens left to reduce
					throw new SyntaxException(lexer.getPos(), "unexpected end of input");

				if ( context == null ) // no nodes created
					throw new SyntaxException(lexer.getPos(), "input has no data");

				return context; // should be the top level node
			}
			// should never be reached
			throw new SyntaxException(lexer.getPos(), "impossible error");
		} 
		while (true);
	}



	/** For unit testing only. Make public to run. */
	static void main(String[] args) throws Exception {

		Node e = (new Parser()).Parse(new StringReader("greeting{message\"hello world\"}"));
		System.out.println("e: " + e);

		/* test valid SDA */

		TestParseAndRender("S1", "empty \"\"", "empty \"\"");
		TestParseAndRender("S2", "empty {  }", "empty{ }");
		TestParseAndRender("S3", "greeting{message\"hello world\"}", "greeting{ message \"hello world\" }");
		TestParseAndRender("S4", "example \"The \\\\ is called a \\\"backslash\\\" in English.\"", "example \"The \\\\ is called a \\\"backslash\\\" in English.\"");

		/* test error checking */

		String s = "SDA syntax violation at position ";
		TestParseError("F1", "", s + "0: input has no data");
		TestParseError("F2", " ", s + "1: input has no data");
		TestParseError("F3", "bad", s + "3: unexpected end of input");
		TestParseError("F4", "2bad", s + "1: identifier cannot start with '2'");
		TestParseError("F5", "b@d", s + "2: identifier cannot contain '@'");
		TestParseError("F6", "\"string\"", s + "8: value \"string\" has no identifier");
		TestParseError("F7", "\"pending", s + "8: trailing or pending quote");
		TestParseError("F8", "trailing\"", s + "9: trailing or pending quote");
		TestParseError("F9", "{", s + "1: block has no identifier");
		TestParseError("F10", "}", s + "1: unexpected block end");
		TestParseError("F11", "noright {", s + "9: unexpected end of input");
		TestParseError("F12", "noleft }", s + "8: unexpected block end");
		TestParseError("F13", "a{} b{}", s + "6: too many root elements");
		TestParseError("F14", "a \"b\" c \"d\"", s + "8: too many root elements");
	}

	private static Parser parser = new Parser();

	static void TestParseError(String name, String in, String out) {

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

	static void TestParseAndRender(String name, String in, String out) 
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
