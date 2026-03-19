package test;


import java.io.File;
import java.util.function.Function;

/** A convenience class with testing methods that accept Lambda expressions */
public class Test {

	private Function<String, String> function;
	private String prefix;
	private int failures;
	
	public Test(Function<String, String> function, String prefix) {
		this.function = function; this.prefix = prefix;
	}
	
	public Test(Function<String, String> function) {
		this(function, "");
	}
	
	public void s(String scenario, String str, String expected) {
		
		String result = function.apply(str);

		if (expected == null) expected = str;
		expected = prefix + expected;
		
		if (result.equals(expected)) 
			System.out.print(scenario + " ");
		else {
			System.out.println("\n" + scenario + " FAILED!");
			System.out.println("    EXPECTED: " + expected);
			System.out.println("    RETURNED: " + result);
			++failures;
		}
	}

	public void checkFailures() throws Exception {
		
		if (failures > 0)
			throw new Exception("Failed tests: " + failures);
	}
	
	// convenience method to load a resource file
	public static File getResourceFile(String name) {
		return new File(Test.class.getResource(name).getFile());
	}

}
