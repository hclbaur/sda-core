package test;

import java.io.IOException;
import java.io.StringReader;

import be.baur.sda.Node;
import be.baur.sda.serialization.SDAParser;
import be.baur.sda.serialization.SyntaxException;

public final class TestSDAParser {

	private static SDAParser parser = new SDAParser();
	private static String samplesda = "addressbook {\r\n" + 
			"	contact \"1\" {\r\n" + 
			"		firstname \"Alice\"\r\n" + 
			"		phonenumber \"06-11111111\"\r\n" + 
			"		phonenumber \"06-22222222\"\r\n" + 
			"	}\r\n" + 
			"	contact \"2\" {\r\n" + 
			"		firstname \"Bob\"\r\n" + 
			"		phonenumber \"06-33333333\"\r\n" + 
			"		phonenumber \"06-44444444\"\r\n" + 
			"	}\r\n" + 
			"}";
	
	public static void main(String[] args) throws Exception {

		Node helloworld = (new SDAParser()).parse(new StringReader("message\"greeting\"{text\"hello world\"}"));
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
		t.test("S03", "empty{}", "empty { }");
		t.test("S04", "  empty  {  }  ", "empty { }");
		t.test("S05", "empty\"\"{}", "empty { }");
		t.test("S06", "  empty  \"\"  {  }  ", "empty { }");
		t.test("S07", "_m1 { t_1 \"hello  world\" } ", "_m1 { t_1 \"hello  world\" }");
		t.test("S08", "_1m \"yo\" { t1_ \"hello  world\" } ", "_1m \"yo\" { t1_ \"hello  world\" }");
		t.test("S09", "example \"The \\\\ is called a \\\"backslash\\\" in English.\"", "example \"The \\\\ is called a \\\"backslash\\\" in English.\"");

		// test invalid SDA
		String s = "SDA syntax violation at position ";
		t.test("F01", "", s + "0: unexpected end of input");
		t.test("F02", "  ", s + "2: unexpected end of input");
		t.test("F03", "bad", s + "3: unexpected end of input");
		t.test("F04", "bad  ", s + "5: unexpected end of input");
		t.test("F05", "2bad", s + "1: node name cannot start with '2'");
		t.test("F06", "b@d", s + "2: unexpected character '@'");
		t.test("F07", "trailing \"", s + "10: unexpected end of input");
		t.test("F08", "trailing \"abc", s + "13: unexpected end of input");
		t.test("F09", "{", s + "1: node name cannot start with '{'");
		t.test("F10", "_{", s + "2: invalid node name (_)");
		t.test("F11", "abc{ { ", s + "6: node name cannot start with '{'");
		t.test("F12", "abc{ _\"\"", s + "7: invalid node name (_)");
		t.test("F13", "abc{ \"", s + "6: node name cannot start with '\"'");
		t.test("F14", " }", s + "2: node name cannot start with '}'");
		t.test("F15", "noright {", s + "9: unexpected end of input");
		t.test("F16", "noleft }", s + "8: unexpected character '}'");
		t.test("F17", "noright \"2\" {", s + "13: unexpected end of input");
		t.test("F18", "noleft \"2\" }", s + "12: excess input after root node");
		t.test("F19", "a{ b{}", s + "6: unexpected end of input");
		t.test("F20", "a{} b{}", s + "5: excess input after root node");
		t.test("F21", "a{ b{} } }", s + "10: excess input after root node");
		t.test("F22", "a \"b\" c \"d\"", s + "7: excess input after root node");
		
		// test performance
		p.testPerf("\nP01", samplesda, 25000, 25);
	}

}
