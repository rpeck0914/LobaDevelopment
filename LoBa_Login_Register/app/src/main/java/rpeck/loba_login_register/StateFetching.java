package rpeck.loba_login_register;

import android.content.Context;

/**
 * Created by Robert Peck on 10/29/2015.
 */
public class StateFetching {

    private States states;
    private static String[] mStatesArray;
    private static int[] mStateIDArray;
    private String mErrorMessage = "";

    private static Context mAppContext;
    ArraySort mArraySort;

    public StateFetching(Context MainActivity, States states){
        this.states = states;
        mAppContext = MainActivity;
    }

    public void pullStates(States s){
        ServerRequests serverRequests = new ServerRequests(mAppContext);
        serverRequests.fetchStateDataAsyncTask(s, new GetStatesCallback() {
            @Override
            public void done(States returnedStates) {
                if(returnedStates == null){
                    mStatesArray = null;
                    mStateIDArray = null;
                    mErrorMessage = "Error Loading States";
                } else {
                    mStatesArray = returnedStates.states;
                    mStateIDArray = returnedStates.stateID;

                    mArraySort = new ArraySort(mStatesArray, mStateIDArray);
                }
            }
        });
    }

    public String[] getStatesArray() {
        return mStatesArray;
    }

    public int[] getStateIDArray() {
        return mStateIDArray;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }
}
