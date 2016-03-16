package com.mobile.collective.implementation.model;

//suggest increase and decrease period in the create flat-view: <|-| BIWEEKLY |+|>
public enum Period{
    WEEKLY(7), BIWEEKLY(14), MONTHLY(30);

    private int duration;

    /*
    {
        //return current if no next.
        public Period next(){
            return this;
        }
        //return current if no previous.
        public Period previous(){
            return this;
        }
    };
    */

    Period(int duration){
        this.duration = duration;
    }

    private double getDuration(){
        return duration;
    }

    public Period next(){
        return values()[ordinal() + 1];
    }

    public Period previous(){
        return values()[ordinal() - 1];
    }
}
