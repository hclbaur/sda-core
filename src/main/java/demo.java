import java.io.FileReader;
import java.util.List;

import be.baur.sda.DataNode;
import be.baur.sda.Node;
import be.baur.sda.SDA;

public class demo {

	public static void main(String[] args) throws Exception {
		
		FileReader fin = new FileReader(args[0]);
		Node root = SDA.parse(fin);
		
		if (! root.isParent()) return;

		for (Node contact : root.find("contact")) {
			
			DataNode name = contact.get("firstname");
			List<DataNode> numbers = contact.find("phonenumber");
			
			System.out.println(name.getValue() + " has " + numbers.size() + " phone number(s).");
			
			int i = 0; 	for (DataNode number : numbers) {
				System.out.println("  Number " + ++i + ": " + number.getValue());
			}
		}
	}
}
