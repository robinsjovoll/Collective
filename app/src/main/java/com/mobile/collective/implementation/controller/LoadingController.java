package com.mobile.collective.implementation.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.mobile.collective.R;
import com.mobile.collective.client_server.HttpType;
import com.mobile.collective.client_server.ServerRequest;
import com.mobile.collective.framework.AppMenu;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.google.android.gms.internal.zzhu.runOnUiThread;

/**
 * Created by Robin on 24.02.2016.
 */
public class LoadingController {
    private AppMenu context;
    private boolean clicked;

    public LoadingController(AppMenu context){
        this.context = context;
    }

    public boolean checkNetwork() {
        while (!isNetworkOnline()){
            createDialog();
        }
        return true;


    }

    public void createDialog(){
        clicked = false;
        final String networkOn = "networkOn";
        new Thread(){
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage(context.getString(R.string.require_network_on_startup))
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        synchronized (networkOn) {
                                            clicked = true;
                                            networkOn.notify();
                                        }
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                });
            }
        }.start();


        synchronized (networkOn) {
            try {
                if (!clicked) {
                    Log.d("Wait for click", "no connection, user has not clicked button, waiting");
                    networkOn.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Check if phone is connected to any network.
     * @return
     */
    public boolean isNetworkOnline() {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }
}
