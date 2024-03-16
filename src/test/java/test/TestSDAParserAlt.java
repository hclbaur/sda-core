package test;

import java.io.StringReader;
import java.util.function.Function;

import be.baur.sda.Node;
import be.baur.sda.serialization.SDAParseException;
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

		Node helloworld = parser.parse(new StringReader("greeting{message\"hello world\"}"));
		System.out.println(helloworld);

		Function<String, String> strfun = str -> {
			try {
				return parser.parse(new StringReader(str)).toString();
			} catch (Exception e) {
				return ((SDAParseException)e).getLocalizedMessage();
			}
		};
				
		Test s = new Test(strfun);
		Test f = new Test(strfun, "error at position ");

		// test valid SDA
		s.ts1("S01", "empty\"\"", "empty \"\"");
		s.ts1("S02", "  empty  \"\"  ", "empty \"\"");
		s.ts1("S03", "empty{}", "empty { }");
		s.ts1("S04", "  empty  {  }  ", "empty { }");
		s.ts1("S05", "_c1 { s_1 \"hello  world\" } ", "_c1 { s_1 \"hello  world\" }");
		s.ts1("S06", "example \"The \\\\ is called a \\\"backslash\\\" in English.\"", "example \"The \\\\ is called a \\\"backslash\\\" in English.\"");
		
		/* test valid SDA */
		f.ts1("F01", "", "0: input has no data");
		f.ts1("F02", "  ", "2: input has no data");
		f.ts1("F03", "bad", "3: unexpected end of input");
		f.ts1("F04", "2bad", "1: identifier cannot start with '2'");
		f.ts1("F05", "b@d", "2: identifier cannot contain '@'");
		f.ts1("F06", "\"string\"", "8: value \"string\" has no identifier");
		f.ts1("F07", "_ \"string\"", "10: invalid node name (_)");
		f.ts1("F08", "\"pending", "8: trailing or pending quote");
		f.ts1("F09", "trailing\"", "9: trailing or pending quote");
		f.ts1("F10", "{", "1: block has no identifier");
		f.ts1("F11", "_ {", "3: invalid node name (_)");
		f.ts1("F12", " }", "2: unexpected block end");
		f.ts1("F13", "noright {", "9: unexpected end of input");
		f.ts1("F14", "noleft }", "8: unexpected block end");
		f.ts1("F15", "a{} b{}", "6: too many root elements");
		f.ts1("F16", "a{ b{} } }", "10: unexpected block end");
		f.ts1("F17", "a \"b\" c \"d\"", "8: too many root elements");
		
		// test performance
		UnitTestPerformance<String> p = new UnitTestPerformance<String>(str -> {
			try {
				parser.parse(new StringReader(str));
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		p.run("\nP01", samplesda, 25000, 25);
	}

}
