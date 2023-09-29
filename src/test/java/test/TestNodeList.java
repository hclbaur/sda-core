package test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import be.baur.sda.Node;
import be.baur.sda.SDA;

public final class TestNodeList {

	public static void main(String[] args) throws Exception {

		Test t = new Test(s -> {
			return s;
		});
		
		InputStream in = TestNodeList.class.getResourceAsStream("/addressbook.sda");
		Node addressbook = SDA.parser().parse(new InputStreamReader(in,"UTF-8"));
		
		List<Node> contacts = addressbook.find("contact");
		List<Node> names = new ArrayList<Node>();
		List<Node> numbers = new ArrayList<Node>();
		
		contacts.forEach(n -> names.add( n.get("firstname") ));
		contacts.stream().flatMap(n -> n.find("phonenumber").stream()).forEach(n -> numbers.add(n));
		
		t.test("S01", addressbook.path(), "/addressbook");
		t.test("S02", contacts.get(0).getValue(), "1");
		t.test("S03", contacts.get(0).path(), "/addressbook/contact[1]");
		t.test("S04", contacts.get(1).getValue(), "2");
		t.test("S05", contacts.get(1).path(), "/addressbook/contact[2]");
		t.test("S06", names.get(0).path(), "/addressbook/contact[1]/firstname");
		t.test("S07", names.get(1).path(), "/addressbook/contact[2]/firstname");
		t.test("S08", numbers.get(0).path(), "/addressbook/contact[1]/phonenumber[1]");
		t.test("S09", numbers.get(1).path(), "/addressbook/contact[1]/phonenumber[2]");
		t.test("S10", numbers.get(2).path(), "/addressbook/contact[2]/phonenumber[1]");
		t.test("S11", numbers.get(3).path(), "/addressbook/contact[2]/phonenumber[2]");
	}
}
