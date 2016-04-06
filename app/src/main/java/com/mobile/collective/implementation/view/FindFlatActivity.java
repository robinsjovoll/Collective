package com.mobile.collective.implementation.view;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobile.collective.R;
import com.mobile.collective.framework.AppMenu;
import com.mobile.collective.implementation.controller.FlatAccessController;
import com.mobile.collective.client_server.HttpType;
import com.mobile.collective.client_server.ServerRequest;
import com.mobile.collective.implementation.controller.MainMenuController;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Ole on 30.03.2016.
 */
public class FindFlatActivity extends AppMenu {

    EditText flatPin_EditText;
    EditText flatName_EditText, flatPrize_EditText;
    Button find_button, newFlat_button;
    HashMap<String,String> params = new HashMap<>();

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
/*
        final Dialog register_flat = new Dialog(FindFlatActivity.this);
        register_flat.setTitle(getResources().getString(R.string.register_flat));
        register_flat.setContentView(R.layout.activity_settings);
        flatName_EditText = (EditText)findViewById(R.id.flatName);
        flatPrize_EditText = (EditText)findViewById(R.id.period_prize);

        String flatName = flatName_EditText.getText().toString();
        String flatPrize = flatPrize_EditText.getText().toString();
        String period = "TODO";
        String email = "TODO";

        params.put("flatName",flatName);
        params.put("period", period);
        params.put("prize",flatPrize);
        params.put("email", email);

        ServerRequest sr = new ServerRequest();
        JSONObject json = sr.getJSON(HttpType.ADDFLAT, getIpAddress() + ":8080/addFlat", params);
*/


    }
}
