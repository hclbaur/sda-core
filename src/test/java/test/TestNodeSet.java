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
		ComplexNode addressbook = (ComplexNode) (new Parser()).Parse(new InputStreamReader(in,"UTF-8"));

		System.out.println("/addressbook: " + addressbook);

		NodeSet contacts = addressbook.get();

		System.out.println("/addressbook/contact: " + contacts);
		System.out.println("/addressbook/contact[2]: " + contacts.get(2));

		NodeSet firstnames = new NodeSet();
		for (Node contact : contacts)
			firstnames.add(((ComplexNode)contact).get("firstname").get(1));

		System.out.println("/addressbook/contact/firstname: " + firstnames);
		System.out.println("(/addressbook/contact/name)[2]: " + firstnames.get(2));
		NodeSet phonenumbers = ((ComplexNode)contacts.get(1)).get("phonenumber");
		System.out.println("/addressbook/contact[1]/phonenumber: " + phonenumbers);
		System.out.println("/addressbook/contact[1]/phonenumber[1]: " + phonenumbers.get(1));
	}
}
