package com.mobile.collective.implementation.controller;


import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


import com.mobile.collective.R;
import com.mobile.collective.framework.AppMenu;
import com.mobile.collective.framework.MainViewPagerAdapter;
import com.mobile.collective.framework.SlidingTabLayout;


/**
 * Created by Robin on 22/02/2016.
 */
public class MainMenuController extends AppMenu {

    Toolbar toolbar;
    ViewPager pager;
    MainViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[];
    int numboftabs =4;

    public void onCreate(Bundle savedInstanceBundle){
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.activity_main);

        Titles= new CharSequence[]{
            getResources().getString(R.string.score_title),
                    getResources().getString(R.string.task_title),
                    getResources().getString(R.string.feed_title),
                    getResources().getString(R.string.setting_title)};

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new MainViewPagerAdapter(getSupportFragmentManager(),Titles, numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.main_pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.main_tabs);
        tabs.setMainMenuController(this);
        tabs.setMainPagesInitialized(true);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.white);
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




}
