import java.util.*;

public class Predicate {
    String name;
    List<Term> terms;

    Predicate(String name, List<Term> terms){
        this.name = name;
        this.terms = terms;
    }

    public String getName(){
        return this.name;
    }
    public List<Term> getTerms(){
        return this.terms;
    }

    //Function to rename variables in each recursion level.
    public void standardize(Map<String, String> substitutions) {
        for (Term term : terms) {
            if (term.isVariable) {
                //Generate a new variable name or reuse an existing substitution
                String newVarName = substitutions.computeIfAbsent(term.getName(), k -> generateUniqueVariable(term.getName()));
                //Rename current term
                term.setName(newVarName);
            }
            //Constants are not modified
        }
    }

    private String generateUniqueVariable(String name) {
        return name + "_" + UUID.randomUUID().toString().replace("-", "").substring(0, 3);
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Predicate predicate = (Predicate) o;
        return Objects.equals(name, predicate.name) && Objects.equals(terms, predicate.terms);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, terms);
    }

    @Override
    public String toString() {
        return "Predicate{" +
                "name='" + name + '\'' +
                ", terms=" + terms +
                '}';
    }
}

