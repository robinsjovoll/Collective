package com.mobile.collective.implementation.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.mobile.collective.implementation.controller.LoadingController;
import com.mobile.collective.implementation.controller.MainMenuController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


/**
 * Created by Robin on 17/02/2016.
 */
public class LoadingActivity extends AppMenu {

    private LoadingController loadingController;
    private LoadingActivity loadingActivity = this;
    private boolean loggedIn = false;

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
        loadingController = new LoadingController(this);

        //After done loading
        new LoadViewTask().execute();
    }

    //To use the AsyncTask, it must be subclassed
    //The parameters <Void, Integer, Void> means the task: <Params, Progress, Result>
    private class LoadViewTask extends AsyncTask<Void, Integer, Void> {


        //The code to be executed in a background thread.
        @Override
        protected Void doInBackground(Void... params) {

            SharedPreferences sharedPref = getSharedPreferences(getString(R.string.profile_preferences), Context.MODE_PRIVATE);
            boolean isLoggedInn = sharedPref.getBoolean(getString(R.string.isLoggedInn), false);
//            Log.e("LoadingScreen", "isloggedin: " + isLoggedInn);
            if(isLoggedInn){
                loggedIn = loadingController.checkNetwork();
            }
             return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            SharedPreferences sharedPref = getSharedPreferences(getString(R.string.profile_preferences), Context.MODE_PRIVATE);
            boolean isLoggedInn = sharedPref.getBoolean(getString(R.string.isLoggedInn), false);
            boolean isInFlat = sharedPref.getBoolean(getString(R.string.isInFlat), false);
//            Log.e("LoadingScreen", "isloggedin: " + isLoggedInn);
            if (isLoggedInn) {
                if(loggedIn){
                    JSONObject userinfo = getFileIO().readUserInformation();
                    HashMap<String, String> params = new HashMap<>();
                    try {
                        params.put("email", userinfo.getString("email"));
                        params.put("password", userinfo.getString("password"));
                        ServerRequest sr = new ServerRequest();
                        final JSONObject json = sr.getJSON(HttpType.LOGIN, getIpAddress() + ":8080/login", params);
                        if (json != null && json.getBoolean("res")) {
                            Toast.makeText(getApplication(), json.getString("response"), Toast.LENGTH_SHORT).show();
                            setPeriodOver(json.getBoolean("periodOver"));
                            if(isInFlat)
                            {
                                goTo(MainMenuController.class);
                            }
                            else
                            {
                                goTo(FindFlatActivity.class);
                            }
                        } else if (json != null && json.getString("response").equals("Invalid Password") | json.getString("response").equals("User not exist")) {
                            Toast.makeText(getApplication(), json.getString("response"), Toast.LENGTH_SHORT).show();
                            goTo(LoginActivity.class);
                        } else {
                            Toast.makeText(getApplication(), getResources().getString(R.string.restart_app_server), Toast.LENGTH_LONG).show();
                            Log.e("LoadingController", "Something went wrong connecting to the server.");
//                    createDialog();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    Log.e("LoadingActivity", "Something went wrong when trying to login in the LoadingController");
                }
            } else {
                goTo(LoginActivity.class);
            }
        }
    }

    @Override
    public void onBackPressed() {

    }




}
