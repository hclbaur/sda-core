
package samples.parser;

import java.io.IOException;
import java.io.Reader;
import java.util.Stack;

import be.baur.sda.DataNode;
import be.baur.sda.io.Parser;
import be.baur.sda.io.SDAParseException;

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
public final class SDAParser implements Parser<DataNode> {

	@Override
	public DataNode parse(Reader input) throws IOException, SDAParseException {

		Tokenizer lexer = new Tokenizer(input);
		Stack<Token> stack = new Stack<Token>();
		Token token;	// the context token

		/* The context node is where we recursively add child elements from
		 * the input. When a complex node is created, it becomes the 
		 * new context node. When all input has been parsed, the context
		 * node (representing the root element) is returned.
		 */
		DataNode context = null;

		do {
			token = lexer.getToken();
			//System.out.println("parseInput() " + lexer.getPos(token.toString()));

			// we got an identifier, must be a new element
			if (token.type == Tokenizer.IDENTIFIER) {

				if ( stack.empty() ) {
					// a root element, check whether it is the first (and only)
					if (context != null) 
						throw new SDAParseException("too many root elements", lexer.getPos());
					stack.push(token); continue;
				}

				if (stack.peek().type == Tokenizer.BLOCK_START) {
					stack.push(token); continue; // identifier of a child element, put on the stack
				}
				// cannot have identifiers without a context (except the root)
				throw new SDAParseException("unexpected identifier \"" + token.value + "\"", lexer.getPos());
			}

			// we got a string, must be value to an element
			if (token.type == Tokenizer.STRING) {

				if ( !stack.empty() ) {

					if (stack.peek().type == Tokenizer.IDENTIFIER) {

						// pop identifier, create a simple node
						DataNode e; String name = stack.pop().value;
						try {
							e = new DataNode(name, token.value);
						} catch (IllegalArgumentException x) {
							throw new SDAParseException(x.getMessage(), lexer.getPos());
						}
						
						if (context == null) context = e;
						else { 
							// if we get here, context should be complex!
							context.add(e); // add child to context node
						}                		
						continue;
					}
				}
				// cannot have a value without preceding identifier
				throw new SDAParseException("value \"" + token.value + "\" has no identifier", lexer.getPos());
			}

			// we got the start of a block, must be a complex element then
			if (token.type == Tokenizer.BLOCK_START) {

				if ( !stack.empty() ) {

					if (stack.peek().type == Tokenizer.IDENTIFIER) {

						// pop identifier, create a complex node
						DataNode c; String name = stack.pop().value;
						try {
							c = new DataNode(name); c.add(null);
						} catch (IllegalArgumentException x) {
							throw new SDAParseException(x.getMessage(), lexer.getPos());
						}
						
						stack.push(token); // push block start on the stack

						if (context != null) {
							context.add(c); // add child to context node
						}
						context = c;  // new becomes context context
						continue;
					}
				}
				// cannot start a block without preceding identifier
				throw new SDAParseException("block has no identifier", lexer.getPos());
			}

			// we got the end of a block, parent of context becomes context
			if (token.type == Tokenizer.BLOCK_END) {

				if ( !stack.empty() ) {

					if (stack.peek().type == Tokenizer.BLOCK_START) {

						DataNode parent = context.getParent();
						if (parent != null)	context = parent;
						stack.pop(); continue;
					}
				}
				// cannot start a block without preceding identifier
				throw new SDAParseException("unexpected block end", lexer.getPos());
			}

			// no more input, check if we are done
			if (token.type == Tokenizer.EOF) {

				if ( !stack.empty() ) // still tokens left to reduce
					throw new SDAParseException("unexpected end of input", lexer.getPos());

				if ( context == null ) // no nodes created
					throw new SDAParseException("input has no data", lexer.getPos());

				return context; // should be the top level node
			}
			// should never be reached
			throw new SDAParseException("impossible error", lexer.getPos());
		} 
		while (true);
	}

}
