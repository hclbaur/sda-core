import java.io.FileReader;
import java.io.IOException;

import be.baur.sda.Node;
import be.baur.sda.NodeSet;
import be.baur.sda.serialization.SDAParser;
import be.baur.sda.serialization.SyntaxException;

public class demo {

	public static void main(String[] args) throws SyntaxException, IOException {
		
		FileReader fin = new FileReader(args[0]);
		Node root = (new SDAParser()).parse(fin);
		
		if (! root.hasNodes()) return;
		
		for (Node contact : root.getNodes().get("contact").get(n -> n.hasNodes())) {
			
			Node name = contact.getNodes().get("firstname").get(1);
			NodeSet numbers = contact.getNodes().get("phonenumber");
			
			System.out.println(name.getValue() + " has " + numbers.size() + " phone number(s).");
			
			int i = 0; 	for (Node number : numbers) {
				System.out.println("  Number " + ++i + ": " + number.getValue());
			}
		}
	}
}
