package test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import be.baur.sda.Node;
import be.baur.sda.parser.Parser;

public final class TestAll {

	/** For unit testing only. Make public to run. */
	public static void main(String[] args) throws Exception {

		Node hello = (new Parser()).Parse(new StringReader("greeting{message\"hello world\"}"));
		InputStream in = Parser.class.getResourceAsStream("/sample.sda");
		Node sample = (new Parser()).Parse(new InputStreamReader(in,"UTF-8"));
		System.out.println("TestAll: " + hello);
		System.out.println("TestAll: " + sample);
	
		test.TestParser.main(args);
		test.examples.TestParser.main(args);
	}
}
