package com.mobile.collective.implementation.model;

import android.graphics.Color;

public class User {
    private String name, mail;
    private Color color;
    private String hash, salt;
    private int periodScore, globalScore;
    private String flatPin;
    private boolean isAdmin;
    private boolean periodOver;
    private int thisPeriod = 0, lastPeriod = 0;

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
    public User() {
        this.isAdmin = false;
    }


    public String getName(){
        return this.name;
    }

    public void makeAdmin(){
        this.isAdmin = true;
    }

    public void removeAsAdmin(){
        this.isAdmin = false;
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

    public void updateGlobalScore(){
        this.globalScore += periodScore;
    }

    public void resetScore(){
        this.periodScore = 0;
    }

    public void increaseScore(int offset){
        this.periodScore += offset;
    }

    public void setFlatPin(String pin){
        this.flatPin = pin;
    }

    public String getFlatPin(){
        return this.flatPin;
    }

    public User getUser()
    {
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPeriodOver() {
        return periodOver;
    }

    public void setPeriodOver(boolean periodOver) {
        this.periodOver = periodOver;
    }

    public void setLastPeriod(int lastPeriod) {
        this.lastPeriod = lastPeriod;
    }

    public int getLastPeriod() {
        return lastPeriod;
    }

    public void setThisPeriod(int thisPeriod) {
        this.thisPeriod = thisPeriod;
    }

    public int getThisPeriod() {
        return thisPeriod;
    }

    public void resetUser(){
        this.name = "";
        this.mail = "";
        this.isAdmin = false;
        this.flatPin = "";
        this.periodOver = false;
    }

    @Override
    public String toString() {
        return "FlatPIN: " + getFlatPin() + " email: " + getMail() + " username: " + getName() + " thisPeriod: " + getThisPeriod() + " lastPeriod: " + getLastPeriod();
    }
}
