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

import com.hydbest.skinboot.R;

import java.io.File;
import java.lang.reflect.Method;

/**
 * Created by csz on 2018/5/21.
 */

public class DrawableActivity extends AppCompatActivity {
    private String mPackName = "com.hydbest.skinplugin";
    private String mPluginPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "app_plugin.apk";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawable);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void loadPlugin(View v) {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            } else {
                AssetManager assetManager = AssetManager.class.newInstance();
                Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
                addAssetPath.invoke(assetManager, mPluginPath);

                Resources superResource = getResources();
                Resources resources = new Resources(assetManager, superResource.getDisplayMetrics(), superResource.getConfiguration());
                Drawable bg = resources.getDrawable(resources.getIdentifier("main_bg", "drawable", mPackName));
                findViewById(R.id.root).setBackground(bg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
