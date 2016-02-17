package com.mobile.collective.implementation.view;

import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.mobile.collective.R;
import com.mobile.collective.framework.AppMenu;

public class LoginActivity extends AppMenu {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EditText mEmail = (EditText) findViewById(R.id.email);
        EditText mPassword = (EditText) findViewById(R.id.password);
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
        //TODO: Create login sequence.
    }

    /**
     * Sends the user to the register activity.
     * @param view
     */
    public void goToRegister(View view){
        goTo(RegisterActivity.class);
    }
}
