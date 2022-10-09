import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;

import be.baur.sda.Node;
import be.baur.sda.NodeSet;
import be.baur.sda.SDA;

public class demo {

	public static void main(String[] args) throws IOException, ParseException  {
		
		FileReader fin = new FileReader(args[0]);
		Node root = SDA.parser().parse(fin);
		
		if (! root.isParent()) return;
		
		for (Node contact : root.getNodes().find("contact").find(n -> n.isParent())) {
			
			Node name = contact.getNodes().get("firstname");
			NodeSet numbers = contact.getNodes().find("phonenumber");
			
			System.out.println(name.getValue() + " has " + numbers.size() + " phone number(s).");
			
			int i = 0; 	for (Node number : numbers) {
				System.out.println("  Number " + ++i + ": " + number.getValue());
			}
		}
	}
}
