package test;

public final class TestAll {

	public static void main(String[] args) throws Exception {

		System.out.println("TestRenderer: ");
		test.TestRenderer.main(args);
		
		System.out.print("TestParser: ");
		test.TestParser.main(args);

		System.out.print("\nTestNodeSet: ");
		test.TestNodeSet.main(args);

		System.out.print("\nExamples TestParser: ");
		test.examples.TestParser.main(args);
	}
}
