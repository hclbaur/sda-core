
package examples.parser.sda;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import be.baur.sda.parser.SyntaxException;

/** 
 * A <code>Tokenizer</code> (or lexical analyzer) is associated with an 
 * input and returns {@link Token}s through the <code>getToken()</code>
 * method. When associated with the following SDA input<br><br>
 * <code>greeting { message "hello" }</code>
 * <br><br>it returns <code>Tokens</code> with type IDENTIFIER (greeting), 
 * BLOCK_START, IDENTIFIER (message), STRING (hello), BLOCK_END, and EOF.
 */
final class Tokenizer {
    
	/** block start: an opening brace. */
	static final int BLS = '{'; 
	/** block end: a closing brace. */
	static final int BLE = '}'; 
	/** quote: a double quote. */
	static final int QUO = '"'; 
	/** escape: a reverse slash. */
	static final int ESC = '\\'; 
	
    /** End of input. Returned when the end of the input is reached. */    
    static final short EOF = -1;
    /** Initial or undefined state. This type should never be returned. */
    static final short UNDEFINED = 0;
    /** Identifier string token. An quoted string without whitespace. */
    static final short IDENTIFIER = 1;
    /** Start of a block token. */
    static final short BLOCK_START = 2;
    /** End of a block token. */
    static final short BLOCK_END = 3;
    /** Quoted string token. Quoted strings may contain whitespace and 
	 * embedded quotes (which must be escaped with a reverse slash). */
    static final short STRING = 4;
    
    /** The input associated with the <code>Tokenizer</code>. */
    private Reader input;
    /** The current state that the <code>Tokenizer</code> is in. */
    private short state;
    /** The position in the input (number of characters read). */
    private long pos;
    
    /** Constructs a <code>Tokenizer</code> and assign an input. */
    Tokenizer(Reader input) {  
        this.input = input;
        state = UNDEFINED;
        pos = 0L;
    }

    /** Returns the current position (number of characters read). */
    long getPos() {
        return pos;
    }
    
    /** Returns the next <code>Token</code> from the input. */
    Token getToken() throws IOException, SyntaxException {

        if (state == BLOCK_START) {
        	short s = state; 
        	state = UNDEFINED;
        	return new Token(s);
        }
        
        String value = "";
        boolean escape = false;
        
        // read until read() returns -1 (EOF)
        for (int c = input.read(); c != -1; c = input.read()) {
            
            ++pos; // increase position
            
            if (state == IDENTIFIER) {
                
				if ( Character.isUnicodeIdentifierPart(c) ) {
                    // part of an identifier, add it to the current value
                    value += (char)c; continue;
                }				
                if ( Character.isWhitespace(c) ) {
                    // white-space ends an identifier
                    state = UNDEFINED; return new Token(IDENTIFIER, value); 
                }
                if (c == BLS) {
                    // block start ends an identifier
                    state = BLOCK_START; return new Token(IDENTIFIER, value); 
                }
                if (c == QUO) {
                    // quote ends an identifier, starts a string
                    state = STRING; return new Token(IDENTIFIER, value); 
                }
                // otherwise it must be an invalid character
                throw new SyntaxException(pos, "identifier cannot contain '" + (char)c + "'");
            }
            
            if (state == STRING) {
                
                if (c == ESC && escape == false) {
                    escape = true; continue;
                }
                if (c == QUO && escape == false) {
                    // quote ends a string
                    state = UNDEFINED; return new Token(STRING, value); 
                }
                // otherwise add it to the current value
                value += (char)c; escape = false; continue;   
            }
            
            if (state == UNDEFINED) {
               
                if (c == BLS) // start a block
                    return new Token(BLOCK_START);
                                
                if (c == BLE) // end a block
                    return new Token(BLOCK_END);
                
                if (c == QUO) {
                    // quote starts a string
                    state = STRING; continue;
                }
                if ( Character.isUnicodeIdentifierStart(c) ) {
                    // start of an identifier
                    state = IDENTIFIER; value += (char)c; continue;
                }
				if ( !Character.isWhitespace(c) ) // skip whitespace
                    throw new SyntaxException(pos, "identifier cannot start with '" + (char)c + "'");
            }
        }
        
        // if we get here, EOF was reached and state should be UNDEFINED
		
		if (state == IDENTIFIER) {
			state = UNDEFINED; return new Token(IDENTIFIER, value); 
		}
		
        if (state == STRING) 
            throw new SyntaxException(pos, "trailing or pending quote");
        else if (state != UNDEFINED) 
            throw new SyntaxException(pos, "unexpected end of input");
        
        input.close();
        return new Token(EOF);
    }
	
	@Override
    public String toString() {
        return getClass().getName() + "[state=" + state + ",pos=" + pos + "]";
    }
    
    /** For unit testing only. Make public to run. */
    static void main(String[] args) throws Exception {
    	
    	Tokenizer lexer = new Tokenizer(
    		new StringReader("greeting{ message \"hello world\" }"));
    	
        Token token;
        do {
            token = lexer.getToken();
            System.out.println("Tokenizer.main(): " + token + " at position " + lexer.getPos());
        } 
        while (token.type != Tokenizer.EOF);
    }
}
