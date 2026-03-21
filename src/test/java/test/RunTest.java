package test;

import junit.framework.TestCase;

public final class RunTest extends TestCase {

    public void testAll() throws Exception  {
        RunTest.main(new String[]{});
    }
    
	public static void main(String[] args) throws Exception {

		System.out.print("TestSDAFormat : ");
		test.TestSDAFormat.main(args);

		System.out.print("\nTestNodeList  : ");
		test.TestDataNode.main(args);

		System.out.print("\nTestSDAParser : ");
		test.TestSDAParser.main(args);

//		System.out.print("\nTestSDAParserAlt: ");
//		test.TestSDAParserAlt.main(args);
		
	}
}
