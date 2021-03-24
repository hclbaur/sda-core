package test;

import java.io.IOException;
import java.io.StringReader;

import be.baur.sda.Node;
import be.baur.sda.serialization.SDAParser;
import be.baur.sda.serialization.SyntaxException;

public final class TestSDAParser {

	private static SDAParser parser = new SDAParser();
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
			} catch (SyntaxException | IOException e) {
				return e.getMessage();
			}
		});
		
		Test p = new Test(s -> {
			try {
				parser.parse(new StringReader(s));
			} catch (SyntaxException | IOException e) {
				e.printStackTrace();
			}
		});
		
		
		// test valid SDA
		t.test("S01", "empty\"\"", "empty \"\"");
		t.test("S02", "  empty  \"\"  ", "empty \"\"");
		t.test("S03", "empty{}", "empty{ }");
		t.test("S04", "  empty  {  }  ", "empty{ }");
		t.test("S05", "_c1 { s_1 \"hello  world\" } ", "_c1{ s_1 \"hello  world\" }");
		t.test("S06", "example \"The \\\\ is called a \\\"backslash\\\" in English.\"", "example \"The \\\\ is called a \\\"backslash\\\" in English.\"");

		// test invalid SDA
		String s = "SDA syntax violation at position ";
		t.test("F01", "", s + "1: unexpected end of input");
		t.test("F02", "  ", s + "3: unexpected end of input");
		t.test("F03", "bad", s + "4: unexpected end of input");
		t.test("F04", "bad  ", s + "6: unexpected end of input");
		t.test("F05", "2bad", s + "1: node name cannot start with '2'");
		t.test("F06", "b@d", s + "2: unexpected character '@'");
		t.test("F07", "trailing\"", s + "10: unexpected end of input");
		t.test("F08", "{", s + "1: node name cannot start with '{'");
		t.test("F09", "_{", s + "2: invalid node name '_'");
		t.test("F10", "trailing{", s + "10: unexpected end of input");
		t.test("F11", "abc{ { ", s + "6: node name cannot start with '{'");
		t.test("F12", "abc{ _\"\"", s + "9: invalid node name '_'");
		t.test("F13", "abc{ \"", s + "6: node name cannot start with '\"'");
		t.test("F14", " }", s + "2: node name cannot start with '}'");
		t.test("F15", "noright {", s + "10: unexpected end of input");
		t.test("F16", "noleft }", s + "8: unexpected character '}'");
		t.test("F17", "a{ b{}", s + "7: unexpected end of input");
		t.test("F18", "a{} b{}", s + "5: excess input after root node");
		t.test("F19", "a{ b{} } }", s + "10: excess input after root node");
		t.test("F20", "a \"b\" c \"d\"", s + "7: excess input after root node");
		
		// test performance
		p.testPerf("\nP01", samplesda, 50000, 20);
	}

}
