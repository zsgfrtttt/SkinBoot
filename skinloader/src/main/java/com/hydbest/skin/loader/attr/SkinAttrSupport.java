package com.hydbest.skin.loader.attr;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import com.hydbest.skin.loader.constant.SkinConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by csz on 2018/5/22.
 */

public class SkinAttrSupport {

    public static List<SkinAttr> getSkinAttrs(Context context, AttributeSet attrs) {

        List<SkinAttr> skinAttrs = new ArrayList<>();
        SkinAttr attr = null;
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            String attrName = attrs.getAttributeName(i);
            String attrValue = attrs.getAttributeValue(i);

            SkinAttrType attrType = getSupprotAttrType(attrName);
            if (attrType == null)
                continue;

            if (attrValue.startsWith("@")) {
                int id = 0;
                try {
                    id = Integer.parseInt(attrValue.substring(1));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    continue;
                }
                String entryName = context.getResources().getResourceEntryName(id);

                Log.i("csz", "entryName : " + entryName);
                if (entryName.startsWith(SkinConfig.ATTR_PREFIX)) {
                    attr = new SkinAttr(attrType, entryName);
                    skinAttrs.add(attr);
                }

            }
        }

        return skinAttrs;
    }

    private static SkinAttrType getSupprotAttrType(String attrName) {
        for (SkinAttrType attrType : SkinAttrType.values()) {
            if (attrType.getAttrType().equals(attrName)) {
                return attrType;
            }
        }
        return null;
    }

}
