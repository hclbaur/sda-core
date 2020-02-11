package be.baur.sda;

import java.io.StringReader;
import java.util.concurrent.CopyOnWriteArraySet;

import be.baur.sda.parser.Parser;

/** A <code>NodeSet</code> represents an collection of {@link Node} objects. 
 * Amongst others, it is used in a {@link ComplexNode} to hold child nodes.
 * It extends a CopyOnWriteArraySet but I doubt that's a good choice.
 */
@SuppressWarnings("serial")
public final class NodeSet extends CopyOnWriteArraySet<Node> {


	/** Get a node from a set, by an index in the range 1 .. size(). */
	Node get(int index) {
		
		if (this.size() < index || index < 1) return null;
		return (Node)this.toArray()[index-1];
	}
	
	
	/** Get a node from a set, by its node name. */
	Node get(String name) {
		for (Node node : this) if (node.name.equals(name)) return node; return null;
	}
	
	
	/** Get all nodes with a particular name from a set, obviously returns a subset. */
	NodeSet subset(String name) {

		NodeSet sub = new NodeSet();
		for (Node node : this) if (node.name.equals(name)) sub.add(node); return sub;
	}

	
	/** Renders the set as a list of SDA elements. */
	public String render() {
		String s = ""; for (Node node : this) s = s + node.render() + " "; return s;
	}
	
	
	/** Returns a concatenation of the string value of all nodes (not very useful). */
	public String toString() {
		String s = ""; for (Node node : this) s = s + node; return s;
	}


	/** For unit testing only. 
	 */
	public static void main(String[] args) throws Exception {
	
	    String p1 = "point{ x \"1\" y \"2\" }";
		String p2 = "point{ x \"3\" y \"4\" }";
		String p3 = "point{ x \"5\" y \"6\" }";
		String p4 = "point{ x \"7\" y \"8\" }";
		String v1 = "vector{ " + p1 + p2 + " }";
		String v2 = "vector{ " + p3 + p4 + " }";
		String vs = "set{ " + v1 + v2 + " }";
	    
		Parser parser = new Parser();
		ComplexNode ns = (ComplexNode) parser.Parse(new StringReader(vs)) ;
		
	    System.out.println("NodeSet.main():");
		System.out.println("set : " + ns.render());
	
		NodeSet vectors = ns.children().subset("vector");
		System.out.println("\n/set/vector   : " + vectors.render());
		System.out.println("/set/vector[2]: " + vectors.get(2).render());	
		
		NodeSet points = vectors.subset("point");
		System.out.println("\nvector/point      : " + points);
		System.out.println("vector/point[2]   : " + vectors.subset("point").get(2));
		System.out.println("(vector/point)[2] : " + points.get(2));
		System.out.println("vector[2]/point   : " + ((ComplexNode) vectors.get(2)).children().get("point").render());
		System.out.println("vector[2]/point[2]: " + ((ComplexNode) vectors.get(2)).children().subset("point").get(2).render());		
	}
}
