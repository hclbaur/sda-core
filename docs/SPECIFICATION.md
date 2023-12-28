# SDA Specification

The following grammar describes an SDA document in EBNF notatation:

<pre>

<b>sda</b> = <b>node</b> ;

<b>node</b> = <b>tag</b>, [<b>ws</b>], <b>value</b> 
     | <b>tag</b>, [<b>ws</b>], [<b>value</b>], [<b>ws</b>], <b>nodeset</b> ;

<b>nodeset</b> = '{', [<b>ws</b>], {<b>node</b>}, [<b>ws</b>], '}'

<b>tag</b> = {'_'}, <b>letter</b>, { <b>letter</b> | <b>digit</b> | '_' } ;

<b>letter</b> = 'A' | 'B' | 'C' | 'D' | 'E' | 'F' | 'G' | 'H' | 'I' | 'J'
       | 'K' | 'L' | 'M' | 'N' | 'O' | 'P' | 'Q' | 'R' | 'S' | 'T'
	   | 'U' | 'V' | 'W' | 'X' | 'Y' | 'Z'
	   | 'a' | 'b' | 'c' | 'd' | 'e' | 'f' | 'g' | 'h' | 'i' | 'j'
	   | 'k' | 'l' | 'm' | 'n' | 'o' | 'p' | 'q' | 'r' | 's' | 't'
	   | 'u' | 'v' | 'w' | 'x' | 'y' | 'z' ;

<b>digit</b> = '0' | '1' | '2' | '3' | '4' | '5' | '6' | '7' | '8' | '9' ;

<b>value</b> = '"', { ( <b>characters</b> - '"' - '\' ) | '\"' | '\\' }, '"' ;

<b>characters</b> = ? all characters ?

<b>ws</b> = ? white space characters ?

</pre>

### In words

An SDA document consists of a single (root) node, which is composed of a name 
tag and a value, or a tag with an optional value followed by a set of zero or 
more child nodes - all of which may be separated by whitespace.

A tag is an identifier that starts with zero or more underscores and a letter, 
then followed by any number of letters, digits and/or underscores.

A value is a character string enclosed in double quotes. A backslash must be 
used to escape any double quotes or backslashes that are part of the value.
