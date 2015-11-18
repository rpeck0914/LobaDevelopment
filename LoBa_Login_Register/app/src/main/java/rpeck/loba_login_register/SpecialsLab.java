package rpeck.loba_login_register;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert Peck on 11/18/2015.
 */
public class SpecialsLab {

    private static SpecialsLab sSpecialsLab;

    private List<DayOfWeek> mDayOfWeeks;

    public static SpecialsLab get(Context context) {
        if(sSpecialsLab == null) {
            sSpecialsLab = new SpecialsLab(context);
        }
        return sSpecialsLab;
    }

    private SpecialsLab(Context context) {
        mDayOfWeeks = new ArrayList<>();
        mDayOfWeeks = BarSpecialsArrayList.getmDaysOfWeek();
    }

    public List<DayOfWeek> getDayOfWeeks() {
        return mDayOfWeeks;
    }

    public DayOfWeek getSpecialDay(String sentDayOfWeek) {
        for (DayOfWeek dayOfWeek : mDayOfWeeks) {
            if(dayOfWeek.getDayOfWeekString().equals(sentDayOfWeek)){
                return dayOfWeek;
            }
        }
        return null;
    }

    public void removeList() {
        mDayOfWeeks.clear();
    }
}
