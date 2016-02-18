package com.mobile.collective.framework;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.mobile.collective.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Robin Sjøvoll on 18.02.2016.
 * Saves the user data as a string, or as shared pref data
 * FileIO is not used widely because server saves user data.
 */
public class AndroidFileIO {


//    private static File savedData;
    private AppMenu context;

    public AndroidFileIO(AppMenu context){
        this.context = context;
    }

    /**
     * Update the user information in the internal storage.
     * @param userInfo
     */
    public void writeUserInformationSaveFile(JSONObject userInfo){
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.userInfo), Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();
        prefEditor.putString(context.getString(R.string.userInfo), userInfo.toString());
        prefEditor.commit();
        Log.e("AndroidFileIO", "userinfo: " + sharedPreferences.getString(context.getString(R.string.userInfo), null));
    }


    /**
     * Writes the initial user information on first start of application.
     * @param userInformation
     */
    public void writeInitialUserInformation(JSONObject userInformation){
        String string = userInformation.toString();
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.userInfo), Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();
        prefEditor.putString( context.getString(R.string.userInfo), string );
        prefEditor.commit();
        Log.e("AndroidFileIO", "userInformation: " + sharedPreferences.getString(context.getString(R.string.userInfo), null));
    }


    public JSONObject readUserInformation(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.userInfo), Context.MODE_PRIVATE);
        String userInformationString = sharedPreferences.getString(context.getString(R.string.userInfo), null);
        JSONObject userInformation = null;
        try {
            userInformation = new JSONObject(userInformationString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return userInformation;
    }

    public void removeSaveFile(){

    }

}
