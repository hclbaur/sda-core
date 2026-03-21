package test;

import java.util.function.Function;

import be.baur.sda.Node;
import be.baur.sda.io.SDAParser;

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

		Node hello = parser.parse("message\"greeting\"{text\"hello world\"}");
		System.out.println(hello);

		Function<String, String> strfun = str -> {
			try {
				return parser.parse(str).toString();
			} catch (Exception e) {
				return e.getLocalizedMessage();
			}
		};
				
		Test t = new Test(strfun);

		// test valid SDA
		t.s("S01", "empty\"\"", "empty \"\"");
		t.s("S02", "  empty  \"\"  ", "empty \"\"");
		t.s("S03", "empty{}", "empty { }");
		t.s("S04", "  empty  {  }  ", "empty { }");
		t.s("S05", "empty\"\"{}", "empty { }");
		t.s("S06", "  empty  \"\"  {  }  ", "empty { }");
		t.s("S07", "_m1 { t_1 \"hello  world\" } ", "_m1 { t_1 \"hello  world\" }");
		t.s("S08", "_1m \"yo\" { t1_ \"hello  world\" } ", "_1m \"yo\" { t1_ \"hello  world\" }");
		t.s("S09", "example \"The \\\\ is called a \\\"backslash\\\" in English.\"", "example \"The \\\\ is called a \\\"backslash\\\" in English.\"");
		
		t.checkFailures();
		
		t = new Test(strfun, "error at position ");
		
		// test invalid SDA
		t.s("F01", "", "0: unexpected end of input");
		t.s("F02", "  ", "2: unexpected end of input");
		t.s("F03", "bad", "3: unexpected end of input");
		t.s("F04", "bad  ", "5: unexpected end of input");
		t.s("F05", "bad bad", "5: unexpected character 'b'");
		t.s("F06", "2bad", "1: node name cannot start with '2'");
		t.s("F07", "b@d", "2: unexpected character '@'");
		t.s("F08", "trailing \"", "10: unexpected end of input");
		t.s("F09", "trailing \"abc", "13: unexpected end of input");
		t.s("F10", "{", "1: node name cannot start with '{'");
		t.s("F11", "_{", "2: invalid node name (_)");
		t.s("F12", "abc{ { ", "6: node name cannot start with '{'");
		t.s("F13", "abc{ _\"\"", "7: invalid node name (_)");
		t.s("F14", "abc{ \"", "6: node name cannot start with '\"'");
		t.s("F15", " }", "2: node name cannot start with '}'");
		t.s("F16", "noright {", "9: unexpected end of input");
		t.s("F17", "noleft }", "8: unexpected character '}'");
		t.s("F18", "noright \"2\" {", "13: unexpected end of input");
		t.s("F19", "noleft \"2\" }", "12: excess input after root node");
		t.s("F20", "a{ b{}", "6: unexpected end of input");
		t.s("F21", "a{} b{}", "5: excess input after root node");
		t.s("F22", "a{ b{} } }", "10: excess input after root node");
		t.s("F23", "a \"b\" c \"d\"", "7: excess input after root node");
		
		t.checkFailures();
		
		// test performance
		var p = new TestPerf<String>(str -> {
			try {
				parser.parse(str);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		p.run("\nP01", samplesda, 10000, 41);
	}

}
