package inter;
import lexer.*; import symbols.*;

public class Set extends Stmt {

   public Id id; public Expr expr;

   public Set(Id i, Expr x) {
      id = i; expr = x;
   }


   public void gen(int b, int a) {
      String exprStr = expr.gen().toString();
      if (exprStr.split(",").length == 3)
         emit("(" + exprStr + ", " + id.toString() + ")");
      else
         emit("(" + ":=, " + exprStr + ",  , " + id.toString() + ")");
   }
}
