package rpeck.loba_login_register;

import android.app.ActionBar;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Robert Peck on 11/18/2015.
 */
public class BarSpecialsFragment extends Fragment {

    private static final String ARG_BAR_SPECIAL_DAY = "day_of_week";

    private DayOfWeek mDayOfWeek;

    private TextView mDayOfTheWeek;
    private TextView mDate;
    private View mLinearLayoutHolder;

    public static BarSpecialsFragment newInstance(String date) {
        Bundle args = new Bundle();
        args.putString(ARG_BAR_SPECIAL_DAY, date);
        BarSpecialsFragment fragment = new BarSpecialsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String date = getArguments().getString(ARG_BAR_SPECIAL_DAY);
        mDayOfWeek = SpecialsLab.get(getActivity()).getSpecialDay(date);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bar_special,container, false);

        mLinearLayoutHolder = v.findViewById(R.id.special_holder_for_text_views);

        mDayOfTheWeek = (TextView) v.findViewById(R.id.day_of_the_week_title);
        mDayOfTheWeek.setText(mDayOfWeek.getDayOfWeekString());

        mDate = (TextView) v.findViewById(R.id.month_and_date_title);
        mDate.setText(mDayOfWeek.getDate());

        List<Specials> mSpecials = mDayOfWeek.getSpecials();

        for (int i = 0; i < mDayOfWeek.getSpecials().size(); i++){

            Specials specials = mSpecials.get(i);
            String details = specials.getSpecial();
            String addedBy = specials.getAddedBy();

            TextView detailsHolder = new TextView(getActivity());
            detailsHolder.setId(i);
            detailsHolder.setText(details);
            detailsHolder.setTextSize(20);
            detailsHolder.setTypeface(Typeface.DEFAULT_BOLD);
            detailsHolder.setGravity(Gravity.RIGHT);
            detailsHolder.setTextColor(getResources().getColor(R.color.single_bar_text_color));
            LinearLayout.LayoutParams detailsHolderParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            detailsHolderParams.setMargins(0,10,0,0);
            ((LinearLayout) mLinearLayoutHolder).addView(detailsHolder);

            TextView addedByHolder = new TextView(getActivity());
            addedByHolder.setId(i);
            addedByHolder.setText(addedBy);
            addedByHolder.setTextSize(14);
            addedByHolder.setGravity(Gravity.RIGHT);
            addedByHolder.setTextColor(getResources().getColor(R.color.single_bar_text_color));
            LinearLayout.LayoutParams addedByHolderParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            addedByHolderParams.setMargins(0,0,0,10);
            ((LinearLayout) mLinearLayoutHolder). addView(addedByHolder);
        }
        return v;
    }
}
