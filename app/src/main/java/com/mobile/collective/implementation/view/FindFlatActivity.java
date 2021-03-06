package com.mobile.collective.implementation.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mobile.collective.R;
import com.mobile.collective.framework.AppMenu;
import com.mobile.collective.client_server.HttpType;
import com.mobile.collective.client_server.ServerRequest;
import com.mobile.collective.implementation.controller.MainMenuController;
import com.mobile.collective.implementation.model.Period;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Ole on 30.03.2016.
 */
public class FindFlatActivity extends AppMenu {

    private EditText flatPin_EditText;
    private  EditText flatName_EditText, flatPrize_EditText;
    private Button find_button, newFlat_button, save_flatSettings, logoutBtn;
    private Spinner periodSpinner;
    private HashMap<String,String> params = new HashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findflat);

        flatPin_EditText = (EditText)findViewById(R.id.flatPin_edittext);
        find_button = (Button)findViewById(R.id.find_button);
        newFlat_button = (Button)findViewById(R.id.createNew_button);


       find_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinFlatByPin();
            }
        });
       newFlat_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewFlat();
            }
        });

    }

    public void joinFlatByPin(){
        String pin = flatPin_EditText.getText().toString();
        String email = "";
        try{
            email = getFileIO().readUserInformation().getString("email");
        } catch (JSONException e){
            e.printStackTrace();
        }
        params.put("flatPIN", pin);
        params.put("email", email);
        ServerRequest sr = new ServerRequest();
        JSONObject json = sr.getJSON(HttpType.ADDUSER, getIpAddress() + ":8080/addUserToFlat", params);
        try {
            if(json != null && json.getBoolean("res")){
                Toast.makeText(getApplication(), json.getString("response"), Toast.LENGTH_SHORT).show();
                SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.profile_preferences), Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putBoolean(getString(R.string.isInFlat), Boolean.TRUE);
                edit.putString(getString(R.string.flatPIN), pin);
                edit.commit();
                if(json.has("flatName")){
                    getUser().setFlatName(json.getString("flatName"));
                }
                if(json.has("prize")){
                    getUser().setFlatPrize(json.getString("prize"));
                }
                if(json.has("thisPeriod")) {
                    getUser().setThisPeriod(Integer.parseInt(json.getString("thisPeriod")));
                }
                if(json.has("lastPeriod")){
                    if(!json.getString("lastPeriod").equals("0")){

                        getUser().setLastPeriod(Integer.parseInt(json.getString("lastPeriod")));
                    }

                }
                getUser().setFlatPin(pin);
                goTo(MainMenuController.class);
            }
            else if (json != null){
                Toast.makeText(getApplication(), json.getString("response"), Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getApplication(), "Error: No response", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
    }
    public void registerNewFlat(){
        final Dialog register_flat = new Dialog(FindFlatActivity.this);
        register_flat.setTitle(getResources().getString(R.string.register_flat));
        register_flat.setContentView(R.layout.dialog_settings);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(register_flat.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        flatName_EditText = (EditText)register_flat.findViewById(R.id.flatName);
        flatPrize_EditText = (EditText)register_flat.findViewById(R.id.period_prize);
        periodSpinner = (Spinner)register_flat.findViewById(R.id.periodSpinner);
        periodSpinner.setAdapter(new ArrayAdapter<Period>(this, R.layout.spinner_item, Period.values()));
        periodSpinner.setSelection(Period.valueOf(7).ordinal());
        logoutBtn = (Button)register_flat.findViewById(R.id.logoutBtn);
        logoutBtn.setVisibility(View.INVISIBLE);
        save_flatSettings = (Button)register_flat.findViewById(R.id.save_button);
        save_flatSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flatName_EditText.getText().toString().isEmpty() || flatPrize_EditText.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.all_fields_filled), Toast.LENGTH_SHORT).show();
                } else {
                    final Period flatPeriod = Period.valueOf((((Spinner) register_flat.findViewById(R.id.periodSpinner)).getSelectedItem().toString()));
                    String flatName = flatName_EditText.getText().toString();
                    String flatPrize = flatPrize_EditText.getText().toString();
                    String period = Integer.toString(flatPeriod.getDuration());
                    String email = getUser().getMail();


                    params.put("flatName", flatName);
                    params.put("period", period);
                    params.put("prize", flatPrize);
                    params.put("email", email);
                    ServerRequest sr = new ServerRequest();
                    JSONObject json = sr.getJSON(HttpType.ADDFLAT, getIpAddress() + ":8080/addFlat", params);
                    if (json != null) {
//                    register_flat.dismiss();
//                    try {
//                        Toast.makeText(getApplicationContext(), json.getString("response"), Toast.LENGTH_SHORT);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                        try {
                            getUser().setFlatPin(json.getString("flatPIN"));
                            getUser().makeAdmin();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        getUser().setFlatName(flatName);
                        getUser().setFlatPrize(flatPrize);
                        getUser().setThisPeriod(Integer.parseInt(period));
                        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.profile_preferences), Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = sharedPreferences.edit();
                        edit.putBoolean(getString(R.string.isInFlat), Boolean.TRUE);
                        edit.commit();

                        goTo(MainMenuController.class);
                    }
                }
            }
        });
        register_flat.show();
        register_flat.getWindow().setAttributes(lp);

    }

    /**
     * Logs the user out
     * @param view
     */
    public void logoutFromFindFlat(View view){
        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.are_you_sure_logout))
                .setNegativeButton(getString(R.string.no),null)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        getUser().resetUser();
                        SharedPreferences sharedPrefProf = getSharedPreferences(getString(R.string.profile_preferences), Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = sharedPrefProf.edit();
                        edit.putBoolean(getString(R.string.isLoggedInn), Boolean.FALSE);
                        edit.commit();
                        goTo(LoginActivity.class);

                    }
                }).create().show();
    }
}
