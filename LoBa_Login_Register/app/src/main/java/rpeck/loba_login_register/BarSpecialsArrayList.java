package rpeck.loba_login_register;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Robert Peck on 11/16/2015.
 */
public class BarSpecialsArrayList {

    private static List<DayOfWeek> mDaysOfWeek = new ArrayList<>();

    public BarSpecialsArrayList() { }

    public void addDayToWeek(DayOfWeek dayOfWeek) {
        mDaysOfWeek.add(dayOfWeek);
        sortArrayListByDate();
    }

    public void addSpecialToExistingDay(DayOfWeek sentDayOfWeek, Specials specials) {
        for(DayOfWeek dayOfWeek: mDaysOfWeek) {
            if (dayOfWeek.getDayOfWeekString().equals(sentDayOfWeek.getDayOfWeekString())) {
                dayOfWeek.addSpecials(specials);
            }
        }
        sortArrayListByDate();
    }

    public static List<DayOfWeek> getmDaysOfWeek() {
        return mDaysOfWeek;
    }

    private void sortArrayListByDate() {
        Collections.sort(mDaysOfWeek, new Comparator<DayOfWeek>() {
            @Override
            public int compare(DayOfWeek lhs, DayOfWeek rhs) {
                return lhs.getDate().compareTo(rhs.getDate());
            }
        });
    }
}
