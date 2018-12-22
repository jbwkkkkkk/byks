package inter;
import symbols.*;

public class While extends Stmt {

   BoolExpr expr; Stmt stmt;

   public While() { expr = null; stmt = null; }

   public void init(BoolExpr x, Stmt s) {
      expr = x;  stmt = s;
   }
   public void gen(int b, int a) {
      after = a;                // save label a
      expr.jumping(0, a);
      int label = newlabel();   // label for stmt
      emitlabel(label);
      stmt.gen(label, b);
      emit("(goto,  ,  , L" + b + ")");
   }
}
