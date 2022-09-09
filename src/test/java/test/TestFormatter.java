package test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;

import be.baur.sda.Node;
import be.baur.sda.SDA;
import be.baur.sda.serialization.SDAFormatter;

public final class TestFormatter {
	
	public static void main(String[] args) throws Exception {
		
		Test t = new Test(s -> {
			return s;
		});
		
		//PrintWriter out = new PrintWriter(System.out);
		StringWriter out = new StringWriter();
        Node node = SDA.parser().parse(new StringReader("node\"1\"{node2{empty1\"\"empty2{}empty\"3\"{}}}"));
		(new SDAFormatter(4)).format(out, node);
		t.test("S01", out.toString(), 
				"node \"1\" {\n" + 
				"    node2 {\n" + 
				"        empty1 \"\"\n" +
				"        empty2 { }\n" + 
				"        empty \"3\" { }\n" + 
				"    }\n" + 
				"}\n");
		
		out = new StringWriter();
		InputStream in = TestFormatter.class.getResourceAsStream("/addressbook.sda");
		node = SDA.parser().parse(new InputStreamReader(in,"UTF-8"));
		(new SDAFormatter()).format(out, node);
		t.test("S02", out.toString(), "addressbook {\n" + 
				"	contact \"1\" {\n" + 
				"		firstname \"Alice\"\n" + 
				"		phonenumber \"06-11111111\"\n" + 
				"		phonenumber \"06-22222222\"\n" + 
				"	}\n" + 
				"	contact \"2\" {\n" + 
				"		firstname \"Bob\"\n" + 
				"		phonenumber \"06-33333333\"\n" + 
				"		phonenumber \"06-44444444\"\n" + 
				"	}\n" + 
				"}\n");
	}

}
