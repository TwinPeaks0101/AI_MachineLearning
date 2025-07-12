import java.util.Objects;

class Term {
    String name;
    boolean isVariable;

    Term(String name, boolean isVariable) {
        this.name = name;
        this.isVariable = isVariable;
    }

    public String getName(){return this.name;}
    public void setName(String name){
        this.name = name;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Term term = (Term) o;
        return isVariable == term.isVariable && Objects.equals(name, term.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, isVariable);
    }

    @Override
    public String toString() {
        return name;
    }
}
