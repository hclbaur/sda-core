# SDA Release Notes

ISSUES/IDEAS
- change underlying collection of NodeSet?

2021-08-01 v1.6.0:
- Added: NodeSet.get(<predicate>).
- Added: ComplexNode.getNodes().
- Removed NodeSet.from().
- Removed public access to ComplexNode.nodes.

2021-04-27 v1.5.1:
- Deprecated NodeSet.from() in favor of NodeSet.of().

2021-03-24 v1.5.0 (hardening release):
- Added: NodeSet.from().
- Override of NodeSet methods to maintain parent-child integrity.
- Encapsulation of most core class fields. Added validation of node names.
- Refactored package hierarchy, added Parser/Formatter interfaces.

2021-03-02 v1.4.2:
- Added: Node.getName() and setName().
- Added: NodeSet.find() and improved Node.path().

2020-10-06 v1.4.1:
- Added: Node.getRoot().
- Added: Node.path() and SDA.escape().
- SyntaxException now extends ParseException.

2020-05-11 v1.4.0:
- Added: NodeSet.get(<class>), override add().
- Removed Render from SDA class.
- Renamed Parse/Render methods to parse/render.

2020-04-08 v1.3.0:
- Lots of rework, added test packages and renderer.

2020-02-10 v1.2.1:
- Moved the parser(s) to sub-packages.
- Removed render() methods and (code) cleanup.

2016 v1.2.0:
- Renamed package namespace to baur.be.
- Added position to SyntaxException.
- SimpleNode: removed getValue() added render().
- SDA: replaced fancy parser with a simpler one.
- Removed explicit Render class.

2016 v1.1.2:
- Parse and Render in a static utility class (SDA).

2008 v1.1.1:
- NodeSet extends CopyOnWriteArraySet<Node> to hold children of ComplexNode, replaces Spath.
- Render now writes to a stream instead of a string.
- PoC implementation of Spath methods to support nodeset selection.

2008 v1.1.0:
- Render now writes to a stream instead of a string.
- PoC implementation of Spath methods to support nodeset selection.
- Parse: eliminates unnamed root node (introduced by 1.0.0).

2008 v1.0.2:
- Node: revert back to 1.0.0 (parent is a node).
- Parse: revert back to 1.0.0 (return a node).
- ComplexNode: returns Iterator to children (wise?).
- Spath: first attempt at Spatch methods.

2008 v1.0.1:
- Node: a parent is by definition a complex node (forward declaration).
- Parse: always returns a complex node (implies that root nodes are always of complex type).

2008 v1.0.0:
- Parse: introduces an unnamed root node to hold the root element (changes rendering code).
