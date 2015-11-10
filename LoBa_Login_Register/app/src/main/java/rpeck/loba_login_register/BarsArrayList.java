package rpeck.loba_login_register;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert Peck on 11/9/2015.
 */
public class BarsArrayList {

    private static List<Bar> mBarList = new ArrayList<>();

    //public BarsArrayList() { }

    public BarsArrayList(Bar bar) {
        mBarList.add(bar);
    }

    public static List<Bar> getmBarList() {
        return mBarList;
    }
}
