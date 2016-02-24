package com.mobile.collective.implementation.model;

import com.mobile.collective.implementation.model.Task;
import com.mobile.collective.implementation.model.User;

import java.util.ArrayList;

public class Flat {
    private ArrayList<User> members, admins;
    private final int roomKey;
    //private Scoreboard scoreboard;
    private ArrayList<Task> tasks;

    public Flat(){
        this.roomKey = 0;
    }



}
