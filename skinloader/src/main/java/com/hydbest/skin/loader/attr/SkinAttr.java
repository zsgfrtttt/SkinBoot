package com.hydbest.skin.loader.attr;

import android.view.View;

/**
 * Created by csz on 2018/5/22.
 */

public class SkinAttr {

    String resName;
    SkinAttrType attrType;

    public SkinAttr(SkinAttrType attrType, String resName)
    {
        this.resName = resName;
        this.attrType = attrType;
    }

    public void apply(View view){
        attrType.apply(view,resName);
    }
}
