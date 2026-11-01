package test;

import java.util.ArrayList;
import java.util.List;

import be.baur.sda.DataNode;
import be.baur.sda.SDA;

public final class TestDataNode {

	public static void main(String[] args) throws Exception {

		Test t1 = new Test(s -> {
				return s;
		});
		
		Test t2 = new Test((o,s) -> {
			try {
				return ((DataNode)o).select(s).toString();
			} catch (Exception e) { return e.getMessage(); }
		});

		DataNode book = SDA.parse(Test.getResourceFile("/addressbook.sda"));
		
		var names = new ArrayList<DataNode>();
		List<DataNode> contacts = book.getAll("contact");
		
		contacts.forEach(n -> names.add( n.get("firstname") ));
		var numbers = book.find(n -> n.getName().equals("phonenumber"));
		
		t1.s("S01", book.path(), "/addressbook");
		t1.s("S02", contacts.get(0).getValue(), "1");
		t1.s("S03", contacts.get(0).path(), "/addressbook/contact[1]");
		t1.s("S04", contacts.get(1).getValue(), "2");
		t1.s("S05", contacts.get(1).path(), "/addressbook/contact[2]");
		t1.s("S06", names.get(0).path(), "/addressbook/contact[1]/firstname");
		t1.s("S07", names.get(1).path(), "/addressbook/contact[2]/firstname");
		t1.s("S08", numbers.get(0).path(), "/addressbook/contact[1]/phonenumber[1]");
		t1.s("S09", numbers.get(1).path(), "/addressbook/contact[1]/phonenumber[2]");
		t1.s("S10", numbers.get(2).path(), "/addressbook/contact[2]/phonenumber[1]");
		t1.s("S11", numbers.get(3).path(), "/addressbook/contact[2]/phonenumber[2]");
		
		t1.s("S12", String.valueOf(numbers.remove(null)), "false");
		t1.s("S13", String.valueOf(numbers.remove(book)), "false");
		t1.s("S14", String.valueOf(numbers.remove(3)), "phonenumber \"06-44444444\"");
		
		DataNode first = (DataNode) numbers.get(0);
		t1.s("S15", String.valueOf(numbers.remove(first)), "true");
		t1.s("S16", String.valueOf(numbers.size()), "2"); // nodes 0 and 3 were removed
		t1.s("S17", String.valueOf(numbers.add(first)), "true"); // re-add node 0 at end
		t1.s("S18", String.valueOf(contacts.get(0).add(first)), "false"); // already a child
		
		t1.s("S19", String.valueOf(book.select("kontact/phonenumber").size()), "0");
		t1.s("S20", String.valueOf(book.select("contact/phonenumber").size()), "4");
		t1.s("S21", String.valueOf(book.select("contact[2]/phonenumber").size()), "2");
		t1.s("S22", String.valueOf(book.select("contact/phonenumber[2]").size()), "2");
		t1.s("S23", String.valueOf(book.select("/contact").size()), "2");
		t1.s("S24", String.valueOf(book.select("*/*").size()), "6");
		t1.s("S25", String.valueOf(book.select("*").size()), "2");
		t1.s("S26", String.valueOf(book.select("/").size()), "1");
		
		t2.so("S27", book, "contact[2]/firstname", "[firstname \"Bob\"]");
		t2.so("S28", book, "contact[1]/*[1]", "[firstname \"Alice\"]");
		t2.so("F29", book, "contact]", "invalid path part: contact]");
		t2.so("F30", book, "contact[]", "invalid path part: contact[]");
		t2.so("F31", book, "contact[a]", "invalid path part: contact[a]");
		t2.so("F32", book, "contact[1]a", "invalid path part: contact[1]a");
		
		t1.checkFailures();
		t2.checkFailures();
	}
}
