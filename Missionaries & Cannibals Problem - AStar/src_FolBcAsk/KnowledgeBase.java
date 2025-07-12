import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class KnowledgeBase {
    Map<Predicate, List<Predicate>> rules = new HashMap<>();
    Set<Predicate> facts = new HashSet<>();


    KnowledgeBase(){};

    public Map<Predicate, List<Predicate>> getRules() {return rules;}
    public Set<Predicate> getFacts() {return facts;}
    public void setRules(Map<Predicate, List<Predicate>> rules){this.rules = rules;}

    public void readKnowledgeBase(String path) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(path));
        String line;
        while((line = br.readLine()) != null){
            line = line.trim();
            if(line.isEmpty()) continue;
            if(!line.contains("->")){
                //FACT
                //rules.put(readPredicate(line), new ArrayList<Predicate>());
                facts.add(readPredicate(line));
            }else{
                //RULE (CONTAINS ->)
                String[] predicateArray = line.split("->");
                //Last element of predicate array : conclusion
                Predicate conclusion = readPredicate(predicateArray[1]);
                String[] premises = predicateArray[0].split("&&");
                List<Predicate> leftSideOfRule = new ArrayList<Predicate>();
                for(int i=0; i< premises.length; i++ ){

                    leftSideOfRule.add(readPredicate(premises[i]));
                    rules.put(conclusion,leftSideOfRule);
                }
            }
        }

    }
    public Predicate readPredicate(String str) {
        String[] strSplit = str.split("\\(");
        //if str : W(x,y)
        //strSplit[0] -- predicate's name : W
        //strSplit[1] -- "x,y)"
        //Remove last parenthesis, strSplit[1] : x,y
        strSplit[1] = strSplit[1].replace(")", "").trim();
        //System.out.println("strSplit[1]= " + strSplit[1]);
        //System.out.println("Predicate created");
        return new Predicate(strSplit[0], readTerms(strSplit[1]));

    }

    public List<Term> readTerms(String str){
        //str cases: x OR x,y OR West OR American,y OR American,West // No functional terms cause ORISTIKES PROTASEIS OUOUUOUO???????????
        List<Term> terms = new ArrayList<>();

        //case: str is variable or constant(only one)
        if(!str.contains(",")){
            if(isVar(str)) {
                terms.add(new Term(str,true));
            }else {
                terms.add(new Term(str, false));
            }
        }else{ // case: a list of var/const
            String[] termArray = str.split(",");
            for (String s : termArray) {
                if (isVar(s)){
                    terms.add(new Term(s, true));
                }else {
                    terms.add(new Term(s, false));
                }
            }
        }

        //System.out.println("Terms: "+ terms);
        return terms;
    }

    public boolean isVar(String str){
        return Character.isLowerCase(str.charAt(0));
    }

    @Override
    public String toString() {
        return "KnowledgeBase{" +
                "rules= " + rules +
                "}" +
                "{facts= " + facts +
                "}";
    }
}

