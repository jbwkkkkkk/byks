package Parser;

import java.util.*;

public class GrammarVar extends GrammarItem {
    Set<Rule> rhs;
    GrammarVar(String value) {
        super(value);
        rhs = new HashSet<>();
    }
    public void addRule(ArrayList<GrammarItem> rhs, int ruleNo) {
        Rule rule = new Rule(rhs, ruleNo);
        this.rhs.add(rule);
    }
}
