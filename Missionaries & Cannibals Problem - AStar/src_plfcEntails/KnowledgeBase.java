import java.util.*;

public class KnowledgeBase {
    Map<String, List<String>> rules = new HashMap<>(); // Key:The conditions of the Horn proposal, Value:The conclusion of the Horn proposition
    Set<String> facts = new HashSet<>(); // The facts of the knowledge base

    public Map<String, List<String>> getRules() {
        return rules;
    }

    public Set<String> getFacts() {
        return facts;
    }

    public void setRules(Map<String, List<String>> rules){
        this.rules = rules;
    }

    public void setFacts(Set<String> facts) {
        this.facts = facts;
    }

    public void fillInRules(){
        List<String> rules = KnowledgeBaseReader.getRules();
        Map<String, List<String>> rulesMap = new HashMap<>();
        for(String rule : rules){ // for rule in rules
            List<String> tempPremise = new ArrayList<>(); // initialize sub list of premise's literals
            String[] result = rule.split("->"); // split rule (x & y -> z)
            String[] subResult = result[0].trim().split("&"); // split premise (x & y)
            for (String item : subResult){ // put every item(literal) to key part(The conditions of the Horn proposal) of map
                tempPremise.add(item.trim());
            }
            rulesMap.put(result[1].trim(), tempPremise); // final add (key, value) pairs to map. value is the conclusion of the Horn proposition
        }
        setRules(rulesMap);
        //System.out.println(this.rules);
    }

    public void fillInFacts(){
        List<String> factsList = KnowledgeBaseReader.getFacts();
        Set<String> facts = new HashSet<>();
        for (String item : factsList){
            facts.add(item);
        }
        setFacts(facts);
        //System.out.println(this.facts);
    }
}
