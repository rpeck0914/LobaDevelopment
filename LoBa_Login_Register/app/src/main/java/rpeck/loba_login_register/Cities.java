package rpeck.loba_login_register;

/**
 * Created by Robert Peck on 10/11/2015.
 */
public class Cities {
    int arraySize;
    int state_id;
    String[] citiesArray;

    public Cities(int arraySize, int state_id, String[] citiesArray) {
        this.arraySize = arraySize;
        this.state_id = state_id;
        this.citiesArray = citiesArray;
        this.citiesArray = new String[arraySize];
    }

    public Cities(int state_id) {
        this.state_id = state_id;
    }
}
