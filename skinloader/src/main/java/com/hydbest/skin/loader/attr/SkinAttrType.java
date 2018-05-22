package com.hydbest.skin.loader.attr;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hydbest.skin.loader.manager.ResourceManager;
import com.hydbest.skin.loader.manager.SkinManager;

/**
 * Created by csz on 2018/5/22.
 */

public enum SkinAttrType {

    BACKGROUD("background") {
        @Override
        public void apply(View view, String resName) {
            Drawable bg = getResourceManager().getDrawableByName(resName);
            if (bg==null){
                return;
            }
            view.setBackgroundDrawable(bg);
        }
    },
    COLOR("textColor") {
        @Override
        public void apply(View view, String resName) {
            if (view instanceof TextView){
                TextView tv = (TextView) view;
                ColorStateList color = getResourceManager().getColorStateList(resName);
                if (color == null){
                    return;
                }
                tv.setTextColor(color);
            }
        }
    },
    SRC("src") {
        @Override
        public void apply(View view, String resName) {
            if (view instanceof ImageView){
                ImageView iv = (ImageView) view;
                Drawable src = getResourceManager().getDrawableByName(resName);
                if (src == null){
                    return;
                }
                iv.setImageDrawable(src);
            }
        }
    };


    String attrType;

    SkinAttrType(String attrType) {
        this.attrType = attrType;
    }

    public String getAttrType()
    {
        return attrType;
    }

    public abstract void apply(View view, String resName) ;

    public ResourceManager getResourceManager(){
        return SkinManager.getInstance().getResourceManager();
    }
}
