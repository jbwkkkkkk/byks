package parser;

import java.io.*;
import java.util.HashMap;

import lexer.*;
import symbols.*;
import inter.*;

public class Parser {
    private Lexer lex;    // lexical analyzer for this parser
    private Token look;   // lookahead tagen
    Env top = null;       // current or top symbol table

    public Parser(Lexer l) throws IOException {
        lex = l;
        move();
    }

    void move() throws IOException {
        look = lex.scan();
    }

    void error(String s) {
        throw new Error("near line " + lex.line + ": " + s);
    }

    void match(int t) throws IOException {
        if (look.tag == t) move();
        else error("syntax error");
    }

    public void program() throws IOException {  // program -> block
        match(Tag.TYPE);
        if (!look.toString().equals("main")) {
            System.err.println("invalid program");
            System.exit(-1);
        }
        match(Tag.ID);
        match('(');
        match(')');
        Stmt s = block();
        int begin = s.newlabel();
        int after = s.newlabel();
        System.out.println("===============\n");
        s.emitlabel(begin);
        s.gen(begin, after);
        s.emitlabel(after);
    }

    Stmt block() throws IOException {  // block -> { decls stmts }
        match('{');
        int startLine = lex.line;
        Env savedEnv = top;
        top = new Env(top);
        decls();
        Stmt s = stmts();
        int endLine = lex.line;
        match('}');
        System.out.printf("Environment <%d>, from line <%d> to line <%d>:\n", top.envID, startLine, endLine);
        for (Token token : top.table.keySet()) {
            System.out.printf("Var <%s> with type <%s>\n", token.content, (token.tag == Type.Int.tag ? "int" : "float"));
        }
        System.out.printf("There are higher environments for environment <%d>: ", top.envID);
        top.printHigherEnvIDs();
        System.out.println();
        top = savedEnv;
        return s;
    }

    void decls() throws IOException {
        while (look.tag == Tag.TYPE) {   // D -> type ID ;
            Type p = type();
            match(Tag.TYPE);
            Token tok = look;
            match(Tag.ID);
            match(';');
            Id id = new Id((Word) tok, p);
            top.put(tok, id);
        }
    }

    Type type() throws IOException {
        Type p = (Type) look;
        return p;
    }

    Stmt stmts() throws IOException {
        if (look.tag == '}') return Stmt.Null;
        else return new Seq(stmt(), stmts());
    }

    Stmt stmt() throws IOException {
        BoolExpr x;
        Stmt s, s1, s2;
        Stmt savedStmt;         // save enclosing loop for breaks

        switch (look.tag) {

            case ';':
                move();
                return Stmt.Null;

            case Tag.IF:
                match(Tag.IF);
                match('(');
                x = boolExpr();
                match(')');
                s1 = stmt();
                if (look.tag != Tag.ELSE) return new If(x, s1);
                match(Tag.ELSE);
                s2 = stmt();
                return new Else(x, s1, s2);

            case Tag.WHILE:
                While whilenode = new While();
                savedStmt = Stmt.Enclosing;
                Stmt.Enclosing = whilenode;
                match(Tag.WHILE);
                match('(');
                x = boolExpr();
                match(')');
                s1 = stmt();
                whilenode.init(x, s1);
                Stmt.Enclosing = savedStmt;  // reset Stmt.Enclosing
                return whilenode;

            case '{':
                return block();

            default:
                return assign();
        }
    }

    Stmt assign() throws IOException {
        Stmt stmt;
        Token t = look;
        match(Tag.ID);
        Id id = top.get(t);
        if (id == null) error(t.toString() + " undeclared");

        if (look.tag != '=')      // S -> id = E ;
            error("invalid assignment");
        move();
        stmt = new Set(id, arthExpr());
        match(';');
        return stmt;
    }

    BoolExpr boolExpr() throws IOException {
        BoolExpr x = null;
        if (look.tag == '!') {
            Token token = look;
            move();
            Token token1 = look;
            move();
            if (token1.tag != '(') error("invalid NOT expr");
            x = new Not(token, boolExpr());
            Token token2 = look;
            move();
            if (token2.tag != ')') error("invalid NOT expr");
        }
        else {
            ArthExpr arthExpr = arthExpr();
            while (look.tag == '<' || look.tag == '>' || look.tag == Tag.EQ) {
                Token tok = look;
                move();
                x = new Rel(tok, arthExpr, arthExpr());
            }
        }
        return x;
    }

    ArthExpr arthExpr() throws IOException {
        ArthExpr x = term();
        while (look.tag == '+' || look.tag == '-') {
            Token tok = look;
            move();
            x = new Arith(tok, x, term());
        }
        return x;
    }

    ArthExpr term() throws IOException {
        ArthExpr x = unary();
        while (look.tag == '*' || look.tag == '/') {
            Token tok = look;
            move();
            x = new Arith(tok, x, unary());
        }
        return x;
    }

    ArthExpr unary() throws IOException {
        if (look.tag == '-') {
            move();
            return new Unary(Word.minus, arthExpr());
        }
        else return factor();
    }

    ArthExpr factor() throws IOException {
        ArthExpr x = null;
        switch (look.tag) {
            case '(':
                move();
                x = arthExpr();
                match(')');
                return x;
            case Tag.ID:
                String s = look.toString();
                Id id = top.get(look);
                if (id == null)
                    error(look.toString() + " undeclared");
                move();
                return id;
            case Tag.INT:
                x = new Constant(look, Type.Int);
                match(Tag.INT);
                return x;
            case Tag.FLOAT:
                x = new Constant(look, Type.Float);
                match(Tag.FLOAT);
                return x;
            default:
                error("syntax error");
                return x;
        }
    }
}
