import java.util.*;

public class PLFCEntails {
    public static boolean entails(KnowledgeBase kb, String query) {
        // Load rules and facts
        kb.fillInRules(); // fill in rules from knowledge base
        kb.fillInFacts(); // fill in initial facts known to be true
        Map<String, List<String>> rules = kb.getRules(); // put rules to map
        Set<String> facts = kb.getFacts(); // put initial facts to set

        // Initialization of auxiliary structures
        Map<String, Integer> count = new HashMap<>(); // initialize map of conclusions as key and count how many literals need for fire up conclusion
        Map<String, Boolean> inferred = new HashMap<>(); // initialization map of the conclusions inferred
        Queue<String> agenda = new LinkedList<>(facts); // initialization of the queue's search front with initial facts known to be true

        // Data preparation
        for (var entry : rules.entrySet()) {
            String conclusion = entry.getKey(); // String to get its value of conclusion from the map
            List<String> premises = entry.getValue(); // list to get the values of premises from the map
            count.put(conclusion, premises.size()); // count how many literals need for fire up conclusion
            inferred.put(conclusion, false); // initialize every conclusion to false //
            premises.forEach(premise -> inferred.putIfAbsent(premise, false)); // run through the list of premises and set any conditions that do not exist to false
        }
        System.out.println("---------------------Initial Data--------------------------------------------------------");
        System.out.println("Rules: " + rules);
        System.out.println("Agenda: " + agenda);
        System.out.println("count: " + count);
        System.out.println("inferred: " + inferred);
        System.out.println("------------------------------------------------------------------------------------------");

        // Edit agenda
        while (!agenda.isEmpty()){ // while the front search is not empty
            String p = agenda.poll(); // pop up the symbol from the agenda
            if (p.equals(query)) { // if the symbol popped up is equal to the query return true
                System.out.println("The query '" + query + "' is successfully concluded");
                return true;
            }
            System.out.println("---------------------Tree Data--------------------------------------------------------");
            System.out.println("Pop up symbol: " + p);
            if (inferred.isEmpty()){
                System.out.println("Τhe file does not have the correct syntax");
                return false;
            }
            if (!inferred.get(p)) {
                inferred.put(p, true); // the symbol extracted from the agenda do it as true
                System.out.println("inferred: " + inferred);
                for (var entry : rules.entrySet()) {
                    if (entry.getValue().contains(p)) { // if the symbol is in premises decrease
                        int newCount = count.merge(entry.getKey(), -1, Integer::sum);
                        if (newCount == 0) { // if count is zero add to the front search
                            agenda.add(entry.getKey());
                            System.out.println("Fire Up: " + entry.getKey());
                        }
                    }
                }
                System.out.println("count: " + count);
                System.out.println("Agenda: " + agenda);
                System.out.println("------------------------------------------------------------------------------------------");
            }
        }

        System.out.println("The query '" + query + "' is not concluded");
        return false;
    }
}


