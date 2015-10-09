package rpeck.loba_login_register;

/**
 * Created by Robert Peck on 10/7/2015.
 */
public class User {
    String mName, mUserName, mPassword;

    public User(String name, String username, String password) {
        this.mName = name;
        this.mUserName = username;
        this.mPassword = password;
    }

    public User(String username, String password) {
        this.mUserName = username;
        this.mPassword = password;
        this.mName = "";
    }
}
