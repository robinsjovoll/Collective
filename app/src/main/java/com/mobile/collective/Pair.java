package com.mobile.collective;

import java.util.Date;
/*
* Class to pair together date and user for history of task performed.
 */
public class Pair<Date, User> {

    private final Date date;
    private final User user;

    public Pair(Date date, User user){
        this.date = date;
        this.user = user;
    }

    public Date getDate(){
        return this.date;
    }

    public User getUser(){
        return this.user;
    }
}
