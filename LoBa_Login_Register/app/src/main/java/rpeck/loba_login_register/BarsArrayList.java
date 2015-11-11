package rpeck.loba_login_register;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert Peck on 11/9/2015.
 */
public class BarsArrayList {

    private static List<Bar> mBarList = new ArrayList<>();
    private static List<BarIDs> mBarIDs = new ArrayList<>();

    //public BarsArrayList() { }

    public BarsArrayList(Bar bar) { mBarList.add(bar); }

    public BarsArrayList() {  }

    public void addBarToList(BarIDs barIDs) {
        mBarIDs.add(barIDs);
    }

    public static List<Bar> getmBarList() { return mBarList; }

    public static List<BarIDs> getmBarIDs() { return mBarIDs; }
}
