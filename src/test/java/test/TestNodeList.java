package test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import be.baur.sda.DataNode;
import be.baur.sda.Node;
import be.baur.sda.SDA;

public final class TestNodeList {

	public static void main(String[] args) throws Exception {

		Test t = new Test(s -> {
			return s;
		});
		

		String filename = TestNodeList.class.getResource("/addressbook.sda").getFile();
		DataNode book = SDA.parse(new File(filename));
		
		List<DataNode> contacts = book.find("contact");
		
		List<Node> names = new ArrayList<Node>();
		contacts.forEach(n -> names.add( n.get("firstname") ));
		
		//contacts.stream().flatMap(n -> n.find("phonenumber").stream()).forEach(n -> numbers.add(n));
		List<Node> numbers = book.findDescendant(n -> n.getName().equals("phonenumber"));
		
		t.ts1("S01", book.path(), "/addressbook");
		t.ts1("S02", contacts.get(0).getValue(), "1");
		t.ts1("S03", contacts.get(0).path(), "/addressbook/contact[1]");
		t.ts1("S04", contacts.get(1).getValue(), "2");
		t.ts1("S05", contacts.get(1).path(), "/addressbook/contact[2]");
		t.ts1("S06", names.get(0).path(), "/addressbook/contact[1]/firstname");
		t.ts1("S07", names.get(1).path(), "/addressbook/contact[2]/firstname");
		t.ts1("S08", numbers.get(0).path(), "/addressbook/contact[1]/phonenumber[1]");
		t.ts1("S09", numbers.get(1).path(), "/addressbook/contact[1]/phonenumber[2]");
		t.ts1("S10", numbers.get(2).path(), "/addressbook/contact[2]/phonenumber[1]");
		t.ts1("S11", numbers.get(3).path(), "/addressbook/contact[2]/phonenumber[2]");
	}
}
