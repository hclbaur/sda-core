# SDA Specification

The following grammar describes an SDA document in EBNF notation, following the
notation rules for XML grammar, located [here](https://www.w3.org/TR/xml/#sec-notation):

<pre>

<b>sda</b> = <b>node</b> ;

<b>node</b> = <b>tag</b> <b>ws</b>? <b>value</b> 
     | <b>tag</b> <b>ws</b>? <b>value</b>? <b>ws</b>? <b>nodeset</b> ;

<b>nodeset</b> = '{' <b>ws</b>? (<b>node</b> <b>ws</b>?)* '}'

<b>tag</b> = ('_'* <b>letter</b> | '_' <b>digit</b>) (<b>letter</b> | <b>digit</b> | '_')* ;

tagstart = '_' | letter
tag = stagstart (tagstart | digit)*
CORRECT?

<b>value</b> = '"' ( [^"\] | '\"' | '\\' )* '"' ;

<b>letter</b> = [a-zA-Z] ;

<b>digit</b> = [0-9] ;

<b>ws</b> = ? white space characters ?

</pre>

### In words

An SDA document consists of a single (root) node, which is composed of a name 
tag and a value, or a tag with an optional value followed by a set of zero or 
more child nodes - all of which may be separated by whitespace.

A tag consists of letters, digits and underscores. It cannot start with a digit 
and must contain at least one character that is not an underscore.

A value is a character string enclosed in double quotes. A backslash must be 
used to escape any double quotes or backslashes that are part of the value.
