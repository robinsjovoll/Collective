package com.mobile.collective.implementation.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.mobile.collective.R;
import com.mobile.collective.client_server.HttpType;
import com.mobile.collective.client_server.ServerRequest;
import com.mobile.collective.framework.AndroidFileIO;
import com.mobile.collective.framework.AppMenu;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


/**
 * Created by Kristian on 11/09/2015.
 */
public class LoadingActivity extends AppMenu {

    @Override
    public void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);


        //TODO: ALPHA CODE, REMOVE AT LAUNCH:
//        This clears the sharedPref for userPrefrences (clear username).
//        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.profile_preferences), Context.MODE_PRIVATE);
//        sharedPref.edit().clear().commit();

        //Load things
        //Sets all the static classes for the application
//        setAppAssets(new Assets(this));
        setFileIO(new AndroidFileIO(this));
//        setMainController(new MainController(getFileIO(), this));


        //After done loading
        new LoadViewTask().execute();
    }

    //To use the AsyncTask, it must be subclassed
    //The parameters <Void, Integer, Void> means the task: <Params, Progress, Result>
    private class LoadViewTask extends AsyncTask<Void, Integer, Void> {


        //The code to be executed in a background thread.
        @Override
        protected Void doInBackground(Void... params) {
             /* This code creates/saves the user data and loads all the application assets
.             */
            try {
                //Get the current thread's token
                synchronized (this) {
                    //IMPORTANT, needs to be done first
                    //If the user already "logged" inn
                    SharedPreferences sharedPref = getSharedPreferences(getString(R.string.profile_preferences), Context.MODE_PRIVATE);
                    boolean isLoggedInn = sharedPref.getBoolean(getString(R.string.isLoggedInn), false);
                    if (!isLoggedInn) {
                        //ONLY DONE THE FIRST TIME THE APPLICATION IS CREATED
//                        JSONObject initialUserinfo = new JSONObject();
//
//                        getFileIO().writeInitialUserInformation();
                    }

                    //Loads the statistics from the phone internal storage
//                    getMainController().loadUserInformation();
//                    if(HTTPSender.info == null || !HTTPSender.info.isLoggedIn()){
//                        Log.e("LoadingScreen", "Did not log in the first time");
//                        getMainController().reCreateUserInformation();
//                    }


                }
            } catch (Exception e) {
                Log.e("LoadScreen", "LOADING APPLICATION DATA FAILED");
                e.printStackTrace();
            }

            return null;
        }

        //after executing the code in the thread
        @Override
        protected void onPostExecute(Void result) {

            SharedPreferences sharedPref = getSharedPreferences(getString(R.string.profile_preferences), Context.MODE_PRIVATE);
            boolean isLoggedInn = sharedPref.getBoolean(getString(R.string.isLoggedInn), false);
            Log.e("LoadingScreen", "isloggedin: " + isLoggedInn);
            if (isLoggedInn) {
                JSONObject userinfo = getFileIO().readUserInformation();
                HashMap<String,String> params = new HashMap<>();
                try {
                    params.put("email",userinfo.getString("email"));
                    params.put("password",userinfo.getString("password"));
                    ServerRequest sr = new ServerRequest();
                    JSONObject json = sr.getJSON(HttpType.LOGIN,"http://192.168.1.102:8080/login",params);
                    if(json.getBoolean("res")){
                        Toast.makeText(getApplication(), json.getString("response"), Toast.LENGTH_SHORT).show();
//                        goTo(MainActivity.class);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                goTo(LoginActivity.class);
            } else {
                goTo(LoginActivity.class);
            }
        }
    }

    @Override
    public void onBackPressed() {

    }

}
