package com.mobile.collective.implementation.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mobile.collective.R;
import com.mobile.collective.client_server.HttpType;
import com.mobile.collective.client_server.ServerRequest;
import com.mobile.collective.framework.AppMenu;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Robin on 17.02.2016.
 */
public class RegisterActivity extends AppMenu {

    EditText eUsername, eEmail, ePassword, eRePassword;
    HashMap<String,String> params = new HashMap<>();



    /**
     * Different regex expressions that validates the different inputs.
     */
    private static final String USERNAME_PATTERN = "^[a-z0-9_-]{3,15}$";
    private Pattern usernamePattern;
    private Matcher usernameMatcher;

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private Pattern emailPattern;
    private Matcher emailMatcher;

    private static final String PASSWORD_PATTERN =
            "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})";
    private  Pattern passwordPattern;
    private Matcher passwordMatcher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        eUsername = (EditText) findViewById(R.id.name_register);
        eEmail = (EditText) findViewById(R.id.email_register);
        ePassword = (EditText) findViewById(R.id.password_register);
        eRePassword = (EditText) findViewById(R.id.re_password_register);

        usernamePattern = Pattern.compile(USERNAME_PATTERN);
        emailPattern = Pattern.compile(EMAIL_PATTERN);
        passwordPattern = Pattern.compile(PASSWORD_PATTERN);

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

    public void tryRegister(View view){
        String username = eUsername.getText().toString().replaceAll("\\s+","");
        String email = eEmail.getText().toString().replaceAll("\\s+","");
        String password = ePassword.getText().toString();
        String rePassword = eRePassword.getText().toString();
        if(!validUsername(username)) {
            Toast.makeText(getApplicationContext(), getString(R.string.invalid_username), Toast.LENGTH_LONG).show();
            return;
        }else if(!validEmail(email)){
            Toast.makeText(getApplicationContext(), getString(R.string.invalid_email), Toast.LENGTH_LONG).show();
            return;
        }else if(!validPassword(password)){
            Toast.makeText(getApplicationContext(), getString(R.string.invalid_password), Toast.LENGTH_LONG).show();
            return;
        }else if(!password.equals(rePassword)){
            Toast.makeText(getApplicationContext(), getString(R.string.invalid_repassword), Toast.LENGTH_LONG).show();
            return;
        }else{
            params.put("email",email);
            params.put("username",username);
            params.put("password",password);
            ServerRequest sr = new ServerRequest();
            JSONObject json = sr.getJSON(HttpType.REGISTER,getIpAddress()+":8080/register",params);
            if(json != null){
                try{
                    String jsonstr = json.getString("response");

                    Toast.makeText(getApplication(),jsonstr,Toast.LENGTH_LONG).show();
                    if(json.getBoolean("res")){
                        SharedPreferences sharedPrefProf = getSharedPreferences(getString(R.string.profile_preferences), Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = sharedPrefProf.edit();
                        edit.putBoolean(getString(R.string.isLoggedInn), Boolean.TRUE);
                        edit.commit();
                        JSONObject userinfo = new JSONObject();
                        userinfo.put("email", email);
                        userinfo.put("username", username);
                        userinfo.put("password", password);
                        getFileIO().writeUserInformationSaveFile(userinfo);
                        goTo(LoginActivity.class);
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                Toast.makeText(getApplication(), getString(R.string.restart_app), Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Checks if the username is valid.
     * @param username
     * @return
     */
    private boolean validUsername(final String username){
        usernameMatcher = usernamePattern.matcher(username);
        return usernameMatcher.matches();
    }

    /**
     * Checks if the email is valid.
     * @param email
     * @return
     */
    private boolean validEmail(final String email){
        emailMatcher = emailPattern.matcher(email);
        return emailMatcher.matches();
    }

    /**
     * Checks if the password is valid.
     * @param password
     * @return
     */
    private boolean validPassword(final String password){
        passwordMatcher = passwordPattern.matcher(password);
        return passwordMatcher.matches();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
        goTo(LoginActivity.class);
    }
}
