package rpeck.loba_login_register;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import java.util.Arrays;
import java.lang.reflect.Array;

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
        States states = new States();
        pullStates(states);

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

    private void loadStateSpinner(final States statesToLoad) {

        Arrays.sort(statesToLoad.states);

        if (statesToLoad != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statesToLoad.states);
            mSelectState.setAdapter(adapter);
            mSelectState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> aro0, View arg1, int arg2, long arg3) {
                    int selectedIndex = 0;
                    String selectedState = mSelectState.getSelectedItem().toString();
                    for(int i = 0; i < statesToLoad.states.length; i++){
                        if(statesToLoad.states[i] == selectedState){
                            selectedIndex = (i + 1);
                        }
                    }
                    Log.d("ERROR", selectedIndex + "");
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {

                }
            });
        } else {
            showErrorMessage();
        }
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

    private void pullStates(States states){
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.fetchStateDataAsyncTask(states, new GetStatesCallback() {
            @Override
            public void done(States returnedStates) {
                //Log.d("States", returnedStates.toString());
                loadStateSpinner(returnedStates);
            }
        });
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
        dialogBuilder.setMessage("Error Loading States");
        dialogBuilder.setPositiveButton("OK", null);
        dialogBuilder.show();
    }
}
