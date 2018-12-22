package inter;
import lexer.*;
import symbols.*;

public class Arith extends Op {

   public Expr expr1, expr2;

   public Arith(Token tok, Expr x1, Expr x2)  {
      super(tok, null); expr1 = x1; expr2 = x2;
      type = Type.max(expr1.type, expr2.type);
      if (type == null ) error("type error");
   }

   public Expr gen() {
      return new Arith(op, expr1.reduce(), expr2.reduce());
   }

   public String toString() {
      String opStr = op == null ? " , " : op.toString();
      String expr1Str = expr1 == null ? " , " : expr1.toString();
      String expr2Str = expr2 == null ? " , " : expr2.toString();
      return opStr + ", " + expr1Str + ", " + expr2Str;
   }
}
