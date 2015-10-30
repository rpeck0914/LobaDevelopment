package rpeck.loba_login_register;


import android.app.AlertDialog;
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

        return v;
    }

}
