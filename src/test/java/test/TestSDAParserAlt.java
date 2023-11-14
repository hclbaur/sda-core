package test;

import java.io.IOException;
import java.io.StringReader;

import be.baur.sda.Node;
import samples.parser.SDAParser;

public final class TestSDAParserAlt {

	private static SDAParser parser = new samples.parser.SDAParser();
	
	private static String samplesda = "addressbook {\r\n" + 
			"	contact {\r\n" + 
			"		firstname \"Alice\"\r\n" + 
			"		phonenumber \"06-11111111\"\r\n" + 
			"		phonenumber \"06-22222222\"\r\n" + 
			"	}\r\n" + 
			"	contact {\r\n" + 
			"		firstname \"Bob\"\r\n" + 
			"		phonenumber \"06-33333333\"\r\n" + 
			"		phonenumber \"06-44444444\"\r\n" + 
			"	}\r\n" + 
			"}";
	
	public static void main(String[] args) throws Exception {

		Node helloworld = (new SDAParser()).parse(new StringReader("greeting{message\"hello world\"}"));
		System.out.println(helloworld);

		Test t = new Test(s -> {
			try {
				return parser.parse(new StringReader(s)).toString();
			} catch (Exception e) {
				return e.getMessage();
			}
		});

		// test valid SDA
		t.test("S01", "empty\"\"", "empty \"\"");
		t.test("S02", "  empty  \"\"  ", "empty \"\"");
		t.test("S03", "empty{}", "empty { }");
		t.test("S04", "  empty  {  }  ", "empty { }");
		t.test("S05", "_c1 { s_1 \"hello  world\" } ", "_c1 { s_1 \"hello  world\" }");
		t.test("S06", "example \"The \\\\ is called a \\\"backslash\\\" in English.\"", "example \"The \\\\ is called a \\\"backslash\\\" in English.\"");
		
		/* test valid SDA */
		String s = "error at position ";
		t.test("F01", "", s + "0: input has no data");
		t.test("F02", "  ", s + "2: input has no data");
		t.test("F03", "bad", s + "3: unexpected end of input");
		t.test("F04", "2bad", s + "1: identifier cannot start with '2'");
		t.test("F05", "b@d", s + "2: identifier cannot contain '@'");
		t.test("F06", "\"string\"", s + "8: value \"string\" has no identifier");
		t.test("F07", "_ \"string\"", s + "10: invalid node name (_)");
		t.test("F08", "\"pending", s + "8: trailing or pending quote");
		t.test("F09", "trailing\"", s + "9: trailing or pending quote");
		t.test("F10", "{", s + "1: block has no identifier");
		t.test("F11", "_ {", s + "3: invalid node name (_)");
		t.test("F12", " }", s + "2: unexpected block end");
		t.test("F13", "noright {", s + "9: unexpected end of input");
		t.test("F14", "noleft }", s + "8: unexpected block end");
		t.test("F15", "a{} b{}", s + "6: too many root elements");
		t.test("F16", "a{ b{} } }", s + "10: unexpected block end");
		t.test("F17", "a \"b\" c \"d\"", s + "8: too many root elements");
		
		// test performance
		UnitTestPerformance<String> p = new UnitTestPerformance<String>(str -> {
			try {
				parser.parse(new StringReader(str));
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		
		p.run("\nP01", samplesda, 25000, 25);
	}

}
