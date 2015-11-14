package rpeck.loba_login_register;


import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SingleBarDetailsFragment extends Fragment implements View.OnClickListener {

    private TextView mBarname, mBarAddress, mBarCity, mBarState, mBarZipCode, mBarPhone;
    private ImageView mMapsImage, mPhoneImage;

    private Bar mBar;
    private UserLocalStore mUserLocalStore;

    public SingleBarDetailsFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_single_bar_details, container, false);

        mBarname = (TextView) v.findViewById(R.id.single_bar_name_title);
        mBarAddress = (TextView) v.findViewById(R.id.single_bar_address);
        mBarCity = (TextView) v.findViewById(R.id.single_bar_city);
        mBarState = (TextView) v.findViewById(R.id.single_bar_state);
        mBarZipCode = (TextView) v.findViewById(R.id.single_bar_zip_code);
        mBarPhone = (TextView) v.findViewById(R.id.single_bar_phone);

        mMapsImage = (ImageView) v.findViewById(R.id.map_image);
        mPhoneImage = (ImageView) v.findViewById(R.id.phone_image);

        mMapsImage.setOnClickListener(this);
        mPhoneImage.setOnClickListener(this);

        int barId = SingleBarDetailsActivity.mbarId;

        mUserLocalStore = new UserLocalStore(getActivity());
        User user = mUserLocalStore.getLoggedInUser();

        mBar = new Bar(barId, user.mState, user.mCity);

        pullBarInformation(mBar);

        return v;
    }

    private void pullBarInformation(Bar bar) {
        ServerRequests serverRequests = new ServerRequests(getActivity());
        serverRequests.fetchBarDetailsDataAsyncTask(bar, new GetBarsCallBack() {
            @Override
            public void done(Bar returnedBar) {
                if(returnedBar == null) {
                    String errorMessage = "Error Collecting Bar Details";
                    showErrorMessage(errorMessage);
                } else {
                    fillBarDataToLayout(returnedBar);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.map_image:
                //TODO Write In Intent To Call Google Maps With The Address
                Toast.makeText(getActivity(), "HAHA you thought google maps was going to open", Toast.LENGTH_SHORT).show();
                break;

            case R.id.phone_image:
                //TODO Write In Intent To Call The Phone App With The Supplied Number
                Toast.makeText(getActivity(), "Are you sure you really want to call this bar??", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void fillBarDataToLayout(Bar bar) {
        mBarname.setText(bar.mBarName);
        mBarAddress.setText(bar.mBarAddress);
        mBarState.setText(bar.mBarState);
        mBarCity.setText(bar.mBarCity);
        mBarZipCode.setText(bar.mBarZipCode);
        mBarPhone.setText(bar.mBarPhone);
    }

    private void showErrorMessage(String errorMessage) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setMessage(errorMessage);
        dialogBuilder.setPositiveButton("OK", null);
        dialogBuilder.show();
    }
}
