package lexer;

public class Token {

	public final int tag;
	public final String content;
	public Token(int t, String content) {
		tag = t;
		this.content = content;
	}
	public String toString() {
		return "(" + (char)tag + ", " + content + ")";
	}
}
