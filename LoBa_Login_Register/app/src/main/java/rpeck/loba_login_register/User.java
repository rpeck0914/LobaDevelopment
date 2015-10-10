package rpeck.loba_login_register;

/**
 * Created by Robert Peck on 10/7/2015.
 */
public class User {
    String mName, mUserName, mPassword, mCity, mState;

    public User(String name, String username, String password, String city, String state) {
        this.mName = name;
        this.mUserName = username;
        this.mPassword = password;
        this.mCity = city;
        this.mState = state;
    }

    public User(String username, String password) {
        this.mUserName = username;
        this.mPassword = password;
        this.mName = "";
    }
}
