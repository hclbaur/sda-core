package test;

public final class TestAll {

	public static void main(String[] args) throws Exception {

		System.out.println("TestParser: ");
		test.TestParser.main(args);

		System.out.println("\n\nTestNodeSet: ");
		test.TestNodeSet.main(args);

<<<<<<< HEAD
		System.out.println("\n\nExamples TestParser: ");
=======
		System.out.println("\nTestRenderer: ");
		test.TestRenderer.main(args);

		System.out.println("\nExamples TestParser: ");
>>>>>>> feature
		test.examples.TestParser.main(args);

	}
}
