package com.mobile.collective.implementation.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.mobile.collective.R;
import com.mobile.collective.implementation.controller.MainMenuController;

/**
 * Created by Robin on 22/02/2016.
 */
public class ScoreTab extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.activity_score,container,false);
        MainMenuController mainMenuController = (MainMenuController) getActivity();
        mainMenuController.setFlatName((TextView) v.findViewById(R.id.flatName_score));
        mainMenuController.setFlatPrize((TextView) v.findViewById(R.id.prize_this_period));
        mainMenuController.setScoreList((ListView)v.findViewById(R.id.listView_scores));
        mainMenuController.setLastWinner((TextView) v.findViewById(R.id.last_winner_person));
        mainMenuController.initScoreTab();
        return v;
    }

}
