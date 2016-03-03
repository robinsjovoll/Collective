package com.mobile.collective.implementation.controller;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.mobile.collective.framework.CustomAcceptedListAdapter;
import com.mobile.collective.framework.CustomSuggestedListAdapter;
import com.mobile.collective.framework.MainViewPagerAdapter;
import com.mobile.collective.framework.SlidingTabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Robin on 22/02/2016.
 */
public class MainMenuController extends AppMenu {

    private boolean initTasks;
    private ListView suggestedTaskList;
    private ListView acceptedTaskList;
//    String selectedTaskName = null;
    String[] acceptedTaskNames;
    String[] suggestedTaskScores;
    String[] acceptedTaskScores;
    String[] suggestedTaskNames;
    Boolean[] approveDisapproveBtn;

    CustomSuggestedListAdapter customSuggestedListAdapter;
    CustomAcceptedListAdapter acceptedListAdapter;



    Toolbar toolbar;
    ViewPager pager;
    MainViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[];
    int numboftabs =4;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Titles= new CharSequence[]{
            getResources().getString(R.string.score_title),
                    getResources().getString(R.string.task_title),
                    getResources().getString(R.string.feed_title),
                    getResources().getString(R.string.setting_title)};

//        toolbar = (Toolbar) findViewById(R.id.tool_bar);
//        setSupportActionBar(toolbar);

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new MainViewPagerAdapter(getSupportFragmentManager(),Titles, numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setMainMenuController(this);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return ContextCompat.getColor(getApplicationContext(), R.color.tabsScrollColor);
            }
        });


        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);

        //Not functional ( view is not made yet)
        //((TextView) findViewById(R.id.start_tur)).setTypeface(Assets.getTypeface(this, Assets.baskerville_old_face_regular));



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Log.w("onOptionsItemSelected", "Start of method");
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Suggests a new task for the flat.
     * @param view
     */
    public void suggestNewTask(View view){
        JSONObject userinfo = getFileIO().readUserInformation();//TEMP
        String email = "null";
        try {//TEMP
            email = userinfo.getString("email");//TEMP
        } catch (JSONException e) {//TEMP
            e.printStackTrace();//TEMP
        }//TEMP
        final Dialog suggest_task = new Dialog(MainMenuController.this);
        suggest_task.setTitle("Suggest New Task");
        suggest_task.setContentView(R.layout.dialog_newtask);
        final EditText eTaskName = (EditText) suggest_task.findViewById(R.id.description_edittext);
        final EditText eTaskScore = (EditText) suggest_task.findViewById(R.id.points_edittext);
        Button submit = (Button) suggest_task.findViewById(R.id.submit_btn);
        Button cancel = (Button) suggest_task.findViewById(R.id.cancelBtn_newTask);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suggest_task.dismiss();
            }
        });
        final String finalEmail = email;
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String taskName = eTaskName.getText().toString();
                final String taskScore = eTaskScore.getText().toString();
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("taskName", taskName);
                params.put("taskScore", taskScore);
                params.put("email", finalEmail);
//                params.put("flatPIN","123");
                ServerRequest sr = new ServerRequest();
                JSONObject json = sr.getJSON(HttpType.CHANGEPASSWORD, getIpAddress() + ":8080/addTask", params);

                if (json != null) {
                    try {
                        Toast.makeText(getApplication(), json.getString("response"), Toast.LENGTH_LONG).show();
                        if(json.getBoolean("res")){
                            initTasksTab();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        suggest_task.show();
    }

    /**
     * Initializes the task tab.
     */
    public void initTasksTab(){
        initTasks = true;

        ServerRequest sr = new ServerRequest();
        HashMap<String,String> params = new HashMap<>();
        params.put("flatPIN", "123");
        JSONObject json = sr.getJSON(HttpType.GETTASKS,getIpAddress()+":8080/getTasks", params);
        try {
            if(json != null){
                if(json.getBoolean("res")) {
                    JSONArray tasks = json.getJSONArray("response");
                    ArrayList<String> tempAcceptedTaskNames = new ArrayList<>();
                    ArrayList<String> tempSuggestedTaskNames = new ArrayList<>();
                    ArrayList<String> tempAcceptedTaskScores = new ArrayList<>();
                    ArrayList<String> tempSuggestedTaskScores = new ArrayList<>();
                    approveDisapproveBtn = new Boolean[tasks.length()];
                    for (int i = 0; i < tasks.length(); i++) {
                        if(!tasks.getJSONObject(i).getBoolean("approved")){
                            tempSuggestedTaskNames.add(tasks.getJSONObject(i).getString("taskName"));
                            tempSuggestedTaskScores.add(tasks.getJSONObject(i).getString("taskScore"));
                            JSONArray jsonArray = (JSONArray)tasks.getJSONObject(i).get("approvedByUser");
                            ArrayList<String> approvedByUsers = new ArrayList<String>();
                            for (int j=0; j<jsonArray.length(); j++) {
                                approvedByUsers.add( jsonArray.getString(j) );
                            }
//                            Log.e("Main", approvedByUsers.toString());
                            if(!approvedByUsers.contains(getFileIO().readUserInformation().getString("email"))){
                                approveDisapproveBtn[i] = true;
                            }else {
                                approveDisapproveBtn[i] = false;
                            }
                        }else{
                            tempAcceptedTaskNames.add( tasks.getJSONObject(i).getString("taskName"));
                            tempAcceptedTaskScores.add(tasks.getJSONObject(i).getString("taskScore"));
                        }
                    }
                    acceptedTaskNames = tempAcceptedTaskNames.toArray(new String[0]);
                    acceptedTaskScores = tempAcceptedTaskScores.toArray(new String[0]);
                    suggestedTaskNames = tempSuggestedTaskNames.toArray(new String[0]);
                    suggestedTaskScores = tempSuggestedTaskScores.toArray(new String[0]);
                    customSuggestedListAdapter = new CustomSuggestedListAdapter(this, suggestedTaskNames, suggestedTaskScores, approveDisapproveBtn);
                    suggestedTaskList=(ListView)findViewById(R.id.suggested_task_list);
                    suggestedTaskList.setAdapter(customSuggestedListAdapter);

//                    suggestedTaskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view,
//                                                int position, long id) {
//                            selectedTaskName = suggestedTaskNames[+position];
//                            Log.e("MainMenuController", selectedTaskName + " is selected");
//                        }
//                    });

                    acceptedListAdapter = new CustomAcceptedListAdapter(this, acceptedTaskNames, acceptedTaskScores);
                    acceptedTaskList=(ListView)findViewById(R.id.accepted_task_list);
                    acceptedTaskList.setAdapter(acceptedListAdapter);

//                    acceptedTaskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view,
//                                                int position, long id) {
////                            String Slecteditem= itemname[+position];
////                            Toast.makeText(getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();
//
//                        }
//                    });
                }else{
                    Toast.makeText(getApplicationContext(), json.getString("response"), Toast.LENGTH_LONG);
                }
            }else {
                Log.e("MainMenuContorller", "Could not connect to server");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * Approves or disapproves a suggested task.
     * @param view
     */
    public void approveTask(View view) {
        JSONObject userinfo = getFileIO().readUserInformation();//TEMP
        String email = "null";
        try {//TEMP
            email = userinfo.getString("email");//TEMP
        } catch (JSONException e) {//TEMP
            e.printStackTrace();//TEMP
        }//TEMP
        LinearLayout buttonParentView = (LinearLayout) view.getParent();
        Button approveBtn = (Button) buttonParentView.getChildAt(0);
        Button disapproveBtn = (Button) buttonParentView.getChildAt(1);
        HashMap<String, String> params = new HashMap<>();
        LinearLayout parentView = (LinearLayout) buttonParentView.getParent();
        RelativeLayout taskNameAndScore = (RelativeLayout) parentView.getChildAt(1);
        TextView taskName = (TextView) taskNameAndScore.getChildAt(0);
        params.put("flatPIN", "123");
        params.put("taskName", taskName.getText().toString());
        params.put("email",email);
        ServerRequest sr = new ServerRequest();
        int index = Arrays.asList(suggestedTaskNames).indexOf(taskName.getText().toString());
        if (approveBtn.getVisibility() == View.VISIBLE) {
            JSONObject jsonObject = sr.getJSON(HttpType.APPROVETASK, getIpAddress() + ":8080/approveTask", params);
            if (jsonObject != null) {
                try {
                    Toast.makeText(getApplicationContext(), jsonObject.getString("response"), Toast.LENGTH_LONG).show();
                    if (jsonObject.getBoolean("res")) {
                        disapproveBtn.setVisibility(View.VISIBLE);
                        approveBtn.setVisibility(View.INVISIBLE);
                        buttonParentView.refreshDrawableState();
                        approveDisapproveBtn[index] = false;
//                        initTasksTab();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }else {
            JSONObject jsonObject = sr.getJSON(HttpType.DISAPPROVETASK, getIpAddress() + ":8080/disapproveTask", params);
            if (jsonObject != null) {
                try {
                    Toast.makeText(getApplicationContext(), jsonObject.getString("response"), Toast.LENGTH_LONG).show();
                    if (jsonObject.getBoolean("res")) {
                        disapproveBtn.setVisibility(View.INVISIBLE);
                        approveBtn.setVisibility(View.VISIBLE);
                        buttonParentView.refreshDrawableState();
                        approveDisapproveBtn[index] = true;
//                        initTasksTab();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        customSuggestedListAdapter.notifyDataSetChanged();
    }

}
