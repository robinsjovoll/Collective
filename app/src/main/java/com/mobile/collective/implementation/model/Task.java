package com.mobile.collective.implementation.model;


import com.mobile.collective.framework.Pair;

import java.util.ArrayList;
import java.util.Date;

public class Task {

    private int points;
    private String description, name;
    private boolean continuous;
    private ArrayList<Pair<Date, User>> history;

    public Task(int points, String description, String name, boolean continuous, ArrayList<Pair<Date, User>> history){
        this.points = points;
        this.description = description;
        this.name = name;
        this.history = history;
    }

    public int getPoints(){
        return this.points;
    }

    public void setPoints(int points){
        this.points = points;
    }

    public String getDescription(){
        return this.description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getName(){
        return this.name;
    }

    public  void setName(String name){
        this.name = name;
    }

    public boolean getContinuous(){
        return this.continuous;
    }

    public void setContinuous(boolean continuous){
        this.continuous = continuous;
    }

    public void updateHistory(Date date, User user){
        history.add(new Pair<Date, User>(date, user));
    }
}
