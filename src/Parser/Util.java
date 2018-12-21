package Parser;

import Lexer.Token;

import java.io.*;
import java.util.*;

public class Util {
    private static Map<String, GrammarItem> val2item = new HashMap<>();
    private static void putToMap(String value, GrammarItem itemRef) {
        if (val2item.get(value) == null) {
            val2item.put(value, itemRef);
        }
    }
    private static GrammarItem getFromMap(String value) {
        GrammarItem item = val2item.get(value);
        if (item != null) return item;
        System.err.println("Grammar Item Not Found");
        System.exit(-1);
        return item;
    }
    public static Set<GrammarItem> readBNFFile(String name) throws IOException {
        putToMap("\"<e>\"", new GrammarLit("\"<e>\""));
        Set<GrammarItem> items = new HashSet<>();
        items.add(getFromMap("\"<e>\""));
        FileInputStream in = new FileInputStream(name);
        Scanner scanner = new Scanner(in);
        GrammarVar var = new GrammarVar("");
        ArrayList<GrammarItem> rule = new ArrayList<>();
        String line;
        int ruleNo = 0;
        while (scanner.hasNext()) {
            line = scanner.nextLine();
            line = line.trim();
            if (line.startsWith("<")) {
                var = new GrammarVar(getFirstGrammarVarName(line));
                putToMap(var.value, var);
                items.add(getFromMap(var.value));
                line = line.substring(line.indexOf(":=") + 2).trim();
            }
            else {
                line = line.substring(line.indexOf("|") + 1).trim();
            }

            rule = new ArrayList<>();
            do {
                GrammarItem item;
                if (line.startsWith("<"))
                    item = new GrammarVar(getFirstGrammarVarName(line));
                else item = new GrammarLit(getFirstGrammarLitValue(line));
                putToMap(item.value, item);
                //items.add(getFromMap(item.value));
                line = line.substring(item.value.length()).trim();
                rule.add(getFromMap(item.value));
            } while (!line.isEmpty());
            ((GrammarVar)(getFromMap(var.value))).addRule(rule, ruleNo);
            ruleNo += 1;
        }
        ((GrammarVar)(getFromMap(var.value))).addRule(rule, ruleNo);
        items.add(getFromMap(var.value));
        return items;
    }
    private static String getFirstGrammarVarName(String line) {
        line = line.trim();
        int start = line.indexOf('<');
        int end  = line.indexOf('>');
        return line.substring(start, end + 1);
    }
    private static String getFirstGrammarLitValue(String line) {
        line = line.trim();
        int start = line.indexOf('\"');
        int end = line.indexOf('\"', start + 1);
        return line.substring(start, end + 1);
    }
    public static Map<GrammarItem, Set<GrammarLit>> getFirstSet(Set<GrammarItem> items) {
        Map<GrammarItem, Set<GrammarLit>> result = new HashMap<>();
        for (GrammarItem item : items) {
            result = getFirstSet(item, result, items);
        }
        return result;
    }
    private static Map<GrammarItem, Set<GrammarLit>>
    getFirstSet(GrammarItem item, Map<GrammarItem, Set<GrammarLit>> knownMap, Set<GrammarItem> items) {
        if (knownMap.get(item) != null) {
            return knownMap;
        }
        else {
            Set<GrammarLit> itemFirst = new HashSet<>();
            if (item instanceof GrammarLit) {
                itemFirst.add((GrammarLit)item);
                knownMap.put(item, itemFirst);
            }
            else {
                GrammarVar var = (GrammarVar)item;
                for (Rule rules : var.rhs) {
                    boolean containE = true;
                    int i = 0;
                    while (containE && i < rules.rhs.size()) {
                        containE = false;
                        knownMap = getFirstSet(rules.rhs.get(i), knownMap, items);
                        Set<GrammarLit> prevFirst = knownMap.get(item);
                        Set<GrammarLit> newSet = new HashSet<>(knownMap.get(rules.rhs.get(i)));
                        if (newSet.contains((GrammarLit)getFromMap("\"<e>\""))) {
                            containE = true;
                            newSet.remove((GrammarLit)getFromMap("\"<e>\""));
                        }
                        if (prevFirst == null) {
                            knownMap.put(item, newSet);
                        } else {
                            prevFirst.addAll(knownMap.get(rules.rhs.get(i)));
                        }
                        if (containE) i += 1;
                    }
                    if (i == rules.rhs.size()) {
                        knownMap.get(var).add((GrammarLit) (getFromMap("\"<e>\"")));
                    }
                }
            }
            return knownMap;
        }
    }
    private static Set<GrammarLit>
    getFirstSetOfAStringOfGrammarItem(List<GrammarItem> items,
                                       Map<GrammarItem, Set<GrammarLit>> firstSets) {
        Set<GrammarLit> result = new HashSet<>();
        GrammarLit e = (GrammarLit) getFromMap("\"<e>\"");
        boolean containE = false;
        for (GrammarItem item : items) {
            Set<GrammarLit> itemFirstSet = new HashSet<>();
            if (item instanceof GrammarLit) {
                itemFirstSet.add((GrammarLit)getFromMap(item.value));
                result.addAll(itemFirstSet);
                break;
            }
            else {
                itemFirstSet.addAll(firstSets.get(item));
                containE = itemFirstSet.contains(e);
                if (!containE) {
                    result.addAll(itemFirstSet);
                    break;
                }
                else {
                    itemFirstSet.remove(e);
                    result.addAll(itemFirstSet);
                }
            }
        }
        if (containE) result.add(e);
        return result;
    }
    public static Map<GrammarItem, Set<GrammarLit>>
    getFollowSet(Map<GrammarItem, Set<GrammarLit>> firstSet, Set<GrammarItem> items) {
        Map<GrammarItem, Set<GrammarLit>> followSet = new HashMap<>();
        for (GrammarItem item : items) {
            if (item instanceof GrammarVar) {
                followSet.put(getFromMap(item.value), new HashSet<>());
            }
        }
        Set<GrammarLit> eofSet = new HashSet<>();
        GrammarLit eof = new GrammarLit("<EOF>");
        putToMap("<EOF>", eof);
        eofSet.add(eof);
        followSet.put(getFromMap("<Program>"), eofSet);
        GrammarItem e = getFromMap("\"<e>\"");
        boolean isChanged = true;
        while (isChanged) {
            isChanged = false;
            for (GrammarItem item : items) {
                if (item instanceof GrammarVar) {
                    //System.out.println(item.value);
                    GrammarVar var = (GrammarVar) item;
                    for (Rule rule : var.rhs) {
                        for (int i = 0; i < rule.rhs.size(); i += 1) {
                            if (rule.rhs.get(i) instanceof GrammarLit) continue;
                            Set<GrammarLit> itemFollow =
                                    getFirstSetOfAStringOfGrammarItem(rule.rhs.subList(i + 1, rule.rhs.size()), firstSet);
                            if (itemFollow.contains(e)) {
                                itemFollow.remove(e);
                                itemFollow.addAll(followSet.get(var));
                            }
                            if (itemFollow.isEmpty()) {
                                itemFollow.addAll(followSet.get(var));
                            }
                            Set<GrammarLit> prevSet = followSet.get(getFromMap(rule.rhs.get(i).value));
                            int prevSize = prevSet.size();
                            prevSet.addAll(itemFollow);
                            int currSize = prevSet.size();
                            isChanged = isChanged ? isChanged : (currSize - prevSize != 0);
                        }
                    }
                }
            }
        }
        return followSet;
    }

    public static void LL1Parse(Map<GrammarItem, Set<GrammarLit>> firstSets,
                                Map<GrammarItem, Set<GrammarLit>> followSet,
                                Set<GrammarItem> items, List<Lexer.Token> tokenList,
                                Map<Integer, SDT.GenQTAction> actionMap) {

    }

    private static void printMap(Map<GrammarItem, Set<GrammarLit>> map) {
        for (GrammarItem item : map.keySet()) {
            System.out.println(item.value + " ==>");
            for (GrammarLit lit : map.get(item)) {
                System.out.print(lit.value + ", ");
            }
            System.out.println("\n---------------");
        }
    }
    public static void testReadBNFFile() throws IOException {
        Set<GrammarItem> items = readBNFFile("docs/grammar.cf");
        for (GrammarItem item : items) {
            System.out.println(item + " :=");
            if (item instanceof GrammarVar) {
                GrammarVar var = (GrammarVar)item;
                for (Rule rule : var.rhs) {
                    for (GrammarItem item1 : rule.rhs) {
                        System.out.print(item1 + " ");
                    }
                    System.out.println("; ruleNo = " + rule.ruleNo);
                }
                System.out.println("---------");
            }
        }
    }
    public static void testGetFirstSet() throws IOException {
        Set<GrammarItem> items = readBNFFile("docs/grammar.cf");
        Map<GrammarItem, Set<GrammarLit>> firstSets = getFirstSet(items);
        printMap(firstSets);
    }
    public static void testGetFollowSet() throws IOException {
        Set<GrammarItem> items = readBNFFile("docs/grammar.cf");
        Map<GrammarItem, Set<GrammarLit>> firstSets = getFirstSet(items);
        Map<GrammarItem, Set<GrammarLit>> followSets = getFollowSet(firstSets, items);
        printMap(followSets);
    }
    public static void main(String[] args) throws IOException {
        testGetFollowSet();
    }
}
