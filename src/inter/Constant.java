package inter;
import lexer.*; import symbols.*;

public class Constant extends ArthExpr {

   public Constant(Token tok, Type p) { super(tok, p); }
   public Constant(int i) { super(new Int(i), Type.Int); isTrue = i; }
   public int isTrue;

    public void jumping(int t, int f) {
      if (  isTrue != 0 && t != 0 ) emit("goto L" + t);
      else if ( isTrue == 0 && f != 0) emit("goto L" + f);
   }
}
