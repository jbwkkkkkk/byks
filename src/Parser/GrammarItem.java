package Parser;

import java.util.*;

public class GrammarItem {
    String value;
    Set<GrammarItem> first;
    Set<GrammarItem> follow;
    GrammarItem(String value) {
        this.value = value;
        first = new HashSet<>();
        follow = new HashSet<>();
    }

    @Override
    public String toString() {
        return value;
    }
}
