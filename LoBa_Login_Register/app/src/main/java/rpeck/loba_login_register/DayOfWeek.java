package rpeck.loba_login_register;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert Peck on 11/16/2015.
 */
public class DayOfWeek {

    private String mDayOfWeekString;
    private String mDate;
    private List<Specials> mSpecials = new ArrayList<>();
    private int mBarId;

    public DayOfWeek(int barId) {
        mBarId = barId;
    }

    public DayOfWeek(String day, String date) {
        mDayOfWeekString = day;
        mDate = date;
    }

    public void addSpecials(Specials specials) {
        mSpecials.add(specials);
    }

    public String getDayOfWeekString() {
        return mDayOfWeekString;
    }

    public String getDate() {
        return mDate;
    }

    public List<Specials> getSpecials() {
        return mSpecials;
    }

    public int getBarId() {
        return mBarId;
    }
}
