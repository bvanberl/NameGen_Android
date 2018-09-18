package com.vanberlo.blake.newname_android;

import android.app.Application;

import io.realm.Realm;

public class CustomApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }

}
