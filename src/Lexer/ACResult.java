package Lexer;

public class ACResult {
    public int type; //0 string, 1 double, 2, int
    public int newPos;
    public String value;
    ACResult(int type, int newPos, String value) {
        this.newPos = newPos;
        this.type = type;
        this.value = value;
    }
}