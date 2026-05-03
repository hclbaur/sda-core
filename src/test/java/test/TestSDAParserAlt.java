package test;

import java.util.function.Function;

import be.baur.sda.io.ParseException;
import samples.parser.SDAParser;

public final class TestSDAParserAlt {

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

		var helloworld = parser.parse("greeting{message\"hello world\"}");
		System.out.println(helloworld);

		Function<String, String> strfun = str -> {
			try {
				return parser.parse(str).toString();
			} catch (Exception e) {
				return ((ParseException)e).getLocalizedMessage();
			}
		};
				
		Test t = new Test(strfun);

		// test valid SDA
		t.s("S01", "empty\"\"", "empty \"\"");
		t.s("S02", "  empty  \"\"  ", "empty \"\"");
		t.s("S03", "empty{}", "empty { }");
		t.s("S04", "  empty  {  }  ", "empty { }");
		t.s("S05", "_c1 { s_1 \"hello  world\" } ", "_c1 { s_1 \"hello  world\" }");
		t.s("S06", "example \"The \\\\ is called a \\\"backslash\\\" in English.\"", "example \"The \\\\ is called a \\\"backslash\\\" in English.\"");
				
		t.checkFailures();
		
		t = new Test(strfun, "error at position ");
		
		/* test invalid SDA */
		t.s("F01", "", "0: input has no data");
		t.s("F02", "  ", "2: input has no data");
		t.s("F03", "bad", "3: unexpected end of input");
		t.s("F04", "2bad", "1: identifier cannot start with '2'");
		t.s("F05", "b@d", "2: identifier cannot contain '@'");
		t.s("F06", "\"string\"", "8: value \"string\" has no identifier");
		t.s("F07", "_ \"string\"", "10: invalid node name (_)");
		t.s("F08", "\"pending", "8: trailing or pending quote");
		t.s("F09", "trailing\"", "9: trailing or pending quote");
		t.s("F10", "{", "1: block has no identifier");
		t.s("F11", "_ {", "3: invalid node name (_)");
		t.s("F12", " }", "2: unexpected block end");
		t.s("F13", "noright {", "9: unexpected end of input");
		t.s("F14", "noleft }", "8: unexpected block end");
		t.s("F15", "a{} b{}", "6: too many root elements");
		t.s("F16", "a{ b{} } }", "10: unexpected block end");
		t.s("F17", "a \"b\" c \"d\"", "8: too many root elements");
				
		t.checkFailures();
		
		// test performance
		var p = new TestPerf<String>(str -> {
			try {
				parser.parse(str);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		p.run("\nP01", samplesda, 25000, 25);
	}

}
