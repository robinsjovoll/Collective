package com.mobile.collective.implementation.controller;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.mobile.collective.R;
import com.mobile.collective.client_server.HttpType;
import com.mobile.collective.client_server.ServerRequest;
import com.mobile.collective.framework.AppMenu;
import com.mobile.collective.framework.CustomAcceptedListAdapter;
import com.mobile.collective.framework.CustomComparator;
import com.mobile.collective.framework.CustomHistoryListAdapter;
import com.mobile.collective.framework.CustomSuggestedListAdapter;
import com.mobile.collective.framework.CustomTaskHistoryListAdapter;
import com.mobile.collective.framework.CustomScoreListAdapter;
import com.mobile.collective.framework.MainViewPagerAdapter;
import com.mobile.collective.framework.SlidingTabLayout;
import com.mobile.collective.implementation.view.FlatmatesView;
import com.mobile.collective.implementation.model.Period;
import com.mobile.collective.implementation.view.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;


/**
 * Created by Robin on 22/02/2016.
 */
public class MainMenuController extends AppMenu implements Serializable {

    /**
     * ListView in the taskTab variables.
     */
    private boolean isTaskTabInitialized;
    private ListView suggestedTaskList;
    private ListView acceptedTaskList;
    private String[] acceptedTaskNames;
    private String[] suggestedTaskScores;
    private String[] acceptedTaskScores;
    private String[] suggestedTaskNames;
    private String[] suggestedByArray;
    private Boolean[] approveDisapproveBtn;
    private CustomSuggestedListAdapter customSuggestedListAdapter;
    private CustomAcceptedListAdapter acceptedListAdapter;

    /**
     * ListView in scoreTab variables
     */
    private ListView scoreList;
    private boolean isScoreTabInit;
    private ArrayList<String> arrayListUsers;
    private ArrayList<String> arrayListScores;
    private String[] scoreTabUsers, scoreTabScores;
    private CustomScoreListAdapter customScoreListAdapter;
    private final String BLUE = "#354579", LIGHTBLUE = "#7ACEF3", ORANGE ="#FF8A00", DARKORANGE = "#EC4912", DARKERORANGE = "#C02217";
    private final String[] colorArray = new String[]{BLUE, LIGHTBLUE, ORANGE, DARKORANGE, DARKERORANGE};
    private int colorInt;

    /**
     * ListView in taskHistory display variables.
     */
    private ListView taskHistoryList;
    private String[] taskHistoryUsernames;
    private String[] taskHistoryDates;
    private CustomTaskHistoryListAdapter customTaskHistoryListAdapter;

    /**
     * ListView in historyTab variables
     */
    private String chosenPeriod = "null";
    private boolean isHistoryTabInit;
    private boolean thisPeriodBtnClicked, lastPeriodBtnClicked, earlierBtnClicked;
    private Calendar calendar;
    private ListView historyTabList;
    private String[] historyTabUsernames, historyTabDates, historyTabTaskNames, historyTabTaskScores;
    ArrayList<String> arrayListUsernames, arrayListDates, arrayListTaskNames, arrayListTaskScores;
    private CustomHistoryListAdapter customHistoryListAdapter;
    private String selectedUsername = "Alle";
    private boolean isSelectedUsername = true;
    private String selectedTaskName = "Alle";
    private boolean isSelectedTaskName = true;
    private Spinner taskSpinner, personSpinner;

    /**
     *  FlatmatesView variables
     */
    private Button showFlatMatesView;

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

        suggestedTaskList=(ListView)findViewById(R.id.suggested_task_list);
        acceptedTaskList=(ListView)findViewById(R.id.accepted_task_list);
        historyTabList = (ListView)findViewById(R.id.historyList);

        Titles= new CharSequence[]{
            getResources().getString(R.string.task_title),
                    getResources().getString(R.string.score_title),
                    getResources().getString(R.string.history_title),
                    getResources().getString(R.string.setting_title)};

//        toolbar = (Toolbar) findViewById(R.id.tool_bar);
//        setSupportActionBar(toolbar);

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new MainViewPagerAdapter(getSupportFragmentManager(),Titles, numboftabs,this);

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

    }
    public void goToFlatmatesView(View v)
    {
        goTo(FlatmatesView.class);
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
        String email = getUser().getMail();
        final Dialog suggest_task = new Dialog(MainMenuController.this);
        suggest_task.setTitle(getResources().getString(R.string.suggest_new_task));
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
                if (taskName.isEmpty() || taskScore.isEmpty()) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.all_fields_filled), Toast.LENGTH_SHORT).show();
                } else {
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("taskName", taskName);
                    params.put("taskScore", taskScore);
                    params.put("email", finalEmail);
                    params.put("flatPIN", "" + getUser().getFlatPin());
                    ServerRequest sr = new ServerRequest();
                    JSONObject json = sr.getJSON(HttpType.ADDTASK, getIpAddress() + ":8080/addTask", params);

                    if (json != null) {
                        try {
                            Toast.makeText(getApplication(), json.getString("response"), Toast.LENGTH_LONG).show();
                            if (json.getBoolean("res")) {
                                initTasksTab();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
        if(getUser().isPeriodOver()){
            new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.push_text))
                    .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            getUser().setPeriodOver(false);
                        }
                    }).create().show();
        }
        ServerRequest sr = new ServerRequest();
        HashMap<String,String> params = new HashMap<>();
        params.put("flatPIN", "" + getUser().getFlatPin());
        JSONObject json = sr.getJSON(HttpType.GETTASKS,getIpAddress()+":8080/getTasks", params);
        try {
            if(json != null){
                if(json.getBoolean("res")) {
                    JSONArray tasks = json.getJSONArray("response");
                    ArrayList<String> suggestedBy = new ArrayList<>();
                    ArrayList<JSONObject> approvedTasks = new ArrayList<>();
                    ArrayList<String> tempAcceptedTaskNames = new ArrayList<>();
                    ArrayList<String> tempSuggestedTaskNames = new ArrayList<>();
                    ArrayList<String> tempAcceptedTaskScores = new ArrayList<>();
                    ArrayList<String> tempSuggestedTaskScores = new ArrayList<>();
                    ArrayList<Boolean> approvedDissaprovedTemp = new ArrayList<>();
                    for (int i = 0; i < tasks.length(); i++) {
                        if(!tasks.getJSONObject(i).getBoolean("approved")){
                            tempSuggestedTaskNames.add(tasks.getJSONObject(i).getString("taskName"));
                            tempSuggestedTaskScores.add(tasks.getJSONObject(i).getString("taskScore"));
                            suggestedBy.add(tasks.getJSONObject(i).getString("suggestedBy"));
                            JSONArray jsonArray = (JSONArray)tasks.getJSONObject(i).get("approvedByUser");
                            ArrayList<String> approvedByUsers = new ArrayList<String>();
                            for (int j=0; j<jsonArray.length(); j++) {
                                approvedByUsers.add( jsonArray.getString(j) );
                            }
                            if(!approvedByUsers.contains(getFileIO().readUserInformation().getString("email"))){
                                approvedDissaprovedTemp.add(true);
                            }else {
                                approvedDissaprovedTemp.add(false);
                            }
                        }else{
                            approvedTasks.add(tasks.getJSONObject(i));
                        }
                    }
                    Collections.sort(approvedTasks, new CustomComparator());
                    Collections.reverse(approvedTasks);
                    for(JSONObject acceptedTask : approvedTasks){
                        tempAcceptedTaskNames.add(acceptedTask.getString("taskName"));
                        tempAcceptedTaskScores.add(acceptedTask.getString("taskScore"));
                    }
                    acceptedTaskNames = tempAcceptedTaskNames.toArray(new String[0]);
                    acceptedTaskScores = tempAcceptedTaskScores.toArray(new String[0]);
                    suggestedTaskNames = tempSuggestedTaskNames.toArray(new String[0]);
                    suggestedTaskScores = tempSuggestedTaskScores.toArray(new String[0]);
                    approveDisapproveBtn = approvedDissaprovedTemp.toArray(new Boolean[0]);
                    suggestedByArray = suggestedBy.toArray(new String[0]);
//                    Log.e("MainMenu", "taskNames: " + acceptedTaskNames);
                    customSuggestedListAdapter = new CustomSuggestedListAdapter(this, suggestedTaskNames, suggestedTaskScores, approveDisapproveBtn, suggestedByArray);
//                    suggestedTaskList=(ListView)findViewById(R.id.suggested_task_list);
                    suggestedTaskList.setAdapter(customSuggestedListAdapter);


                    acceptedListAdapter = new CustomAcceptedListAdapter(this, acceptedTaskNames, acceptedTaskScores, getUser().isAdmin());
//                    acceptedTaskList=(ListView)findViewById(R.id.accepted_task_list);
                    acceptedTaskList.setAdapter(acceptedListAdapter);

                }else{
                    acceptedTaskNames = new String[0];
                    acceptedTaskScores = new String[0];

                    Toast.makeText(getApplicationContext(), json.getString("response"), Toast.LENGTH_LONG).show();
                    acceptedListAdapter = new CustomAcceptedListAdapter(this, acceptedTaskNames, acceptedTaskScores, true);
                    acceptedTaskList.setAdapter(acceptedListAdapter);
                }
            }else {
                Log.e("MainMenuContorller", "Could not connect to server");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

//        HashMap<String, String> params1 = new HashMap<>();
//        params1.put("flatName", "Coolest flat ever");
//        params1.put("period","7");
//        params1.put("prize","Cake");
//        params1.put("email", "robinsjovoll@hotmail.com");
//        sr.getJSON(HttpType.LOGIN, getIpAddress() + ":8080/addFlat", params1);

    }

    /**
     * Approves or disapproves a suggested task.
     * @param view
     */
    public void approveTask(View view) {
        String email = getUser().getMail();
        LinearLayout buttonParentView = (LinearLayout) view.getParent();
        Button approveBtn = (Button) buttonParentView.getChildAt(0);
        Button disapproveBtn = (Button) buttonParentView.getChildAt(1);
        HashMap<String, String> params = new HashMap<>();
        LinearLayout parentView = (LinearLayout) buttonParentView.getParent();
        RelativeLayout taskNameAndScore = (RelativeLayout) parentView.getChildAt(1);
        TextView taskName = (TextView) taskNameAndScore.getChildAt(0);
        params.put("flatPIN", ""+getUser().getFlatPin());
        params.put("taskName", taskName.getText().toString());
        params.put("email",email);
        ServerRequest sr = new ServerRequest();
        int index = Arrays.asList(suggestedTaskNames).indexOf(taskName.getText().toString());
        if (approveBtn.getVisibility() == View.VISIBLE) {
            JSONObject jsonObject = sr.getJSON(HttpType.APPROVETASK, getIpAddress() + ":8080/approveTask", params);
            if (jsonObject != null) {
                try {
                    Toast.makeText(getApplicationContext(), jsonObject.getString("response"), Toast.LENGTH_SHORT).show();
                    if (jsonObject.getBoolean("res")) {
                        disapproveBtn.setVisibility(View.VISIBLE);
                        approveBtn.setVisibility(View.INVISIBLE);
                        buttonParentView.refreshDrawableState();
                        approveDisapproveBtn[index] = false;
                        if(jsonObject.getString("response").equals("Approved")){
                            initTasksTab();
                        }
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

    /**
     * Does the task.
     * @param view
     */
    public void taskDone(View view){
        String email = getUser().getMail();
        RelativeLayout parent = (RelativeLayout) view.getParent();
        LinearLayout parentView = (LinearLayout) parent.getParent();
        RelativeLayout relativeLayout = (RelativeLayout) parentView.getChildAt(0);
        TextView taskName = (TextView) relativeLayout.getChildAt(0);
        final String taskNameString = taskName.getText().toString();
        final String finalEmail = email;
        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.are_you_sure) + " " + taskNameString)
                .setNegativeButton(R.string.cancel_reset, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton(R.string.approve, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        //Add description and/or take picture.
                        HashMap<String, String> params = new HashMap<String, String>();
                        params.put("taskName", taskNameString);
                        params.put("email", finalEmail);
                        params.put("flatPIN", ""+getUser().getFlatPin());
                        ServerRequest sr = new ServerRequest();
                        JSONObject jsonObject = sr.getJSON(HttpType.DOTASK, getIpAddress() + ":8080/doTask", params);
                        if (jsonObject != null) {
                            try {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("response"), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }).create().show();
    }

    /**
     * Display the history of the specific task
     * @param view
     */
    public void showTaskHistory(View view){
        HashMap<String,String> params = new HashMap<>();
        RelativeLayout parentView = (RelativeLayout)view.getParent();
        TextView taskName = (TextView) parentView.getChildAt(0);

        params.put("flatPIN",""+getUser().getFlatPin());
        params.put("taskName", taskName.getText().toString());

        ServerRequest sr = new ServerRequest();
        JSONObject jsonObject = sr.getJSON(HttpType.TASKHISTORY, getIpAddress() + ":8080/getTaskHistory", params);

        if(jsonObject != null){
            try {
                if(jsonObject.getBoolean("res")){
                    JSONArray taskHistoryArray = jsonObject.getJSONArray("response");
                    taskHistoryUsernames = new String[taskHistoryArray.length()];
                    taskHistoryDates = new String[taskHistoryArray.length()];
                    for (int j=0; j<taskHistoryArray.length(); j++) {
                        taskHistoryUsernames[j] = taskHistoryArray.getJSONObject(j).getString("username");
                        taskHistoryDates[j] = taskHistoryArray.getJSONObject(j).getString("date");
                    }
                    final Dialog task_history = new Dialog(MainMenuController.this);
                    task_history.setTitle(getResources().getString(R.string.history_dialog_title));
                    task_history.setContentView(R.layout.dialog_task_history);
                    customTaskHistoryListAdapter = new CustomTaskHistoryListAdapter(this, taskHistoryUsernames, taskHistoryDates);
                    taskHistoryList=(ListView)task_history.findViewById(R.id.task_history_list);
                    taskHistoryList.setAdapter(customTaskHistoryListAdapter);
                    Button back = (Button) task_history.findViewById(R.id.backBtn);

                    back.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            task_history.dismiss();
                        }
                    });
                    task_history.show();
                }else {
                    Toast.makeText(getApplicationContext(), jsonObject.getString("response"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Delete a specific task if user is admin
     * @param view
     */
    public void deleteTask(View view){
        RelativeLayout parent = (RelativeLayout) view.getParent();
        LinearLayout parentView = (LinearLayout) parent.getParent();
        RelativeLayout relativeLayout = (RelativeLayout) parentView.getChildAt(0);
        TextView taskName = (TextView) relativeLayout.getChildAt(0);
        final String taskNameString = taskName.getText().toString();
        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.are_you_sure_delete) + " " + taskNameString)
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        //Add description and/or take picture.
                        HashMap<String, String> params = new HashMap<String, String>();
                        params.put("taskName", taskNameString);
                        params.put("flatPIN", ""+getUser().getFlatPin());
                        ServerRequest sr = new ServerRequest();
                        JSONObject jsonObject = sr.getJSON(HttpType.DELETETASK, getIpAddress() + ":8080/deleteTask", params);
                        if (jsonObject != null) {
                            try {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("response"), Toast.LENGTH_SHORT).show();
                                initTasksTab();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).create().show();
    }

    /**
     * Edit a specific task if user is admin
     * @param view
     */
    public void editTask(View view){
        RelativeLayout buttonParent = (RelativeLayout)view.getParent();
        LinearLayout viewParent = (LinearLayout) buttonParent.getParent();
        RelativeLayout nameParent = (RelativeLayout)viewParent.getChildAt(0);
        final TextView taskName = (TextView) nameParent.getChildAt(0);
        TextView taskScore = (TextView) viewParent.getChildAt(1);
        final Dialog editTask = new Dialog(MainMenuController.this);
        editTask.setTitle(getResources().getString(R.string.edit_task_title));
        editTask.setContentView(R.layout.dialog_newtask);
        final EditText eTaskName = (EditText) editTask.findViewById(R.id.description_edittext);
        final EditText eTaskScore = (EditText) editTask.findViewById(R.id.points_edittext);
        eTaskName.setText(taskName.getText());
        eTaskScore.setText(taskScore.getText().toString().substring(7));
        Button submit = (Button) editTask.findViewById(R.id.submit_btn);
        Button cancel = (Button) editTask.findViewById(R.id.cancelBtn_newTask);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTask.dismiss();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String oldTaskName = taskName.getText().toString();
                final String newTaskName = eTaskName.getText().toString();
                final String taskScore = eTaskScore.getText().toString();
                if (newTaskName.isEmpty() || taskScore.isEmpty()) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.all_fields_filled), Toast.LENGTH_SHORT).show();
                }else{
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("oldTaskName", oldTaskName);
                    params.put("newTaskName", newTaskName);
                    params.put("taskScore", taskScore);
                    params.put("flatPIN",""+getUser().getFlatPin());
                    ServerRequest sr = new ServerRequest();
                    JSONObject json = sr.getJSON(HttpType.EDITTASK, getIpAddress() + ":8080/editTask", params);

                    if (json != null) {
                        try {
                            Toast.makeText(getApplication(), json.getString("response"), Toast.LENGTH_LONG).show();
                            if (json.getBoolean("res")) {
                                initTasksTab();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        editTask.show();
    }

    public boolean isTaskTabInitialized() {
        return isTaskTabInitialized;
    }

    public void setIsTaskTabInitialized(boolean isTaskTabInitialized) {
        this.isTaskTabInitialized = isTaskTabInitialized;
    }

    public void setSuggestedTaskList(ListView suggestedTaskList) {
        this.suggestedTaskList = suggestedTaskList;
    }

    public void setHistoryTabList(ListView historyTabList) {
        this.historyTabList = historyTabList;
    }

    public void setScoreList(ListView scoreList) {
        this.scoreList = scoreList;
    }

    public void setAcceptedTaskList(ListView acceptedTaskList) {
        this.acceptedTaskList = acceptedTaskList;
    }

    /**
     * Initializes the scoreTab.
     */
    public void initScoreTab() {
        final ServerRequest sr = new ServerRequest();
        HashMap<String, String> params = new HashMap<>();
        params.put("flatPIN", ""+getUser().getFlatPin());
        final JSONObject json = sr.getJSON(HttpType.GETSCORES, getIpAddress() + ":8080/getScores", params);
        if (json != null) {
            try {
                if (json.getBoolean("res")) {
                    JSONArray response = json.getJSONArray("response");
                    arrayListUsers = new ArrayList<>();
                    arrayListScores = new ArrayList<>();

                    // Clearing array to avoid duplicates
                    arrayListUsers.clear();
                    arrayListScores.clear();
                    scoreTabUsers = new String[0];
                    scoreTabScores = new String[0];

                    for (int i = 0; i < response.length(); i++) {
                        arrayListUsers.add(response.getJSONObject(i).getString("username"));
                        arrayListScores.add(response.getJSONObject(i).getString("score"));
                        scoreTabUsers = arrayListUsers.toArray(new String[0]);
                        scoreTabScores = arrayListScores.toArray(new String[0]);
                    }

                    customScoreListAdapter = new CustomScoreListAdapter(this, scoreTabUsers,scoreTabScores, colorArray);
//                    scoreList = (ListView) findViewById(R.id.listView_scores);
                    scoreList.setDivider(null);
                    scoreList.setAdapter(customScoreListAdapter);

                    params = new HashMap<>();
                    params.put("flatPIN", ""+getUser().getFlatPin());
                    JSONObject jsonObject = sr.getJSON(HttpType.GETLASTPERIODWINNER, getIpAddress() + ":8080/getLastPeriodWinner", params);
                    if(jsonObject != null){
                        if(jsonObject.getBoolean("res")) {
                            JSONObject periodWinner = jsonObject.getJSONObject("response");
                            String date = periodWinner.getString("date").substring(8, 10) + "-" + periodWinner.getString("date").substring(5, 7) + "-" + periodWinner.getString("date").substring(0, 4);
                            String lastWinnerString = date + " - " + periodWinner.getString("username");

                            TextView lastWinner = (TextView) findViewById(R.id.last_winner_person);
                            lastWinner.setText(lastWinnerString);
                        }else {
                            Toast.makeText(getApplicationContext(), json.getString("response"), Toast.LENGTH_SHORT).show();
                        }
                    }


                }else {
                    Toast.makeText(getApplicationContext(), json.getString("response"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        isScoreTabInit = true;
    }

    /**
     * Initializes the historyTab.
     */
    public void initHistoryTab(){
        final ServerRequest sr = new ServerRequest();
            HashMap<String, String> params = new HashMap<>();
        params.put("flatPIN", ""+getUser().getFlatPin());
        params.put("numberOfHistories", "10"); //TEMP
        final JSONObject json = sr.getJSON(HttpType.GETFEEDHISTORY, getIpAddress() + ":8080/getFeedHistory", params);
        if (json != null) {
            try {
                if (json.getBoolean("res")) {


                    JSONArray response = json.getJSONArray("response");

                    arrayListUsernames = new ArrayList<>();
                    arrayListDates = new ArrayList<>();
                    arrayListTaskNames = new ArrayList<>();
                    arrayListTaskScores = new ArrayList<>();

                    for (int i = 0; i < response.length(); i++) {
                        arrayListUsernames.add(response.getJSONObject(i).getString("username"));
                        arrayListDates.add(response.getJSONObject(i).getString("date"));
                        arrayListTaskNames.add(response.getJSONObject(i).getString("taskName"));
                        arrayListTaskScores.add(response.getJSONObject(i).getString("taskScore"));
                    }

                    historyTabUsernames = arrayListUsernames.toArray(new String[0]);
                    historyTabDates = arrayListDates.toArray(new String[0]);
                    historyTabTaskNames = arrayListTaskNames.toArray(new String[0]);
                    historyTabTaskScores = arrayListTaskScores.toArray(new String[0]);

                    customHistoryListAdapter = new CustomHistoryListAdapter(this, historyTabUsernames, historyTabDates, historyTabTaskNames, historyTabTaskScores);
//                    historyTabList = (ListView) findViewById(R.id.historyList);
                    historyTabList.setAdapter(customHistoryListAdapter);

                    String[] tempTaskNames = new String[historyTabTaskNames.length + 1];
                    tempTaskNames[0] = "Alle";
                    for (int i = 0; i < historyTabTaskNames.length; i++) {
                        tempTaskNames[i + 1] = historyTabTaskNames[i];
                    }

                    Set<String> tempTaskSet = new LinkedHashSet<>(Arrays.asList(tempTaskNames));
                    String[] taskNames = tempTaskSet.toArray(new String[0]);

                    ArrayAdapter<String> taskAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, taskNames);
//                        Log.e("MainMenu", tempTaskSet + " tasknames");
                    taskSpinner.setAdapter(taskAdapter);

                    taskSpinner.setSelection(taskAdapter.getPosition(selectedTaskName));
                    taskSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                                Log.v("item", (String) parent.getItemAtPosition(position));
                                selectedTaskName = (String) parent.getItemAtPosition(position);
                                checkFilter(sr);

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                    String[] tempUserNames = new String[historyTabUsernames.length + 1];
                    tempUserNames[0] = "Alle";
                    for (int i = 0; i < historyTabUsernames.length; i++) {
                        tempUserNames[i + 1] = historyTabUsernames[i];
                    }

                    Set<String> tempUsernameSet = new LinkedHashSet<>(Arrays.asList(tempUserNames));
                    String[] userNames = tempUsernameSet.toArray(new String[0]);

                    ArrayAdapter<String> personAdapter = new ArrayAdapter<String>(this,
                                R.layout.spinner_item, userNames);
                    personSpinner.setAdapter(personAdapter);


                    personSpinner.setSelection(personAdapter.getPosition(selectedUsername));
                    personSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view,
                                                       int position, long id) {
//                                Log.v("item", (String) parent.getItemAtPosition(position));
                                selectedUsername = (String) parent.getItemAtPosition(position);
                                checkFilter(sr);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                // TODO Auto-generated method stub
                            }
                        });

                    if(isHistoryTabInit) {
                        checkFilter(sr);
                    }
                } else {
                    historyTabUsernames = new String[0];
                    historyTabDates = new String[0];
                    historyTabTaskNames = new String[0];
                    historyTabTaskScores = new String[0];
                    customHistoryListAdapter = new CustomHistoryListAdapter(this, historyTabUsernames, historyTabDates, historyTabTaskNames, historyTabTaskScores);
//                    historyTabList = (ListView) findViewById(R.id.historyList);
                    historyTabList.setAdapter(customHistoryListAdapter);
                    Toast.makeText(getApplicationContext(), json.getString("response"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Button button_this = (Button)findViewById(R.id.this_periodBtn);
        Button button_last = (Button)findViewById(R.id.last_periodBtn);
        Button button_earlier = (Button)findViewById(R.id.earlierBtn);

        if(chosenPeriod.equals("thisPeriod")){
            button_this.setBackgroundResource(R.drawable.gradient_btn_selected);
        }else if(chosenPeriod.equals("lastPeriod")){
            button_last.setBackgroundResource(R.drawable.gradient_btn_selected);
        }else if(chosenPeriod.equals("earlier")){
            button_earlier.setBackgroundResource(R.drawable.gradient_btn_selected);
        }

        isHistoryTabInit = true;

    }

    /**
     * Intitializes the SettingsTab.
     */
    public void initSettingsTab() {
        final ServerRequest sr = new ServerRequest();
        HashMap<String,String> params = new HashMap<>();
        params.put("flatPIN", getUser().getFlatPin());
        JSONObject json = sr.getJSON(HttpType.GETFLATSETTINGS,getIpAddress()+":8080/getFlatSettings", params);
        try {
            if(json != null){
                if(json.getBoolean("res")) {
                    JSONArray flatSettings = json.getJSONArray("response");

                    String flatName = flatSettings.get(0).toString();
                    Period flatPeriod = Period.valueOf(Integer.parseInt(flatSettings.get(1).toString()));
                    String flatPrize = flatSettings.get(2).toString();

                    /*Set text to current flat name.*/
                    ((EditText) findViewById(R.id.flatName)).setText(flatName, TextView.BufferType.EDITABLE);

                    /*Populate Spinner with the enum values.*/
                    Spinner periodSpinner = (Spinner) findViewById(R.id.periodSpinner);
                    periodSpinner.setAdapter(new ArrayAdapter<Period>(this, android.R.layout.simple_spinner_item, Period.values()));
                    periodSpinner.setSelection(Period.valueOf(flatPeriod.toString()).ordinal());

                    /*Set text to current prize.*/
                    ((EditText) findViewById(R.id.period_prize)).setText(flatPrize, TextView.BufferType.EDITABLE);

                }else{
                    Toast.makeText(getApplicationContext(), json.getString("response"), Toast.LENGTH_LONG).show();
                }
            }else{
                Log.e("MainMenuContorller", "Could not connect to server");
            }
        } catch (JSONException e) {
           e.printStackTrace();
        }
    }



    public void save_settings(View view){
        final LinearLayout settings_view = (LinearLayout) view.getParent();
        final String flatName = ((EditText) settings_view.findViewById(R.id.flatName)).getText().toString();
        final Period flatPeriod = Period.valueOf((((Spinner) settings_view.findViewById(R.id.periodSpinner)).getSelectedItem().toString()));
        final String flatPrize = ((EditText) settings_view.findViewById(R.id.period_prize)).getText().toString();
        if (flatName.isEmpty() || flatPrize.isEmpty()) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.all_fields_filled), Toast.LENGTH_SHORT).show();
        }else{
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("flatName", flatName);
            params.put("flatPeriod", Integer.toString(flatPeriod.getDuration()));
            params.put("flatPrize", flatPrize);
            params.put("flatPIN",getUser().getFlatPin());
            ServerRequest sr = new ServerRequest();
            JSONObject json = sr.getJSON(HttpType.EDITFLAT, getIpAddress() + ":8080/editFlat", params);

            if (json != null) {
                try {
                    Toast.makeText(getApplication(), json.getString("response"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Checks the filters and init the history tab based on them
     * @param sr
     */
    private void checkFilter(ServerRequest sr){
        if(selectedTaskName.equals("Alle") && selectedUsername.equals("Alle") && !isSelectedTaskName && !isSelectedUsername){
            isSelectedTaskName = true;
            isSelectedUsername = true;
            initHistoryTab();
        }else if(selectedTaskName.equals("Alle") && !selectedUsername.equals("Alle")){
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("flatPIN", ""+getUser().getFlatPin());
            params.put("numberOfHistories", "10");
            params.put("username", selectedUsername);
            JSONObject jsonObject = sr.getJSON(HttpType.TASKHISTORY, getIpAddress() + ":8080/getFeedHistoryBasedOnUsername", params);
            isSelectedTaskName = false;
            isSelectedUsername = false;
            initHistoryTabUsernameAndTaskFilter(jsonObject);
        } else if(!selectedTaskName.equals("Alle") && selectedUsername.equals("Alle")){
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("flatPIN", ""+getUser().getFlatPin());
            params.put("numberOfHistories", "10");
            params.put("taskName", selectedTaskName);
            isSelectedTaskName = false;
            isSelectedUsername = false;
            JSONObject jsonObject = sr.getJSON(HttpType.TASKHISTORY, getIpAddress() + ":8080/getFeedHistoryBasedOnTaskName", params);
            initHistoryTabUsernameAndTaskFilter(jsonObject);
        } else if(!selectedTaskName.equals("Alle") && !selectedUsername.equals("Alle")){
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("flatPIN", ""+getUser().getFlatPin());
            params.put("numberOfHistories", "10");
            params.put("username", selectedUsername);
            params.put("taskName", selectedTaskName);
            isSelectedTaskName = false;
            isSelectedUsername = false;
            JSONObject jsonObject = sr.getJSON(HttpType.TASKHISTORY, getIpAddress() + ":8080/getTasksFeedBasedOnUsernameAndTaskName", params);
            initHistoryTabUsernameAndTaskFilter(jsonObject);
        }
        dateFilter(chosenPeriod);
    }

    /**
     * Init the historyTab based on filtering on username and task name.
     * @param history
     */
    private void initHistoryTabUsernameAndTaskFilter(JSONObject history){
        if(history != null) {
            try {
                if (history.getBoolean("res")) {


                    JSONArray response = history.getJSONArray("response");

                    arrayListUsernames = new ArrayList<>();
                    arrayListDates = new ArrayList<>();
                    arrayListTaskNames = new ArrayList<>();
                    arrayListTaskScores = new ArrayList<>();

                    for(int i = 0; i < response.length(); i++){
                        arrayListUsernames.add(response.getJSONObject(i).getString("username"));
                        arrayListDates.add(response.getJSONObject(i).getString("date"));
                        arrayListTaskNames.add(response.getJSONObject(i).getString("taskName"));
                        arrayListTaskScores.add(response.getJSONObject(i).getString("taskScore"));
                    }

                    historyTabUsernames = arrayListUsernames.toArray(new String[0]);
                    historyTabDates = arrayListDates.toArray(new String[0]);
                    historyTabTaskNames = arrayListTaskNames.toArray(new String[0]);
                    historyTabTaskScores = arrayListTaskScores.toArray(new String[0]);

                    customHistoryListAdapter = new CustomHistoryListAdapter(this, historyTabUsernames, historyTabDates, historyTabTaskNames, historyTabTaskScores);
//                    historyTabList = (ListView)findViewById(R.id.historyList);
                    historyTabList.setAdapter(customHistoryListAdapter);

                }else {
                    Toast.makeText(getApplicationContext(), history.getString("response"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void setTaskSpinner(Spinner taskSpinner) {
        this.taskSpinner = taskSpinner;
    }

    public void setPersonSpinner(Spinner personSpinner) {
        this.personSpinner = personSpinner;
    }

    /**
     * Init the historyTab filtering the dates of the tasks on this period
     * @param view
     */
    public void filterOnPeriod(View view){
        String periodFilter = (String) view.getTag();
        Button button_this = (Button)findViewById(R.id.this_periodBtn);
        Button button_last = (Button)findViewById(R.id.last_periodBtn);
        Button button_earlier = (Button)findViewById(R.id.earlierBtn);

        if(periodFilter.equals("thisPeriod")){
            if(thisPeriodBtnClicked){
                thisPeriodBtnClicked = false;
                button_this.setBackgroundResource(R.drawable.gradient_btn);
                chosenPeriod = "null";
            }else{
                thisPeriodBtnClicked = true;
                button_this.setBackgroundResource(R.drawable.gradient_btn_selected);
                chosenPeriod = "thisPeriod";
            }
            button_last.setBackgroundResource(R.drawable.gradient_btn);
            button_earlier.setBackgroundResource(R.drawable.gradient_btn);
            lastPeriodBtnClicked = false;
            earlierBtnClicked = false;
        }else if(periodFilter.equals("lastPeriod")){
            if(lastPeriodBtnClicked){
                lastPeriodBtnClicked = false;
                button_last.setBackgroundResource(R.drawable.gradient_btn);
                chosenPeriod = "null";
            }else{
                lastPeriodBtnClicked = true;
                button_last.setBackgroundResource(R.drawable.gradient_btn_selected);
                chosenPeriod = "lastPeriod";
            }
            button_this.setBackgroundResource(R.drawable.gradient_btn);
            button_earlier.setBackgroundResource(R.drawable.gradient_btn);
            thisPeriodBtnClicked = false;
            earlierBtnClicked = false;
        } else if(periodFilter.equals("earlier")){
            if(earlierBtnClicked){
                earlierBtnClicked = false;
                button_earlier.setBackgroundResource(R.drawable.gradient_btn);
                chosenPeriod = "null";
            }else{
                earlierBtnClicked = true;
                button_earlier.setBackgroundResource(R.drawable.gradient_btn_selected);
                chosenPeriod = "earlier";
            }
            button_last.setBackgroundResource(R.drawable.gradient_btn);
            button_this.setBackgroundResource(R.drawable.gradient_btn);
            thisPeriodBtnClicked = false;
            lastPeriodBtnClicked = false;
        }
        dateFilter(chosenPeriod);
    }

    /**
     * Filters the tab based on the period selected
     * @param period
     */
    private void dateFilter(String period) {
        if (arrayListDates != null) {
            Date startDate = new Date();
            Date endDate = new Date();
            calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            if (period.equals("thisPeriod")) {
                calendar.add(Calendar.DATE, -getUser().getThisPeriod());
                startDate = calendar.getTime();
            } else if (period.equals("lastPeriod")) {
                calendar.add(Calendar.DATE, -getUser().getThisPeriod() - getUser().getLastPeriod());
                startDate = calendar.getTime();
                calendar.setTime(endDate);
                calendar.add(Calendar.DATE, -getUser().getThisPeriod());
                endDate = calendar.getTime();
            } else if (period.equals("earlier")) {
                calendar.add(Calendar.DATE, -getUser().getThisPeriod() - getUser().getLastPeriod());
                endDate = calendar.getTime();
                startDate = null;
            } else if (period.equals("null")) {
                startDate = null;
                endDate = null;
            }
            ArrayList<Integer> indexes = new ArrayList<>();
            Date compareDate = null;

            for (int i = 0; i < arrayListDates.size(); i++) {

                try {
                    compareDate = new SimpleDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss").parse(arrayListDates.get(i));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (startDate == null && endDate == null) {
                    continue;
                } else if (startDate == null && !compareDate.before(endDate)) {
                    indexes.add(i);
                } else if (!compareDate.after(startDate) || !compareDate.before(endDate)) {
                    indexes.add(i);
                }
            }
            ArrayList<String> copyArrayListDates = new ArrayList<>(arrayListDates);
            ArrayList<String> copyArrayListTaskNames = new ArrayList<>(arrayListTaskNames);
            ArrayList<String> copyArrayListTaskScores = new ArrayList<>(arrayListTaskScores);
            ArrayList<String> copyArrayListUsernames = new ArrayList<>(arrayListUsernames);

            int counter = 0;
            for (int index : indexes) {
                copyArrayListDates.remove(index - counter);
                copyArrayListTaskNames.remove(index - counter);
                copyArrayListTaskScores.remove(index - counter);
                copyArrayListUsernames.remove(index - counter);
                counter++;
            }


            historyTabUsernames = copyArrayListUsernames.toArray(new String[0]);
            historyTabDates = copyArrayListDates.toArray(new String[0]);
            historyTabTaskNames = copyArrayListTaskNames.toArray(new String[0]);
            historyTabTaskScores = copyArrayListTaskScores.toArray(new String[0]);

//            Log.e("MainMenu", "taskNames: " + copyArrayListTaskNames);

            customHistoryListAdapter = new CustomHistoryListAdapter(this, historyTabUsernames, historyTabDates, historyTabTaskNames, historyTabTaskScores);
//            historyTabList = (ListView) findViewById(R.id.historyList);
            historyTabList.setAdapter(customHistoryListAdapter);
        }
    }

    /**
     * Send notification to users when the period is over
     */
    public void endPeriod() {
        Intent notificationIntent = new Intent(getApplicationContext(), MainMenuController.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent notifyPIntent =
                PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setVibrate(new long[] { 500, 500 })
                        .setAutoCancel(true)
                        .setContentIntent(notifyPIntent)
                        .setContentTitle(getString(R.string.push_title))
                        .setContentText(getString(R.string.push_text));
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
    }

    /**
     * Logs the current user out and sends them to login screen
     * @param view
     */
    public void logout(View view){
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
