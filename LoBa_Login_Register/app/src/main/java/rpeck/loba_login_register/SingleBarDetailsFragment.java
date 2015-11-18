package rpeck.loba_login_register;


import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class SingleBarDetailsFragment extends Fragment implements View.OnClickListener {

    private TextView mBarname, mBarAddress, mBarCity, mBarState, mBarZipCode, mBarPhone;
    private ImageView mMapsImage, mPhoneImage;

    private Bar mBar;
    private UserLocalStore mUserLocalStore;
    private LoadBarSpecialsCommunicator loadCommunicator;

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
        mBarAddress.setOnClickListener(this);
        mBarPhone.setOnClickListener(this);

        int barId = SingleBarDetailsActivity.mbarId;

        mUserLocalStore = new UserLocalStore(getActivity());

        String state = new StateAbbreviations(mUserLocalStore.getSelectedCityAndState("selectedState")).getStateAbbrev();

        mBar = new Bar(barId, state, mUserLocalStore.getSelectedCityAndState("selectedCity") );

        pullBarInformation(mBar);

        loadCommunicator = (LoadBarSpecialsCommunicator) getActivity();

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
                    pullBarSpecials(returnedBar);

                }
            }
        });
    }

    private void pullBarSpecials(Bar bar) {
        DayOfWeek dayOfWeek = new DayOfWeek(bar.mBarID);
        ServerRequests serverRequests = new ServerRequests(getActivity());
        serverRequests.fetchBarSpecialsDataAsyncTask(dayOfWeek, new GetBarSpecialsCallback() {
            @Override
            public void done(DayOfWeek returnedDayOfWeek) {
                if (returnedDayOfWeek == null) {
                    loadCommunicator.loadViewPager("ErrorLoadingSpecials");
                } else {
                    loadCommunicator.loadViewPager("LoadBarSpecials");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.map_image:
                googleMapsIntentCall();
                break;

            case R.id.phone_image:
                phoneIntentCall();
                break;

            case R.id.single_bar_address:
                googleMapsIntentCall();
                break;

            case R.id.single_bar_phone:
                phoneIntentCall();
                break;
        }
    }

    private void fillBarDataToLayout(Bar bar) {
        mBarname.setText(bar.mBarName);
        mBarAddress.setText(bar.mBarAddress);
        mBarState.setText(bar.mBarState + " ");
        mBarCity.setText(bar.mBarCity + ", ");
        mBarZipCode.setText(bar.mBarZipCode);
        mBarPhone.setText(bar.mBarPhone);
    }

    private void showErrorMessage(String errorMessage) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setMessage(errorMessage);
        dialogBuilder.setPositiveButton("OK", null);
        dialogBuilder.show();
    }

    private void googleMapsIntentCall() {
        String addressString = (mBarAddress.getText().toString() + " " + mBarCity.getText().toString() + mBarState.getText().toString() + mBarZipCode.getText().toString());
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + addressString);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    private void phoneIntentCall() {
        Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
        phoneIntent.setData(Uri.parse("tel:" + mBarPhone.getText().toString()));
        startActivity(phoneIntent);
    }
}
