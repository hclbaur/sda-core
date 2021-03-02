package test;

import java.io.IOException;
import java.io.StringReader;

import be.baur.sda.Node;
import be.baur.sda.parse.Parser;
import be.baur.sda.parse.SyntaxException;

public final class TestParser {

	private static Parser parser = new Parser();
	
	public static void main(String[] args) throws Exception {

		Node helloworld = (new Parser()).parse(new StringReader("greeting{message\"hello world\"}"));
		System.out.println(helloworld);

		Test t = new Test(s -> {
			try {
				return parser.parse(new StringReader(s)).toString();
			} catch (SyntaxException | IOException e) {
				return e.getMessage();
			}
		});
		
		/* test valid SDA */
		t.test("S01", "empty\"\"", "empty \"\"");
		t.test("S02", "  empty  \"\"  ", "empty \"\"");
		t.test("S03", "empty{}", "empty{ }");
		t.test("S04", "  empty  {  }  ", "empty{ }");
		t.test("S05", "c1 { s1 \"hello  world\" } ", "c1{ s1 \"hello  world\" }");
		t.test("S06", "example \"The \\\\ is called a \\\"backslash\\\" in English.\"", "example \"The \\\\ is called a \\\"backslash\\\" in English.\"");

		/* test invalid SDA */
		String s = "SDA syntax violation at position ";
		t.test("F01", "", s + "1: unexpected end of input");
		t.test("F02", "  ", s + "3: unexpected end of input");
		t.test("F03", "bad", s + "4: unexpected end of input");
		t.test("F04", "bad  ", s + "6: unexpected end of input");
		t.test("F05", "2bad", s + "1: node name cannot start with '2'");
		t.test("F06", "b@d", s + "2: unexpected character '@'");
		t.test("F07", "trailing\"", s + "10: unexpected end of input");
		t.test("F08", "{", s + "1: node name cannot start with '{'");
		t.test("F09", "trailing{", s + "10: unexpected end of input");
		t.test("F10", "abc{ { ", s + "6: node name cannot start with '{'");
		t.test("F11", "abc{ \"", s + "6: node name cannot start with '\"'");
		t.test("F12", " }", s + "2: node name cannot start with '}'");
		t.test("F13", "noright {", s + "10: unexpected end of input");
		t.test("F14", "noleft }", s + "8: unexpected character '}'");
		t.test("F15", "a{ b{}", s + "7: unexpected end of input");
		t.test("F16", "a{} b{}", s + "5: excess input after root node");
		t.test("F17", "a{ b{} } }", s + "10: excess input after root node");
		t.test("F18", "a \"b\" c \"d\"", s + "7: excess input after root node");
	}

}
