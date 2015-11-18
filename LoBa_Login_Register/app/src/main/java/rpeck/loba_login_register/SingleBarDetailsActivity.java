package rpeck.loba_login_register;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class SingleBarDetailsActivity extends FragmentActivity implements View.OnClickListener, LoadBarSpecialsCommunicator {

    private TextView mLoggedinName, mAddSpecial;
    public static int mbarId;

    private ViewPager mViewPager;
    private List<DayOfWeek> mDayOfWeeks;
    private BarSpecialsArrayList mBarSpecialsArrayList;

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

        Fragment fragment1 = fm1.findFragmentById(R.id.single_bar_details_top_fragment_container);

        if(fragment1 == null) {
            fragment1 = new SingleBarDetailsFragment();
            fm1.beginTransaction()
                    .add(R.id.single_bar_details_top_fragment_container, fragment1)
                    .commit();
        }

        mViewPager = (ViewPager) findViewById(R.id.activity_bar_special_pager_view_pager);
        mDayOfWeeks = SpecialsLab.get(this).getDayOfWeeks();

        mLoggedinName = (TextView) findViewById(R.id.logged_in_name);
        mAddSpecial = (TextView) findViewById(R.id.add_bar_special);

        mUserLocalStore = new UserLocalStore(this);

        mAddSpecial.setOnClickListener(this);

        displayUserDetails();
    }

    private void LoadBarSpecials() {
        //SpecialsLab specialsLab = SpecialsLab.get(this);


        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                DayOfWeek dayOfWeek = mDayOfWeeks.get(position);
                return BarSpecialsFragment.newInstance(dayOfWeek.getDayOfWeekString());
            }

            @Override
            public void notifyDataSetChanged() {
                super.notifyDataSetChanged();
            }

            @Override
            public int getCount() {
                return mDayOfWeeks.size();
            }
        });

        for(int i = 0; i < mDayOfWeeks.size(); i++) {
            if(mDayOfWeeks.get(i).getDayOfWeekString().equals("Friday")){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
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

    @Override
    public void loadViewPager(String name) {
        if (name == "ErrorLoadingSpecials") {
            String errorMessage = "No Bar Specials Listed For This Bar. If You Would Like To Add A Special, Please Hit The Add Special Link";
            showErrorMessage(errorMessage);
        } else if (name == "LoadBarSpecials") {
            LoadBarSpecials();
        }
    }

    private void showErrorMessage(String errorMessage) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setMessage(errorMessage);
        dialogBuilder.setPositiveButton("OK", null);
        dialogBuilder.show();
    }
}
