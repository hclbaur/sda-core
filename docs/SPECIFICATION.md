# SDA Specification

The following grammar describes an SDA document in EBNF notatation:

<pre>

<b>sda</b> = <b>node</b> ;

<b>node</b> = ( <b>nodename</b>, [<b>ws</b>], <b>nodevalue</b> )
     | ( <b>nodename</b>, [<b>ws</b>], [<b>nodevalue</b>], [<b>ws</b>], <b>nodeset</b> ) ;

<b>nodeset</b> = '{', [<b>ws</b>], {<b>node</b>}, [<b>ws</b>], '}'

<b>nodename</b> = {'_'}, <b>letter</b>, {<b>letter</b> | <b>digit</b> | '_'} ;

<b>letter</b> = 'A' | 'B' | 'C' | 'D' | 'E' | 'F' | 'G' | 'H' | 'I' | 'J'
       | 'K' | 'L' | 'M' | 'N' | 'O' | 'P' | 'Q' | 'R' | 'S' | 'T'
	   | 'U' | 'V' | 'W' | 'X' | 'Y' | 'Z'
	   | 'a' | 'b' | 'c' | 'd' | 'e' | 'f' | 'g' | 'h' | 'i' | 'j'
	   | 'k' | 'l' | 'm' | 'n' | 'o' | 'p' | 'q' | 'r' | 's' | 't'
	   | 'u' | 'v' | 'w' | 'x' | 'y' | 'z' ;

<b>digit</b> = '0' | '1' | '2' | '3' | '4' | '5' | '6' | '7' | '8' | '9' ;

<b>nodevalue</b> = '"', { ( <b>char</b> - '"' - '\' ) | '\"' | '\\' }, '"' ;

<b>char</b> = ? any character ?

<b>ws</b> = ? white space ?

</pre>

In words, an SDA document consists of a single (root) node, which is composed of a name and a value, or a name with an optional value followed by a set of zero or more child nodes - all of which may be separated by whitespace.
