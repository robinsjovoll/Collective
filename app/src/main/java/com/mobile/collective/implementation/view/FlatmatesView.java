package com.mobile.collective.implementation.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.collective.R;
import com.mobile.collective.client_server.HttpType;
import com.mobile.collective.client_server.ServerRequest;
import com.mobile.collective.framework.AppMenu;
import com.mobile.collective.framework.CustomFlatMatesListAdapter;
import com.mobile.collective.implementation.controller.MainMenuController;
import com.mobile.collective.implementation.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class FlatmatesView extends AppMenu {

    private TextView displayRights;
    private ListView flatMatesList;
    private CustomFlatMatesListAdapter flatMatesListAdapter;
    private String[] flatmateUsernames;
    private String[] flatmateEmails;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flatmates);

        flatMatesList = (ListView) findViewById(R.id.flat_mates_list);
        displayRights = (TextView) findViewById(R.id.display_rights);
        initFlatMatesView();
    }

    public void initFlatMatesView(){
        ServerRequest sr = new ServerRequest();
        HashMap<String, String> params = new HashMap<>();
        params.put("flatPIN", getUser().getFlatPin());
        JSONObject jsonObject = sr.getJSON(HttpType.GETFLATMATES, getIpAddress() + ":8080/getFlatMates",params);
        if(jsonObject != null) {
            try {
                JSONArray flatMates = jsonObject.getJSONArray("response");
                Log.e("FlatMatesView", flatMates.toString());
                ArrayList<String> tempFlatMatesUsername = new ArrayList<>();
                ArrayList<String> tempFlatMatesEmail = new ArrayList<>();
                for(int i = 0; i < flatMates.length(); i++){
                    tempFlatMatesUsername.add(flatMates.getJSONArray(i).getString(0));
                    tempFlatMatesEmail.add(flatMates.getJSONArray(i).getString(1));
                }
                flatmateUsernames = tempFlatMatesUsername.toArray(new String[0]);
                flatmateEmails = tempFlatMatesEmail.toArray(new String[0]);
                flatMatesListAdapter = new CustomFlatMatesListAdapter(this, flatmateUsernames, getUser().getName(), getUser().isAdmin(), flatmateEmails);
                flatMatesList.setAdapter(flatMatesListAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public void goToMainView(View v) {
        goTo(MainMenuController.class);
    }

    /**
     * Remove the specific user from the flat
     * @param view
     */
    public void removeUser(View view){
        RelativeLayout parentLayout = (RelativeLayout)view.getParent();
        final TextView userToRemove = (TextView)parentLayout.getChildAt(1);
        final String message;
        if(userToRemove.getText().toString().equals(getUser().getMail())) {
            message = getString(R.string.are_you_sure_leave);
        }else {
           message = getString(R.string.are_you_sure_kick);
        }
            new AlertDialog.Builder(this)
                    .setMessage(message)
                    .setNegativeButton(getString(R.string.no), null)
                    .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            HashMap<String, String> params = new HashMap<>();
                            params.put("email", userToRemove.getText().toString());
                            params.put("flatPIN", getUser().getFlatPin());
                            ServerRequest sr = new ServerRequest();
                            JSONObject jsonObject = sr.getJSON(HttpType.REMOVEUSER, getIpAddress() + ":8080/removeUser", params);
                            if (jsonObject != null) {
                                try {
                                    if (jsonObject.getBoolean("res")) {
                                        Toast.makeText(getApplicationContext(), jsonObject.getString("response"), Toast.LENGTH_SHORT);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            if(userToRemove.getText().toString().equals(getUser().getMail())){
                                SharedPreferences sharedPrefProf = getSharedPreferences(getString(R.string.profile_preferences), Context.MODE_PRIVATE);
                                SharedPreferences.Editor edit = sharedPrefProf.edit();
                                edit.putBoolean(getString(R.string.isInFlat), Boolean.FALSE);
                                edit.commit();
                                getUser().setFlatPin("");
                                getUser().removeAsAdmin();
                                getUser().resetScore();
                                goTo(FindFlatActivity.class);
                            }else{
                                flatMatesListAdapter.notifyDataSetChanged();
                                initFlatMatesView();
                            }
                        }
                    }).create().show();


    }

    /**
     * Upgrades a specific user to admin, but at the same time removes THIS user from being admin
     * @param view
     */
    public void promoteUser(View view){
        RelativeLayout parentLayout = (RelativeLayout)view.getParent();
        final TextView userToPromote = (TextView)parentLayout.getChildAt(1);
        final TextView usernameToPromote = (TextView)parentLayout.getChildAt(0);

        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.are_you_sure_promote1) + " " + usernameToPromote.getText().toString() + " " + getString(R.string.are_you_sure_promote2))
                        .setNegativeButton(getString(R.string.no), null)
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                                HashMap<String, String> params = new HashMap<>();
                                params.put("userToPromote", userToPromote.getText().toString());
                                params.put("userToDemote", getUser().getMail());

                                ServerRequest sr = new ServerRequest();
                                JSONObject jsonObject = sr.getJSON(HttpType.PROMOTEUSER, getIpAddress() + ":8080/promoteUser", params);
                                if (jsonObject != null) {
                                    try {
                                        if (jsonObject.getBoolean("res")) {
                                            Toast.makeText(getApplicationContext(), jsonObject.getString("response"), Toast.LENGTH_SHORT);
                                            getUser().removeAsAdmin();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                flatMatesListAdapter.notifyDataSetChanged();
                                initFlatMatesView();
                            }
                        }).create().show();

    }

    public boolean checkUserRights() {

        try {
            if (getUser().isAdmin()) {
                displayRights.setText("Admin-innsyn");
            }
            else {
                displayRights.setText("Bruker-innsyn");
            }
            return true;
        } catch(NullPointerException ne) {
            ne.printStackTrace();
            displayRights.setText("Ukjent brukerstatus");
        }

    return false;

    }




}
