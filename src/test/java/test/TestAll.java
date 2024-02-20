package test;

public final class TestAll {

	public static void main(String[] args) throws Exception {

		System.out.print("TestSDAFormatter: ");
		test.TestFormatter.main(args);

		System.out.print("\nTestNodeList: ");
		test.TestNodeList.main(args);

		System.out.print("\nTestSDAParser: ");
		test.TestSDAParser.main(args);

//		System.out.print("\nTestSDAParserAlt: ");
//		test.TestSDAParserAlt.main(args);
		
	}
}
