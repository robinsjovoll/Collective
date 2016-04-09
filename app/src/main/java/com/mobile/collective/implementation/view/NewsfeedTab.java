package com.mobile.collective.implementation.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Spinner;

import com.mobile.collective.R;
import com.mobile.collective.implementation.controller.MainMenuController;

/**
 * Created by Robin on 22/02/2016.
 */
public class NewsfeedTab extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.activity_history,container,false);
        MainMenuController mainMenuController = (MainMenuController) getActivity();
        mainMenuController.setHistoryTabList((ListView)v.findViewById(R.id.historyList));
        mainMenuController.setTaskSpinner((Spinner) v.findViewById(R.id.taskSpinner));
        mainMenuController.setPersonSpinner((Spinner)v.findViewById(R.id.personSpinner));
        mainMenuController.initHistoryTab();
        return v;
    }

}
