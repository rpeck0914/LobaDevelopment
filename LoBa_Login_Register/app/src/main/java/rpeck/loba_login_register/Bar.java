package rpeck.loba_login_register;

/**
 * Created by Robert Peck on 11/5/2015.
 */
public class Bar {

    public int mBarID;
    public String mBarName;
    public String mBarAddress;
    public String  mBarZipCode;
    public String mBarPhone;
    public String mBarState;
    public String mBarCity;

    public Bar() { }

    public Bar(int barID, String barState, String barCity) {
        mBarID = barID;
        mBarState = barState;
        mBarCity = barCity;
    }

    public Bar(int barID, String barName, String barAddress, String barState, String barCity, String barZipCode, String barPhone) {
        this.mBarID = barID;
        this.mBarName = barName;
        this.mBarAddress = barAddress;
        this.mBarState = barState;
        this.mBarCity = barCity;
        this.mBarZipCode = barZipCode;
        this.mBarPhone = barPhone;
    }
}
