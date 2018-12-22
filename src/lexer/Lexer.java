package lexer;

import java.io.*;
import java.util.*;

import symbols.*;

public class Lexer {
    public final ArrayList<Token> tokens;
    InputStream in;
    public static int line = 1;
    char peek = ' ';
    HashMap<String, Word> words = new HashMap<>();

    void reserve(Word w) {
        words.put(w.lexeme, w);
    }

    public Lexer(byte[] program) {
        tokens = new ArrayList<>();
        in = new ByteArrayInputStream(program);
        reserve(new Word("if", Tag.IF));
        reserve(new Word("else", Tag.ELSE));
        reserve(new Word("while", Tag.WHILE));
        reserve(Type.Int);
        reserve(Type.Float);
    }

    void nextCh() throws IOException {
        peek = (char) in.read();
    }

    boolean nextCh(char c) throws IOException {
        nextCh();
        if (peek != c) return false;
        peek = ' ';
        return true;
    }

    public Token scan() throws IOException {
        for (; ; nextCh()) {
            if (peek == ' ' || peek == '\t') continue;
            else if (peek == '\n') line = line + 1;
            else break;
        }
        switch (peek) {
            case '&':
                if (nextCh('&')) {
                    tokens.add(Word.and);
                    return Word.and;
                }
                else Util.ScannerAbort(line);
            case '|':
                if (nextCh('|')) {
                    tokens.add(Word.or);
                    return Word.or;
                }
                else Util.ScannerAbort(line);
            case '=':
                if (nextCh('=')) {
                    tokens.add(Word.eq);
                    return Word.eq;
                }
                else {
                    Token tok = new Token('=', "=");
                    tokens.add(tok);
                    return tok;
                }
        }
        if (Character.isDigit(peek)) {
            int v = 0;
            do {
                v = 10 * v + Character.digit(peek, 10);
                nextCh();
            } while (Character.isDigit(peek));
            if (peek != '.') {
                Token tok = new Int(v);
                tokens.add(tok);
                return tok;
            }
            float x = v;
            float d = 10;
            for (; ; ) {
                nextCh();
                if (!Character.isDigit(peek)) break;
                x = x + Character.digit(peek, 10) / d;
                d = d * 10;
            }
            Token tok = new Float(x);
            tokens.add(tok);
            return tok;
        }
        if (Character.isLetter(peek)) {
            StringBuilder b = new StringBuilder();
            do {
                b.append(peek);
                nextCh();
            } while (Character.isLetterOrDigit(peek));
            String s = b.toString();
            Word w = words.get(s);
            if (w != null) return w;
            w = new Word(s, Tag.ID);
            words.put(s, w);
            tokens.add(w);
            return w;
        }
        Token tok = new Token(peek, Character.toString(peek));
        peek = ' ';
        tokens.add(tok);
        return tok;
    }
}
