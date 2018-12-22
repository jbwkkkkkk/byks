package inter;

import lexer.Token;
import symbols.Type;

public class BoolExpr extends Expr {
    BoolExpr(Token token, Type p) {
        super(token, p);
    }
}
