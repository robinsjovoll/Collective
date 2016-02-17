package com.mobile.collective.implementation.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.mobile.collective.R;
import com.mobile.collective.framework.AppMenu;


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
//        setFileIO(new AndroidFileIO(this));
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
//                        getMainController().createUserInformation();
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
//                goTo(MainMenu.class);
                goTo(LoginActivity.class);
            } else {
                //Initialize the next Activity if not logged inn and set the logged inn boolean to true, so that the next time the user starts the application he/she will not go to the introduction menu.
                SharedPreferences.Editor edit = sharedPref.edit();
                edit.putBoolean(getString(R.string.isLoggedInn), Boolean.TRUE);
                edit.commit();
                goTo(LoginActivity.class);
            }
        }
    }

    @Override
    public void onBackPressed() {

    }

}
