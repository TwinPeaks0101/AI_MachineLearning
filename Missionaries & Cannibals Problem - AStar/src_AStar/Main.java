import java.util.ArrayList;
import java.util.Collections;

public class Main {
    public static void main(String[] args) throws CloneNotSupportedException {
        int N, M, K;
        if (args.length > 0) {
            N = Integer.parseInt(args[0]);
            M = Integer.parseInt(args[1]);
            K = Integer.parseInt(args[2]);
        }else{
            N = 20; /* number of cats/mice */
            M = 4; /* max boat capacity */
            K = 100; /* max number of river-crossings */
//            K = 2147483646;
        }

        State initialState = new State(N,N,1,0,0,0,M);  /* initial state */
//        initialState.print();
        initialState.heuristic();
        System.out.println("Initial State : " + initialState.toString());

        SpaceSearcher searcher = new SpaceSearcher();
        long start = System.currentTimeMillis();
        State terminalState = searcher.AStarClosedSet(initialState, K);
        long end = System.currentTimeMillis();
        if(terminalState == null) System.out.println("Could not find a solution.");
        else
        {
            // print the path from beginning to start.
            State temp = terminalState; // begin from the end.
            ArrayList<State> path = new ArrayList<>();
            path.add(terminalState);
            while(temp.getFather() != null) // if father is null, then we are at the root.
            {
                path.add(temp.getFather());
                temp = temp.getFather();
            }
            // reverse the path and print.
            Collections.reverse(path);
            for(State item: path)
            {
                item.print();
            }
            System.out.println();
            System.out.println("Search time:" + (double)(end - start) / 1000 + " sec.");  // total time of searching in seconds.
        }
    }
}