package com.mobile.collective.implementation.model;

import android.util.Pair;
import java.util.ArrayList;

public class Scoreboard {
    private ArrayList<Pair<User, Integer>> scoreList;
    private Period period;

    public Scoreboard(ArrayList<User> users, Period period){
        this.period = period;
        this.scoreList = updateScoreboard(users);
    }

    public ArrayList<Pair<User, Integer>> updateScoreboard(ArrayList<User> users){
        for(User user : users){
            scoreList.add(new Pair<>(user, user.getPeriodScore()));
        }
        return scoreList;
    }
    public ArrayList<Pair<User, Integer>> getScorelist()
    {
        return scoreList;
    }
}
