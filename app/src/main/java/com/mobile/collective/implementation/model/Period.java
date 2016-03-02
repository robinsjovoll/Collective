package com.mobile.collective.implementation.model;

//suggest increase and decrease period in the create flat-view: <|-| BIWEEKLY |+|>
public enum Period{
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

    Period(){
    }

    public Period next(){
        return values()[ordinal() + 1];
    }

    public Period previous(){
        return values()[ordinal() - 1];
    }
}
