package com.mobile.collective.implementation.model;

import com.mobile.collective.implementation.model.Task;
import com.mobile.collective.implementation.model.User;

import java.util.ArrayList;

public class Flat {
    private ArrayList<User> flatmates;
    private final int flatPin;
    private ArrayList<Task> tasks;

    public Flat(){
        this.flatPin = generatePin();
        this.flatmates = new ArrayList<>();
        this.tasks = new ArrayList<>();
        addDefaultTasks();
    }

    private int generatePin(){
        return 0;
    }

    public boolean addFlatmate(User user){
        return flatmates.add(user);
    }

    public boolean removeFlatmate(User user){
        return flatmates.remove(user);
    }

    public ArrayList<User> getFlatmates(){
        return flatmates;
    }

    private void addDefaultTasks(){
        //TODO
    }



}
