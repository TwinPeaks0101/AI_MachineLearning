import java.util.ArrayList;
import java.util.Objects;

public class State implements Comparable<State>, Cloneable{
    private int f, h, g; /* costs */
    private State father;
    private int catsLeft, miceLeft,boatLeft,catsRight, miceRight, boatRight;
    private final int boatCapacity;
    private final int totalTime;

    //TODO: Get rid of arrays!
    /*constructors*/
    public State(int catsLeft,int miceLeft,int boatLeft, int catsRight,int miceRight, int boatRight, int boatCapacity)
    {
        this.h = 0;
        this.g = 0;
        this.f = 0;
        this.father = null;
        this.catsLeft = catsLeft;
        this.miceLeft = miceLeft;
        this.boatLeft = boatLeft;
        this.catsRight = catsRight;
        this.miceRight = miceRight;
        this.boatRight = boatRight;
        this.boatCapacity = boatCapacity;
        this.totalTime = 0;
    }
    // deep copy constructor
    @Override
    public State clone() throws CloneNotSupportedException {
        return (State) super.clone();
    }

    public int getF() {return this.f;}
    public void setF(int f) {this.f = f;}

    public int getG() {return this.g;}
    public void setG(int g) {this.g = g;}

    public int getH() {return this.h;}
    public void setH(int h) {this.h = h;}

    public State getFather() {return this.father;}
    public void setFather(State f) {this.father = f;}

    public int getCatsLeft() {return this.catsLeft;}
    public void setCatsLeft(int catsLeft) {this.catsLeft = catsLeft;}

    public int getMiceLeft() {return miceLeft;}
    public void setMiceLeft(int miceLeft) {this.miceLeft = miceLeft;}

    public int getBoatLeft() {return this.boatLeft;}
    public void setBoatLeft(int boatLeft) {this.boatLeft = boatLeft;}

    public int getCatsRight() {return this.catsRight;}
    public void setCatsRight(int catsRight) {this.catsRight = catsRight;}

    public int getMiceRight() {return miceRight;}
    public void setMiceRight(int miceRight) {this.miceRight = miceRight;}

    public int getBoatRight() {return this.boatRight;}
    public void setBoatRight(int boatRight) {this.boatRight = boatRight;}
    public int getBoatCapacity(){return this.boatCapacity;}

    /**
     *
     */
    public boolean isFinal() {
        return this.getH() == 0;
    }

    /** Action #1 LOAD AND MOVE BOAT:
     * A valid action should meet these restrictions:
     * 1) Boat shouldn't be empty.
     * 2) Total number to be loaded shouldn't exceed boat capacity.
     * 3) If boat is on the left/right side:
     *      cats/mice to be loaded shouldn't exceed the existing number on that side
     * 4) Remaining number of cats shouldn't exceed number of mice neither on current side nor on boat!
     * 5) Number of cats that arrive at the other side shouldn't exceed the number of mice.
     * @param cats number of cats to load in the boat
     * @param mice number of mice to load in the boat
     */
    public boolean loadBoat(int cats, int mice){
        //restriction 1
        if(cats == 0 && mice == 0) {
            //System.out.println("Boat shouldn't be empty.");
            return false;
        }
        //restriction 2
        if(cats + mice > this.getBoatCapacity()){
            //System.out.println("Total number to be loaded shouldn't exceed boat capacity.");
            return false;
        }
        //restriction 4 for boat
        if(cats > mice && mice > 0){
            //System.out.println("Number of cats shouldn't exceed number of mice on boat.");
            return false;
        }


        //boat is at left side
        if(this.getBoatLeft() == 1){
            //restriction 3
            if(cats > this.getCatsLeft() || mice > this.getMiceLeft()) {
                //System.out.println("Cats/mice to be loaded shouldn't exceed the existing number on left side.");
                return false;
            }
            //restriction 4 for left side
            if(((this.getCatsLeft() - cats) > (this.getMiceLeft() - mice)) && (this.getMiceLeft()-mice)!=0){
                //System.out.println("Remaining number of cats shouldn't exceed number of mice on left side.");
                return false;
            }
            //restriction 5 for right side
            if(((cats + this.getCatsRight()) > (mice + this.getMiceRight())) && (mice + this.getMiceRight()!=0)){
                //System.out.println("Number of cats that arrive at the right side shouldn't exceed the number of mice.");
                return false;
            }

            //update values for valid move:
            this.setCatsLeft(this.getCatsLeft() - cats);
            this.setCatsRight(this.getCatsRight() + cats);
            this.setMiceLeft(this.getMiceLeft() - mice);
            this.setMiceRight(this.getMiceRight() + mice);
            //move boat to the right side
            this.setBoatLeft(0);
            this.setBoatRight(1);
            return true;
        }
        //boat is at right side
        if(this.getBoatRight() == 1){
            //restriction 3
            if(cats > this.getCatsRight() || mice > this.getMiceRight()) {
                //System.out.println("Cats/mice to be loaded shouldn't exceed the existing number on right side.");
                return false;
            }
            //restriction 4 for right side
            if(((this.getCatsRight() - cats) > (this.getMiceRight() - mice)) && (this.getMiceRight()-mice)!=0) {
                //System.out.println("Remaining number of cats shouldn't exceed number of mice on right side.");
                return false;
            }
            //restriction 5
            if(((cats + this.getCatsLeft()) > (mice + this.getMiceLeft())) && (mice + this.getMiceLeft()!=0)) {
                //System.out.println("Number of cats that arrive at the left side shouldn't exceed the number of mice.");
                return false;
            }

            //update values for valid move:
            this.setCatsLeft(this.getCatsLeft() + cats);
            this.setCatsRight(this.getCatsRight() - cats);
            this.setMiceLeft(this.getMiceLeft() + mice);
            this.setMiceRight(this.getMiceRight() - mice);
            // move boat to the left side
            this.setBoatLeft(1);
            this.setBoatRight(0);
            return true;
        }
        return false;
    }

    public void evaluate()
    {
        this.setF(this.getH() + this.getG());
    }

    public void heuristic() {
        int remainingCatsLeft = this.getCatsLeft();
        int remainingMiceLeft = this.getMiceLeft();
        int boatCapacity = this.getBoatCapacity();

        // calculate the total number of individuals remaining on the left side
        int totalRemainingIndividuals = remainingCatsLeft + remainingMiceLeft;

        // calculate how many times the boat needs to be filled to pass all the individuals to the right
        int boatTripsNeeded = (int) Math.ceil((double) totalRemainingIndividuals / boatCapacity);

        // If the boat is on the right and there are individuals on the left, it takes an extra trip to return.
        if (this.getBoatLeft() == 0 && totalRemainingIndividuals > 0) {
            boatTripsNeeded += 1;
        }

        this.setH(boatTripsNeeded);
    }

    /** Generates all possible combinations of cats and mice
     * and calls loadBoat() to check whether each combination is valid or not.
     * if the combination is valid, the new state generated sets its father as the caller, gets printed and added to
     * the children list.
     * TODO: Finish description
     * @return  an arraylist of valid children for the caller.
     */
    public ArrayList<State> getChildren() throws CloneNotSupportedException {
        ArrayList<State> children = new ArrayList<>();
        for(int cats = 0; cats <= this.getBoatCapacity(); cats++){
            for(int mice = 0; mice <= this.getBoatCapacity(); mice++){
                State child = this.clone();
                //System.out.println("New state for: "+cats+" cats and "+mice+" mice:");
                if(child.loadBoat(cats,mice)){
                    child.setFather(this);
                    child.heuristic();
                    child.setG(this.getG() + 1);
                    child.evaluate();

                    children.add(child);
                    //if child.equals(this.getFather()) => cycle?
                    // if : evaluation? calculate f ????????
                }
            }
        }
        //System.out.println("Found "+ children.size()+ " valid states.");
        return children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof State state)) return false;
        return getH() == state.getH() && getCatsLeft() == state.getCatsLeft() && getMiceLeft() == state.getMiceLeft() && getBoatLeft() == state.getBoatLeft() && getCatsRight() == state.getCatsRight() && getMiceRight() == state.getMiceRight() && getBoatRight() == state.getBoatRight() && getBoatCapacity() == state.getBoatCapacity();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getH(), getCatsLeft(), getMiceLeft(), getBoatLeft(), getCatsRight(), getMiceRight(), getBoatRight(), getBoatCapacity());
    }

    @Override
    public int compareTo(State s)
    {
        return Double.compare(this.f, s.getF()); // compare based on the heuristic score.
    }

    @Override
    public String toString() {
        return "State{" +
                "f=" + f +
                ", h=" + h +
                ", g=" + g +
                ", father=" + father +
                ", totalTime=" + totalTime +
                '}';
    }

    /**
     *  Representation of states.
     *  (number of cats on the left side, number of mice on the left side) if boat exists on the left side print: "[]"
     *  ||
     *  if boat exists on the right side print: "[]" (number of cats on the right side, number of mice on the right side)
     */
    public void print() {
        System.out.println("(" + this.getCatsLeft() + " cats," + this.getMiceLeft() + " mice) " + boatPrintPosition(this.getBoatLeft()) + " || " + boatPrintPosition(this.getBoatRight()) + " (" + this.getCatsRight() + " cats," + this.getMiceRight() + " mice)");
        System.out.println("---------------------------------------------");
    }

    /** Used for printing boat.
     * @param boatExists takes two values (1 or 0) whether the boat is on the side we examine or not
     */
    private String boatPrintPosition(int boatExists){
        if(boatExists == 0) return "";
        else return "[]";
    }


}
