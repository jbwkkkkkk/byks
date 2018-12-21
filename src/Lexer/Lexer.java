package Lexer;

import java.util.*;

public class Lexer {
    public ArrayList<Token> tokenize(String program) {
        ArrayList<Token> result = new ArrayList<>();
        for (int i = 0; i < program.length();) {
            if (program.charAt(i) == ' ' || program.charAt(i) == '\t' || program.charAt(i) == '\n') {
                i += 1;
                continue;
            }
            if (Character.isAlphabetic(program.charAt(i))) {
                ACResult matchResult = Util.matchString(program, i);
                int tagCode = Util.getKeyWordCode(matchResult.value);
                if (tagCode == -1) {
                    result.add(Token.newIdentifier(matchResult.value));
                }
                else {
                    result.add(Token.newToken(tagCode));
                }
                i = matchResult.newPos;
            }
            else if (Character.isDigit(program.charAt(i))) {
                ACResult matchResult = Util.matchNumber(program, i);
                if (matchResult.type == 1) {
                    result.add(Token.newDoubleLit(matchResult.value));
                }
                else {
                    result.add(Token.newIntLit(matchResult.value));
                }
                i = matchResult.newPos;
            }
            else {
                char currChar = program.charAt(i);
                int tagCode = -1;
                if (currChar == ':' || currChar == '|'
                        || currChar == '&' || currChar == '=') {
                    tagCode = Util.getSymbolCode(program.substring(i, i + 2));
                    i += 2;
                }
                else {
                    tagCode = Util.getSymbolCode(program.substring(i, i + 1));
                    i += 1;
                }
                if (tagCode == -1)
                    Util.abort("unkonwn symbol");
                result.add(Token.newToken(tagCode));
            }
        }
        return result;
    }
}
