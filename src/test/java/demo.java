import java.io.FileReader;
import java.io.IOException;

import be.baur.sda.ComplexNode;
import be.baur.sda.Node;
import be.baur.sda.NodeSet;
import be.baur.sda.SimpleNode;
import be.baur.sda.serialization.SDAParser;
import be.baur.sda.serialization.SyntaxException;

public class demo {

	public static void main(String[] args) throws SyntaxException, IOException {
		
		FileReader fin = new FileReader(args[0]);
		ComplexNode root = (ComplexNode)(new SDAParser()).parse(fin);

		for (Node c : root.getNodes().get("contact")) {
			
			ComplexNode contact = (ComplexNode) c;
			SimpleNode name = (SimpleNode) contact.getNodes().get("firstname").get(1);
			NodeSet numbers = contact.getNodes().get("phonenumber");
			
			System.out.println(name.getValue() + " has " + numbers.size() + " phone number(s).");
			
			int i = 0; 	for (Node n : numbers) {
				System.out.println("  Number " + ++i + ": " + ((SimpleNode) n).getValue());
			}
		}
		
		
//		A more defensive version	
//		FileReader fin = new FileReader(args[0]);
//		Node root = (new SDAParser()).parse(fin);
//		
//		if (! (root instanceof ComplexNode)) return;
//		NodeSet contacts = ((ComplexNode) root).getNodes().get("contact").get(ComplexNode.class);
//		
//		for (Node c : contacts) {
//			
//			ComplexNode contact = (ComplexNode) c;
//			SimpleNode name = (SimpleNode) contact.getNodes().get("firstname").get(SimpleNode.class).get(1);
//			NodeSet numbers = contact.getNodes().get("phonenumber").get(SimpleNode.class);
//			
//			System.out.println((name == null ? "An unnamed contact" : name.getValue()) 
//					+ " has " + numbers.size() + " phone number(s).");
//			
//			int i = 0; for (Node n : numbers) {
//				System.out.println("  Number " + ++i + ": " + ((SimpleNode) n).getValue());
//			}
//			
//		}

	}

}
