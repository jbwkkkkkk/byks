package SDT;

import Parser.GrammarItem;
import java.util.*;

public interface GenQTAction {
    void action(int ruleNo, List<GrammarItem> args);
}
