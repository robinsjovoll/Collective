package com.mobile.collective.implementation.model;


import com.mobile.collective.framework.Pair;

import java.util.ArrayList;
import java.util.Date;

public class Task {

    private int points;
    private String description, name;
    private boolean continuous, approved;
    private ArrayList<Pair<Date, User>> history;

    public Task(int points, String description, String name, boolean continuous){
        this.points = points;
        this.description = description;
        this.name = name;
        this.continuous = continuous;
        this.history = new ArrayList<>();
    }

    public int getPoints(){
        return this.points; }

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

    public boolean getApproved(){
        return this.approved;
    }

    public void approveTask(){
        this.approved = true;
    }

    public void updateHistory(Date date, User user){
        this.history.add(new Pair<>(date, user));
    }
}
