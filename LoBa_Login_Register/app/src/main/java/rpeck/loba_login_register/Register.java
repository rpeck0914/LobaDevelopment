package rpeck.loba_login_register;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Register extends Activity implements View.OnClickListener{

    private Button mButtonRegister;
    private EditText mEnterName, mEnterUserName, mEnterPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEnterName = (EditText) findViewById(R.id.enter_name);
        mEnterUserName = (EditText) findViewById(R.id.enter_username);
        mEnterPassword = (EditText) findViewById(R.id.enter_Password);

        mButtonRegister = (Button) findViewById(R.id.button_register);
        mButtonRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_register:

                String name = mEnterName.getText().toString();
                String username = mEnterUserName.getText().toString();
                String password = mEnterPassword.getText().toString();

                User registeredData = new User(name, username, password);

                break;
        }
    }
}
