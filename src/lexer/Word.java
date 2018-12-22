package lexer;
public class Word extends Token {

   public String lexeme = "";
   public Word(String s, int tag) { super(tag, s); lexeme = s; }
   public String toString() { return lexeme; }

   public static final Word
           and = new Word("&&", Tag.AND),
           or = new Word("||", Tag.OR),
           eq = new Word("==", Tag.EQ),
           minus = new Word("minus", Tag.MINUS),
           temp = new Word("t", Tag.TEMP);
}
