import java.util.List;
import java.util.Objects;

public class Rule {
    List<Predicate> premises;
    Predicate conclusion;

    Rule(List<Predicate> premises, Predicate conclusion){
        this.premises = premises;
        this.conclusion = conclusion;
    }

    @Override
    public int hashCode() {
        return Objects.hash(premises, conclusion);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rule rule = (Rule) o;
        return Objects.equals(premises, rule.premises) && Objects.equals(conclusion, rule.conclusion);
    }

    @Override
    public String toString() {
        return "Rule{" +
                "leftSide=" + premises +
                ", rightSide=" + conclusion +
                '}';
    }
}
