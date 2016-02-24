package com.mobile.collective.implementation.model;

import android.graphics.Color;

public class User {
    private String name, mail;
    private Color color;
    private String hash, salt;
    private int score;
    private boolean isAdmin;

    public User(String name, String mail, Color color, String hash, String salt, boolean isAdmin){
        this.name = name;
        this.mail = mail;
        this.color = color;
        this.score = 0;
        this.isAdmin = isAdmin;
    }

    public String getName(){
        return this.name;
    }

    public void makeAdmin(){
        isAdmin = true;
    }

    public boolean isAdmin(){
        return isAdmin;
    }

    public void setMail(String mail){
        this.mail = mail;
    }

    public String getMail(){
        return this.mail;
    }

    public int getScore(){
        return this.score;
    }

    public void resetScore(){
        this.score = 0;
    }

    public void increaseScore(int offset){
        this.score += offset;
    }

}