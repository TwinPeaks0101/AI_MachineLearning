import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class KnowledgeBaseReader {
    private static String filePath;
    private static List<String> facts = new ArrayList<>();
    private static List<String> rules = new ArrayList<>();

    public static String getFilePath() {
        return filePath;
    }

    public static List<String> getFacts() {
        return facts;
    }

    public static List<String> getRules() {
        return rules;
    }


    /**
     * read the Knowledge Base's file
     * fill up  the fact's list and the rule's list
     * @param filePath is the name of file
     * @throws IOException
     */
    public static void readKnowledgeBase(String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))){
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim(); // remove spaces before and after the line

                // If the line is a comment or empty, we ignore it
                if (line.startsWith("#") || line.isEmpty()) {
                    continue;
                }

                // if the line contains "->" is rule
                if (line.contains("->")) {
                    rules.add(line);
                } else {
                    facts.add(line);
                }
            }
        }catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
    }

    /**
     * print the facts
     */
    public static void printFacts(){
        System.out.println("# Facts");
        for (String fact : facts){
            System.out.println(" - " + fact);
        }
    }

    /**
     * print the rules
     */
    public static void printRules(){
        System.out.println("# Horn's rules");
        for (String rule : rules){
            System.out.println(" - " + rule);
        }
    }
}