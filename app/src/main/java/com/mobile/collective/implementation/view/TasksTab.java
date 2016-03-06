package com.mobile.collective.implementation.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mobile.collective.R;
import com.mobile.collective.implementation.controller.MainMenuController;

/**
 * Created by Robin on 22/02/2016.
 */
public class TasksTab extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.activity_task,container,false);
//        Bundle bundle = getArguments();
//        MainMenuController mainMenuController = (MainMenuController) bundle.getSerializable("MainController");
        MainMenuController mainMenuController = (MainMenuController) getActivity();
        mainMenuController.setSuggestedTaskList((ListView)v.findViewById(R.id.suggested_task_list));
        mainMenuController.setAcceptedTaskList((ListView)v.findViewById(R.id.accepted_task_list));
        mainMenuController.initTasksTab();
        return v;
    }

}
