package inter;

import lexer.Token;
import symbols.Type;

public class ArthExpr extends Expr {
    ArthExpr(Token tok, Type p) {
        super(tok, p);
    }
}
