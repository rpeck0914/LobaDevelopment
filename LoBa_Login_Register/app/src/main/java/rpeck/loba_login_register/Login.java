package rpeck.loba_login_register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends Activity implements View.OnClickListener {

    private Button mButtonLogin;
    private EditText mEnterUserName, mEnterPassword;
    private TextView mRegisterLink;
    private UserLocalStore userLocalStore;

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

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case  R.id.button_login:
                User user = new User(null, null);
                
                userLocalStore.storeUserData(user);
                userLocalStore.setUserLoggedIn(true);

                break;

            case R.id.register_link:
                startActivity(new Intent(this, Register.class));
                break;
        }
    }
}
