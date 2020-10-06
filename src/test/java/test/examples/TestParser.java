
package test.examples;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import be.baur.sda.Node;
import be.baur.sda.parse.SyntaxException;
import examples.parse.sda.Parser;
import test.Test;

public final class TestParser {

	public static void main(String[] args) throws Exception {

		Node helloworld = (new Parser()).parse(new StringReader("greeting{message\"hello world\"}"));
		InputStream in = Parser.class.getResourceAsStream("/sample.sda");
		Node samplefile = (new Parser()).parse(new InputStreamReader(in,"UTF-8"));
		System.out.println("helloworld: " + helloworld);
		System.out.println("samplefile: " + samplefile);

		Test t = new Test(s -> {
			try {
				return parser.parse(new StringReader(s)).toString();
			} catch (SyntaxException | IOException e) {
				return e.getMessage();
			}
		});
		
		/* test valid SDA */
		t.test("S1", "empty \"\"", "empty \"\"");
		t.test("S2", "empty {  }", "empty{ }");
		t.test("S3", "greeting{message\"hello world\"}", "greeting{ message \"hello world\" }");
		t.test("S4", "example \"The \\\\ is called a \\\"backslash\\\" in English.\"", "example \"The \\\\ is called a \\\"backslash\\\" in English.\"");

		/* test valid SDA */
		String s = "SDA syntax violation at position ";
		t.test("F1", "", s + "0: input has no data");
		t.test("F2", " ", s + "1: input has no data");
		t.test("F3", "bad", s + "3: unexpected end of input");
		t.test("F4", "2bad", s + "1: identifier cannot start with '2'");
		t.test("F5", "b@d", s + "2: identifier cannot contain '@'");
		t.test("F6", "\"string\"", s + "8: value \"string\" has no identifier");
		t.test("F7", "\"pending", s + "8: trailing or pending quote");
		t.test("F8", "trailing\"", s + "9: trailing or pending quote");
		t.test("F9", "{", s + "1: block has no identifier");
		t.test("F10", "}", s + "1: unexpected block end");
		t.test("F11", "noright {", s + "9: unexpected end of input");
		t.test("F12", "noleft }", s + "8: unexpected block end");
		t.test("F13", "a{} b{}", s + "6: too many root elements");
		t.test("F14", "a \"b\" c \"d\"", s + "8: too many root elements");
	}

	private static Parser parser = new Parser();
}
