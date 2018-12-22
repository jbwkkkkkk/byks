package lexer;
public class Float extends Token {

	public final float value;
	public Float(float v) { super(Tag.FLOAT, Double.toString(v)); value = v; }
	public String toString() { return "" + value; }
}
