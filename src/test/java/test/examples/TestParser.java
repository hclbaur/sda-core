
package test.examples;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import be.baur.sda.Node;
import be.baur.sda.parse.SyntaxException;
import examples.parse.sda.Parser;

public final class TestParser {

	public static void main(String[] args) throws Exception {

		Node helloworld = (new Parser()).parse(new StringReader("greeting{message\"hello world\"}"));
		InputStream in = Parser.class.getResourceAsStream("/sample.sda");
		Node samplefile = (new Parser()).parse(new InputStreamReader(in,"UTF-8"));
		System.out.println("helloworld: " + helloworld);
		System.out.println("samplefile: " + samplefile);

		/* test valid SDA */

		testParseAndRender("S1", "empty \"\"", "empty \"\"");
		testParseAndRender("S2", "empty {  }", "empty{ }");
		testParseAndRender("S3", "greeting{message\"hello world\"}", "greeting{ message \"hello world\" }");
		testParseAndRender("S4", "example \"The \\\\ is called a \\\"backslash\\\" in English.\"", "example \"The \\\\ is called a \\\"backslash\\\" in English.\"");

		/* test error checking */

		String s = "SDA syntax violation at position ";
		testParseError("F1", "", s + "0: input has no data");
		testParseError("F2", " ", s + "1: input has no data");
		testParseError("F3", "bad", s + "3: unexpected end of input");
		testParseError("F4", "2bad", s + "1: identifier cannot start with '2'");
		testParseError("F5", "b@d", s + "2: identifier cannot contain '@'");
		testParseError("F6", "\"string\"", s + "8: value \"string\" has no identifier");
		testParseError("F7", "\"pending", s + "8: trailing or pending quote");
		testParseError("F8", "trailing\"", s + "9: trailing or pending quote");
		testParseError("F9", "{", s + "1: block has no identifier");
		testParseError("F10", "}", s + "1: unexpected block end");
		testParseError("F11", "noright {", s + "9: unexpected end of input");
		testParseError("F12", "noleft }", s + "8: unexpected block end");
		testParseError("F13", "a{} b{}", s + "6: too many root elements");
		testParseError("F14", "a \"b\" c \"d\"", s + "8: too many root elements");
	}

	private static Parser parser = new Parser();

	static void testParseError(String name, String in, String out) {

		try { parser.parse(new StringReader(in)); }
		catch (Exception e) { 
			if (e.getMessage().equals(out)) 
				System.out.print(name + " ");
			else {
				System.out.println(name + " FAILED!");
				System.out.println("EXPECTED: " + out);
				System.out.println("RETURNED: " + e.getMessage());
			}
			return;
		}
		System.out.println(name + ": FAIL - exception expected");
	}

	static void testParseAndRender(String scenario, String in, String out) 
			throws IOException, SyntaxException {

		Node e = parser.parse(new StringReader(in));

		if (e.toString().equals(out)) 
			System.out.print(scenario + " ");
		else {
			System.out.println(scenario + " FAILED!");
			System.out.println("EXPECTED: " + out);
			System.out.println("RETURNED: " + e);
		}
		return;
	}
}
