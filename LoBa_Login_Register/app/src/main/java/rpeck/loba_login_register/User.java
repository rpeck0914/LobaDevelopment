package rpeck.loba_login_register;

/**
 * Created by Robert Peck on 10/7/2015.
 */
public class User {
    //User Class Creates Users With All Their Data

    String mName, mUserName, mPassword, mCity, mState;
    int mCityID;

    public User(String name, String username, String password, String city, String state, int cityid) {
        this.mName = name;
        this.mUserName = username;
        this.mPassword = password;
        this.mCity = city;
        this.mState = state;
        this.mCityID = cityid;
    }

    public User(String username, String password) {
        this.mUserName = username;
        this.mPassword = password;
        this.mName = "";
    }
}
