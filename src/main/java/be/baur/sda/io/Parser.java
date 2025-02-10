package be.baur.sda.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Objects;

import be.baur.sda.Node;

/**
 * A {@code Parser} is a <i>deserializer</i> that reads an input stream (in a
 * format specific to the type of parser) and creates a {@code Node}. A sample
 * implementation is the default SDA parser.
 * 
 * @see SDAParser
 */
public interface Parser<T extends Node> {

	/**
	 * Creates a node from a character input stream.
	 * 
	 * @param input an input stream, not null
	 * @return a (root) node, never null
	 * @throws IOException    if an I/O operation failed
	 * @throws ParseException if a parsing error occurs
	 */
	public T parse(Reader input) throws IOException, ParseException;


	/**
	 * Creates a node from a file with SDA content.
	 * 
	 * @param file an input file, not null
	 * @return a (root) node, never null
	 * @throws IOException    if an I/O operation failed
	 * @throws ParseException if a parsing error occurs
	 */
	default T parse(File file) throws IOException, ParseException {

		Objects.requireNonNull(file, "input file must not be null");
		try (
			FileInputStream fs = new FileInputStream(file);
			Reader is = new InputStreamReader(fs, "UTF-8");
			Reader br = new BufferedReader(is);
		) {
			return parse(br);
		} 
		catch (IOException e) {
			throw new IOException("error reading from " + file, e);
		}
		catch (ParseException e) {
			throw new FileParseException(file.toString(), e);
		}
	}


	/**
	 * Creates a node from a string.
	 * 
	 * @param input an input string, not null
	 * @return a (root) node, never null
	 * @throws IOException    if an I/O operation failed
	 * @throws ParseException if a parsing error occurs
	 */
	default T parse(String input) throws IOException, ParseException {

		Objects.requireNonNull(input, "input string must not be null");
		return parse(new StringReader(input));
	}

}