package com.hydbest.skinboot.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hydbest.skinboot.R;
import com.hydbest.skinboot.adapter.TextAdapter;
import com.hydbest.skinboot.util.ThemeHelper;
import com.hydbest.skinboot.util.ThemeMode;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by csz on 2018/5/17.
 */

public class ThemeModeActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout mLayoutRoot;
    private CardView mCardView;
    private TextView mTvTitle;
    private RecyclerView mRv;
    private Button mBtnChange;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTheme();
        setContentView(R.layout.activity_theme_mode);
        initView();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null)
            actionBar.hide();
    }

    private void initView() {
        mLayoutRoot = findViewById(R.id.layout_root);
        mCardView = findViewById(R.id.cardView);
        mTvTitle = findViewById(R.id.tv_title);

        mBtnChange = findViewById(R.id.btnChange);
        mBtnChange.setOnClickListener(this);

        mRv= findViewById(R.id.rv);
        mRv.setLayoutManager(new LinearLayoutManager(this));
        mRv.setAdapter(new TextAdapter(null));
    }

    private void initTheme() {
        ThemeHelper.getInstance().initTheme(this);
    }
    /**
     * 刷新UI界面
     */
    private void refreshUI() {
        TypedValue background = new TypedValue();//背景色
        TypedValue textColor = new TypedValue();//字体颜色
        TypedValue lineBg = new TypedValue();
        Resources.Theme theme = getTheme();
        theme.resolveAttribute(R.attr.themeBackground, background, true);
        theme.resolveAttribute(R.attr.themeTextColor, textColor, true);
        theme.resolveAttribute(R.attr.themeDivider, lineBg, true);

        mLayoutRoot.setBackgroundResource(background.resourceId);
        mCardView.setBackgroundResource(background.resourceId);
        mTvTitle.setBackgroundResource(background.resourceId);
        mBtnChange.setBackgroundResource(background.resourceId);

        mTvTitle.setTextColor(getResources().getColor(textColor.resourceId));
        mBtnChange.setTextColor(getResources().getColor(textColor.resourceId));

        Resources resources = getResources();
        int childCount = mRv.getChildCount();
        for (int childIndex = 0; childIndex < childCount; childIndex++) {
            ViewGroup childView = (ViewGroup) mRv.getChildAt(childIndex);
            TextView tvText = (TextView) childView.findViewById(R.id.tv);
            tvText.setBackgroundResource(background.resourceId);
            tvText.setTextColor(resources.getColor(textColor.resourceId));

            View lineView = childView.findViewById(R.id.line);
            lineView.setBackgroundResource(lineBg.resourceId);
        }

        //让 RecyclerView 缓存在 Pool 中的 Item 失效
        //那么，如果是ListView，要怎么做呢？这里的思路是通过反射拿到 AbsListView 类中的 RecycleBin 对象，然后同样再用反射去调用 clear 方法
        Class<RecyclerView> recyclerViewClass = RecyclerView.class;
        try {
            Field declaredField = recyclerViewClass.getDeclaredField("mRecycler");
            declaredField.setAccessible(true);
            Method declaredMethod = Class.forName(RecyclerView.Recycler.class.getName()).getDeclaredMethod("clear", (Class<?>[]) new Class[0]);
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(declaredField.get(mRv), new Object[0]);
            RecyclerView.RecycledViewPool recycledViewPool = mRv.getRecycledViewPool();
            recycledViewPool.clear();

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        refreshStatusBar();
    }

    /**
     * 刷新 StatusBar
     */
    private void refreshStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = getTheme();
            theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
            getWindow().setStatusBarColor(getResources().getColor(typedValue.resourceId));
        }
    }

    @Override
    public void onClick(View view) {
        new AlertDialog.Builder(this)
                .setSingleChoiceItems(new String[]{"日间模式","夜间模式","深海主题"}, ThemeHelper.getInstance().getThemeMode().value(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i== 0 && ThemeHelper.getInstance().getThemeMode() != ThemeMode.DAY){
                            ThemeHelper.getInstance().changeTheme(ThemeModeActivity.this,ThemeMode.DAY);
                            refreshUI();
                        }else if (i==1 && ThemeHelper.getInstance().getThemeMode() != ThemeMode.NIGHT){
                            ThemeHelper.getInstance().changeTheme(ThemeModeActivity.this,ThemeMode.NIGHT);
                            refreshUI();
                        }else if (i==2 && ThemeHelper.getInstance().getThemeMode() != ThemeMode.BLUE){
                            ThemeHelper.getInstance().changeTheme(ThemeModeActivity.this,ThemeMode.BLUE);
                            refreshUI();
                        }
                    }
                }).create().show();
    }
}
