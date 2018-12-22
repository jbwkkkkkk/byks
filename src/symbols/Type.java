package symbols;
import lexer.*;
public class Type extends Word {
   private Type(String s, int tag) { super(s, tag);}
   public static Type max(Type t1, Type t2) {
      if (t1 == Float ||t2 == Float) return Float;
      else return Int;
   }
   public static final Type
      Int   = new Type( "int", Tag.TYPE),
      Float = new Type( "float", Tag.TYPE);
}
