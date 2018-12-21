package Lexer;

public class Token {
    private int tagCode;
    private String value;
    private Token(int tagCode, String value) {
        this.tagCode = tagCode;
        this.value = value;
    }
    public static Token newIdentifier(String value) {
        return new Token(4, value);
    }
    public static Token newIntLit(String value) {
        return new Token(1, value);
    }
    public static Token newDoubleLit(String value) {
        return new Token(0, value);
    }
    public static Token newToken(int tagCode) {
        if (tagCode < 0 || tagCode > 26)
            Util.abort("error tagCode");
        if (tagCode == 0 || tagCode == 1 || tagCode == 4)
            Util.abort("you should call the corresponding function");
        return new Token(tagCode, "");
    }

    public int getTagCode() {
        return tagCode;
    }
    public String getValue() {
        return value;
    }
}
