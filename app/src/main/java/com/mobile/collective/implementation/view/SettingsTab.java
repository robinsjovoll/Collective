package com.mobile.collective.implementation.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import com.mobile.collective.R;
import com.mobile.collective.implementation.controller.MainMenuController;

/**
 * Created by Robin on 22/02/2016.
 */
public class SettingsTab extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.activity_settings,container,false);
        MainMenuController mainMenuController = (MainMenuController) getActivity();
        mainMenuController.setPeriodSpinner((Spinner) v.findViewById(R.id.periodSpinner));
        mainMenuController.setEflatName((EditText) v.findViewById(R.id.flatName));
        mainMenuController.setPeriodPrize((EditText) v.findViewById(R.id.period_prize));
        mainMenuController.initSettingsTab();
        return v;
    }

}
