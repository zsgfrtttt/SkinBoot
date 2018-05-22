package com.hydbest.skin.loader.manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.hydbest.skin.loader.attr.SkinView;
import com.hydbest.skin.loader.callback.ISkinChangedListener;
import com.hydbest.skin.loader.callback.ISkinChangingCallback;
import com.hydbest.skin.loader.util.PrefUtil;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by csz on 2018/5/22.
 */

public class SkinManager {

    private Context mContext;
    private Resources mResources;
    private ResourceManager mResourceManager;
    private PrefUtil mPrefUtil;

    private boolean mUsePlugin;
    /**
     * 换肤资源后缀
     */
    private String mSuffix = "";
    private String mCurPluginPath;
    private String mCurPluginPkg;


    private Map<ISkinChangedListener, List<SkinView>> mSkinViewMaps = new HashMap<ISkinChangedListener, List<SkinView>>();
    private List<ISkinChangedListener> mSkinChangedListeners = new ArrayList<ISkinChangedListener>();

    private SkinManager() {
    }

    private static class Holder {
        private static SkinManager INSTANCE = new SkinManager();
    }

    public static SkinManager getInstance() {
        return Holder.INSTANCE;
    }

    public void init(Context context) {
        mContext = context.getApplicationContext();
        mPrefUtil = new PrefUtil(mContext);

        String skinPluginPath = mPrefUtil.getPluginPath();
        String skinPluginPkg = mPrefUtil.getPluginPkgName();
        mSuffix = mPrefUtil.getSuffix();
        if (TextUtils.isEmpty(skinPluginPath))
            return;
        File file = new File(skinPluginPath);
        if (!file.exists()) return;
        try {
            loadPlugin(skinPluginPath, skinPluginPkg, mSuffix);
            mCurPluginPath = skinPluginPath;
            mCurPluginPkg = skinPluginPkg;
        } catch (Exception e) {
            //TODO
            mPrefUtil.clear();
            e.printStackTrace();
        }
    }

    private void loadPlugin(String skinPath, String skinPkgName, String suffix) throws Exception {
        //checkPluginParams(skinPath, skinPkgName);
        AssetManager assetManager = AssetManager.class.newInstance();
        Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
        addAssetPath.invoke(assetManager, skinPath);

        Resources superRes = mContext.getResources();
        mResources = new Resources(assetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());
        mResourceManager = new ResourceManager(mResources, skinPkgName, suffix);
        mUsePlugin = true;
    }

    public ResourceManager getResourceManager() {
        if (!mUsePlugin) {
            mResourceManager = new ResourceManager(mContext.getResources(), mContext.getPackageName(), mSuffix);
        }
        return mResourceManager;
    }

    private void checkPluginParamsThrow(String skinPath, String skinPkgName) {
        if (!checkPluginParams(skinPath, skinPkgName)) {
            throw new IllegalArgumentException("skinPluginPath or skinPkgName can not be empty ! ");
        }
    }

    private boolean checkPluginParams(String skinPath, String skinPkgName) {
        if (TextUtils.isEmpty(skinPath) || TextUtils.isEmpty(skinPkgName)) {
            return false;
        }
        return true;
    }


    /**
     * 恢复默认皮肤
     */
    public void removeAnySkin() {
        clearPluginInfo();
        notifyChangedListeners();
    }

    public boolean needChangeSkin() {
        return mUsePlugin || !TextUtils.isEmpty(mSuffix);
    }

    /**
     * 应用内换肤，传入资源区别的后缀
     */
    public void changeSkin(String suffix) {
        clearPluginInfo();
        mSuffix = suffix;
        mPrefUtil.putPluginSuffix(suffix);
        notifyChangedListeners();
    }

    public void changeSkin(final String skinPluginPath, final String pkgName, ISkinChangingCallback callback) {
        changeSkin(skinPluginPath, pkgName, "", callback);
    }

    /**
     * 根据suffix选择插件内某套皮肤，默认为""
     */
    @SuppressLint("StaticFieldLeak")
    public void changeSkin(final String skinPluginPath, final String pkgName, final String suffix, ISkinChangingCallback callback) {
        if (callback == null)
            callback = ISkinChangingCallback.DEFAULT_CHANGING_LISTENER;
        final ISkinChangingCallback skinChangingCallback = callback;

        checkPluginParamsThrow(skinPluginPath, pkgName);
       if (skinPluginPath.equals(mCurPluginPath) && pkgName.equals(mCurPluginPkg)) {
            if (TextUtils.isEmpty(mSuffix) && TextUtils.isEmpty(suffix)){
                return;
            }
            if (!TextUtils.isEmpty(mSuffix) && mSuffix.equals(suffix)){
                return;
            }

        }
        skinChangingCallback.onStart();
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... params) {
                try {
                    loadPlugin(skinPluginPath, pkgName, suffix);
                } catch (Exception e) {
                    e.printStackTrace();
                    return -1;
                }

                return 0;
            }

            @Override
            protected void onPostExecute(Integer result) {
                if (result == -1) {
                    skinChangingCallback.onError(null);
                }
                try {
                    updatePluginInfo(skinPluginPath, pkgName, suffix);
                    notifyChangedListeners();
                    skinChangingCallback.onComplete();
                } catch (Exception e) {
                    e.printStackTrace();
                    skinChangingCallback.onError(e);
                }

            }
        }.execute();
    }

    private void clearPluginInfo() {
        mCurPluginPath = null;
        mCurPluginPkg = null;
        mUsePlugin = false;
        mSuffix = null;
        mPrefUtil.clear();
    }

    private void updatePluginInfo(String skinPluginPath, String pkgName, String suffix) {
        mPrefUtil.putPluginPath(skinPluginPath);
        mPrefUtil.putPluginPkg(pkgName);
        mPrefUtil.putPluginSuffix(suffix);
        mCurPluginPkg = pkgName;
        mCurPluginPath = skinPluginPath;
        mSuffix = suffix;
    }

    public void notifyChangedListeners() {
        for (ISkinChangedListener listener : mSkinChangedListeners) {
            listener.onSkinChanged();
        }
    }

    public void apply(ISkinChangedListener listener) {
        List<SkinView> skinViews = getSkinViews(listener);

        if (skinViews == null) return;
        for (SkinView skinView : skinViews) {
            skinView.apply();
        }
    }

    public void addSkinView(ISkinChangedListener listener, List<SkinView> skinViews) {
        mSkinViewMaps.put(listener, skinViews);
    }

    public List<SkinView> getSkinViews(ISkinChangedListener listener) {
        return mSkinViewMaps.get(listener);
    }

    public void registerListener(ISkinChangedListener listener)
    {
        mSkinChangedListeners.add(listener);
    }

    public void unRegisterListener(ISkinChangedListener listener)
    {
        mSkinChangedListeners.remove(listener);
        mSkinViewMaps.remove(listener);
    }


}
