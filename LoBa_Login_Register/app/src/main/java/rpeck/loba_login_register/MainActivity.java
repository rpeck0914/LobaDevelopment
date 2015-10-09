package rpeck.loba_login_register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity implements View.OnClickListener{

    private Button mButtonLogout;
    private EditText mEnterUserName;
    private UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEnterUserName = (EditText) findViewById(R.id.enter_username);

        mButtonLogout = (Button) findViewById(R.id.button_logout);
        mButtonLogout.setOnClickListener(this);

        userLocalStore = new UserLocalStore(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(authenticate() == true) {
            displayUserDetails();
        } else {
            startActivity(new Intent(MainActivity.this, Login.class));
        }

    }

    private boolean authenticate() {
        if (userLocalStore.getLoggedInUser() == null) {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            return false;
        }
        return true;
    }

    private void displayUserDetails() {
        User user = userLocalStore.getLoggedInUser();

        mEnterUserName.setText(user.mName);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_logout:
                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);

                startActivity(new Intent(this, Login.class));
                break;
        }
    }
}
