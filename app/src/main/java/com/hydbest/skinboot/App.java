package com.hydbest.skinboot;

import android.app.Application;

import com.hydbest.skin.loader.manager.SkinManager;

/**
 * Created by csz on 2018/5/22.
 */

public class App extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        SkinManager.getInstance().init(this);
    }
}
