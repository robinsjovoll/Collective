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


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Ole on 30.03.2016.
 */
public class FindFlatActivity extends AppMenu {

    private EditText flatPin_EditText;
    private Button find_button, newFlat_button;
    HashMap<String,String> params = new HashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findflat);

        flatPin_EditText = (EditText)findViewById(R.id.flatPin_edittext);
        find_button = (Button)findViewById(R.id.find_button);
        newFlat_button = (Button)findViewById(R.id.createNew_button);

    }

    public void joinFlatByPin(View view){
        String pin = flatPin_EditText.getText().toString();

        params.put("flatPIN", pin);
        ServerRequest sr = new ServerRequest();
        JSONObject json = sr.getJSON(HttpType.LOGIN, getIpAddress() + ":8080/login", params);

        try {
            if(json != null && json.getBoolean("res")){

            }
            else{
                Toast.makeText(getApplication(), json.getString("response"), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void registerNewFlat(){
        final Dialog register_flat = new Dialog(FindFlatActivity.this);
        register_flat.setTitle(getResources().getString(R.string.register_flat));
        register_flat.setContentView(R.layout.activity_settings);
    }
}
