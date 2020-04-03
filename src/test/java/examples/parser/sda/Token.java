
package examples.parser.sda;

/** A generic token, returned by a {@link Tokenizer}. Tokens are fairly
 * simple objects that do not evolve once they have been created, so they
 * have static <code>type</code> and <code>value</code> fields, and no
 * methods other than constructors.
 */
final class Token {
    
    final short type;
    final String value;

    /** Constructs a <code>Token</code> without associated value. */
    Token(short type) {
        this.type = type;
        this.value = null;
    }
    
    /** Constructs a <code>Token</code> with an associated value. */
    Token(short type, String value) {
        this.type = type;
        this.value = value;
    }
    
	@Override
    public String toString() {
        return getClass().getName() + "[type=" + type + ",value=" + value + "]";
    }
}

