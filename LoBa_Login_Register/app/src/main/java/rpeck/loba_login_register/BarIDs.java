package rpeck.loba_login_register;

/**
 * Created by Robert Peck on 11/4/2015.
 */
public class BarIDs {

    int cityid;
    public int mBarID;
    public String mBarNames;

    public BarIDs(int cityid){
        this.cityid = cityid;
    }

    public BarIDs(int barids, String barNames) {
        this.mBarID = barids;
        this.mBarNames = barNames;
    }
}
