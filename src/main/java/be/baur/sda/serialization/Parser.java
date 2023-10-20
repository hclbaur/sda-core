package be.baur.sda.serialization;

import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;

import be.baur.sda.Node;


/**
 * A <code>Parser</code> is a <i>deserializer</i> that reads an input stream (in
 * a format specific to the type of parser) and creates a {@code Node}. A sample
 * implementation is the default SDA parser.
 * 
 * @see SDAParser
 */
public interface Parser<T extends Node> {

	/**
	 * Creates a node from the specified input stream.
	 * 
	 * @param input an input stream
	 * @return a (root) node
	 * @throws IOException    if an input exception occurs
	 * @throws ParseException if a parse exception occurs
	 */
	T parse(Reader input) throws IOException, ParseException;

}
