package com.hydbest.skinboot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.hydbest.skinboot.activity.DrawableActivity;
import com.hydbest.skinboot.activity.ThemeModeActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void theme(View view) {
        startActivity(new Intent(this, ThemeModeActivity.class));
    }

    public void plugin(View view) {
        startActivity(new Intent(this, DrawableActivity.class));
    }
}
