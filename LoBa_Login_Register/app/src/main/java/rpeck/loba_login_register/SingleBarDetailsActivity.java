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

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SingleBarDetailsActivity extends FragmentActivity implements View.OnClickListener, LoadBarSpecialsCommunicator {

    private TextView mBackButton, mAddSpecial;
    public static int mbarId;
    private boolean backBoolean = false;

    private ViewPager mViewPager;
    private List<DayOfWeek> mDayOfWeeks;

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

        mBackButton = (TextView) findViewById(R.id.back_button);
        mAddSpecial = (TextView) findViewById(R.id.add_bar_special);

        mUserLocalStore = new UserLocalStore(this);

        mAddSpecial.setOnClickListener(this);
        mBackButton.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(backBoolean) {
            clearSpecialList();
            FragmentManager manager = getSupportFragmentManager();
            SingleBarDetailsFragment f1 = (SingleBarDetailsFragment) manager.findFragmentById(R.id.single_bar_details_top_fragment_container);
            f1.pullBarSpecials(f1.getBar());
            backBoolean = false;
        }
        //TODO Get THIS TO UPDATE THE SPECIALS!!!
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearSpecialList();
    }

    private void LoadBarSpecials() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                DayOfWeek dayOfWeek = mDayOfWeeks.get(position);
                return BarSpecialsFragment.newInstance(dayOfWeek.getDate());
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

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String dateString = month + "/" + day;

        boolean foundDate = false;

        for(int i = 0; i < mDayOfWeeks.size(); i++) {
            if(mDayOfWeeks.get(i).getDate().equals(dateString)){
                mViewPager.setCurrentItem(i);
                foundDate = true;
                break;
            }
        }

        if(foundDate != true){
            mViewPager.setCurrentItem(mDayOfWeeks.size());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_bar_special:
                backBoolean = true;
                Intent intent = AddBarSpecial.newIntent(this, mbarId + "");
                startActivity(intent);
                break;
            case R.id.back_button:
                this.finish();
                break;
        }
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

    private void clearSpecialList(){
        SpecialsLab specialsLab = SpecialsLab.get(this);
        specialsLab.removeList();
    }

    private void showErrorMessage(String errorMessage) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setMessage(errorMessage);
        dialogBuilder.setPositiveButton("OK", null);
        dialogBuilder.show();
    }
}
