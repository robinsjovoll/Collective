package com.mobile.collective.client_server;

/**
 * Created by Robin on 12.02.2016.
 */
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class ServerRequest {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    public ServerRequest() {
    }

    public JSONObject getRequest(HttpType type, String urltxt, HashMap<String, String> params) {

        URL url = null;
        try {
            Log.e("ServerRequest", "URL: " + urltxt);
            url = new URL(urltxt);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            conn.setFixedLengthStreamingMode(
                    getQuery(params).getBytes().length);
            PrintWriter out = new PrintWriter(conn .getOutputStream());
            out.print(getQuery(params));
            out.close();
            System.out.println("Response Code: " + conn.getResponseCode());
            //TODO: MAKE THIS A GET METHOD.
            InputStream in = new BufferedInputStream(conn.getInputStream());
            json = org.apache.commons.io.IOUtils.toString(in, "UTF-8");
            Log.e("ServerRequest","Response: " + json);

//            OutputStream os = conn.getOutputStream();
//            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
//            writer.write(getQuery(params));
//            writer.flush();
//            writer.close();
//            os.close();

//            conn.connect();

//            System.out.println(json);
        } catch (IOException e) {
            e.printStackTrace();
        }



        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }


        return jObj;

    }

    public JSONObject postRequest(HttpType type, String urltxt, HashMap<String, String> params) {

        URL url = null;
        try {
            Log.e("ServerRequest", "URL: " + urltxt);
            url = new URL(urltxt);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            conn.setFixedLengthStreamingMode(
                    getQuery(params).getBytes().length);
            PrintWriter out = new PrintWriter(conn .getOutputStream());
            out.print(getQuery(params));
            out.close();
            System.out.println("Response Code: " + conn.getResponseCode());
            InputStream in = new BufferedInputStream(conn.getInputStream());
            json = org.apache.commons.io.IOUtils.toString(in, "UTF-8");
            Log.e("ServerRequest","Response: " + json);

//            OutputStream os = conn.getOutputStream();
//            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
//            writer.write(getQuery(params));
//            writer.flush();
//            writer.close();
//            os.close();

//            conn.connect();

//            System.out.println(json);
        } catch (IOException e) {
            e.printStackTrace();
        }



        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }


        return jObj;

    }
    JSONObject jobj;
    public JSONObject getJSON(HttpType type, String url, HashMap<String,String> params) {

        Params param = new Params(url,params);
        Request myTask = new Request(type);
        try{
            jobj= myTask.execute(param).get();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }catch (ExecutionException e){
            e.printStackTrace();
        }
        return jobj;
    }


    private static class Params {
        String url;
        HashMap<String,String> params;


        Params(String url, HashMap<String,String> params) {
            this.url = url;
            this.params = params;

        }
    }

    private class Request extends AsyncTask<Params, String, JSONObject> {
        HttpType type;

        public Request(HttpType type){
            this.type = type;
        }
        @Override
        protected JSONObject doInBackground(Params... args) {

            ServerRequest request = new ServerRequest();
            JSONObject json = null;
            if(type == HttpType.LOGIN || type == HttpType.REGISTER || type == HttpType.CHANGEPASSWORD){
                json = request.postRequest(type, args[0].url, args[0].params);
            }if(type == HttpType.GETTASKS){
                json = request.getRequest(type, args[0].url, args[0].params);
            }

            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {

            super.onPostExecute(json);

        }

    }

    /**
     * Computes the query based on params.
     * @param params
     * @return
     * @throws UnsupportedEncodingException
     */
    private String getQuery(HashMap<String,String> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (String key : params.keySet())
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(params.get(key), "UTF-8"));
        }

        return result.toString();
    }
}
