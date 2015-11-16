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

    private BarIDs mBarID;

    private static ArraySort mStateArraySort;
    private static ArraySort mCityArraySort;

    private boolean mStateSpinnersLoadedFlag = false;
    private boolean mCitySpinnersLoadedFlag = false;

    private BarLab mBarLab;

    private Communicator comm;

    public CityStateSpinnerFragment() {
        // Required empty public constructor
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

        mUserLocalStore = new UserLocalStore(getActivity());

        comm = (Communicator) getActivity();
        return v;
    }

    //pullStates Method Sends A Request To ServerRequests To Pull The State Information From The Database
    private void pullStates(States states){
        ServerRequests serverRequests = new ServerRequests(getActivity());
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
        ServerRequests serverRequests = new ServerRequests(getActivity());
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
        //Sends The States And Their ID's Over To Be Sorted
        mStateArraySort = new ArraySort(statesToLoad.states, statesToLoad.stateID);

        //Loads The State Spinner With The Array Of States
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, mStateArraySort.getNameArray());
        mStateSpinner.setAdapter(adapter);

        mStateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> aro0, View arg1, int arg2, long arg3) {
                //Override Method For Selected A Different State In The State Spinner

                User user = mUserLocalStore.getLoggedInUser();

                //Finds The Selected State's ID To Fetch The Cities Within That State
                int selectedIndex;
                String selectedState = mStateSpinner.getSelectedItem().toString();
                for (int i = 0; i < mStateArraySort.getNameArray().length; i++) {
                    if (mStateArraySort.getNameArray()[i] == selectedState) {
                        selectedIndex = i;
                        Log.d("ERROR", mStateArraySort.getIDArray()[selectedIndex] + "");

                        //Creates A New City With The StateID
                        Cities cities = new Cities(mStateArraySort.getIDArray()[selectedIndex]);

                        mUserLocalStore.selectedCityAndState(selectedState, "State");

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
        setUsersStateAndCitySpinners(mStateArraySort, "state");
    }

    //loadCitySpinner Method To Load The Cities Spinner With The Cities Passed Over From The Database Based On The Selected State
    private void loadCitySpinner(Cities cities) {

        //Sends Over The Array Of Cites And Their ID's To Get Sorted
        mCityArraySort = new ArraySort(cities.citiesArray, cities.cityID);

        //sort(cities.citiesArray, cities.cityID, 2);

        //Loads The City Spinner With The Array Of Cities
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, mCityArraySort.getNameArray());
        mCitySpinner.setAdapter(adapter);
        mCitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> aro0, View arg1, int arg2, long arg3) {
                User user = mUserLocalStore.getLoggedInUser();

                int selectedIndex;
                String selectedCity = mCitySpinner.getSelectedItem().toString();
                for (int i = 0; i < mCityArraySort.getNameArray().length; i++) {
                    if (mCityArraySort.getNameArray()[i].equals(selectedCity)) {
                        selectedIndex = i;
                        Log.d("ERROR", mCityArraySort.getIDArray()[selectedIndex] + "");
                        mSelectedCityID = mCityArraySort.getIDArray()[selectedIndex];
                        BarLab barLab = BarLab.get(getActivity());
                        barLab.removeList();
                        pullBarIDs();
                        comm.respond("updateUI");
                        mUserLocalStore.selectedCityAndState(selectedCity, "City");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //Override Method For When Nothing Is Selected In The State Spinner??
            }
        });
        setUsersStateAndCitySpinners(mCityArraySort, "city");
    }

    private void setUsersStateAndCitySpinners(ArraySort name, String id) {

        User user = mUserLocalStore.getLoggedInUser();

        if(user != null && id == "state" && mStateSpinnersLoadedFlag == false) {
            String stateWithNulls = user.mState.toString();
            String state = stateWithNulls.replaceAll("\\u0000", "");
            for (int i = 0; i < name.getNameArray().length; i++) {
                if (state.compareToIgnoreCase(name.getNameArray()[i].toString()) == 0) {
                    mStateSpinner.setSelection(i);
                    mStateSpinnersLoadedFlag = true;
                    i = name.getNameArray().length;
                }
            }
        }

        if(user != null && id == "city" && mCitySpinnersLoadedFlag == false) {
            String cityWithNulls = user.mCity.toString();
            String city = cityWithNulls.replaceAll("\\u0000", "");
            for (int i = 0; i < name.getNameArray().length; i++) {
                if (city.compareToIgnoreCase(name.getNameArray()[i].toString()) == 0) {
                    mCitySpinner.setSelection(i);
                    mCitySpinnersLoadedFlag = true;
                    i = name.getNameArray().length;
                }
            }
        }
    }

    private void pullBarIDs() {
        mBarID = new BarIDs(mSelectedCityID);

        ServerRequests serverRequests = new ServerRequests(getActivity());
        serverRequests.fetchBarIDsDataAsyncTask(mBarID, new GetBarIDsCallback() {
            @Override
            public void done(BarIDs returnedBarIDs) {
                if (returnedBarIDs == null) {
                    String errorMessage = "No Bars Exist For This City";
                    showErrorMessage(errorMessage);
                } else {
                    mBarID = returnedBarIDs;
                    comm.respond("updateUI");
                }
            }
        });
    }

    //showErrorMessage Method Builds An AlertDialog To Be Shown Letting The User Know Their Was An Error
    private void showErrorMessage(String errorMessage) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setMessage(errorMessage);
        dialogBuilder.setPositiveButton("OK", null);
        dialogBuilder.show();
    }

    public Spinner getStateSpinner() {
        return mStateSpinner;
    }

    public Spinner getCitySpinner() {
        return mCitySpinner;
    }
}
