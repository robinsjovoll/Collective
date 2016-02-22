package com.mobile.collective.framework;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mobile.collective.implementation.view.NewsfeedTab;
import com.mobile.collective.implementation.view.ScoreTab;
import com.mobile.collective.implementation.view.SettingsTab;
import com.mobile.collective.implementation.view.TaskTab;


/**
 * Created by Robin on 21/02/2016.
 */
public class MainViewPagerAdapter extends FragmentPagerAdapter {


    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created

    /**
     * Different menus/tabs.
     */
    private ScoreTab scoreTab;
    private TaskTab taskTab;
    private NewsfeedTab newsfeedTab;
    private SettingsTab settingsTab;


    // Build a Constructor and assign the passed Values to appropriate values in the class
    public MainViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

        scoreTab = new ScoreTab();
        taskTab = new TaskTab();
        newsfeedTab = new NewsfeedTab();
        settingsTab = new SettingsTab();



    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        if(position == 0){ // if the position is 0 we are returning the First tab
            return taskTab;
        }
        else if(position == 1)    {         // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
            return  scoreTab;
        }
        else if(position == 2) {            // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
            return newsfeedTab;
        }
        else {        // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
            return settingsTab;
        }

    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }

    public CharSequence[] getTitles() {
        return Titles;
    }

}

