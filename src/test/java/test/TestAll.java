package test;

public final class TestAll {

	public static void main(String[] args) throws Exception {

		System.out.println("TestParser: ");
		test.TestParser.main(args);

		System.out.println("\n\nTestNodeSet: ");
		test.TestNodeSet.main(args);

		System.out.println("\n\nExamples TestParser: ");
		test.examples.TestParser.main(args);

	}
}
