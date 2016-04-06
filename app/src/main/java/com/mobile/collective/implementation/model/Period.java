package com.mobile.collective.implementation.model;

import java.util.HashMap;
import java.util.Map;

//suggest increase and decrease period in the create flat-view: <|-| BIWEEKLY |+|>
public enum Period{
    WEEKLY(7), BIWEEKLY(14), MONTHLY(30);

    private int duration;



    private static Map<Integer, Period> map = new HashMap<>();

    static {
        for (Period periodEnum : Period.values()) {
            map.put(periodEnum.duration, periodEnum);
        }
    }

    Period(final int duration){
        this.duration = duration;
    }
    public int getDuration(){
        return duration;
    }

    public static Period valueOf(int duration){
        try{
            return map.get(duration);
        }catch(IllegalArgumentException ex){
            throw ex;
        }
    }

}
