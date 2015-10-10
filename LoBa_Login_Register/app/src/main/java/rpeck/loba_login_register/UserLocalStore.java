package rpeck.loba_login_register;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Robert Peck on 10/7/2015.
 */
public class UserLocalStore {

    public static final String SP_NAME = "userDetails";

    SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context) {
        userLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    public void storeUserData(User user) {
        SharedPreferences.Editor userLocalDatabaseEditor  = userLocalDatabase.edit();
        userLocalDatabaseEditor.putString("name", user.mName);
        userLocalDatabaseEditor.putString("username", user.mUserName);
        userLocalDatabaseEditor.putString("password", user.mPassword);
        userLocalDatabaseEditor.putString("city", user.mCity);
        userLocalDatabaseEditor.putString("state", user.mState);
        userLocalDatabaseEditor.commit();
    }

    public void setUserLoggedIn(boolean loggedIn) {
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.putBoolean("loggedIn", loggedIn);
        userLocalDatabaseEditor.commit();
    }

    public void clearUserData() {
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.clear();
        userLocalDatabaseEditor.commit();
    }

    public User getLoggedInUser() {
        if (userLocalDatabase.getBoolean("loggedIn", false) == false) {
            return null;
        }

        String name = userLocalDatabase.getString("name", "");
        String username = userLocalDatabase.getString("username", "");
        String password = userLocalDatabase.getString("password", "");
        String city = userLocalDatabase.getString("city", "");
        String state = userLocalDatabase.getString("state", "");

        User user = new User(name, username, password, city, state);
        return user;
    }
}
