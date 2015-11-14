package rpeck.loba_login_register;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert Peck on 11/5/2015.
 */
public class BarLab {

    private static BarLab sBarLab;

    private List<BarIDs> mBars;

    public static BarLab get(Context context) {
        if(sBarLab == null) {
            sBarLab = new BarLab(context);
        }
        return sBarLab;
    }

    private BarLab(Context context) {

        mBars = new ArrayList<>();

        mBars = BarsArrayList.getmBarIDs();
    }

    public List<BarIDs> getBars() { return mBars; }

    public BarIDs getBar(int barid) {
        for (BarIDs barids : mBars) {
            if (barids.mBarID == barid) {
                return barids;
            }
        }
        return null;
    }

    public void removeList() {
        mBars.clear();
    }
}
