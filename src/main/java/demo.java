import java.io.File;

import be.baur.sda.SDA;

public class demo {

	public static void main(String[] args) throws Exception {
		
		var root = SDA.parse(new File(args[0]));
		
		if (! root.isParent()) return;

		for (var contact : root.getAll("contact")) {
			
			var name = contact.get("firstname");
			var numbers = contact.getAll("phonenumber");
			
			System.out.println(name.getValue() + " has " + numbers.size() + " phone number(s).");
			
			int i = 0; 	for (var number : numbers) {
				System.out.println("  Number " + ++i + ": " + number.getValue());
			}
		}
	}
}
