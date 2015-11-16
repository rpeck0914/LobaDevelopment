package rpeck.loba_login_register;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Robert Peck on 10/7/2015.
 */
public class UserLocalStore {
    //UserLocalStore Class Stores The User's Details Locally On The Device

    public static final String SP_NAME = "userDetails";

    SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context) {
        userLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    public void storeUserData(User user) {
        //Stores The User's Information In A Local Database
        SharedPreferences.Editor userLocalDatabaseEditor  = userLocalDatabase.edit();
        userLocalDatabaseEditor.putString("name", user.mName);
        userLocalDatabaseEditor.putString("username", user.mUserName);
        userLocalDatabaseEditor.putString("password", user.mPassword);
        userLocalDatabaseEditor.putString("city", user.mCity);
        userLocalDatabaseEditor.putString("state", user.mState);
        userLocalDatabaseEditor.putInt("cityid", user.mCityID);
        userLocalDatabaseEditor.commit();
    }

    public void setUserLoggedIn(boolean loggedIn) {
        //Sets The User's Log In Status To True
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.putBoolean("loggedIn", loggedIn);
        userLocalDatabaseEditor.commit();
    }

    public void clearUserData() {
        //Clears The User's Data From The Local Database When They Log Out
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.clear();
        userLocalDatabaseEditor.commit();
    }

    public User getLoggedInUser() {
        //Retrieves The User's Information Stored Locally And Creates The User To Be Used
        if (userLocalDatabase.getBoolean("loggedIn", false) == false) {
            return null;
        }

        String name = userLocalDatabase.getString("name", "");
        String username = userLocalDatabase.getString("username", "");
        String password = userLocalDatabase.getString("password", "");
        String city = userLocalDatabase.getString("city", "");
        String state = userLocalDatabase.getString("state", "");
        int cityid = userLocalDatabase.getInt("cityid", 1);

        User user = new User(name, username, password, city, state, cityid);
        return user;
    }

    public void selectedCityAndState(String name, String type) {
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        if(type == "State") {
            userLocalDatabaseEditor.putString("selectedState", name);
        } else if(type == "City") {
            userLocalDatabaseEditor.putString("selectedCity", name);
        }
        userLocalDatabaseEditor.commit();
    }

    public String getSelectedCityAndState(String type) {

        String name = null;

        if(type == "selectedState") {
            name = userLocalDatabase.getString(type, "");
        } else if (type == "selectedCity") {
            name = userLocalDatabase.getString(type, "");
        }
        return name;
    }
}
