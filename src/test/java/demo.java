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

		for (Node contact : root.getNodes().get("contact")) {
			
			Node name = contact.getNodes().get("firstname").get(1);
			NodeSet numbers = contact.getNodes().get("phonenumber");
			
			System.out.println(name.getValue() + " has " + numbers.size() + " phone number(s).");
			
			int i = 0; 	for (Node number : numbers) {
				System.out.println("  Number " + ++i + ": " + number.getValue());
			}
		}
		
		
//		A (somewhat) more defensive version	
//	
//		if (! root.isParent()) return;
//		NodeSet contacts = root.getNodes().get("contact").get(n -> n.isParent());
//		
//		for (Node contact : contacts) {
//			
//			Node name = contact.getNodes().get("firstname").get(n -> !n.isParent()).get(1);
//			NodeSet numbers = contact.getNodes().get("phonenumber").get(n -> !n.isParent());
//			
//			System.out.println((name == null ? "An unnamed contact" : name.getValue()) 
//					+ " has " + numbers.size() + " phone number(s).");
//			
//			int i = 0; for (Node number : numbers) {
//				System.out.println("  Number " + ++i + ": " + number.getValue());
//			}	
//		}

	}

}
