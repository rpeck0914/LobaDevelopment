package rpeck.loba_login_register;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private TextView mSpecialDetail;
    private TextView mAddedByTag;

    public static BarSpecialsFragment newInstance(String dayofweek) {
        Bundle args = new Bundle();
        args.putString(ARG_BAR_SPECIAL_DAY, dayofweek);
        BarSpecialsFragment fragment = new BarSpecialsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String dayOfWeek = getArguments().getString(ARG_BAR_SPECIAL_DAY);
        mDayOfWeek = SpecialsLab.get(getActivity()).getSpecialDay(dayOfWeek);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bar_special,container, false);

        mDayOfTheWeek = (TextView) v.findViewById(R.id.day_of_the_week_title);
        mDayOfTheWeek.setText(mDayOfWeek.getDayOfWeekString());

        mDate = (TextView) v.findViewById(R.id.month_and_date_title);
        mDate.setText(mDayOfWeek.getDate());

        mSpecialDetail = (TextView) v.findViewById(R.id.special_slot_1);
        mAddedByTag = (TextView) v.findViewById(R.id.special_slot_1_added_by);

        List<Specials> mSpecials = mDayOfWeek.getSpecials();

        for (int i = 0; i < mDayOfWeek.getSpecials().size(); i++){
            Specials specials = mSpecials.get(i);
            String details = specials.getSpecial();
            String addedby = specials.getAddedBy();
            mSpecialDetail.setText(details);
            mAddedByTag.setText(addedby);
        }

//        mSpecialDetail.setText("$2 Shots");
//        mAddedByTag.setText("rpeck");


        return v;
    }
}
