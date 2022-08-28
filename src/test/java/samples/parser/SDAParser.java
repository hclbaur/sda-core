
package samples.parser;

import java.io.IOException;
import java.io.Reader;
import java.util.Stack;

import be.baur.sda.Node;
import be.baur.sda.serialization.Parser;
import be.baur.sda.serialization.SyntaxException;

/**
 * Alternative SDA parser (actually the first one I wrote, and lacking support
 * for SDA 2.0, mind you). For example, when processing the following SDA input:
 * 
 * <pre>
 * greeting { message "hello" }
 * </pre>
 * 
 * the parser returns a node 'greeting', containing a node 'message' with a
 * value of "hello".<br>
 */
public final class SDAParser implements Parser {

	public Node parse(Reader input) throws IOException, SyntaxException {

		Tokenizer lexer = new Tokenizer(input);
		Stack<Token> stack = new Stack<Token>();
		Token token;	// the context token

		/* The context node is where we recursively add child elements from
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
						throw new SyntaxException("too many root elements", lexer.getPos());
					stack.push(token); continue;
				}

				if (stack.peek().type == Tokenizer.BLOCK_START) {
					stack.push(token); continue; // identifier of a child element, put on the stack
				}
				// cannot have identifiers without a context (except the root)
				throw new SyntaxException("unexpected identifier \"" + token.value + "\"", lexer.getPos());
			}

			// we got a string, must be value to an element
			if (token.type == Tokenizer.STRING) {

				if ( !stack.empty() ) {

					if (stack.peek().type == Tokenizer.IDENTIFIER) {

						// pop identifier, create a simple node
						Node e; String name = stack.pop().value;
						try {
							e = new Node(name, token.value);
						} catch (IllegalArgumentException x) {
							throw new SyntaxException(x.getMessage(), lexer.getPos());
						}
						
						if (context == null) context = e;
						else { 
							// if we get here, context should be complex!
							context.getNodes().add(e); // add child to context node
						}                		
						continue;
					}
				}
				// cannot have a value without preceding identifier
				throw new SyntaxException("value \"" + token.value + "\" has no identifier", lexer.getPos());
			}

			// we got the start of a block, must be a complex element then
			if (token.type == Tokenizer.BLOCK_START) {

				if ( !stack.empty() ) {

					if (stack.peek().type == Tokenizer.IDENTIFIER) {

						// pop identifier, create a complex node
						Node c; String name = stack.pop().value;
						try {
							c = new Node(name); c.addNode(null);
						} catch (IllegalArgumentException x) {
							throw new SyntaxException(x.getMessage(), lexer.getPos());
						}
						
						stack.push(token); // push block start on the stack

						if (context != null) {
							context.getNodes().add(c); // add child to context node
						}
						context = c;  // new becomes context context
						continue;
					}
				}
				// cannot start a block without preceding identifier
				throw new SyntaxException("block has no identifier", lexer.getPos());
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
				throw new SyntaxException("unexpected block end", lexer.getPos());
			}

			// no more input, check if we are done
			if (token.type == Tokenizer.EOF) {

				if ( !stack.empty() ) // still tokens left to reduce
					throw new SyntaxException("unexpected end of input", lexer.getPos());

				if ( context == null ) // no nodes created
					throw new SyntaxException("input has no data", lexer.getPos());

				return context; // should be the top level node
			}
			// should never be reached
			throw new SyntaxException("impossible error", lexer.getPos());
		} 
		while (true);
	}

}
