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

    private String[] mSortedStates;
    private int[] mSortedStateID;

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

        sort(statesToLoad.states, statesToLoad.stateID);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mSortedStates);
            mSelectState.setAdapter(adapter);
            mSelectState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> aro0, View arg1, int arg2, long arg3) {
                    int selectedIndex;
                    String selectedState = mSelectState.getSelectedItem().toString();
                    for(int i = 0; i < mSortedStates.length; i++){
                        if(mSortedStates[i] == selectedState){
                            selectedIndex = (i);
                            Log.d("ERROR", mSortedStateID[selectedIndex]+"");

                            Cities cities = new Cities(mSortedStateID[selectedIndex]);
                            pullCities(cities);
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {

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

    private void pullStates(States states){
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.fetchStateDataAsyncTask(states, new GetStatesCallback() {
            @Override
            public void done(States returnedStates) {
                if(returnedStates == null){
                    String errorMessage = "Error Loading States";
                    showErrorMessage(errorMessage);
                }else {
                    loadStateSpinner(returnedStates);
                }
            }
        });
    }

    private void pullCities(Cities cities) {
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.fetchCityDataAsyncTask(cities, new GetCitiesCallback() {
            @Override
            public void done(Cities returnedCities) {
                if (returnedCities == null) {
                    String errorMessage = "Error Loading Cities";
                    showErrorMessage(errorMessage);
                } else {
                    loadCitySpinner(returnedCities);
                }
            }
        });
    }

    private void showErrorMessage(String errormessage) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Register.this);
        dialogBuilder.setMessage(errormessage);
        dialogBuilder.setPositiveButton("OK", null);
        dialogBuilder.show();
    }

    private void sort(String[] name, int[] id) {
        mSortedStates = name;
        mSortedStateID = id;
        boolean flag = true;
        String tempState;
        int tempID;

        while(flag) {
            flag = false;
            for (int i = 0; i < mSortedStates.length - 1; i++) {
                if(mSortedStates[i].compareToIgnoreCase(mSortedStates[i + 1]) > 0) {
                    tempState = mSortedStates[i];
                    tempID = mSortedStateID[i];
                    mSortedStates[i] = mSortedStates[i + 1];
                    mSortedStateID[i] = mSortedStateID[i + 1];
                    mSortedStates[i + 1] = tempState;
                    mSortedStateID[i + 1] = tempID;

                    flag = true;
                }
            }
        }
    }
}
