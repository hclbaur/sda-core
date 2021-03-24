package test;

import java.util.Date;
import java.util.function.Consumer;
import java.util.function.Function;

/** A convenience class with testing methods that accept Lamba expressions */
public final class Test {

	private Function<String, String> strfun;
	private Consumer<String> strcon;
	
	public Test(Function<String, String> strfun) {
		this.strfun = strfun;
	}
	
	public Test(Consumer<String> strcon) {
		this.strcon = strcon;
	}

	
	public void test(String scenario, String input, String expected) {
		
		String result = strfun.apply(input);

		if (expected == null) expected = input;
		if (result.equals(expected)) 
			System.out.print(scenario + " ");
		else {
			System.out.println("\n" + scenario + " FAILED!");
			System.out.println("    EXPECTED: " + expected);
			System.out.println("    RETURNED: " + result);
		}
	}

	
	public void testError(String scenario, String input, String expected) {
		
		try { 
			strfun.apply(input);
		}
		catch (Exception e) { 
			if (e.getMessage().equals(expected)) 
				System.out.print(scenario + " ");
			else {
				System.out.println("\n" + scenario + " FAILED!");
				System.out.println("    EXPECTED: " + expected);
				System.out.println("    RETURNED: " + e.getMessage());
			}
			return;
		}
		System.out.println(scenario + " FAILED - exception expected");
	}

	
	public void testPerf(String scenario, String input, long iterations, long runs) {

		System.out.print(scenario);
		long total = 0, r = runs;
		while (r > 0) {
			
			long i = iterations;
			long start = new Date().getTime();
			while (i > 0) {
				strcon.accept(input); --i;
			}
			long duration = new Date().getTime() - start;
			System.out.print(" " + duration);
			total += duration; --r;
		}
		System.out.print(" avg: " + (total/runs));
	}

}
