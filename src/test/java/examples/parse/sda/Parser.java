
package examples.parse.sda;

import java.io.IOException;
import java.io.Reader;
import java.util.Stack;

import be.baur.sda.ComplexNode;
import be.baur.sda.Node;
import be.baur.sda.SimpleNode;
import be.baur.sda.parse.SyntaxException;

public final class Parser {

	/** Example of an alternative parser (actually the first version I wrote).
	 * It uses a {@link Tokenizer} to read and validate input {@link Token}s
	 * and reduces them to {@link Node}s.
	 * <br>For example, after processing the following SDA input<br><br>
	 * <code>greeting { message "hello" }</code>
	 * <br><br>it returns a <code>ComplexNode</code> 'greeting' with a
	 * <code>SimpleNode</code> child 'message' that has a value of "greeting".
	 */
	public Node parse(Reader input) 
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

}
