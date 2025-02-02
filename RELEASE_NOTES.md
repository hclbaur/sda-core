# Release Notes

## [2.2.1] - 2025-??-??

In development.

- Added DataNode.copy() method.
- Added Node.findDescendant() method.
- Added default methods Parser.parse(File) and Parser.parse(String).
- Added convenience methods SDA.parse(File) and SDA.parse(String).
- Added default methods Formatter.format(File, Node) and Formatter.format(Node).
- Added convenience methods SDA.format(File, DataNode) and SDA.format(DataNode).
- Closed issue #16 (SDAFormatter throws if input node is null).
- Closed issue #17 (Add a parse(File) method and review exceptions).

## Compatibility

- Requires Java 8.

## Previous releases

### [2.2.0] - 2024-02-20

As of this release, Node is refactored into an interface (Node), an abstract 
class (AbstractNode) and a concrete class (DataNode). Also, the exception 
classes have been thrown around a bit (pun intended).

- `Removed` java.text.ParseException.
- `Renamed` SyntaxException to SDAParseException.
- `Replaced` SDA.parser() with SDA.parse().
- Added ParseException.
- Added ProcessingException.
- Added Node.get(Predicate).
- Added Result and Results.
- Closed issue #7.

### [2.1.1] - 2023-10-20

- Added Node.remove().
- Closed issues #3, #6 and #9.

### [2.1.0] - 2023-09-29

This release no longer supports the NodeSet class, which - with the benefit of 
hindsight - was not a great idea to begin with. In addition, the method to get 
child nodes will now always return an unmodifiable (and possibly empty) list, 
rather than a modifiable one or a null reference. Some additional methods have 
been added to make up for the loss of NodeSet.

- `Removed` NodeSet class, use List<Node> instead.
- `Removed` Node.getNodes(), use Node.nodes() instead.
- `Removed` Node.isComplex(), use !Node.isLeaf() instead.
- Added Node.get(String), find(String) and find(Predicate).
- Closed issues #1, #2, #4 and #5.

### [2.0.0] - 2022-10-09

This release adds full support for SDA 2. While the syntax of SDA 2
is backwards compatible with version 1, the library is definitely not.
When you are also using SDS, make sure you upgrade to a release that 
supports the SDA 2 syntax.

- Added Node.isComplex().
- Added Node NodeSet.get(String).
- `Changed` Node.addNode() to Node.add().
- `Changed` Node.hasNodes() to Node.isParent().
- `Changed` Parser.parse() to throw ParseException (was Exception).
- `Renamed` (NodeSet) NodeSet.get(String) to NodeSet.find(String).
- `Renamed` (NodeSet) NodeSet.get(Predicate) to NodeSet.find(Predicate).
- `Renamed` NodeSet.find(Node) to NodeSet.locate(Node).
- `Removed` NodeSet.get(Class<?> cls).
- `Removed` SimpleNode and ComplexNode.

### [1.6.2] - 2022-08-12

This release may be considered a "stepping stone" towards SDA 2.
When refactoring your code, note that Node.getNodes() may return null,
whereas ComplexNode.getNodes() would always return an empty NodeSet.

- `Deprecated` SimpleNode and ComplexNode.
- `Refactored` Node, which is no longer abstract.
- Added Node.hasNodes() and Node.addNode().

### [1.6.1] - 2022-06-20
- `Improved` performance of default SDA parser.
- Fixed offsets in SyntaxException.
- Added demo and documentation in markdown.
- `Removed` documentation in ODT format.

### [1.6.0] - 2021-08-01 (first public release)
- Added NodeSet.get(<predicate>).
- Added ComplexNode.getNodes().
- `Removed` NodeSet.from().
- `Removed` public access to ComplexNode.nodes.

### [1.5.1] - 2021-04-27
- Deprecated NodeSet.from() in favor of NodeSet.of().

### [1.5.0] - 2021-03-24 (hardening release)
- Added: NodeSet.from().
- Override of NodeSet methods to maintain parent-child integrity.
- Encapsulation of most core class fields. Added validation of node names.
- Refactored package hierarchy, added Parser/Formatter interfaces.

### [1.4.2]] - 2021-03-02
- Added: Node.getName() and setName().
- Added: NodeSet.find() and improved Node.path().

### [1.4.1] - 2020-10-06
- Added: Node.getRoot().
- Added: Node.path() and SDA.escape().
- SyntaxException now extends ParseException.

### [1.4.0] - 2020-05-11
- Added: NodeSet.get(<class>), override add().
- Removed Render from SDA class.
- Renamed Parse/Render methods to parse/render.

### [1.3.0] - 2020-04-08
- Lots of rework, added test packages and renderer.

### [1.2.1] - 2020-02-10
- Moved the parser(s) to sub-packages.
- Removed render() methods and (code) cleanup.

### [1.2.0] - 2016
- Renamed package namespace to baur.be.
- Added position to SyntaxException.
- SimpleNode: removed getValue() added render().
- SDA: replaced fancy parser with a simpler one.
- Removed explicit Render class.

### [1.1.2] - 2016
- Parse and Render in a static utility class (SDA).

### [1.1.1] - 2008
- NodeSet extends CopyOnWriteArraySet<Node> to hold children of ComplexNode, replaces Spath.
- Render now writes to a stream instead of a string.
- PoC implementation of Spath methods to support nodeset selection.

### [1.1.0] - 2008
- Render now writes to a stream instead of a string.
- PoC implementation of Spath methods to support nodeset selection.
- Parse: eliminates unnamed root node (introduced by 1.0.0).

### [1.0.2] - 2008
- Node: revert back to 1.0.0 (parent is a node).
- Parse: revert back to 1.0.0 (return a node).
- ComplexNode: returns Iterator to children (wise?).
- Spath: first attempt at Spatch methods.

### [1.0.1] - 2008
- Node: a parent is by definition a complex node (forward declaration).
- Parse: always returns a complex node (implies that root nodes are always of complex type).

### [1.0.0] - 2008
- Parse: introduces an unnamed root node to hold the root element (changes rendering code).
