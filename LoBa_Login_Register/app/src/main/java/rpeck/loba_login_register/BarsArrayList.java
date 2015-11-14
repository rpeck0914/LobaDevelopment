package rpeck.loba_login_register;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Robert Peck on 11/9/2015.
 */
public class BarsArrayList {

    private static List<BarIDs> mBarIDs = new ArrayList<>();

    public BarsArrayList() {  }

    public void addBarToList(BarIDs barIDs) {
        mBarIDs.add(barIDs);

        Collections.sort(mBarIDs, new Comparator<BarIDs>() {
            @Override
            public int compare(BarIDs lhs, BarIDs rhs) {
                return lhs.mBarNames.compareTo(rhs.mBarNames);
            }
        });
    }

    public static List<BarIDs> getmBarIDs() { return mBarIDs; }
}
