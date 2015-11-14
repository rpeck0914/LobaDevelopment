package rpeck.loba_login_register;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class SingleBarDetailsActivity extends FragmentActivity implements View.OnClickListener {

    private TextView mLoggedinName, mAddSpecial;
    public static int mbarId;

    private UserLocalStore mUserLocalStore;

    public static final String EXTRA_BAR_ID = "rpeck.loba_login_register.bar_id";

    public static Intent newIntent(Context context, String barId) {
        Intent intent = new Intent(context, SingleBarDetailsActivity.class);
        intent.putExtra(EXTRA_BAR_ID, barId);
        mbarId = Integer.parseInt(barId);
        return intent;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_bar_details);

        FragmentManager fm1 = getSupportFragmentManager();
        FragmentManager fm2 = getSupportFragmentManager();

        Fragment fragment1 = fm1.findFragmentById(R.id.single_bar_details_top_fragment_container);
        Fragment fragment2 = fm2.findFragmentById(R.id.single_bar_details_bottom_fragment_container);

        if(fragment1 == null) {
            fragment1 = new SingleBarDetailsFragment();
            fm1.beginTransaction()
                    .add(R.id.single_bar_details_top_fragment_container, fragment1)
                    .commit();
        }

        mLoggedinName = (TextView) findViewById(R.id.logged_in_name);
        mAddSpecial = (TextView) findViewById(R.id.add_bar_special);

        mUserLocalStore = new UserLocalStore(this);

        mAddSpecial.setOnClickListener(this);

        displayUserDetails();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_bar_special:
                //TODO Start New Activity To Add A Bar Special!
                break;
        }
    }

    private void displayUserDetails() {
        User user = mUserLocalStore.getLoggedInUser();
        mLoggedinName.setText(user.mName);
    }
}
