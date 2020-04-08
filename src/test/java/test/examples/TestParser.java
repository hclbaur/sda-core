
package test.examples;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;

import be.baur.sda.Node;
import be.baur.sda.SDA;
import be.baur.sda.parse.SyntaxException;
import examples.parse.sda.Parser;

public final class TestParser {

	public static void main(String[] args) throws Exception {

		Node helloworld = (new Parser()).Parse(new StringReader("greeting{message\"hello world\"}"));
		InputStream in = Parser.class.getResourceAsStream("/sample.sda");
		Node samplefile = (new Parser()).Parse(new InputStreamReader(in,"UTF-8"));
		System.out.println("helloworld: " + helloworld);
		System.out.println("samplefile: " + samplefile);

		/* test valid SDA */

		TestParseAndRender("S1", "empty \"\"", "empty \"\"");
		TestParseAndRender("S2", "empty {  }", "empty{ }");
		TestParseAndRender("S3", "greeting{message\"hello world\"}", "greeting{ message \"hello world\" }");
		TestParseAndRender("S4", "example \"The \\\\ is called a \\\"backslash\\\" in English.\"", "example \"The \\\\ is called a \\\"backslash\\\" in English.\"");

		/* test error checking */

		String s = "SDA syntax violation at position ";
		TestParseError("F1", "", s + "0: input has no data");
		TestParseError("F2", " ", s + "1: input has no data");
		TestParseError("F3", "bad", s + "3: unexpected end of input");
		TestParseError("F4", "2bad", s + "1: identifier cannot start with '2'");
		TestParseError("F5", "b@d", s + "2: identifier cannot contain '@'");
		TestParseError("F6", "\"string\"", s + "8: value \"string\" has no identifier");
		TestParseError("F7", "\"pending", s + "8: trailing or pending quote");
		TestParseError("F8", "trailing\"", s + "9: trailing or pending quote");
		TestParseError("F9", "{", s + "1: block has no identifier");
		TestParseError("F10", "}", s + "1: unexpected block end");
		TestParseError("F11", "noright {", s + "9: unexpected end of input");
		TestParseError("F12", "noleft }", s + "8: unexpected block end");
		TestParseError("F13", "a{} b{}", s + "6: too many root elements");
		TestParseError("F14", "a \"b\" c \"d\"", s + "8: too many root elements");
	}

	private static Parser parser = new Parser();

	static void TestParseError(String name, String in, String out) {

		try { parser.Parse(new StringReader(in)); }
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

	static void TestParseAndRender(String name, String in, String out) 
			throws IOException, SyntaxException {

		Node e = parser.Parse(new StringReader(in));
		StringWriter s = new StringWriter(); 
		SDA.Render(s, e);

		if (s.toString().equals(out)) 
			System.out.print(name + " ");
		else {
			System.out.println(name + " FAILED!");
			System.out.println("EXPECTED: " + out);
			System.out.println("RETURNED: " + s);
		}
		return;
	}
}
