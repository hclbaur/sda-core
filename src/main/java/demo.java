import java.io.File;

import be.baur.sda.SDA;

/**
 * The demo class demonstrates the usage of the SDA library to parse an SDA file
 * and extract contact information.
 */
public class demo {

	/**
	 * The main method of the demo program. It reads an SDA file specified as a
	 * command-line argument, parses it, and prints out the names and phone numbers
	 * of contacts found in the file.
	 *
	 * @param args command-line arguments, args[0] should be the path to the file
	 * @throws Exception if there is an error reading or parsing the file
	 */
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
