package test;


import java.io.File;
import java.util.function.Function;

/** A convenience class with testing methods that accept Lambda expressions */
public class Test {

	Function<String, String> strfun;
	String prefix;
	
	public Test(Function<String, String> strfun, String prefix) {
		this.strfun = strfun; this.prefix = prefix;
	}
	
	public Test(Function<String, String> strfun) {
		this(strfun, "");
	}
	
	public void ts1(String scenario, String str, String expected) {
		
		String result = strfun.apply(str);

		if (expected == null) expected = str;
		expected = prefix + expected;
		
		if (result.equals(expected)) 
			System.out.print(scenario + " ");
		else {
			System.out.println("\n" + scenario + " FAILED!");
			System.out.println("    EXPECTED: " + expected);
			System.out.println("    RETURNED: " + result);
		}
	}

	
	// convenience method to load a resource file
	public static File getResourceFile(String name) {
		return new File(Test.class.getResource(name).getFile());
	}

}
