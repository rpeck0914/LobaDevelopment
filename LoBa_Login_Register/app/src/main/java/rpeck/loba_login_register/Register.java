package rpeck.loba_login_register;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class Register extends Activity implements View.OnClickListener{

    private Button mButtonRegister;
    private EditText mEnterName, mEnterUserName, mEnterPassword;
    private Spinner mSelectState, mSelectCity;
    private String[] mStates = new String[4];
    private int stateID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEnterName = (EditText) findViewById(R.id.enter_name);
        mEnterUserName = (EditText) findViewById(R.id.enter_userName);
        mEnterPassword = (EditText) findViewById(R.id.enter_Password);
        mSelectState = (Spinner) findViewById(R.id.enter_state);
        mSelectCity = (Spinner) findViewById(R.id.enter_city);

        loadStateSpinner();

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
                String state = mSelectState.getSelectedItem().toString();
                String city = mSelectCity.getSelectedItem().toString();

                User user = new User(name, username, password, city, state);

                registerUser(user);
                break;
        }
    }

    private void loadStateSpinner() {
        mStates[0] = "Please Select A State";
        mStates[1] = "Michigan";
        mStates[2] = "Indiana";
        mStates[3] = "Ohio";

        pullStates();


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mStates);
        mSelectState.setAdapter(adapter);
        mSelectState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> aro0, View arg1, int arg2, long arg3) {
                if(mStates[1] == mSelectState.getSelectedItem().toString()){
                    stateID = 1;
                }
                if(mStates[2] == mSelectState.getSelectedItem().toString()){
                    stateID = 2;
                }
                if(mStates[3] == mSelectState.getSelectedItem().toString()){
                    stateID = 3;
                }

                if(stateID != 0) {
                    Cities cities = new Cities(stateID);

                    pullCities(cities);
                }
            }
            @Override
            public  void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    private void loadCitySpinner(Cities cities) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cities.citiesArray);
        mSelectCity.setAdapter(adapter);
    }

    private void registerUser(User user) {
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.storeUserDataInBackground(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });

    }

    private void pullStates(){
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.fetchStateDataAsyncTask();
    }

    private void pullCities(Cities cities) {
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.fetchCityDataAsyncTask(cities, new GetCitiesCallback() {
            @Override
            public void done(Cities returnedCities) {
                if (returnedCities == null) {
                    showErrorMessage();
                } else {
                    loadCitySpinner(returnedCities);
                }
            }
        });
    }

    private void showErrorMessage() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Register.this);
        dialogBuilder.setMessage("Error Loading Cities");
        dialogBuilder.setPositiveButton("OK", null);
        dialogBuilder.show();
    }
}
