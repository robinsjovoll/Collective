package com.mobile.collective.implementation.model;

import android.graphics.Color;

public class User {
    private String name, mail;
    private Color color;
    private String hash, salt;
    private int periodScore, globalScore;
    private int flatPin;
    private boolean isAdmin;

    public User(String name, String mail, String hash, String salt, boolean isAdmin){
        this.name = name;
        this.mail = mail;
//      this.color = color;
        this.hash = hash;
        this.salt = salt;
        this.periodScore = 0;
        this.globalScore = 0;
        this.isAdmin = false;
    }

    public String getName(){
        return this.name;
    }

    public void makeAdmin(){
        this.isAdmin = true;
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

    public int getPeriodScore(){
        return this.periodScore;
    }

    public void resetScore(){
        this.periodScore = 0;
    }

    public void increaseScore(int offset){
        this.periodScore += offset;
    }

    public void setFlatPin(int pin){
        this.flatPin = pin;
    }

    public int getFlatPin(){
        return this.flatPin;
    }

}
