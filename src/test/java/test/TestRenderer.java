package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;

import be.baur.sda.Node;
import be.baur.sda.parse.Parser;
import be.baur.sda.parse.SyntaxException;
import be.baur.sda.render.Renderer;

public final class TestRenderer {
	
	public static void main(String[] args) throws IOException, SyntaxException {
		
		Node node;
		PrintWriter stdout = new PrintWriter(System.out);
        node = (new Parser()).parse(new StringReader("complex{ simple\"\"empty{} }"));
		(new Renderer()).Render(stdout, node, 4);
		InputStream in = Parser.class.getResourceAsStream("/addressbook.sda");
		node = (new Parser()).parse(new InputStreamReader(in,"UTF-8"));
		(new Renderer()).render(stdout, node);
		//stdout.close();

	}

}
