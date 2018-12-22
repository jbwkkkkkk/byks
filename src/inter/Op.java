package inter;
import lexer.*; import symbols.*;

public class Op extends ArthExpr {

   public Op(Token tok, Type p)  { super(tok, p); }

   public Expr reduce() {
      Expr x = gen();
      Temp t = new Temp(type);
      //emit( t.toString() + " = " + x.toString() );
      emit("(" + x.toString() + ", " + t.toString() + ")");
      return t;
   }
}
