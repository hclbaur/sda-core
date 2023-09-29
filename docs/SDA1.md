# SDA V1

<pre>
    <b>This is the documentation for SDA version 1, kept for sentimental reasons only.</b>
</pre>

- [The basics](/docs/SDA1.md#the-basics)
- [The rules](/docs/SDA1.md#the-rules)
	- [Tags](/docs/SDA1.md#tags)
	- [Content](/docs/SDA1.md#content)
	- [White-space](/docs/SDA1.md#white-space)
	- [The root of all…](/docs/SDA1.md#the-root-of-all)
- [Empty nodes](/docs/SDA1.md#empty-nodes)
- [Unsupported features](/docs/SDA1.md#unsupported-features)
	- [Attributes](/docs/SDA1.md#attributes)
	- [Explicit nil](/docs/SDA1.md#explicit-nil)
	- [Mixed content](/docs/SDA1.md#mixed-content)
	- [Namespaces](/docs/SDA1.md#namespaces)
	- [... and the rest](/docs/SDA1.md#-and-the-rest)
- [Conclusion](/docs/SDA1.md#conclusion)


## The basics

Consider the following:

	name "John Doe"

This constitutes about the simplest example of an SDA *node*. If you are at 
least somewhat familiar with XML, you will recognize this as the equivalent 
of the following element[^1]:

	<name>John Doe</name>

[^1]: In XML, elements are a specific type of node. There are also comment 
nodes, text nodes, attribute nodes, processing instructions, etc. In SDA 
there is only nodes (or elements if you like, but I will call them nodes).

In both, the `name` tag describes the nature of the data, in this case, a 
name. As you can see, SDA does not have start and end tags to enclose 
content: it uses double quotes instead.

Let's take this a little further. In XML, elements can contain other elements:

	<name>
		<first>John</first>
		<last>Doe</last>
	</name>

This is used to create a hierarchy in data. Sure enough, SDA supports this, 
but it uses a block style notation to differentiate between *complex* nodes 
(with node content) like `name` and nodes with *simple* content, like `first` 
and `last`:

	name {
		first "John"
		last "Doe"
	}

By now you must have guessed I have a background in programming. I cannot 
help myself; this syntax appeals so much more to me than XML, it is almost 
alarming.

SDA syntax does not get more complicated than this. Of course, there are some 
rules that we adhere to in order to produce proper (or *well-formed* if you 
like) SDA. These are the subject of the next section.

## The rules

For SDA to be syntactically sound, the following rules must be followed:

- Node names (tags) are case sensitive identifiers.
- Simple content is enclosed in double quotes.
- Complex content is enclosed in matching braces.
- White-space is preserved in simple content only.
- There is only one top level node.

### Tags

Node names (or *tags*) are case sensitive. This means that the following

	myname "John"
	MYNAME "John"
	MyName "John"

are all distinct nodes. In order to avoid confusion and keep your SDA as readable 
as possible, you should choose a convention and stick to it, whether it be lower, 
upper, sentence or camel case.

Tags are like the *identifiers* in most programming languages. More specific, they 
may consist only of letters, digits and underscores. They cannot start with a digit 
and must contain at least one character that's not an underscore. And finally, 
non-English letters and digits (like Greek symbols) are all excluded.

### Content

Simple content is always enclosed in double quotes. Literal quotes (that are 
*part* of the data) must be escaped with a backslash (\), as must be the 
literal backslash itself:

	example "The \\ is called a \"backslash\" in English."

Complex content is enclosed between left and right curly braces. There is (in 
theory) no limit to the level of nesting, but take care to properly match up 
all braces:

	Top {
		Lower {
			AndLower { ... }
		}
	}

### White-space

Only in simple content is white-space considered significant and preserved. 
This means that if an application was to parse SDA and subsequently render it 
as text, only white-space enclosed in quotes (including line breaks) is 
guaranteed to come out unaffected. For example:

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

The top level or root[^2] node is special in the sense that there must be 
exactly one. This means that

	given_name "John"
	surname "Doe"

is a valid SDA fragment, but in order to make it a valid SDA *document* it 
should be hosted by a complex node, like so:

	full_name { given_name "John" surname "Doe" } 

[^2]: Again, there is a difference between XML and SDA terminology. In XML, 
the root node is an "invisible" node containing the entire document (and the 
root element). In SDA, the root node is just the top-level node that holds 
all other ones.

## Empty nodes

There is a lot that can be said about empty nodes, and I will return to the 
subject in the next section. But the bottom line is that SDA supports 
"emptiness", which means that both

	result ""

and

	result {}

are perfectly valid. Nevertheless, I feel obliged to point out that you 
should not clutter your SDA with empty nodes just because you can (if you 
happen to have no data for a particular node). 

Rather, consider omitting them altogether (unless it is a root node, of course). 
There is (at least) two good reasons for this:

- Empty nodes are not in the interest of conserving resources and readability.
- They place an additional burden on the consumer (recipient) of the data.

To understand the latter, you should realize that data exchange in which empty 
content is actually *meaningful* is not that common. Usually, empty nodes will have 
to be ignored – and as a result the application needs to check not only for 
presence but also for *non-emptiness*. This is easily overlooked and may cause 
problems.

This is no way specific to SDA. It is my experience that empty content is 
heavily overused in real life XML-enabled applications. After all, *not* 
creating elements often takes additional effort. If – for instance - content 
is created by an application using operations that return a string value, an 
empty element might be inserted whenever an empty string is returned.

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