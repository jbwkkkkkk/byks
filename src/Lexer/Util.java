package Lexer;
import java.util.*;

public class Util {
    public static Map<String, Integer> keyWords = new HashMap<>();
    public static Map<String, Integer> symbols = new HashMap<>();
    static {
        keyWords.put("double", 2);
        keyWords.put("int", 3);
        keyWords.put("if", 5);
        keyWords.put("for", 6);
        keyWords.put("while", 7);
        keyWords.put("else", 8);
        keyWords.put("return", 9);

        symbols.put(":=", 10);
        symbols.put("==", 11);
        symbols.put("<", 12);
        symbols.put(">", 13);
        symbols.put("&&", 14);
        symbols.put("||", 15);
        symbols.put("+", 16);
        symbols.put("-", 17);
        symbols.put("*", 18);
        symbols.put("/", 19);
        symbols.put("%", 20);
        symbols.put("(", 21);
        symbols.put(")", 22);
        symbols.put("{", 23);
        symbols.put("}", 24);
        symbols.put(",", 25);
        symbols.put(";", 26);
    }
    public static int getKeyWordCode(String key) {
        return getCode(key, keyWords);
    }
    public static int getSymbolCode(String key) {
        return getCode(key, symbols);
    }
    private static int getCode(String key, Map<String, Integer> map) {
        if (map.containsKey(key)) {
            return map.get(key);
        }
        return -1;
    }
    public static int abort(String msg) {
        System.err.println(msg);
        System.exit(-1);
        return -1;
    }
    public static ACResult matchNumber(String prog, int i) {
        int prevPos = i;
        boolean isDouble = false;
        while (true) {
            if (Character.isDigit(prog.charAt(i)))
                i += 1;
            if (!isDouble && prog.charAt(i) == '.') {
                isDouble = true;
                i += 1;
            }
            else break;
        }
        int resultType = isDouble ? 1 : 2;
        return new ACResult(resultType, i, prog.substring(prevPos, i));
    }
    public static ACResult matchString(String prog, int i) {
        int prevPos = i;
        while (true) {
            if (Character.isAlphabetic(prog.charAt(i)))
                i += 1;
            else
                break;
        }
        return new ACResult(0, i, prog.substring(prevPos, i));
    }
}
