package com.mobile.collective.implementation.model;

import com.mobile.collective.implementation.model.Task;
import com.mobile.collective.implementation.model.User;

import java.util.ArrayList;

public class Household {
    private ArrayList<User> members, admins;
    private final int roomKey;
    //private Scoreboard scoreboard;
    private ArrayList<Task> tasks;

    public Household(){
        this.roomKey = 0;
    }



}
