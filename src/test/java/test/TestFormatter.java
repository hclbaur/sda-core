package test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import be.baur.sda.DataNode;
import be.baur.sda.SDA;
import be.baur.sda.serialization.SDAFormatter;

public final class TestFormatter {
	
	private static SDAFormatter formatter = new SDAFormatter();
	private static SDAFormatter formatter4 = new SDAFormatter(4);
	
	public static void main(String[] args) throws Exception {
		
		Test t = new Test(s -> {
			return s;
		});
		

        final DataNode node = SDA.parse(new StringReader("node\"1\"{node2{empty1\"\"empty2{}empty\"3\"{}}}"));
		String str = formatter4.format(node);
		t.ts1("S01", str, 
				"node \"1\" {\n" + 
				"    node2 {\n" + 
				"        empty1 \"\"\n" +
				"        empty2 { }\n" + 
				"        empty \"3\" { }\n" + 
				"    }\n" + 
				"}\n");
		
		InputStream in = TestFormatter.class.getResourceAsStream("/addressbook.sda");
		final DataNode book = SDA.parse(new InputStreamReader(in,"UTF-8"));
		str = formatter.format(book);
		t.ts1("S02", str, "addressbook {\n" + 
				"	contact \"1\" {\n" + 
				"		firstname \"Alice\"\n" + 
				"		phonenumber \"06-11111111\"\n" + 
				"		phonenumber \"06-22222222\"\n" + 
				"	}\n" + 
				"	contact \"2\" {\n" + 
				"		firstname \"Bob\"\n" + 
				"		phonenumber \"06-33333333\"\n" + 
				"		phonenumber \"06-44444444\"\n" + 
				"	}\n" + 
				"}\n"
		);
	
		// test performance
		
//		UnitTestPerformance<Node> perf = new UnitTestPerformance<Node>(
//			n -> { n.toString(); }   // test toString
//		);
//		perf.run("\nP01", book, 25000, 25);

//		UnitTestPerformance<Node> perf = new UnitTestPerformance<Node>(n -> {
//			try {
//				formatter.format(book); // test formatter
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		});
//		perf.run("\nP02", node, 25000, 25);

	}
}
