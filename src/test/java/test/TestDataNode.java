package test;

import java.util.ArrayList;
import java.util.List;

import be.baur.sda.DataNode;
import be.baur.sda.SDA;

public final class TestDataNode {

	public static void main(String[] args) throws Exception {

		Test t = new Test(s -> {
			return s;
		});

		DataNode book = SDA.parse(Test.getResourceFile("/addressbook.sda"));
		
		var names = new ArrayList<DataNode>();
		List<DataNode> contacts = book.getAll("contact");
		
		contacts.forEach(n -> names.add( n.get("firstname") ));
		var numbers = book.find(n -> n.getName().equals("phonenumber"));
		
		t.s("S01", book.path(), "/addressbook");
		t.s("S02", contacts.get(0).getValue(), "1");
		t.s("S03", contacts.get(0).path(), "/addressbook/contact[1]");
		t.s("S04", contacts.get(1).getValue(), "2");
		t.s("S05", contacts.get(1).path(), "/addressbook/contact[2]");
		t.s("S06", names.get(0).path(), "/addressbook/contact[1]/firstname");
		t.s("S07", names.get(1).path(), "/addressbook/contact[2]/firstname");
		t.s("S08", numbers.get(0).path(), "/addressbook/contact[1]/phonenumber[1]");
		t.s("S09", numbers.get(1).path(), "/addressbook/contact[1]/phonenumber[2]");
		t.s("S10", numbers.get(2).path(), "/addressbook/contact[2]/phonenumber[1]");
		t.s("S11", numbers.get(3).path(), "/addressbook/contact[2]/phonenumber[2]");
		
		t.s("S12", String.valueOf(numbers.remove(null)), "false");
		t.s("S13", String.valueOf(numbers.remove(book)), "false");
		t.s("S14", String.valueOf(numbers.remove(3)), "phonenumber \"06-44444444\"");
		DataNode first = (DataNode) numbers.get(0);
		t.s("S15", String.valueOf(numbers.remove(first)), "true");
		t.s("S16", String.valueOf(numbers.size()), "2"); // nodes 0 and 3 were removed
		t.s("S17", String.valueOf(numbers.add(first)), "true"); // re-add node 0 at end
		t.s("S18", String.valueOf(contacts.get(0).add(first)), "false"); // already a child
		
		t.checkFailures();
	}
}
