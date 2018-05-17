package com.hydbest.skinboot.util;

/**
 * Created by csz on 2018/5/17.
 */

public enum ThemeMode {
    DAY(0),
    NIGHT(1),
    BLUE(2);

    private int mValue ;

    ThemeMode(int value){
        this.mValue = value;
    }

    public int value(){
        return mValue;
    }
}
