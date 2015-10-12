package rpeck.loba_login_register;

import android.app.ListActivity;
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
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


public class ServerRequests {
    ProgressDialog progressDialog;
    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    public static final String SERVER_ADDRESS = "http://loba.hostoi.com/";

    public ServerRequests(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing...");
        progressDialog.setMessage("Please wait...");
    }

    public void storeUserDataInBackground(User user, GetUserCallback userCallBack) {
        progressDialog.show();
        new StoreUserDataAsyncTask(user, userCallBack).execute();
    }

    public void fetchUserDataAsyncTask(User user, GetUserCallback userCallBack) {
        progressDialog.show();
        new fetchUserDataAsyncTask(user, userCallBack).execute();
    }

    public void fetchStateDataAsyncTask(){
        progressDialog.show();
        new fetchStateDataAsyncTask().execute();
    }

    public void fetchCityDataAsyncTask(Cities cities, GetCitiesCallback getCitiesCallback) {
        progressDialog.show();
        new fetchCityDataAsyncTask(cities, getCitiesCallback);
    }

    /**
     * parameter sent to task upon execution progress published during
     * background computation result of the background computation
     */

    public class StoreUserDataAsyncTask extends AsyncTask<Void, Void, Void> {
        User user;
        GetUserCallback userCallBack;

        public StoreUserDataAsyncTask(User user, GetUserCallback userCallBack) {
            this.user = user;
            this.userCallBack = userCallBack;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("name", user.mName));
            dataToSend.add(new BasicNameValuePair("username", user.mUserName));
            dataToSend.add(new BasicNameValuePair("password", user.mPassword));
            dataToSend.add(new BasicNameValuePair("city", user.mCity));
            dataToSend.add(new BasicNameValuePair("state", user.mState));

            HttpParams httpRequestParams = getHttpRequestParams();

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "Register.php");

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        private HttpParams getHttpRequestParams() {
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            return httpRequestParams;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            userCallBack.done(null);
        }

    }

    public class fetchUserDataAsyncTask extends AsyncTask<Void, Void, User> {
        User user;
        GetUserCallback userCallBack;

        public fetchUserDataAsyncTask(User user, GetUserCallback userCallBack) {
            this.user = user;
            this.userCallBack = userCallBack;
        }

        @Override
        protected User doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("username", user.mUserName));
            dataToSend.add(new BasicNameValuePair("password", user.mPassword));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchUserData.php");

            User returnedUser = null;

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                JSONObject jObject = new JSONObject(result);

                if (jObject.length() != 0){
                    String name = jObject.getString("name");
                    String city = jObject.getString("city");
                    String state = jObject.getString("state");

                    returnedUser = new User(name, user.mUserName, user.mPassword, city, state);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return returnedUser;
        }

        @Override
        protected void onPostExecute(User returnedUser) {
            super.onPostExecute(returnedUser);
            progressDialog.dismiss();
            userCallBack.done(returnedUser);
        }
    }

    public class fetchStateDataAsyncTask extends AsyncTask<Void, Void, Void>{

        private String[] stateArray;

        public fetchStateDataAsyncTask() {

        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpGet get = new HttpGet(SERVER_ADDRESS + "FetchStateData.php");


            
            try {
                HttpResponse httpResponse = client.execute(get);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                //JSONObject jObject = new JSONObject(result);
                JSONArray jArray = new JSONArray(result);

                stateArray = new String[jArray.length()];

                for(int i = 1; i < jArray.length(); i++) {
                    stateArray[i] = jArray.getString(i);
                }

            } catch (Exception e) {
                
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }

        public String[] getStateArray() {
            return stateArray;
        }
    }

    public class fetchCityDataAsyncTask extends AsyncTask<Void, Void, Cities> {
        Cities cities;
        GetCitiesCallback citiesCallback;

        public fetchCityDataAsyncTask(Cities cities, GetCitiesCallback citiesCallback) {
            this.cities = cities;
            this.citiesCallback = citiesCallback;
        }

        @Override
        protected Cities doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("stateID", cities.state_id + ""));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchCitiesFromState.php");

            String[] citiesArray;
            Cities returnedCities = null;

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                JSONObject jObject = new JSONObject(result);

                citiesArray = new String[jObject.length()];

                //citiesArray = jObject.getString("counter");

//                for(int i = 0; i < jObject.length(); i++) {
//                    citiesArray[i] = jObject.getString("cityname");
//                }
                returnedCities = new Cities(jObject.length(), cities.state_id, citiesArray);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return returnedCities;
        }

        @Override
        protected void onPostExecute(Cities returnedCities) {
            super.onPostExecute(returnedCities);
            progressDialog.dismiss();
            citiesCallback.done(returnedCities);
        }
    }
}