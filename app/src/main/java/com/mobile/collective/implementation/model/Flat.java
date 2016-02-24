package com.mobile.collective.implementation.model;

import com.mobile.collective.implementation.model.Task;
import com.mobile.collective.implementation.model.User;

import java.util.ArrayList;
import java.util.Date;

public class Flat {
    private ArrayList<User> flatmates;
    private final int flatPin;
    private ArrayList<Task> tasks;
    private Period period;
    private String prize;
    private User admin; //In case of only one admin per flat.


    //suggest increase and decrease period in the create flat-view: <|-| BIWEEKLY |+|>
    private enum Period{
        WEEKLY, BIWEEKLY, MONTHLY {
            //return current if no next.
            public Period next(){
                return this;
            }
            //return current if no previous.
            public Period previous(){
                return this;
            }
        };

        public Period next(){
            return values()[ordinal() + 1];
        }

        public Period previous(){
            return values()[ordinal() - 1];
        }
    }

    public Flat(User user, Period period, String prize){
        this.period = period;
        this.prize = prize;
        this.flatPin = generatePin();
        this.flatmates = new ArrayList<>();
        this.tasks = new ArrayList<>();
        addDefaultTasks();

        this.flatmates.add(user);
        user.makeAdmin();
        this.admin = user;
    }

    public String getPrize(){
        return this.prize;
    }

    public void setPrize(String prize){
        this.prize = prize;
    }

    public void increasePeriod(){
        this.period.next();
    }

    public void decreasePeriod(){
        this.period.previous();
    }

    public Period getPeriod(){
        return this.period;
    }

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

    private int generatePin(){
        //TODO
        return 0;
    }

    private void addDefaultTasks(){
        //TODO
    }

}
