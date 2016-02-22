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
 * Created by Robin on 20/02/2016.
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


        //After done loading
        new LoadViewTask().execute();
    }

    //To use the AsyncTask, it must be subclassed
    //The parameters <Void, Integer, Void> means the task: <Params, Progress, Result>
    private class LoadViewTask extends AsyncTask<Void, Integer, Void> {


        //The code to be executed in a background thread.
        @Override
        protected Void doInBackground(Void... params) {
             return null;
        }

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

                    if(json != null & json.getBoolean("res")){
                        Toast.makeText(getApplication(), json.getString("response"), Toast.LENGTH_SHORT).show();
//                        goTo(MainActivity.class);
                    }else{
                        Toast.makeText(getApplication(), getString(R.string.restart_app), Toast.LENGTH_LONG).show();
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
