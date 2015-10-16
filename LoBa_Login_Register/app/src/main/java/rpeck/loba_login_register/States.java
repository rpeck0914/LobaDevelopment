package rpeck.loba_login_register;

/**
 * Created by Robert Peck on 10/12/2015.
 */
public class States {
    //States Class Creates A Class That Stores The Array Of States
    
    public String[] states;
    public int[] stateID;

    public States(){}

    public States(int arraysize, String[] statenames, int[] stateID) {
        this.states = new String[arraysize];
        this.stateID = new int[arraysize];
        this.states = statenames;
        this.stateID = stateID;
    }
}
