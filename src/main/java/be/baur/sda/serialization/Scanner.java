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
    	this.input = input; p=0;
    }

	
	/**
	 * Advance the scanner to the next character in the input stream.
	 * If <code>skipWhite</code> is true, whitespace will be skipped.
	 */
    void advance(boolean skipWhite) throws IOException {
    	do {
    		c = input.read(); ++p;
    	} 
    	while (skipWhite && Character.isWhitespace(c));
    }
    

    static final int EOF = -1;
    /** Check and abort when EOF is reached. */
    void checkEOF() throws SyntaxException {
    	if (c == EOF) throw new SyntaxException("unexpected end of input", p-1);
    }
    
    
    /** Look for a valid node name and return it. */
    String getNodeName() throws SyntaxException, IOException  {
    	
    	String s = "";
    	
    	checkEOF();
    	if (! SDA.isNameStart(c)) 
    		throw new SyntaxException("node name cannot start with '" + (char)c + "'", p);
    	
    	do { // add to result until we get something that is not part of a node name
    		s = s + (char)c; advance(false);
    	} while (SDA.isNamePart(c));

    	if (Character.isWhitespace(c)) advance(true);
    	checkEOF();  // dangling node names are not allowed
 
    	return s;
    }
    
    
//    /** Returns a token from the input, e.g. a string without whitespace. */
//    String getToken() throws SyntaxException, IOException  {
//    	
//    	String s = "";
//    	
//    	checkEOF();
//    	
//    	do { // add to result until we get whitespace
//    		s = s + (char)c; advance(false);
//    	} while (! Character.isWhitespace(c));
//
//    	if (c != EOF) advance(true);
//    	checkEOF();  // dangling node names are not allowed
// 
//    	return s;
//    }
    
    
    /** Look for a quoted string and return it (without quotes). */
    String getQuotedString() throws SyntaxException, IOException  {
    	
    	String s = ""; boolean escape = false;
    	
    	if (c != SDA.QUOTE)  // must start with quote
    		throw new SyntaxException("unexpected character '" + (char)c + "'", p);
    	
    	// add to result until we get the end quote or EOF, handle escaped characters  	
    	while (true) {
    		advance(false); checkEOF();
    		if (!escape && c == SDA.BSLASH) {escape = true; continue; };
    		if (escape) { s = s + (char)c; escape = false; continue; }
    		if (c == SDA.QUOTE) break;
    		s = s + (char)c;
    	}
    	
    	advance(true); // skip over the end quote and white-space that follows
    	return s;
    }   
}