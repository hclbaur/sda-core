package test;

import java.util.function.Function;

import be.baur.sda.Node;
import be.baur.sda.serialization.SDAParser;

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
				
		Test s = new Test(strfun);
		Test f = new Test(strfun, "error at position ");

		// test valid SDA
		s.ts1("S01", "empty\"\"", "empty \"\"");
		s.ts1("S02", "  empty  \"\"  ", "empty \"\"");
		s.ts1("S03", "empty{}", "empty { }");
		s.ts1("S04", "  empty  {  }  ", "empty { }");
		s.ts1("S05", "empty\"\"{}", "empty { }");
		s.ts1("S06", "  empty  \"\"  {  }  ", "empty { }");
		s.ts1("S07", "_m1 { t_1 \"hello  world\" } ", "_m1 { t_1 \"hello  world\" }");
		s.ts1("S08", "_1m \"yo\" { t1_ \"hello  world\" } ", "_1m \"yo\" { t1_ \"hello  world\" }");
		s.ts1("S09", "example \"The \\\\ is called a \\\"backslash\\\" in English.\"", "example \"The \\\\ is called a \\\"backslash\\\" in English.\"");

		// test invalid SDA
		f.ts1("F01", "", "0: unexpected end of input");
		f.ts1("F02", "  ", "2: unexpected end of input");
		f.ts1("F03", "bad", "3: unexpected end of input");
		f.ts1("F04", "bad  ", "5: unexpected end of input");
		f.ts1("F05", "bad bad", "5: unexpected character 'b'");
		f.ts1("F06", "2bad", "1: node name cannot start with '2'");
		f.ts1("F07", "b@d", "2: unexpected character '@'");
		f.ts1("F08", "trailing \"", "10: unexpected end of input");
		f.ts1("F09", "trailing \"abc", "13: unexpected end of input");
		f.ts1("F10", "{", "1: node name cannot start with '{'");
		f.ts1("F11", "_{", "2: invalid node name (_)");
		f.ts1("F12", "abc{ { ", "6: node name cannot start with '{'");
		f.ts1("F13", "abc{ _\"\"", "7: invalid node name (_)");
		f.ts1("F14", "abc{ \"", "6: node name cannot start with '\"'");
		f.ts1("F15", " }", "2: node name cannot start with '}'");
		f.ts1("F16", "noright {", "9: unexpected end of input");
		f.ts1("F17", "noleft }", "8: unexpected character '}'");
		f.ts1("F18", "noright \"2\" {", "13: unexpected end of input");
		f.ts1("F19", "noleft \"2\" }", "12: excess input after root node");
		f.ts1("F20", "a{ b{}", "6: unexpected end of input");
		f.ts1("F21", "a{} b{}", "5: excess input after root node");
		f.ts1("F22", "a{ b{} } }", "10: excess input after root node");
		f.ts1("F23", "a \"b\" c \"d\"", "7: excess input after root node");
		
		// test performance
		
		UnitTestPerformance<String> perf = new UnitTestPerformance<String>(str -> {
			try {
				parser.parse(str);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		perf.run("\nP01", samplesda, 20000, 31);
	}

}
