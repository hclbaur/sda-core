package be.baur.sda.serialization;

import java.io.IOException;
import java.io.Reader;

import be.baur.sda.SDA;

/**
 * The <code>Scanner</code> scans an input stream and supplies tokens to the
 * <code>Parser</code>.
 */
final class Scanner {
    
    private Reader input; // the input stream
	int c; // current character in the stream
	int p; // current position in the stream
	
    
    /** Create and initialize a scanner. */
    Scanner(Reader input) throws IOException {
    	this.input = input; p=0; advance();
    }

	
    /** Advance the scanner. */
    void advance() throws IOException {
    	c = input.read(); ++p;
    }
    
    /** Check and abort when EOF is reached. */
    static final int EOF = -1;
    void checkEOF() throws SyntaxException {
    	if (c == EOF) throw new SyntaxException("unexpected end of input", p);
    }

	
    /** Advance while skipping over whitespace. */
    void skipwhite() throws IOException {
    	while (Character.isWhitespace(c)) advance();
    }
    
    
    /** Look for a valid node name and return it. */
    String getNodeName() throws SyntaxException, IOException  {
    	
    	String s = "";
    	
    	skipwhite(); checkEOF();
    	
    	if (! SDA.isNameStart(c)) 
    		throw new SyntaxException("node name cannot start with '" + (char)c + "'", p);
    	
    	do { // add to result until we get something that is not part of a node name
    		s = s + (char)c; advance();
    	} while (SDA.isNamePart(c));
    	
    	skipwhite(); checkEOF();  // dangling node names are not allowed
 
    	return s;
    }
    
    
    /** Look for a quoted string and return it (without quotes). */
    String getQuotedString() throws SyntaxException, IOException  {
    	
    	String s = ""; boolean escape = false;
    	
    	if (c != SDA.QUOTE)  // must start with quote
    		throw new SyntaxException("unexpected character '" + (char)c + "'", p);
    	
    	// add to result until we get the end quote or EOF, handle escaped characters  	
    	while (true) {
    		advance(); checkEOF();
    		if (!escape && c == SDA.BSLASH) {escape = true; continue; };
    		if (escape) { s = s + (char)c; escape = false; continue; }
    		if (c == SDA.QUOTE) break;
    		s = s + (char)c;
    	}
    	
    	advance(); skipwhite(); // skip the end quote and whitespace

    	return s;
    }
    
}