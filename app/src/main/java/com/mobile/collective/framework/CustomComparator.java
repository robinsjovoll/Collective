package com.mobile.collective.framework;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Robin on 04.03.2016.
 */
public class CustomComparator implements Comparator<JSONObject> {
    @Override
    public int compare(JSONObject o1, JSONObject o2) {
        JSONArray jsonArray1 = null;
        ArrayList<String> taskHistory1 = new ArrayList<>();
        ArrayList<String> taskHistory2 = new ArrayList<>();
        try {
            jsonArray1 = (JSONArray)o1.get("taskHistory");
            taskHistory1 = new ArrayList<String>();
            for (int j=0; j<jsonArray1.length(); j++) {

                taskHistory1.add( jsonArray1.getString(j) );

            }
            JSONArray jsonArray2 = (JSONArray)o2.get("taskHistory");
            taskHistory2 = new ArrayList<String>();
            for (int j=0; j<jsonArray2.length(); j++) {
                taskHistory2.add( jsonArray2.getString(j) );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Integer taskHistory1Size = taskHistory1.size();
        Integer taskHistory2Size = taskHistory2.size();
        return taskHistory1Size.compareTo(taskHistory2Size);
    }
}