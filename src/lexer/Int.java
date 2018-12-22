package lexer;

public class Int extends Token {

	public final int value;
	public Int(int v) { super(Tag.INT, Integer.toString(v)); value = v; }
	public String toString() { return "" + value; }
}
