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

//        for(int i = 0; i < 20; i++) {
//            Bar bar = new Bar();
//            bar.mBarID = i;
//            bar.mBarName =  "Bar #" + i;
//            bar.mBarAddress = "123 Main St.";
//            bar.mBarZipCode = 1234;
//            bar.mBarPhone = "123-234-1242";
//            mBars.add(bar);
//        }

        mBars = BarsArrayList.getmBarIDs();
    }

    public List<BarIDs> getBars() { return mBars; }

    public BarIDs getBar(int barid) {
        for (BarIDs barids : mBars) {
            if (barids.mBarIDs == barid) {
                return barids;
            }
        }
        return null;
    }

    public void removeList() {
        mBars.clear();
    }
}
