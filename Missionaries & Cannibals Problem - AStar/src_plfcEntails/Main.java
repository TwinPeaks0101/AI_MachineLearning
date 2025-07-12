import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        if (args.length > 0) {
            KnowledgeBaseReader.readKnowledgeBase(args[0]);
            KnowledgeBase base = new KnowledgeBase();

            Scanner in = new Scanner(System.in);
            System.out.print("Give a query: ");
            String input = in.nextLine();
            in.close();
            PLFCEntails.entails(base, input);
        }
        else{
            System.out.println("No file detected.");
        }
    }
}
