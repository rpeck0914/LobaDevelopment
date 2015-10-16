package rpeck.loba_login_register;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends Activity implements View.OnClickListener {

    //Private Variables For The Login Activity Layout
    private Button mButtonLogin;
    private EditText mEnterUserName, mEnterPassword;
    private TextView mRegisterLink;

    //Creates A Variable For UserLocalStore To Sore The Users Data Locally On Device
    UserLocalStore userLocalStore;
    
    //// TODO: 10/15/2015 Get the layout looking better

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Sets The Layout Objects To Their Variables
        mEnterUserName = (EditText) findViewById(R.id.enter_username);
        mEnterPassword = (EditText) findViewById(R.id.enter_Password);

        mRegisterLink = (TextView) findViewById(R.id.register_link);
        mButtonLogin = (Button) findViewById(R.id.button_login);

        //On Click Listeners For Clicks On Buttons
        mButtonLogin.setOnClickListener(this);
        mRegisterLink.setOnClickListener(this);

        //Instantiates UserLocalStore Variable To Save Logged In Users Data
        userLocalStore = new UserLocalStore(this);
    }

    @Override
    public void onClick(View v) {
        //Switch Case For Reading The On Click Listeners
        switch (v.getId()) {
            case  R.id.button_login:
                //Captures User Input When Clicking LogIn Button
                String username = mEnterUserName.getText().toString();
                String password = mEnterPassword.getText().toString();

                //Creates A User Based On The User's Input
                User user = new User(username, password);

                //Sends User's Inputted Info Over To Be Verified
                authenticate(user);
                break;

            case R.id.register_link:
                //Starts Register Activity If User Clicks Register Button
                startActivity(new Intent(this, Register.class));
                break;
        }
    }

    private void authenticate(User user) {
        //Creates A New Instance Of ServerRequests To Send Over The User's Inputted Info
        ServerRequests serverRequests = new ServerRequests(this);
        //Calls The FetchUserDataAsyncTask Method That Will Start The User Validation On The Database End
        serverRequests.fetchUserDataAsyncTask(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                //When The FetchUserDataAsyncTask Method Is Completed This Override Method Is Called
                if(returnedUser == null) {
                    //A Null User Returned Will Lead To An Error Message Popping Up By Calling The Method showErrorMessage
                    showErrorMessage();
                } else {
                    //If User Exists The logUserIn Method Is Called By Sending Over The User Info
                    logUserIn(returnedUser);
                }
            }
        });
    }

    private void showErrorMessage() {
        //Error Message That Is Displayed If User Doesn't Exist
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Login.this);
        dialogBuilder.setMessage("Incorrect User Detail");
        dialogBuilder.setPositiveButton("OK", null);
        dialogBuilder.show();
    }

    private void logUserIn(User userToLogIn) {
        //logUserIn Method Takes The User And Stores It Locally In The Devices Data,
        //Then Starts The MainActivity
        try {
            userLocalStore.storeUserData(userToLogIn);
            userLocalStore.setUserLoggedIn(true);
            startActivity(new Intent(this, MainActivity.class));
        } catch (Exception e) {
            Log.i("BOOM", e.toString());
        }
    }
}
