# SDA Specification, version 2

- [The basics](/docs/SPECIFICATION.md#the-basics)
- [The rules](/docs/SPECIFICATION.md#the-rules)
	- [Tags](/docs/SPECIFICATION.md#tags)
	- [Content](/docs/SPECIFICATION.md#content)
	- [White-space](/docs/SPECIFICATION.md#white-space)
	- [The root of all…](/docs/SPECIFICATION.md#the-root-of-all)
- [Empty nodes](/docs/SPECIFICATION.md#empty-nodes)
- [Unsupported features](/docs/SPECIFICATION.md#unsupported-features)
	- [Attributes](/docs/SPECIFICATION.md#attributes)
	- [Explicit nil](/docs/SPECIFICATION.md#explicit-nil)
	- [Mixed content](/docs/SPECIFICATION.md#mixed-content)
	- [Namespaces](/docs/SPECIFICATION.md#namespaces)
	- [... and the rest](/docs/SPECIFICATION.md#-and-the-rest)
- [Conclusion](/docs/SPECIFICATION.md#conclusion)


## Introduction

This is the specification of the SDA syntax, version 2. Unlike the specification 
for version 1, I am going to try and keep this more to the point, and focus on 
what SDA is, rather than what it is *not* and how it relates to XML. But should 
you feel inclined towards reading my musings on these matters, you can find the 
original [here](/docs/SPECIFICATIONV1.md). 


## The basics

Consider the following:

	name "John Doe"

This constitutes about the simplest example of an SDA *node*, consisting of 
an identifier describing the nature of the content, a value enclosed in double 
quotes. As you may have guessed, the value is a name. 

Nodes with *simple* content like this are sometimes called "leaf nodes", as 
opposed to "parent nodes" which contain other nodes, like this: 

	name {
		first "John"
		last "Doe"
	}

SDA uses a block style notation to create nodes with *complex* content, and as 
such, to organise date in hierarchical form. There is (at least in theory) no 
limit to the level of nesting; nodes can contain nodes, that contain other
nodes, *ad infinitum*.

Perhaps surprisingly, nodes can have both simple and complex content, as in:

	name "johnd" {
		first "John"
		last "Doe"
	}

SDA syntax does not get more complicated than this. Of course, there are  rules 
we must adhere to in order to produce proper or *well-formed* SDA. These are the 
subject of the next section.


## The rules

For SDA to be syntactically sound, the following rules must be followed:

- Node names are case sensitive identifiers.
- Simple content is enclosed in quotes and must be escaped if applicable.
- White-space is preserved in simple content only.
- There can only be one top level node.

### Tags

Node names (or *tags*) are case sensitive tokens (so they cannot contain 
white-space). This means that the following tags

	myname "John"
	MYNAME "John"
	MyName "John"

are all distinct. In order to avoid confusion and keep your SDA readable you 
should choose a convention and stick to it.

Tags may consist only of letters, digits and underscores. They cannot start with 
a digit and must contain at least one character that is not an underscore. Also, 
non-English letters and digits (like Greek symbols) are all excluded.

### Content

Simple content is always enclosed in double quotes. Literal quotes (that are part 
of the data) must be escaped with a backslash (\), as must be a literal backslash:

	example "The \\ is called a \"backslash\" in English."

Only in simple content is white-space considered significant and preserved. This 
means that if an application was to parse SDA and subsequently render it as text, 
only white-space enclosed in quotes (including line breaks) is guaranteed to come 
out unaffected. For example:

	person {
		name    "John   Doe"
	}

might later be formatted as

	person{ name "John   Doe" }

and both would be correct representations of the same data. In fact, so would

	person{name"John   Doe"}

but this is not a recommended rendering style if some level of readability is 
desired. It might be used to minimize storage, though.

### The root of all…

The top level (or *root*) node is special in the sense that there must be 
exactly one. This means that

	first "John"
	last "Doe"

is a valid SDA fragment, but not a valid SDA *document*.


## Empty nodes

SDA nodes can be empty, in more than one way in fact. For example, this is a node 
with an empty value:

	empty ""

and so is this one, except that here we have empty complex content:

	empty {}
	
which is short-hand for the semantically equivalent

	empty "" {}

By convention, and for the sake of readbility, we shall omit the implied empty 
value for nodes with complex content only. 

So, is `empty {}` a leaf node or a parent node? Obviously, it has no child nodes,
so it must be a leaf node. On the other hand, one might argue it has an empty set 
of child nodes. Lacking a better term, we might call it a "vacant parent", which
could have complex content.


## Unsupported features

Because SDA is – well – simple, it goes without saying that several things 
are not supported.

### Attributes

In XML, attributes are simple (unstructured) name/value pairs associated with 
the start tag of an element. For example, as in:

	<temperature unit="Celsius">25</temperature>

Attributes often carry information *about* the data (that is, they are considered 
*meta* *data*). However, that distinction is largely a matter of perception, as all 
information is ultimately data to *some* application. And why would meta data 
always be unstructured?

At the end of the day there is nothing that you can do with attributes that 
you cannot do just as well with regular elements, as in:

	<temperature>
		<unit>Celsius</unit>
		<value>25</value>
	</temperature>

This is probably why there are many and passionate debates in the XML 
community about whether attributes should be used, and if so, how.

Because attributes seem to fail the necessity test, there are none in SDA, 
and the equivalent node of the temperature element would be:

	temperature {
		unit "Celsius"
		value "25"
	}

### Explicit nil

In XML, it is syntactically valid to produce elements that have no content, 
such as

	value></value>

or the shorthand notation:

	<value/>

So, what are empty elements good for, and how are they different from elements that 
are simply absent? Actually, there are quite a few differences. 

- Sometimes, empty elements are used to achieve a particular effect. For example, 
in XHTML mark-up, the \<br/> element inserts a line break, and the \<hr/> element 
displays a horizontal ruler (on visual display units anyway). So, for that purpose, 
empty is obviously not the same as absence.

- When the XML is governed by a *schema*, elements can have default or fixed 
values, which are automatically inserted by the parser when the element in 
the actual XML instance is empty. 

- For mandatory XML elements of type `string`, an empty element (which can be 
thought of as containing the empty string) is the only way to pass validation 
if no data is available.

But it gets better (or worse, depending on your point of view). When XML is used 
for data exchange, empty content sometimes *is* the data. For example, if we need 
to indicate that a value should be (re-)set to an undefined value, we require a way 
to convey that the value is explicit empty, rather than unknown (in which case we 
should omit it). 

For this reason, XML schema allows us to define so-called *nillable* elements, 
which can contain the value `nil`, like so:

	<value xsi:nil="true"></value>

or

	<value xsi:nil="true" />

Note that "nillability" (if that is even a word) overrules any limitations on 
the content model, so it applies to all types (not just strings) including 
complex types.

I think it is fair to say that the matter of empty and nil content in XML is 
less trivial than it seems. So, what about SDA?

Because SDA is intended for data exchange rather than mark-up, the use of 
empty elements for effect, does not really apply. And although we have not 
discussed anything like an SDA schema (yet), default content is not supported 
in SDA. The idea of implicit values in data exchange is a bit troubling anyway.

So, for all practical purposes, empty nodes in SDA may be considered to have 
*actual* *empty* *content*. In other words, they are the equivalent of nil elements 
in XML.

### Mixed content

The following XML element contains both elements and text nodes:

	<mixed>This is <em>one</em> example of <bold>mixed</bold> content.</mixed>

I believe that mixed content is primarily used in mark-up applications. When 
the focus is on data exchange, mixed content is not that common at all. Also, 
the SDA syntax is not really suited to accommodate it (okay, I admit that is 
the real reason). The SDA equivalent would look something like:

	mixed { "This is " em "one" " example of " bold "mixed" "content." }

This breaks the familiar named value pair syntax and introduces text nodes 
without name. In theory that could work, but for practical reasons SDA will 
not support mixed content.

### Namespaces

Although it seems unlikely that one can do without something like a namespace when 
combining content from different sources, I am not prepared to deal with this 
concept at the time of this writing. So, namespaces are not supported in SDA. And 
by the way, JSON seems to do just fine without them.

### ... and the rest

Also not supported are features like:
 
- comments.
- processing instructions.
- header information and declarations.

These are all deemed unnecessary at the time of this writing.

## Conclusion

It goes without saying that SDA is not a replacement for XML. Nevertheless, 
it allows one to structure and exchange data in a simple format that is 
convenient to read and parse, both by applications and humans. 

Most likely I will not return to this subject, but one never knows. After 
all, I have not covered SDA schema, S-Path and SDA transformation yet :)

----