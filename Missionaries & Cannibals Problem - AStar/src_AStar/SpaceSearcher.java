import java.util.ArrayList;
import java.util.HashSet;

public class SpaceSearcher {
    private ArrayList<State> frontier;
    private HashSet<State> closedSet;

    /*constructor*/
    SpaceSearcher(){
        this.frontier = new ArrayList<>();
        this.closedSet = new HashSet<>();
    }

    public State AStarClosedSet(State initialState, int steps) throws CloneNotSupportedException {
        int K = steps;
        if(initialState.isFinal()) return initialState;
        K--;
        this.frontier.add(initialState);

        while(!frontier.isEmpty()){
            if (K < 0) return null;
            State currentState = this.frontier.removeFirst();
            if(currentState.isFinal()){
                return currentState;
            }

            if (!closedSet.contains(currentState)) {
                closedSet.add(currentState);
                frontier.addAll(currentState.getChildren());
            }
        }
        return null;
    }
}
