package Lexer;

import java.io.*;
import java.nio.file.*;
import java.nio.file.Files;
import java.util.*;

class LexerTester {
    public static void main(String[] args) throws IOException{
        Lexer lexer = new Lexer();
        Path path = FileSystems.getDefault().getPath("test.c");
        System.out.println(path.toAbsolutePath());
        String prog = new String(Files.readAllBytes(path));
        ArrayList<Token> tokens = lexer.tokenize(prog);
        for (Token t : tokens) {
            System.out.printf("%d, %s\n", t.getTagCode(), t.getValue());
        }
    }
}