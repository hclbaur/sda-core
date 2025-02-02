import java.io.File;
import java.util.List;

import be.baur.sda.DataNode;
import be.baur.sda.Node;
import be.baur.sda.SDA;

public class demo {

	public static void main(String[] args) throws Exception {
		
		Node root = SDA.parse(new File(args[0]));
		
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
