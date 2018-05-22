package com.hydbest.skinboot.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hydbest.skin.loader.base.BaseSkinActivity;
import com.hydbest.skin.loader.manager.SkinManager;
import com.hydbest.skinboot.R;

import java.io.File;
import java.lang.reflect.Method;

/**
 * Created by csz on 2018/5/21.
 */

public class DrawableActivity extends BaseSkinActivity {
    private String mPackName = "com.hydbest.skinplugin";
    private String mPluginPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "app-debug.apk";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawable);
    }

    public void defaultSkin(View view){
        SkinManager.getInstance().changeSkin(null);
    }
    public void greenSkin(View view){
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
            return;
        }
        SkinManager.getInstance().changeSkin(mPluginPath,mPackName,"green",null);
    }
    public void redSkin(View view){
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
            return;
        }
        SkinManager.getInstance().changeSkin(mPluginPath,mPackName,"red",null);
    }
    public void graySkin(View view){
        SkinManager.getInstance().changeSkin("gray");
    }
}
