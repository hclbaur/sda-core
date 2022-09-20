package be.baur.sda.serialization;

import java.io.IOException;
import java.io.Reader;

import be.baur.sda.Node;


/**
 * A <code>Parser</code> (in SDA context) is a <i>deserializer</i> that reads an
 * input stream (in a format specific to the type of parser) and creates a
 * {@link Node}. A sample implementation is the default {@link SDAParser}.
 */
public interface Parser {

	/**
	 * Creates a node from a character input stream.
	 * 
	 * @param input an input stream
	 * @return a (root) node
	 * @throws IOException if an input exception occurs
	 * @throws SyntaxException if a parse exception occurs
	 */
	Node parse(Reader input) throws IOException, SyntaxException;

}
