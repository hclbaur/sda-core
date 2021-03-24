package be.baur.sda.serialization;

import java.io.Reader;

import be.baur.sda.Node;


/**
 * A <code>Parser</code> (in SDA context) is a <em>deserializer</em> that can
 * read an input stream (in a format specific to the type of parser) and
 * (re-)create an SDA document in memory.
 */
public interface Parser {

	/**
	 * Parses a character <code>input</code> stream and creates a <code>Node</code>.
	 * @return Node - the root node.
	 * @throws Exception
	 */
	Node parse(Reader input) throws Exception;

}
