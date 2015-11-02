package rpeck.loba_login_register;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import android.support.annotation.MainThread;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


/**
 * A simple {@link Fragment} subclass.
 */
public class CityStateSpinnerFragment extends Fragment {

    private Spinner mStateSpinner;
    private Spinner mCitySpinner;

    private int mSelectedCityID;

    private UserLocalStore mUserLocalStore;

    ArraySort mArraySort;
    Context mAppContext;

    public CityStateSpinnerFragment(Context MainActivity) {
        // Required empty public constructor
        mAppContext = MainActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.fragment_city_state_spinner, container, false);

        mStateSpinner = (Spinner) v.findViewById(R.id.enter_state);
        mCitySpinner = (Spinner) v.findViewById(R.id.enter_city);

        States states = new States();

        pullStates(states);

        mUserLocalStore = new UserLocalStore(mAppContext);

        setUsersStateAndCitySpinners();

        return v;
    }

    //pullStates Method Sends A Request To ServerRequests To Pull The State Information From The Database
    private void pullStates(States states){
        ServerRequests serverRequests = new ServerRequests(mAppContext);
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
        ServerRequests serverRequests = new ServerRequests(mAppContext);
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

    //loadStateSpinner Method To Load The State Spinner With The States Passed Over From The Database
    private void loadStateSpinner(final States statesToLoad) {

        mArraySort = new ArraySort(statesToLoad.states, statesToLoad.stateID);
        //Sends The States And Their ID's Over To Be Sorted
        //sort(statesToLoad.states, statesToLoad.stateID, 1);

        //Loads The State Spinner With The Array Of States
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mAppContext, android.R.layout.simple_spinner_item, mArraySort.getNameArray());
        mStateSpinner.setAdapter(adapter);

        mStateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> aro0, View arg1, int arg2, long arg3) {
                //Override Method For Selected A Different State In The State Spinner

                //Finds The Selected State's ID To Fetch The Cities Within That State
                int selectedIndex;
                String selectedState = mStateSpinner.getSelectedItem().toString();
                for (int i = 0; i < mArraySort.getNameArray().length; i++) {
                    if (mArraySort.getNameArray()[i] == selectedState) {
                        selectedIndex = i;
                        Log.d("ERROR", mArraySort.getIDArray()[selectedIndex] + "");

                        //Creates A New City With The StateID
                        Cities cities = new Cities(mArraySort.getIDArray()[selectedIndex]);
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
        mArraySort = new ArraySort(cities.citiesArray, cities.cityID);

        //sort(cities.citiesArray, cities.cityID, 2);

        //Loads The City Spinner With The Array Of Cities
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mAppContext, android.R.layout.simple_spinner_item, mArraySort.getNameArray());
        mCitySpinner.setAdapter(adapter);
        mCitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> aro0, View arg1, int arg2, long arg3) {
                String selectedCity = mCitySpinner.getSelectedItem().toString();
                for (int i = 0; i < mArraySort.getNameArray().length; i++) {
                    if(mArraySort.getNameArray()[i] == selectedCity) {
                        mSelectedCityID = i;
                        Log.d("ERROR", mArraySort.getIDArray()[mSelectedCityID] + "");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //Override Method For When Nothing Is Selected In The State Spinner??
            }
        });
    }

    private void setUsersStateAndCitySpinners() {

        User user = mUserLocalStore.getLoggedInUser();

        mStateSpinner.setSelection(mArraySort.getNameArray().getPostition(user.mState.toString()));
    }

    //showErrorMessage Method Builds An AlertDialog To Be Shown Letting The User Know Their Was An Error
    private void showErrorMessage(String errormessage) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mAppContext);
        dialogBuilder.setMessage(errormessage);
        dialogBuilder.setPositiveButton("OK", null);
        dialogBuilder.show();
    }
}
