package rpeck.loba_login_register;


/**
 * Created by Robert Peck on 10/30/2015.
 */
public class ArraySort {

    private String[] mNameArray;
    private int[] mIDArray;
    //private int mArrayId;

    public ArraySort(String[] name, int[] id){
        mNameArray = name;
        mIDArray = id;
        //mArrayId = arrayID;

        sort();
    }

    private void sort(){
        boolean flag = true;
        String tempName;
        int tempID;

        while (flag) {
            flag = false;
            for(int i = 0; i < mNameArray.length - 1; i++){
                if(mNameArray[i].compareToIgnoreCase(mNameArray[i + 1]) > 0){
                    tempName = mNameArray[i];
                    tempID = mIDArray[i];
                    mNameArray[i] = mNameArray[i + 1];
                    mIDArray[i] = mIDArray[i + 1];
                    mNameArray[i + 1] = tempName;
                    mIDArray[i + 1] = tempID;

                    flag = true;
                }
            }
        }
    }

    public String[] getNameArray() {
        return mNameArray;
    }

    public int[] getIDArray() {
        return mIDArray;
    }
}
