package main;
import java.io.*;
import lexer.*;
import parser.*;

public class Main {

	public static void main(String[] args) throws IOException {
		InputStream in = new FileInputStream("tests/test.t");
		BufferedInputStream bin = new BufferedInputStream(in);
		byte[] program = bin.readAllBytes();
		//InputStream in = System.in;
		Lexer lex = new Lexer(program);
		Parser parse = new Parser(lex);
		parse.program();
		System.out.println("\n\n===============\n");
		for (Token token : lex.tokens) {
			System.out.println("(" + token.tag + ", " + token.content + ")");
		}
	}
}
