package com.hydbest.skin.loader.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.hydbest.skin.loader.constant.SkinConfig;


/**
 * Created by csz on 2018/5/22.
 */

public class PrefUtil
{
    private Context mContext;

    public PrefUtil(Context context)
    {
        this.mContext = context.getApplicationContext();
    }

    public String getPluginPath()
    {
        SharedPreferences sp = mContext.getSharedPreferences(SkinConfig.PREF_NAME, Context.MODE_PRIVATE);
        return sp.getString(SkinConfig.KEY_PLUGIN_PATH, "");
    }

    public String getSuffix()
    {
        SharedPreferences sp = mContext.getSharedPreferences(SkinConfig.PREF_NAME, Context.MODE_PRIVATE);
        return sp.getString(SkinConfig.KEY_PLUGIN_SUFFIX, "");
    }

    public boolean clear()
    {
        SharedPreferences sp = mContext.getSharedPreferences(SkinConfig.PREF_NAME, Context.MODE_PRIVATE);
        return sp.edit().clear().commit();
    }

    public void putPluginPath(String path)
    {
        SharedPreferences sp = mContext.getSharedPreferences(SkinConfig.PREF_NAME, Context.MODE_PRIVATE);
        sp.edit().putString(SkinConfig.KEY_PLUGIN_PATH, path).apply();
    }

    public void putPluginPkg(String pkgName)
    {
        SharedPreferences sp = mContext.getSharedPreferences(SkinConfig.PREF_NAME, Context.MODE_PRIVATE);
        sp.edit().putString(SkinConfig.KEY_PLUGIN_PKG, pkgName).apply();
    }

    public String getPluginPkgName()
    {
        SharedPreferences sp = mContext.getSharedPreferences(SkinConfig.PREF_NAME, Context.MODE_PRIVATE);
        return sp.getString(SkinConfig.KEY_PLUGIN_PKG, "");
    }

    public void putPluginSuffix(String suffix)
    {
        SharedPreferences sp = mContext.getSharedPreferences(SkinConfig.PREF_NAME, Context.MODE_PRIVATE);
        sp.edit().putString(SkinConfig.KEY_PLUGIN_SUFFIX, suffix).apply();
    }
}
