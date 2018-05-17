package com.hydbest.skinboot.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;

import com.hydbest.skinboot.R;

/**
 * Created by csz on 2018/5/17.
 */

public class ThemeHelper {

    private ThemeMode mThemeMode ;

    private ThemeHelper(){
        //默认为白天模式
        setMode(ThemeMode.DAY);
    }

    private static final class Holder {
        private static final ThemeHelper INSTANCE = new ThemeHelper();
    }

    public static ThemeHelper getInstance(){
        return Holder.INSTANCE;
    }

    public void setMode(ThemeMode mode){
        this.mThemeMode = mode;
    }

    public ThemeMode getThemeMode(){
        return this.mThemeMode;
    }

    public void initTheme(Activity activity){
        switch (mThemeMode){
            case DAY:
                activity.setTheme(R.style.DayTheme);
                break;
            case NIGHT:
                activity.setTheme(R.style.NightTheme);
                break;
            case BLUE:
                activity.setTheme(R.style.BlueTheme);
                break;
        }
    }

    public void changeTheme(Activity activity,ThemeMode mode){
        if (this.mThemeMode != mode){
            showAnimation(activity);
            setMode(mode);
            initTheme(activity);
        }
    }


    /**
     * 展示一个切换动画
     */
    private void showAnimation(Activity activity) {
        final View decorView = activity.getWindow().getDecorView();
        Bitmap cacheBitmap = getCacheBitmapFromView(decorView);
        if (decorView instanceof ViewGroup && cacheBitmap != null) {
            final View view = new View(activity);
            view.setBackgroundDrawable(new BitmapDrawable(activity.getResources(), cacheBitmap));
            ViewGroup.LayoutParams layoutParam = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            ((ViewGroup) decorView).addView(view, layoutParam);
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
            objectAnimator.setDuration(300);
            objectAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    ((ViewGroup) decorView).removeView(view);
                }
            });
            objectAnimator.start();
        }
    }

    /**
     * 获取一个 View 的缓存视图
     */
    private Bitmap getCacheBitmapFromView(View view) {
        final boolean drawingCacheEnabled = true;
        view.setDrawingCacheEnabled(drawingCacheEnabled);
        view.buildDrawingCache(drawingCacheEnabled);
        final Bitmap drawingCache = view.getDrawingCache();
        Bitmap bitmap;
        if (drawingCache != null) {
            bitmap = Bitmap.createBitmap(drawingCache);
            view.setDrawingCacheEnabled(false);
        } else {
            bitmap = null;
        }
        return bitmap;
    }

}
