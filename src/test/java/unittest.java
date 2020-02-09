import java.io.*;
import be.baur.sda.*;

/** For unit testing only. 
 */
public class unittest {

	public static void main(String[] args) 
	throws IOException, SyntaxException {
        
		/* test valid SDA */
		
		TestParseAndRender("S01", "empty\"\"", "empty \"\"");
		TestParseAndRender("S02", "  empty  \"\"  ", "empty \"\"");
    	TestParseAndRender("S03", "empty{}", "empty{ }");
    	TestParseAndRender("S04", "  empty  {  }  ", "empty{ }");
    	TestParseAndRender("S05", "c1 { s1 \"hello  world\" } ", "c1{ s1 \"hello  world\" }");
		TestParseAndRender("S06", "example \"The \\\\ is called a \\\"backslash\\\" in English.\"", "example \"The \\\\ is called a \\\"backslash\\\" in English.\"");
		
		/* test error checking */
		
		String s = "SDA syntax violation at position ";
		TestParseError("F01", "", s + "1: unexpected end of input");
		TestParseError("F02", "  ", s + "3: unexpected end of input");
		TestParseError("F03", "bad", s + "4: unexpected end of input");
		TestParseError("F04", "bad  ", s + "6: unexpected end of input");
		TestParseError("F05", "2bad", s + "1: node name cannot start with '2'");
		TestParseError("F06", "b@d", s + "2: unexpected character '@'");
		TestParseError("F07", "trailing\"", s + "10: unexpected end of input");
		TestParseError("F08", "{", s + "1: node name cannot start with '{'");
		TestParseError("F09", "trailing{", s + "10: unexpected end of input");
		TestParseError("F10", "abc{ { ", s + "6: node name cannot start with '{'");
		TestParseError("F11", "abc{ \"", s + "6: node name cannot start with '\"'");
		TestParseError("F12", " }", s + "2: node name cannot start with '}'");
		TestParseError("F13", "noright {", s + "10: unexpected end of input");
		TestParseError("F14", "noleft }", s + "8: unexpected character '}'");
		TestParseError("F15", "a{ b{}", s + "7: unexpected end of input");
		TestParseError("F16", "a{} b{}", s + "5: excess input after root node");
		TestParseError("F17", "a{ b{} } }", s + "10: excess input after root node");
		TestParseError("F18", "a \"b\" c \"d\"", s + "7: excess input after root node");

	}
	
	static void TestParseError(String name, String in, String out) {
        
		try { SDA.Parse(new StringReader(in)); }
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
        
		Node e = SDA.Parse(new StringReader(in));
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
