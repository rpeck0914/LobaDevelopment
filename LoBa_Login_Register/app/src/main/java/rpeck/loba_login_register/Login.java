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

    private Button mButtonLogin;
    private EditText mEnterUserName, mEnterPassword;
    private TextView mRegisterLink;
    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEnterUserName = (EditText) findViewById(R.id.enter_username);
        mEnterPassword = (EditText) findViewById(R.id.enter_Password);

        mRegisterLink = (TextView) findViewById(R.id.register_link);

        mButtonLogin = (Button) findViewById(R.id.button_login);

        mButtonLogin.setOnClickListener(this);
        mRegisterLink.setOnClickListener(this);

        userLocalStore = new UserLocalStore(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case  R.id.button_login:
                String username = mEnterUserName.getText().toString();
                String password = mEnterPassword.getText().toString();

                User user = new User(username, password);

                authenticate(user);
                break;

            case R.id.register_link:
                startActivity(new Intent(this, Register.class));
                break;
        }
    }

    private void authenticate(User user) {
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.fetchUserDataAsyncTask(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                if(returnedUser == null) {
                    showErrorMessage();
                } else {
                    logUserIn(returnedUser);
                }
            }
        });
    }

    private void showErrorMessage() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Login.this);
        dialogBuilder.setMessage("Incorrect User Detail");
        dialogBuilder.setPositiveButton("OK", null);
        dialogBuilder.show();
    }

    private void logUserIn(User userToLogIn) {
        try {
            userLocalStore.storeUserData(userToLogIn);
            userLocalStore.setUserLoggedIn(true);
            startActivity(new Intent(this, MainActivity.class));
        } catch (Exception e) {
            Log.i("BOOM", e.toString());
        }
    }
}
