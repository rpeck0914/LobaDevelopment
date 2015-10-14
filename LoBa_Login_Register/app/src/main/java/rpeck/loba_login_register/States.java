package rpeck.loba_login_register;

/**
 * Created by Robert Peck on 10/12/2015.
 */
public class States {

    //private int arraysize;
    private String[] states;

    public States(){}

    public States(int arraysize, String[] statenames) {
        //this.arraysize = arraysize;
        this.states = new String[arraysize];
        this.states = statenames;
    }

    public String[] getStates() {
        return states;
    }
}
