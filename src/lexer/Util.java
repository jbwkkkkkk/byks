package lexer;

public class Util {
    public static void abort() {
        System.exit(-1);
    }
    public static void ScannerAbort(int lineNo) {
        System.out.printf("at line %d: unknown token\n", lineNo);
        abort();
    }
}
