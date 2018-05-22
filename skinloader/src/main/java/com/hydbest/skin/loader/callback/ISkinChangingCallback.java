package com.hydbest.skin.loader.callback;

/**
 * Created by csz on 2018/5/22.
 */

public interface ISkinChangingCallback {
    void onStart();

    void onComplete();

    void onError(Exception e);

    public static ISkinChangingCallback DEFAULT_CHANGING_LISTENER = new ISkinChangingCallback() {
        @Override
        public void onStart() {

        }

        @Override
        public void onComplete() {

        }

        @Override
        public void onError(Exception e) {

        }
    };
}
