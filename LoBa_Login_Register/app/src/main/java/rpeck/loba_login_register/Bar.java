package rpeck.loba_login_register;

/**
 * Created by Robert Peck on 11/5/2015.
 */
public class Bar {

    public int mBarID;
    public String mBarName;
    public String mBarAddress;
    public int mBarZipCode;
    public String mBarPhone;

    public Bar() { }

    public Bar(int barID) {
        mBarID = barID;
    }

    public Bar(int barID, String barName, String barAddress, int barZipCode, String barPhone) {
        this.mBarID = barID;
        this.mBarName = barName;
        this.mBarAddress = barAddress;
        this.mBarZipCode = barZipCode;
        this.mBarPhone = barPhone;
    }
}
