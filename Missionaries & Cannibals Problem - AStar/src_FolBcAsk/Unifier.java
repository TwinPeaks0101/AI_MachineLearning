import java.util.HashMap;
import java.util.Map;


public class Unifier {
    private Map<String, String> substitutions;

    Unifier(){
        this.substitutions = new HashMap<>();
    }

    public Map<String, String> getSubstitutions(){return this.substitutions;}

     boolean unify(Predicate p1, Predicate p2) {
         // Predicates must have the same name and number of terms
         if (!p1.getName().equals(p2.getName()) || p1.getTerms().size() != p2.getTerms().size()) {
             return false;
         }

         for (int i = 0; i < p1.getTerms().size(); i++) {
             Term term1 = p1.getTerms().get(i);
             Term term2 = p2.getTerms().get(i);
             if (!unify(term1, term2)) {
                 // Fail if any terms cannot unify
                 return false;
             }
         }
         // All terms unified successfully
         return true;
     }

    boolean unify(Term t1, Term t2) {
        if(t1.equals(t2)) return true; //constants or already unified variables

        if (!t1.isVariable && !t2.isVariable ) return t1.getName().equals(t2.getName());

        if (t1.isVariable) return substitute(t1.getName(), t2.getName());

        if(t2.isVariable) return substitute(t2.getName(), t1.getName());

        return false;

    }

    private boolean substitute(String variable, String value) {
        // Check for existing substitution for the variable
        if (substitutions.containsKey(variable)) {
            String currentValue = substitutions.get(variable);
            return unify(new Term(currentValue, false), new Term(value, false)); // Unify the current value with the new value
        }

        // Check if the value itself is a variable that already has a substitution
        if (substitutions.containsKey(value)) {
            return substitute(variable, substitutions.get(value));
        }

        // Add new substitution
        substitutions.put(variable, value);
        substitution(variable, value);
        return true;
    }

    private void substitution(String variable, String value) {
        for (Map.Entry<String, String> entry : substitutions.entrySet()) {
            if (entry.getValue().equals(variable)) {
                // Update substitutions
                substitutions.put(entry.getKey(), value);
            }
        }
    }


    @Override
    public String toString() {
        return substitutions.toString();
    }
}
