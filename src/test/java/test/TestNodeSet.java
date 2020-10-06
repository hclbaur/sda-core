package test;

import java.io.InputStream;
import java.io.InputStreamReader;

import be.baur.sda.ComplexNode;
import be.baur.sda.Node;
import be.baur.sda.NodeSet;
import be.baur.sda.parse.Parser;

/** A <code>NodeSet</code> is an collection of {@link Node} objects. 
 * It extends a CopyOnWriteArraySet and defines a convenience methods
 * to get a reference to specific member nodes by index or name.
 */
public final class TestNodeSet {

	public static void main(String[] args) throws Exception {

		InputStream in = Parser.class.getResourceAsStream("/addressbook.sda");
		ComplexNode addressbook = (ComplexNode) (new Parser()).parse(new InputStreamReader(in,"UTF-8"));

		System.out.println(addressbook.path() + ": " + addressbook);

		NodeSet contacts = addressbook.get();
		Node contact2 = contacts.get(2);
		
		System.out.println(contact2.path() + "[2]: " + contact2);

		NodeSet firstnames = new NodeSet();
		for (Node contact : contacts)
			firstnames.add(((ComplexNode)contact).get("firstname").get(1));
		Node firstname2 = firstnames.get(2);
		
		System.out.println(firstname2.path() + "[2]: " + firstname2);
		
		NodeSet phonenumbers = ((ComplexNode)contacts.get(1)).get("phonenumber");
		Node phonenumber1 = phonenumbers.get(1);
		
		System.out.println(phonenumber1.path() + "[1]: " + phonenumber1);
	}
}
