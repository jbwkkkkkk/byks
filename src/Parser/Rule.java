package Parser;

import java.util.ArrayList;

public class Rule {
    ArrayList<GrammarItem> rhs;
    int ruleNo;
    Rule(ArrayList<GrammarItem> rule, int ruleNo) {
        this.rhs = rule;
        this.ruleNo = ruleNo;
    }
}