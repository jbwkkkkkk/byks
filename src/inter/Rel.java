package inter;
import lexer.*; import symbols.*;

public class Rel extends Logical {

   public Rel(Token tok, Expr x1, Expr x2) { super(tok, x1, x2); }

   public void jumping(int t, int f) {
      Expr a = expr1.reduce();
      Expr b = expr2.reduce();
      String test = a.toString() + " " + op.toString() + " " + b.toString();
      Temp res = new Temp(Type.Int);
      emit(String.format("(%s, %s, %s, %s)", op.toString(), a.toString(), b.toString(), res.toString()));
      //emit("(" + op.toString() + ", " + a.toString() + ", " + b.toString() + "");
      emitjumps(res.toString(), t, f);
   }
}
