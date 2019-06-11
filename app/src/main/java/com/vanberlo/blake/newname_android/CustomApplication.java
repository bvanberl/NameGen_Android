package com.vanberlo.blake.newname_android;

import android.app.Application;

import io.realm.Realm;

/**
 * Custom Application class for Realm initialization
 */
public class CustomApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }

}
