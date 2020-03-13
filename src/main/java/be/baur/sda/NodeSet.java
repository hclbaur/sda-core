package be.baur.sda;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.CopyOnWriteArraySet;

import be.baur.sda.parser.Parser;

/** A <code>NodeSet</code> is an collection of {@link Node} objects. 
 * It extends a CopyOnWriteArraySet and defines a convenience methods
 * to get a reference to specific member nodes by index or name.
 */
@SuppressWarnings("serial")
public final class NodeSet extends CopyOnWriteArraySet<Node> {


	/** Get a single node from a set, by an index in the range 1 .. size(). */
	Node get(int index) {

		if (this.size() < index || index < 1) return null;
		return (Node)this.toArray()[index-1];
	}


	/** Get one or more nodes with a particular name from a set.*/
	NodeSet get(String name) {

		NodeSet sub = new NodeSet();
		for (Node node : this) if (node.name.equals(name)) sub.add(node); 
		return sub;
	}


	/** Renders the set as a list of SDA elements. */
	public String toString() {

		String s = ""; 
		for (Node node : this) s = s + node.toString() + " "; 
		return s;
	}


	/** For unit testing only. 
	 */
	public static void main(String[] args) throws Exception {

		InputStream in = Parser.class.getResourceAsStream("/phonebook.sda");
		ComplexNode book = (ComplexNode) (new Parser()).Parse(new InputStreamReader(in,"UTF-8"));

		System.out.println("book: " + book);

		NodeSet contacts = book.get("contact");

		System.out.println("\n/book/contact: " + contacts);
		System.out.println("\n/book/contact[2]: " + contacts.get(2));

		NodeSet names = new NodeSet();
		for (Node contact : contacts) names.add(((ComplexNode)contact).get("name").get(1));

		System.out.println("\n/book/contact/name: " + names);
		System.out.println("\n(/book/contact/name)[2]: " + names.get(2));
		System.out.println("\n/book/contact[2]/name: " + ((ComplexNode)contacts.get(2)).get("name"));
	}
}
