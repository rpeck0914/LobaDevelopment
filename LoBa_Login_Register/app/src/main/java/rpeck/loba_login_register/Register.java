package rpeck.loba_login_register;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Register extends Activity implements View.OnClickListener{

    //Private Variables For The Register Activity Layout
    private Button mButtonRegister;
    private EditText mEnterName, mEnterUserName, mEnterPassword, mEnterConfirmPassword;
    private Spinner mSelectState, mSelectCity;

    private int mSelectedCityID;

    ArraySort mStateArraySort;
    ArraySort mCityArraySort;

    Encryption mEncryption;

    //// TODO: 10/15/2015 Get the layout looking better 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Sets The Layout Objects To Their Variables
        mEnterName = (EditText) findViewById(R.id.enter_name);
        mEnterUserName = (EditText) findViewById(R.id.enter_userName);
        mEnterPassword = (EditText) findViewById(R.id.enter_Password);
        mEnterConfirmPassword = (EditText) findViewById(R.id.confirm_password);
        mSelectState = (Spinner) findViewById(R.id.enter_state);
        mSelectCity = (Spinner) findViewById(R.id.enter_city);
        mButtonRegister = (Button) findViewById(R.id.button_register);

        //On Click Listeners For Clicks On Buttons
        mButtonRegister.setOnClickListener(this);

        //Instantiates A Variable For The States Class
        States states = new States();

        //Calls pullStates Method While Passing The states Class Variable
        pullStates(states);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //Switch Case For Reading The On Click Listeners
            case R.id.button_register:

                //Grabs All The User Input And Creates A New User Based On The Input Information
                String name = mEnterName.getText().toString();
                String username = mEnterUserName.getText().toString();
                String password = mEnterPassword.getText().toString();
                mEncryption = new Encryption(password);
                password = mEncryption.getResult();
                //password = md5(password);
                String confirmPassword = mEnterConfirmPassword.getText().toString();
                mEncryption = new Encryption(confirmPassword);
                confirmPassword = mEncryption.getResult();
                //confirmPassword = md5(confirmPassword);
                String state = mSelectState.getSelectedItem().toString();
                String city = mSelectCity.getSelectedItem().toString();

                if (validateRegisteringUser(name, username, password, confirmPassword)) {
                    //Creates A New User With Valid Input
                    User user = new User(name, username, password, city, state, mSelectedCityID);

                    //Sends The Newly Created User Over To The registerUser Method
                    registerUser(user);
                }
                break;
        }
    }

    //loadStateSpinner Method To Load The State Spinner With The States Passed Over From The Database
    private void loadStateSpinner(final States statesToLoad) {

        mStateArraySort = new ArraySort(statesToLoad.states, statesToLoad.stateID);
        //Sends The States And Their ID's Over To Be Sorted
        //sort(statesToLoad.states, statesToLoad.stateID, 1);

            //Loads The State Spinner With The Array Of States
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mStateArraySort.getNameArray());
            mSelectState.setAdapter(adapter);
            mSelectState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> aro0, View arg1, int arg2, long arg3) {
                    //Override Method For Selected A Different State In The State Spinner

                    //Finds The Selected State's ID To Fetch The Cities Within That State
                    int selectedIndex;
                    String selectedState = mSelectState.getSelectedItem().toString();
                    for (int i = 0; i < mStateArraySort.getNameArray().length; i++) {
                        if (mStateArraySort.getNameArray()[i] == selectedState) {
                            selectedIndex = i;
                            Log.d("ERROR", mStateArraySort.getIDArray()[selectedIndex] + " State");

                            //Creates A New City With The StateID
                            Cities cities = new Cities(mStateArraySort.getIDArray()[selectedIndex]);
                            //Calls The pullCites Method And Sends The Cities Object Over
                            pullCities(cities);
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    //Override Method For When Nothing Is Selected In The State Spinner??
                }
            });
    }

    //loadCitySpinner Method To Load The Cities Spinner With The Cities Passed Over From The Database Based On The Selected State
    private void loadCitySpinner(Cities cities) {

        //Sends Over The Array Of Cites And Their ID's To Get Sorted
        mCityArraySort = new ArraySort(cities.citiesArray, cities.cityID);

        //sort(cities.citiesArray, cities.cityID, 2);

        //Loads The City Spinner With The Array Of Cities
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mCityArraySort.getNameArray());
        mSelectCity.setAdapter(adapter);
        mSelectCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> aro0, View arg1, int arg2, long arg3) {
                int selectedIndex;
                String selectedCity = mSelectCity.getSelectedItem().toString();
                for (int i = 0; i < mCityArraySort.getNameArray().length; i++) {
                    if (mCityArraySort.getNameArray()[i].equals(selectedCity)) {
                        selectedIndex = i;
                        Log.d("ERROR", mCityArraySort.getIDArray()[selectedIndex] + "");
                        mSelectedCityID = mCityArraySort.getIDArray()[selectedIndex];
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //Override Method For When Nothing Is Selected In The State Spinner??
            }
        });
    }

    //registerUser Method Takes The Passed Over User And Sends It Over To Server Requests To Be Added To The Database
    private void registerUser(User user) {
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.storeUserDataInBackground(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                //Override Method That's Called After The User Is Added To The Database
                startActivity(new Intent(Register.this, Login.class));
            }
        });

    }

    //pullStates Method Sends A Request To ServerRequests To Pull The State Information From The Database
    private void pullStates(States states){
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.fetchStateDataAsyncTask(states, new GetStatesCallback() {
            @Override
            public void done(States returnedStates) {
                if (returnedStates == null) {
                    //If There Is An Error And The States Don't Come Over An Error Message Is Displayed Showing Their Is An Error
                    String errorMessage = "Error Loading States";
                    showErrorMessage(errorMessage);
                } else {
                    //If There Is No Errors The States Are Then Sent Over To Be Loaded In The State Spinner
                    loadStateSpinner(returnedStates);
                }
            }
        });
    }

    //pullCities Method Sends A Request To ServerRequests With The StateID To Pull The Cites Information From The Database
    private void pullCities(Cities cities) {
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.fetchCityDataAsyncTask(cities, new GetCitiesCallback() {
            @Override
            public void done(Cities returnedCities) {
                if (returnedCities == null) {
                    //If There Is An Error And The Cities Don't Come Over An Error Message Is Displayed Showing Their Is An Error
                    String errorMessage = "Error Loading Cities";
                    showErrorMessage(errorMessage);
                } else {
                    //If There Is No Errors The Cities Are Then Sent Over To Be Loaded In The City Spinner
                    loadCitySpinner(returnedCities);
                }
            }
        });
    }

    //showErrorMessage Method Builds An AlertDialog To Be Shown Letting The User Know Their Was An Error
    private void showErrorMessage(String errormessage) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Register.this);
        dialogBuilder.setMessage(errormessage);
        dialogBuilder.setPositiveButton("OK", null);
        dialogBuilder.show();
    }

    private boolean validateRegisteringUser(String name, String username, String password, String confirmPassword) {
        boolean userValid = false;

        if (!name.isEmpty() && name != null) {
            if (!username.isEmpty() && username != null) {
                if (!password.isEmpty() && password != null) {
                    if (!confirmPassword.isEmpty() && confirmPassword != null) {
                        if (password.equals(confirmPassword)) {
                            userValid = true;
                        } else {
                            Toast.makeText(this, "Passwords Do Not Match", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Please ReEnter Your Password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Please Enter A Password", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please Enter A Username!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please Enter A Name!", Toast.LENGTH_SHORT).show();
        }

        return userValid;
    }
}
