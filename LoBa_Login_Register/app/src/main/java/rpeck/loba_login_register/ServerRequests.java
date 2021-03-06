package rpeck.loba_login_register;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.DefaultedHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ServerRequests {
    //ServerRequests Class Makes Calls To The Database And Relays Information To The App

    //ProgressDialog Variable For Displaying A Progress Dialog Box
    ProgressDialog progressDialog;

    //Constant For Setting The Timeout Of The Connection To The Database
    public static final int CONNECTION_TIMEOUT = 1000 * 15;

    //Constant For Holding The Server Address That Holds The Databases
    public static final String SERVER_ADDRESS = "http://loba.hostoi.com/";

    public ServerRequests(Context context) {
        //Constructor That Sets The Parameters Of The Progress Dialog Box
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing...");
        progressDialog.setMessage("Please wait...");
    }

    public void storeUserDataInBackground(User user, GetUserCallback userCallBack) {
        //Method To Store The Registering User's Data In The Database
        progressDialog.show();
        new StoreUserDataAsyncTask(user, userCallBack).execute();
    }

    public void fetchUserDataAsyncTask(User user, GetUserCallback userCallBack) {
        //Method To Fetch The Users Data When Logging Into The App
        progressDialog.show();
        new FetchUserDataAsyncTask(user, userCallBack).execute();
    }

    public void fetchStateDataAsyncTask(States states, GetStatesCallback statesCallback){
        //Method To Fetch The Array Of States Stored In The Database
        progressDialog.show();
        new FetchStateDataAsyncTask(states, statesCallback).execute();
    }

    public void fetchCityDataAsyncTask(Cities cities, GetCitiesCallback getCitiesCallback) {
        //Method To Fetch The Array Of Cities Stored In The Database, This Is Based Off The StateID
        progressDialog.show();
        new FetchCityDataAsyncTask(cities, getCitiesCallback).execute();
    }

    public void fetchBarIDsDataAsyncTask(BarIDs barIDs, GetBarIDsCallback getBarIDsCallback) {
        progressDialog.show();
        new FetchBarIDsDataAsyncTask(barIDs, getBarIDsCallback).execute();
    }

    public void fetchBarDetailsDataAsyncTask(Bar bar, GetBarsCallBack getBarsCallBack) {
        progressDialog.show();
        new FetchBarDetailsDataAsyncTask(bar, getBarsCallBack).execute();
    }

    public void fetchBarSpecialsDataAsyncTask(DayOfWeek dayofweek, GetBarSpecialsCallback getBarSpecialsCallback) {
        progressDialog.show();
        new FetchBarSpecialsDataAsyncTask(dayofweek, getBarSpecialsCallback).execute();
    }

    public void storeSpecialDataAsyncTask(DayOfWeek dayOfWeek, GetBarSpecialsCallback getBarSpecialsCallback) {
        progressDialog.show();
        new StoreSpecialDataAsyncTask(dayOfWeek, getBarSpecialsCallback).execute();
    }

    public class StoreUserDataAsyncTask extends AsyncTask<Void, Void, Void> {
        //Class That Takes The User's Data And Sends The Information Over To Be Stored In The Database
        User user;
        GetUserCallback userCallBack;

        public StoreUserDataAsyncTask(User user, GetUserCallback userCallBack) {
            this.user = user;
            this.userCallBack = userCallBack;
        }

        @Override
        protected Void doInBackground(Void... params) {
            //Creates An ArrayList To Store The User's Data To Be Sent Over To The Database
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("name", user.mName));
            dataToSend.add(new BasicNameValuePair("username", user.mUserName));
            dataToSend.add(new BasicNameValuePair("password", user.mPassword));
            dataToSend.add(new BasicNameValuePair("city", user.mCity));
            dataToSend.add(new BasicNameValuePair("state", user.mState));
            dataToSend.add(new BasicNameValuePair("cityid", user.mCityID + ""));

            //Sets All The HTTP Variables To Connect To The Database
            HttpParams httpRequestParams = getHttpRequestParams();

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "Register.php");

            try {
                //Sends The User's Information Over To The Database
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);
            } catch (Exception e) {
                //Catches Any Error's That May Occur
                e.printStackTrace();
            }
            return null;
        }

        private HttpParams getHttpRequestParams() {
            //Sets The Connection Timeout To The Connection
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            return httpRequestParams;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //Method Is Called After The Execution Has Been Performed And Dismisses The Progress Dialog Box
            progressDialog.dismiss();
            //Calls The User Call Back Interface To Show The Execution Is Done
            userCallBack.done(null);
        }

    }

    public class FetchUserDataAsyncTask extends AsyncTask<Void, Void, User> {
        //Class That Takes The User's Username And Password, Then Sends The Information Over To Be Verified As Existing
        User user;
        GetUserCallback userCallBack;

        public FetchUserDataAsyncTask(User user, GetUserCallback userCallBack) {
            this.user = user;
            this.userCallBack = userCallBack;
        }

        @Override
        protected User doInBackground(Void... params) {
            //Creates An ArrayList To Store The User's Username And Password To Be Sent Over
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("username", user.mUserName));
            dataToSend.add(new BasicNameValuePair("password", user.mPassword));

            //Sets All The HTTP Variables To Connect To The Database
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchUserData.php");

            //Instantiates A New User And Sets It To Null
            User returnedUser = null;

            try {
                //Executes The Connection
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                //Pulls The Retrieved Information And Stores It In A JSONObject
                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                JSONObject jObject = new JSONObject(result);

                //The JSONObject Is Then Separated Into It's Parts And Stored
                if (jObject.length() != 0){
                    String name = jObject.getString("name");
                    String city = jObject.getString("city");
                    String state = jObject.getString("state");
                    int cityid = jObject.getInt("cityid");

                    //New User Is Created Off The Returned Information From The Database
                    returnedUser = new User(name, user.mUserName, user.mPassword, city, state, cityid);
                }

            } catch (Exception e) {
                //Catches Any Error's That May Occur
                e.printStackTrace();
            }
            return returnedUser;
        }

        @Override
        protected void onPostExecute(User returnedUser) {
            super.onPostExecute(returnedUser);
            //Method Is Called After The Execution Has Been Performed And Dismisses The Progress Dialog Box
            progressDialog.dismiss();
            //Calls The User Call Back Interface To Show The Execution Is Done
            userCallBack.done(returnedUser);
        }
    }

    public class FetchStateDataAsyncTask extends AsyncTask<Void, Void, States>{
        //Class That Retrieves All The States Out Of The Database
        States states;
        GetStatesCallback statesCallback;
        private String[] stateArray;
        private int[] stateIDArray;

        public FetchStateDataAsyncTask(States states, GetStatesCallback statesCallback) {
            this.states = states;
            this.statesCallback = statesCallback;
        }

        @Override
        protected States doInBackground(Void... voids) {
            //Sets All The HTTP Variables To Connect To The Database
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpGet get = new HttpGet(SERVER_ADDRESS + "FetchStateData.php");

            //Instantiates A New States Class And Sets It To Null
            States returnedStates = null;

            try {
                //Executes The Connection
                HttpResponse httpResponse = client.execute(get);

                //Pulls The Retrieved Information And Stores It In A JSONObject
                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                JSONObject jObject = new JSONObject(URLDecoder.decode(result, "UTF-8"));

                //Sets The Array Sizes Based On The Length Of The Information Sent Over From The Database
                stateArray = new String[jObject.length()];
                stateIDArray = new int[jObject.length()];

                //Loads The Data In JSONObject Into The Arrays
                for(int i = 0; i < jObject.length(); i++) {
                    stateArray[i] = jObject.getString((i + 1)+"");
                    stateIDArray[i] = (i + 1);
                }

                //New States Class Is Created Off Of The Information Returning From The Database
                returnedStates = new States(jObject.length(), stateArray, stateIDArray);

            } catch (Exception e) {
                //Catches Any Error's That May Occur
                e.printStackTrace();
                Log.d("ERROR", e.toString());
            }
            return returnedStates;
        }

        @Override
        protected void onPostExecute(States returnedStates) {
            super.onPostExecute(returnedStates);
            //Method Is Called After The Execution Has Been Performed And Dismisses The Progress Dialog Box
            progressDialog.dismiss();
            //Calls The States Call Back Interface To Show The Execution Is Done
            statesCallback.done(returnedStates);
        }
    }

    public class FetchCityDataAsyncTask extends AsyncTask<Void, Void, Cities> {
        //Class That Retrieves All The Cities Out Of The Database Based On The Sent Over StateID
        Cities cities;
        GetCitiesCallback citiesCallback;
        private String[] citiesArray;
        private int[] citiesIDArray;

        public FetchCityDataAsyncTask(Cities cities, GetCitiesCallback citiesCallback) {
            this.cities = cities;
            this.citiesCallback = citiesCallback;
        }

        @Override
        protected Cities doInBackground(Void... params) {
            //Creates An ArrayList To Stores The StateID To Be Sent Over
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("stateid", cities.state_id + ""));

            //Sets All The HTTP Variables To Connect To The Database
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchCitiesFromState.php");

            //Instantiates A New Cities Class And Sets It To Null
            Cities returnedCities = null;

            try {
                //Executes The Connection
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                //Pulls The Retrieved Information And Stores It In A JSONObject
                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                JSONObject jObject = new JSONObject(URLDecoder.decode(result, "UTF-8"));

                //Sets The Array Sizes Based On The Length Of The Information Sent Over From The Database
                citiesArray = new String[jObject.length()];
                citiesIDArray = new int[jObject.length()];
                //Creates A Variable To Collect The Key Of Each City
                Iterator<String> keys = jObject.keys();

                //Loads The Data In JSONObject Into The Arrays
                for (int i = 0; i <jObject.length(); i++){
                    String key = keys.next();
                    citiesIDArray[i] = Integer.parseInt(key);
                    citiesArray[i] = jObject.getString(key);
                }

                //New Cities Class Is Created Off Of The Information Returning From The Database
                returnedCities = new Cities(jObject.length(),cities.state_id, citiesArray, citiesIDArray);

            } catch (Exception e) {
                //Catches Any Error's That May Occur
                e.printStackTrace();
            }
            return returnedCities;
        }

        @Override
        protected void onPostExecute(Cities returnedCities) {
            super.onPostExecute(returnedCities);
            //Method Is Called After The Execution Has Been Performed And Dismisses The Progress Dialog Box
            progressDialog.dismiss();
            //Calls The Cities Call Back Interface To Show The Execution Is Done
            citiesCallback.done(returnedCities);
        }
    }

    public class FetchBarIDsDataAsyncTask extends AsyncTask<Void, Void, BarIDs> {
        BarIDs mBarIDs;
        GetBarIDsCallback mGetBarIDsCallback;
        private int barid;
        private String barName;
        BarsArrayList mBarsArrayList;

        public FetchBarIDsDataAsyncTask(BarIDs barIDs, GetBarIDsCallback getBarIDsCallback) {
            this.mBarIDs = barIDs;
            this.mGetBarIDsCallback = getBarIDsCallback;
            mBarsArrayList = new BarsArrayList();
        }

        @Override
        protected BarIDs doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("cityid", mBarIDs.cityid + ""));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchBarIdAndName.php");

            BarIDs returnedBarIDs = null;

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                JSONObject jObject = new JSONObject(URLDecoder.decode(result, "UTF-8"));

                Iterator<String> keys = jObject.keys();

                for (int i = 0; i < jObject.length(); i++) {
                    String key = keys.next();
                    barid = Integer.parseInt(key);
                    barName = jObject.getString(key);

                    returnedBarIDs = new BarIDs(barid, barName);
                    mBarsArrayList.addBarToList(returnedBarIDs);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return returnedBarIDs;
        }

        @Override
        protected void onPostExecute(BarIDs returnedBarIDs) {
            super.onPostExecute(returnedBarIDs);
            progressDialog.dismiss();
            mGetBarIDsCallback.done(returnedBarIDs);
        }
    }

    public class FetchBarDetailsDataAsyncTask extends AsyncTask<Void, Void, Bar> {
        Bar mBar;
        GetBarsCallBack mGetBarsCallBack;
        String mBarState;
        String mBarCity;

        public FetchBarDetailsDataAsyncTask(Bar bar, GetBarsCallBack getBarsCallBack) {
            mBar = bar;
            mGetBarsCallBack = getBarsCallBack;
            mBarState = mBar.mBarState;
            mBarCity = mBar.mBarCity;
        }

        @Override
        protected Bar doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("barid", mBar.mBarID + ""));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchBarData.php");

            Bar returnedBar = null;

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                JSONObject jObject = new JSONObject(URLDecoder.decode(result, "UTF-8"));

                if (jObject.length() != 0) {
                    String name = jObject.getString("name");
                    String address = jObject.getString("address");
                    String zipcode = jObject.getString("zipcode");
                    String phone = jObject.getString("phone");

                    returnedBar = new Bar(mBar.mBarID, name, address, mBarState, mBarCity, zipcode, phone);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return returnedBar;
        }

        @Override
        protected void onPostExecute(Bar returnedBar) {
            super.onPostExecute(returnedBar);
            progressDialog.dismiss();
            mGetBarsCallBack.done(returnedBar);
        }
    }

    public class FetchBarSpecialsDataAsyncTask extends AsyncTask<Void, Void, DayOfWeek> {
        DayOfWeek mDayOfWeek;
        BarSpecialsArrayList mBarSpecialsArrayList;
        Specials mSpecials;
        GetBarSpecialsCallback mGetBarSpecialsCallback;
        boolean dayExists = false;

        public FetchBarSpecialsDataAsyncTask(DayOfWeek dayOfWeek, GetBarSpecialsCallback getBarSpecialsCallback) {
            mDayOfWeek = dayOfWeek;
            mGetBarSpecialsCallback = getBarSpecialsCallback;
        }

        @Override
        protected DayOfWeek doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("barid", mDayOfWeek.getBarId() + ""));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchBarSpecials.php");

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                JSONObject jObject = new JSONObject(URLDecoder.decode(result, "UTF-8"));

                Iterator iterator = jObject.keys();

                while (iterator.hasNext()) {

                    String key = (String) iterator.next();

                    JSONObject record = jObject.getJSONObject(key);

                    String dayofweek = record.getString("dayofweek");
                    String special = record.getString("special");
                    String addedby = record.getString("addedby");
                    String dateforspecial = record.getString("dateforspecial");

                    if(mBarSpecialsArrayList != null) {
                        for (DayOfWeek dayOfWeek : mBarSpecialsArrayList.getmDaysOfWeek()) {
                            if(dayOfWeek.getDate().equals(dateforspecial)) {
                                mSpecials = new Specials(special, addedby);
                                mBarSpecialsArrayList.addSpecialToExistingDay(dayOfWeek, mSpecials);
                                dayExists = true;
                                break;
                            }
                        }
                    } else {
                        mBarSpecialsArrayList = new BarSpecialsArrayList();
                    }
                    if(dayExists == false) {
                        mSpecials = new Specials(special, addedby);
                        mDayOfWeek = new DayOfWeek(dayofweek, dateforspecial);
                        mDayOfWeek.addSpecials(mSpecials);
                        mBarSpecialsArrayList.addDayToWeek(mDayOfWeek);
                    }
                    dayExists = false;
                }

            } catch (Exception e) {
                e.printStackTrace();
                mDayOfWeek = null;
            }

            return mDayOfWeek;
        }

        @Override
        protected void onPostExecute(DayOfWeek returnedDayOfWeek) {
            super.onPostExecute(returnedDayOfWeek);
            progressDialog.dismiss();
            mGetBarSpecialsCallback.done(returnedDayOfWeek);
        }
    }

    public class StoreSpecialDataAsyncTask extends AsyncTask<Void, Void, Void> {
        DayOfWeek mDayOfWeek;
        GetBarSpecialsCallback mGetBarSpecialsCallback;
        String description;
        String addedBy;

        public StoreSpecialDataAsyncTask(DayOfWeek dayOfWeek, GetBarSpecialsCallback getBarSpecialsCallback) {
            mDayOfWeek = dayOfWeek;
            mGetBarSpecialsCallback = getBarSpecialsCallback;
            List<Specials> extractSpecial;
            extractSpecial = mDayOfWeek.getSpecials();
            Specials extractSingleSpecial;
            extractSingleSpecial = extractSpecial.get(0);

            description = extractSingleSpecial.getSpecial();
            addedBy = extractSingleSpecial.getAddedBy();
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("barid", mDayOfWeek.getBarId() + ""));
            dataToSend.add(new BasicNameValuePair("day", mDayOfWeek.getDayOfWeekString()));
            dataToSend.add(new BasicNameValuePair("description", description));
            dataToSend.add(new BasicNameValuePair("addedby", addedBy));
            dataToSend.add(new BasicNameValuePair("date", mDayOfWeek.getDate()));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "AddSpecial.php");

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            mGetBarSpecialsCallback.done(null);
        }
    }
}