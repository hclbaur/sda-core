package be.baur.sda.parse;

import java.io.IOException;
import java.io.Reader;

import be.baur.sda.SDA;

/** 
 * The <code>Scanner</code> scans an input stream and supplies tokens to the parser.
 * Because it is used in the parser package only, everything has package visibility. 
 */

final class Scanner {
    
    private Reader input; // the input stream
	int c; // current character in the stream
	long p; // current position in the stream
	
    
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
    	if (c == EOF) throw new SyntaxException(p, "unexpected end of input");
    }

	
    /** Advance while skipping over whitespace. */
    void skipwhite() throws IOException {
    	while (Character.isWhitespace(c)) advance();
    }
    
    
    /** Look for a valid node name and return it. */
    String getNodeName() throws SyntaxException, IOException  {
    	
    	String s = "";
    	
    	skipwhite(); checkEOF();
    	
    	if (!Character.isUnicodeIdentifierStart(c)) 
    		throw new SyntaxException(p, "node name cannot start with '" + (char)c + "'");
    	
    	do { // add to result until we get something that is not part of a node name
    		s = s + (char)c; advance();
    	} while (Character.isUnicodeIdentifierPart(c));
    	
    	skipwhite(); checkEOF();  // dangling node names are not allowed
 
    	return s;
    }
    
    
    /** Look for a quoted string and return it (without quotes). */
    String getQuotedString() throws SyntaxException, IOException  {
    	
    	String s = ""; boolean escape = false;
    	
    	if (c != SDA.QUOTE)  // must start with quote
    		throw new SyntaxException(p, "unexpected character '" + (char)c + "'");
    	
    	// add to result until we get the end quote or EOF, handle escaped characters  	
    	while (true) {
    		advance(); checkEOF();
    		if (!escape && c == SDA.ESCAPE) {escape = true; continue; };
    		if (escape) { s = s + (char)c; escape = false; continue; }
    		if (c == SDA.QUOTE) break;
    		s = s + (char)c;
    	}
    	
    	advance(); skipwhite(); // skip the end quote and whitespace

    	return s;
    }
    
}