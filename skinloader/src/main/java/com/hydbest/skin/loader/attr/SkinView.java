package com.hydbest.skin.loader.attr;

import android.view.View;

import java.lang.ref.SoftReference;
import java.util.List;

/**
 * Created by csz on 2018/5/22.
 */

public class SkinView {
    SoftReference<View> mViewRefrence ;
    List<SkinAttr> mAttrs;

    public SkinView(View view, List<SkinAttr> skinAttrs)
    {
        this.mViewRefrence = new SoftReference<View>(view);
        this.mAttrs = skinAttrs;
    }

    public void apply(){
        View view = mViewRefrence.get();
        if (view == null || mAttrs == null){
            return;
        }
        for (SkinAttr attr : mAttrs) {
            attr.apply(view);
        }
    }
}
