package rpeck.loba_login_register;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import junit.framework.Test;

public class MainActivity extends FragmentActivity implements View.OnClickListener{

    //Private Variables For The Main Activity Layout
    private TextView mLoggedInName, mLogOutLink;

    //Creates A Variable For UserLocalStore To Sore The Users Data Locally On Device
    private UserLocalStore userLocalStore;
    //// TODO: 10/15/2015 Populate State and City Spinners and set default to users data
    //// TODO: 10/15/2015 Get XML to the desired layout to match the concept
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();

        Fragment fragment = fm.findFragmentById(R.id.top_fragment_container);

        if (fragment == null) {
            fragment = new CityStateSpinnerFragment();
            fm.beginTransaction()
                    .add(R.id.top_fragment_container, fragment)
                    .commit();
        }

        //Sets The Layout Objects To Their Variables
        mLoggedInName = (TextView) findViewById(R.id.logged_in_name);

        mLogOutLink = (TextView) findViewById(R.id.log_out_link);

        //Instantiates UserLocalStore Variable To Save Logged In Users Data
        userLocalStore = new UserLocalStore(this);

        //On Click Listeners For Clicks On Buttons
        mLogOutLink.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //onStart Method Checks To See If User's Data Is Stored Locally And Logs Them In
        if(authenticate() == true) {
            displayUserDetails();
        } else {    //If Data Is Not Stored Locally, The Login Activity Is Called
            startActivity(new Intent(MainActivity.this, Login.class));
        }

    }

    //authenticate Method Checks To See If The User's Local Data Is Null, If So Loads The Login Intent
    private boolean authenticate() {
        if (userLocalStore.getLoggedInUser() == null) {
            //Intent intent = new Intent(this, Login.class);
            //startActivity(intent);
            return false;
        }
        return true;
    }

    //displayUserDetails Method Pulls The User Stored Locally And Sets The Layout To The User's Details
    private void displayUserDetails() {
        User user = userLocalStore.getLoggedInUser();

        mLoggedInName.setText(user.mName);

    }

    @Override
    public void onClick(View v) {
        //Switch Case For Reading The On Click Listeners
        switch (v.getId()) {
            case R.id.log_out_link:
                //If User Clicks Logout Button The Local Data Stored Is Cleared And Then Starts The Login Activity
                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);

                startActivity(new Intent(this, Login.class));
                break;
        }
    }

//    @Override
//    public void onBackPressed() {
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
//        dialogBuilder.setCancelable(false);
//        dialogBuilder.setTitle("Exit App");
//        dialogBuilder.setMessage("Are You Sure You Want To Exit The App?");
//        dialogBuilder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                finish();
//            }
//        });
//        dialogBuilder.setNegativeButton("No", null);
//        dialogBuilder.show();
//    }
}
