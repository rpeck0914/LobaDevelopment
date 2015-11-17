package rpeck.loba_login_register;

/**
 * Created by Robert Peck on 11/13/2015.
 */
public class Specials {

    private String mSpecial;
    private String mAddedBy;

    public Specials(String special, String addedBy) {
        mSpecial = special;
        mAddedBy = addedBy;
    }

    public String getSpecial() {
        return mSpecial;
    }

    public String getAddedBy() {
        return mAddedBy;
    }

    public void setSpecial(String special) {
        mSpecial = special;
    }

    public void setAddedBy(String addedBy) {
        mAddedBy = addedBy;
    }
}
