package rpeck.loba_login_register;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.List;

/**
 * Created by Robert Peck on 11/13/2015.
 */
public class BarSpecialsPagerActivity extends FragmentActivity {

    private ViewPager mViewPager;
    private List<Specials> mSpecials;

    private static final String EXTRA_BAR_ID = "rpeck.loba_login_register.bar_id";

    public static Intent newIntent(Context context, String barId) {
        Intent intent = new Intent(context, BarSpecialsPagerActivity.class);
        intent.putExtra(EXTRA_BAR_ID, barId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_bar_specials_pager);

        String barId = getIntent().getStringExtra(EXTRA_BAR_ID);

        //mViewPager = (ViewPager) findViewById(R.id.activity_bar_specials_pager_view_pager);

        //mSpecials = SpecialLab.get(this).getSpecials();

        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                //Specials specials = mSpecials.get(i);
                //return SpecialsFragment.newInstance(specials.getBarId());
                return null;
            }

            @Override
            public int getCount() {
                //return mSpecials.size();
                return 0;
            }
        });

        for (int i = 0; i < mSpecials.size(); i++) {
            //if (mSpecials.get(i).)
        }
    }
}
