# SDA Tutorial

- [Introduction](/docs/TUTORIAL.md#introduction)
- [The basics](/docs/TUTORIAL.md#the-basics)
- [The rules](/docs/TUTORIAL.md#the-rules)
	- [Tags](/docs/TUTORIAL.md#tags)
	- [Content](/docs/TUTORIAL.md#content)
	- [The root of all…](/docs/TUTORIAL.md#the-root-of-all)
	- [Empty nodes](/docs/TUTORIAL.md#empty-nodes)
- [Unsupported features](/docs/TUTORIAL.md#unsupported-features)
- [Conclusion](/docs/TUTORIAL.md#conclusion)


## Introduction

This tutorial will teach you the SDA format, version 2. Unlike the tutorial 
for version 1, I will keep this one more to the point, and focus on what SDA is, 
rather than what it is not, or how it relates to XML. But should you feel so
inclined towards reading my musings on these matters, you can find the original 
[here](/docs/SDA1.md). 


## The basics

Consider the following:

	name "John Doe"

This constitutes about the simplest example of an SDA *node*, consisting of an identifier (or tag) and a value enclosed in double quotes. As you may have guessed, the value in this 
example is a name. 

Nodes with *simple* content like this are usually called "leaf nodes", as opposed to "parent nodes" which contain other nodes, like so: 

	name {
		first "John"
		last "Doe"
	}

SDA uses a block style notation to create nodes with *complex* content, and as such, to organise date in hierarchical form. There is (at least in theory) no limit to the level of nesting; nodes can contain nodes, that can contain other nodes, *ad infinitum*.

Perhaps surprisingly, SDA nodes can have both simple and complex content (or *mixed* content if you like) as in:

	name "johnd" {
		first "John"
		last "Doe"
	}

SDA syntax does not get more complicated than this. Of course, there are rules we must adhere to in order to produce proper or *well-formed* SDA. These are the subject of the next section.


## The rules

For SDA to be syntactically sound, the following rules must be followed:

- Node names are case sensitive identifiers.
- Simple content is enclosed in double quotes.
- Whitespace is preserved in simple content only.
- There can only be one top level node.
- Empty nodes are relevant.

### Tags

Node names (tags) are case sensitive tokens (so they cannot contain whitespace). This means that the following tags

	myname "John"
	MYNAME "John"
	MyName "John"

are all distinct. In order to avoid confusion and keep your SDA readable you should choose a convention and stick to it.

Tags may consist only of letters, digits and underscores. They cannot start with a digit and must contain at least one character that is not an underscore. Also, non-English letters and digits (like Greek symbols) are all excluded.

### Content

Simple content is *always* enclosed in double quotes. Unlike some other data formats (for example JSON) SDA is not strongly typed. Everything is essentially a `string`. In other words, the following examples are all *invalid* in SDA:

	age 54
	birthday 1968-02-28
	married false
	
Since quotes are used to delimit the values, literal quotes that are *part* of the data must be escaped with a backslash (\\), as must be a literal backslash itself:

	example "The \\ is called a \"backslash\" in English."

Only in simple content is whitespace considered significant and preserved. This means that if an application was to parse SDA and subsequently render it as text, only whitespace enclosed in quotes (including line breaks) is guaranteed to come out unaffected. For example:

	person {
		name    "John   Doe"
	}

might be formatted as

	person{ name "John   Doe" }

and both would be correct representations of the same data. In fact, so would

	person{name"John   Doe"}

but this is not a recommended rendering style if some level of readability is desired. It could be used to minimize storage, though.

### The root of all…

The top level (or *root*) node is special in the sense that there must be exactly one. This means that

	first "John"
	last "Doe"

is a valid SDA fragment, but not a valid SDA *document*.

### Empty nodes

SDA nodes can be empty, in more than one way in fact. For example, this is a node with an empty value:

	empty ""

and so is this one, except that here we have empty complex content:

	empty {}
	
which is semantically equivalent to

	empty "" {}

By convention, and for the sake of readbility, we shall omit the implied empty value for nodes with complex content only.

So, is `empty {}` a leaf node or a parent node? Obviously, it has no child nodes, so it must be a leaf node. On the other hand, one might argue it has an empty set of child nodes. Lacking a better term, we call this a "vacant parent", and it is neither a leaf nor a parent.

Recall that in SDA, all simple content is enclosed in double quotes, including the empty value. There is no equivalent of an explicit `nil` or `null` value, so for all practical purposes, empty nodes in SDA may be considered to have actual - yet empty - content. And as such they are relevant, e.g. parsers should not ignore them, so omitting a node is not the same as including an empty one.


## Unsupported features

Because SDA is – well – simple, it goes without saying that several things you are used to being available in other formats, are not supported. In particular, you will not find or be able to include

- comments or annotations
- namespaces or prefixes
- headers or declarations

These are all deemed unnecessary at the time of this writing.


## Conclusion

Obviously, SDA is not a replacement for XML, or even JSON. Nevertheless, it allows one to structure and exchange data in a format that is convenient to read and parse, both by applications and humans. It also has a companion called [SDS](https://github.com/hclbaur/sds-core), which is a schema language to formally describe SDA documents, and a transformation language, called [SDT](https://github.com/hclbaur/sdt-core).

----