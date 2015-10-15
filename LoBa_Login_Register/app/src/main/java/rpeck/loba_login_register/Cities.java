package rpeck.loba_login_register;

/**
 * Created by Robert Peck on 10/11/2015.
 */
public class Cities {

    int state_id;
    public String[] citiesArray;
    public int[] cityID;

    public Cities(int arraySize, int state_id, String[] citiesArray, int[] cityID) {
        this.state_id = state_id;
        this.citiesArray = new String[arraySize];
        this.cityID = new int[arraySize];
        this.citiesArray = citiesArray;
        this.cityID = cityID;

    }

    public Cities(int state_id) {
        this.state_id = state_id;
    }
}
