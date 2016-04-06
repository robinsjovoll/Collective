package com.mobile.collective.implementation.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobile.collective.R;
import com.mobile.collective.client_server.HttpType;
import com.mobile.collective.client_server.ServerRequest;
import com.mobile.collective.framework.AppMenu;
import com.mobile.collective.implementation.controller.MainMenuController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Robin on 17/02/2016.
 */
public class LoginActivity extends AppMenu {

    EditText eEmail, ePassword;
    HashMap<String,String> params = new HashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        eEmail = (EditText) findViewById(R.id.email);
        ePassword = (EditText) findViewById(R.id.password);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Contacts the server and tries to log in the user with the specified credentials.
     * @param view
     */
    public void tryLogin(View view){
        String email = eEmail.getText().toString().replaceAll("\\s+","");
        String password = ePassword.getText().toString();

        params.put("email", email);
        params.put("password", password);
        ServerRequest sr = new ServerRequest();
        final JSONObject json = sr.getJSON(HttpType.LOGIN,getIpAddress()+":8080/login",params);
        try {
            if(json != null && json.getBoolean("res")){
                Toast.makeText(getApplication(), json.getString("response"), Toast.LENGTH_SHORT).show();
                JSONObject userinfo = new JSONObject();
                userinfo.put("email",email);
                userinfo.put("password",password);
                userinfo.put("token",json.getString("token"));
                userinfo.put("grav",json.getString("grav"));
                getFileIO().writeUserInformationSaveFile(userinfo);
                SharedPreferences sharedPrefProf = getSharedPreferences(getString(R.string.profile_preferences), Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = sharedPrefProf.edit();
                edit.putBoolean(getString(R.string.isLoggedInn), Boolean.TRUE);
                boolean isInFlat = sharedPrefProf.getBoolean(getString(R.string.isInFlat), false);
                edit.commit();
                setPeriodOver(json.getBoolean("periodOver"));
                if(isInFlat)
                {
                    goTo(MainMenuController.class);
                }
                else
                {
                    goTo(FindFlatActivity.class);
                }
            }else if (json != null && !json.getBoolean("res")){
                Toast.makeText(getApplication(),json.getString("response"),Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getApplication(), getString(R.string.restart_app), Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends the user to the register activity.
     * @param view
     */
    public void goToRegister(View view){
        goTo(RegisterActivity.class);
    }

    /**
     * Resets the password of a specified email account.
     * @param view
     */
    public void resetPassword(View view){
        final Dialog reset = new Dialog(LoginActivity.this);
        reset.setTitle("Reset Password");
        reset.requestWindowFeature(Window.FEATURE_NO_TITLE);
        reset.setContentView(R.layout.activity_reset_password);
        final EditText eEmail = (EditText) reset.findViewById(R.id.email_reset);
        Button cont = (Button) reset.findViewById(R.id.continueBtn);
        Button cancel = (Button) reset.findViewById(R.id.cancelBtn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset.dismiss();
            }
        });
        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("LoginActivity","ContinueClicked");
                final String resetEmail = eEmail.getText().toString();
                params = new HashMap<String, String>();
                params.put("email", resetEmail);
                ServerRequest sr = new ServerRequest();
                JSONObject json = sr.getJSON(HttpType.CHANGEPASSWORD,getIpAddress()+":8080/api/resetpass", params);

                if (json != null) {
                    try {
                        String jsonstr = json.getString("response");
                        if(json.getBoolean("res")){
                            Log.e("JSON", jsonstr);
                            Toast.makeText(getApplication(), jsonstr, Toast.LENGTH_LONG).show();
                            reset.setContentView(R.layout.activity_reset_code);
                            Button cont_code = (Button)reset.findViewById(R.id.continueBtn_code);
                            final EditText code = (EditText)reset.findViewById(R.id.code_reset);
                            final EditText newpass = (EditText)reset.findViewById(R.id.new_password);
                            Button cancel1 = (Button)reset.findViewById(R.id.cancelBtn_code);
                            cancel1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    reset.dismiss();
                                }
                            });
                            cont_code.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String code_txt = code.getText().toString();
                                    String npass_txt = newpass.getText().toString();
                                    Log.e("Code",code_txt);
                                    Log.e("New pass", npass_txt);
                                    params = new HashMap<String, String>();
                                    params.put("email",resetEmail);
                                    params.put("code", code_txt);
                                    params.put("newpass", npass_txt);
                                    ServerRequest sr = new ServerRequest();
                                    JSONObject json = sr.getJSON(HttpType.CHANGEPASSWORD,getIpAddress()+":8080/api/resetpass/chg", params);

                                    if (json != null) {
                                        try {

                                            String jsonstr = json.getString("response");
                                            if(json.getBoolean("res")){
                                                reset.dismiss();
                                                Toast.makeText(getApplication(),jsonstr,Toast.LENGTH_LONG).show();
                                                JSONObject userInfo = getFileIO().readUserInformation();
                                                userInfo.put("password",npass_txt);

                                            }else{
                                                Toast.makeText(getApplication(),jsonstr,Toast.LENGTH_LONG).show();

                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                }
                            });
                        }else{

                            Toast.makeText(getApplication(),jsonstr,Toast.LENGTH_LONG).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }

        });

        reset.show();

    }
}
