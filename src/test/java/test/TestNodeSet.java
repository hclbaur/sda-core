package test;

import java.io.InputStream;
import java.io.InputStreamReader;

import be.baur.sda.ComplexNode;
import be.baur.sda.Node;
import be.baur.sda.NodeSet;
import be.baur.sda.SDA;

/** A <code>NodeSet</code> is an collection of {@link Node} objects. 
 * It extends a CopyOnWriteArraySet and defines a convenience methods
 * to get a reference to specific member nodes by index or name.
 */
public final class TestNodeSet {

	public static void main(String[] args) throws Exception {

		Test t = new Test(s -> {
			return s;
		});
		
		InputStream in = TestNodeSet.class.getResourceAsStream("/addressbook.sda");
		ComplexNode addressbook = (ComplexNode) SDA.parser().parse(new InputStreamReader(in,"UTF-8"));
		NodeSet contacts = addressbook.getNodes();
		NodeSet names = new NodeSet();
		NodeSet numbers = new NodeSet();
		
		contacts.forEach( n -> names.add( ((ComplexNode) n).getNodes().get("firstname").get(1)) );
		contacts.stream().flatMap(n -> ((ComplexNode) n).getNodes().get("phonenumber").stream()).forEach(n -> numbers.add(n));
		
		t.test("S01", addressbook.path(), "/addressbook");
		t.test("S02", contacts.get(1).path(), "/addressbook/contact[1]");
		t.test("S03", contacts.get(2).path(), "/addressbook/contact[2]");
		t.test("S04", names.get(1).path(), "/addressbook/contact[1]/firstname");
		t.test("S05", names.get(2).path(), "/addressbook/contact[2]/firstname");
		t.test("S06", numbers.get(1).path(), "/addressbook/contact[1]/phonenumber[1]");
		t.test("S07", numbers.get(2).path(), "/addressbook/contact[1]/phonenumber[2]");
		t.test("S08", numbers.get(3).path(), "/addressbook/contact[2]/phonenumber[1]");
		t.test("S09", numbers.get(4).path(), "/addressbook/contact[2]/phonenumber[2]");
	}
}
