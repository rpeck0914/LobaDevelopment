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

public class Register extends Activity implements View.OnClickListener{

    //Private Variables For The Register Activity Layout
    private Button mButtonRegister;
    private EditText mEnterName, mEnterUserName, mEnterPassword;
    private Spinner mSelectState, mSelectCity;

    //Private Variables For Holding State And City Information
    private String[] mSortedStates;
    private int[] mSortedStateID;
    private String[] mSortedCities;
    private int[] mSortedCityId;

    //// TODO: 10/15/2015 Get the layout looking better 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Sets The Layout Objects To Their Variables
        mEnterName = (EditText) findViewById(R.id.enter_name);
        mEnterUserName = (EditText) findViewById(R.id.enter_userName);
        mEnterPassword = (EditText) findViewById(R.id.enter_Password);
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
                String state = mSelectState.getSelectedItem().toString();
                String city = mSelectCity.getSelectedItem().toString();
                //// TODO: 10/15/2015 Add Validation For Users Registering
                User user = new User(name, username, password, city, state);

                //Sends The Newly Created User Over To The registerUser Method
                registerUser(user);
                break;
        }
    }

    //loadStateSpinner Method To Load The State Spinner With The States Passed Over From The Database
    private void loadStateSpinner(final States statesToLoad) {

        //Sends The States And Their ID's Over To Be Sorted
        sort(statesToLoad.states, statesToLoad.stateID, 1);

            //Loads The State Spinner With The Array Of States
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mSortedStates);
            mSelectState.setAdapter(adapter);
            mSelectState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> aro0, View arg1, int arg2, long arg3) {
                    //Override Method For Selected A Different State In The State Spinner

                    //Finds The Selected State's ID To Fetch The Cities Within That State
                    int selectedIndex;
                    String selectedState = mSelectState.getSelectedItem().toString();
                    for(int i = 0; i < mSortedStates.length; i++){
                        if(mSortedStates[i] == selectedState){
                            selectedIndex = (i);
                            Log.d("ERROR", mSortedStateID[selectedIndex]+"");

                            //Creates A New City With The StateID
                            Cities cities = new Cities(mSortedStateID[selectedIndex]);
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
        sort(cities.citiesArray, cities.cityID, 2);

        //Loads The City Spinner With The Array Of Cities
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cities.citiesArray);
        mSelectCity.setAdapter(adapter);
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
                if(returnedStates == null){
                    //If There Is An Error And The States Don't Come Over An Error Message Is Displayed Showing Their Is An Error
                    String errorMessage = "Error Loading States";
                    showErrorMessage(errorMessage);
                }else {
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

    //sort Method Takes The Arrays That Are Sent Over And Sorts Them Alphabetically And Sets Them To The Appropriate Variables
    private void sort(String[] name, int[] id, int arrayID) {
        String[] nameArray = name;
        int[] idArray = id;

        boolean flag = true;
        String tempState;
        int tempID;

        while(flag) {
            flag = false;
            for (int i = 0; i < nameArray.length - 1; i++) {
                if(nameArray[i].compareToIgnoreCase(nameArray[i + 1]) > 0) {
                    tempState = nameArray[i];
                    tempID = idArray[i];
                    nameArray[i] = nameArray[i + 1];
                    idArray[i] = idArray[i + 1];
                    nameArray[i + 1] = tempState;
                    idArray[i + 1] = tempID;

                    flag = true;
                }
            }
        }
        if (arrayID == 1){
            mSortedStates = nameArray;
            mSortedStateID = idArray;
        } else {
            mSortedCities = nameArray;
            mSortedCityId = idArray;
        }
    }
}
