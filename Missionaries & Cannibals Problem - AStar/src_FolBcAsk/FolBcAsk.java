import java.io.IOException;
import java.util.*;

public class FolBcAsk {
    public static void main(String[] args) throws IOException {
    
        KnowledgeBase kb = new KnowledgeBase();
        if(args.length >0){
            kb.readKnowledgeBase(args[0]);
        }else{
            kb.readKnowledgeBase("KB/kb1.txt");
        }
        
        //Print Knowledge Base
        System.out.println(kb.rules);
        System.out.println(kb.facts);

        Scanner in = new Scanner(System.in);
        System.out.print("Give a query: ");
        String input = in.nextLine();
        in.close();

        Predicate query = kb.readPredicate(input);
        List<Predicate> goals = new ArrayList<>();
        goals.add(query);
        //initialize unifier before calling backward chaining
        Unifier unifier = new Unifier();
        Set<Predicate> visitedGoals = new HashSet<>();
        Map<String, String> result = folBcAsk(kb, goals, unifier.getSubstitutions(), unifier, visitedGoals);

        if (result == null) {
            System.out.println("NO");

        } else {

            System.out.println("YES");
            System.out.println("Substitution the proof did to answer query: ");
            System.out.println(result);
        }


    }


    public static Map<String, String> folBcAsk(KnowledgeBase kb, List<Predicate> goals, Map<String, String> bindingList, Unifier unifier, Set<Predicate> visitedGoals) {
        // If no more goals, return the current binding list
        if (goals.isEmpty()) {
            return bindingList;
        }

        List<Predicate> newGoals = new ArrayList<>(goals);

        Predicate cur_goal = newGoals.remove(0);
        System.out.println("Current Goal: " + cur_goal);

        // Check if this goal has already been processed to prevent infinite recursion
        if (visitedGoals.contains(cur_goal)) {
            System.out.println("Goal already processed, skipping: " + cur_goal);
            return null;
        }

        // Check if the current goal matches any known fact
        for (Predicate fact : kb.getFacts()) {
            Unifier factUnifier = new Unifier();
            if (factUnifier.unify(cur_goal, fact)) {
                System.out.println("Matched with Fact: " + fact);
                bindingList.putAll(factUnifier.getSubstitutions());
                visitedGoals.add(cur_goal);

                // Recursively process remaining goals
                Map<String, String> result = folBcAsk(kb, newGoals, bindingList, unifier, visitedGoals);
                if (result != null) {
                    return result; // Return success if proof is found
                }

                visitedGoals.remove(cur_goal);
            }
        }

        // Check if the current goal matches any rule in the knowledge base
        for (Map.Entry<Predicate, List<Predicate>> rule : kb.getRules().entrySet()) {
            Predicate conclusion = rule.getKey();
            List<Predicate> premises = rule.getValue();

            Unifier ruleUnifier = new Unifier();
            if (ruleUnifier.unify(cur_goal, conclusion)) {
                System.out.println("Unified with Rule: " + conclusion);
                visitedGoals.add(cur_goal);


                processPremises(premises, newGoals, bindingList);

                // Recursively call folBcAsk
                Map<String, String> result = folBcAsk(kb, newGoals, bindingList, unifier, visitedGoals);
                if (result != null) {
                    return result; // Return success if proof is found
                }

                // Remove goals referred to previous recursion
                for (int i = 0; i < premises.size(); i++) {
                    newGoals.remove(0);
                }

            }
        }

        // Goal cannot be proven
        System.out.println("Failed to prove: " + cur_goal);
        return null;
    }

    private static void processPremises(List<Predicate> premises, List<Predicate> goals, Map<String, String> bindingList) {
        for (Predicate premise : premises) {
            // Standardize variables in all premises of the rule
            Map<String, String> standardSubs = new HashMap<>();
            premise.standardize(standardSubs);

            // Apply current substitutions to the standardized premise
            for (Term term : premise.getTerms()) {
                if (term.isVariable && bindingList.containsKey(term.getName())) {
                    term.setName(bindingList.get(term.getName()));
                }
            }

            // Add the standardized premise to the goal list
            goals.add(premise);
        }
    }

}
