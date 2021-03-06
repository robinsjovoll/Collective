package com.mobile.collective.implementation.model;


import java.util.ArrayList;
import java.util.Date;

public class Flat {
    private ArrayList<User> flatmates;
    private final int flatPin;
    private ArrayList<Task> tasks;
    private Period period;
    private String prize;
    private Scoreboard scoreboard;
    private User admin; //In case of only one admin per flat.

    public Flat(User user, int flatPin){
        this.period = Period.ANNENHVERUKE;
        this.flatmates = new ArrayList<>();
        this.tasks = new ArrayList<>();
        this.flatPin = flatPin;
        this.flatmates.add(user);
        this.scoreboard = new Scoreboard(flatmates, this.period);
        user.makeAdmin();
        this.admin = user;
    }

    public String getPrize(){
        return this.prize;
    }

    public void setPrize(String prize){
        this.prize = prize;
    }

    public Period getPeriod(){
        return this.period;
    }

    public Scoreboard getScoreboard() { return this.scoreboard; }

    public boolean addFlatmate(User user){
        return this.flatmates.add(user);
    }

    public boolean removeFlatmate(User user){
        return this.flatmates.remove(user);
    }

    public boolean promoteFlatmate(User user){
        if(flatmates.contains(user)){
            user.makeAdmin();
            this.admin = user;
            return true;
        }else{
            return false;
        }
    }

    public ArrayList<User> getFlatmates(){
        return this.flatmates;
    }

    public boolean addTask(Task task){
        return this.tasks.add(task);
    }

    //Should admin have to approve a completed task?
    public void completeTask(User user, Task task, Date date){
        user.increaseScore(task.getPoints());
        task.updateHistory(date, user);
        if (!task.getContinuous()){
            tasks.remove(task);
        }
    }
}
