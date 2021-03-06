package com.task.user;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // REALM
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .allowWritesOnUiThread(false)
                .allowQueriesOnUiThread(false)
                .build();
        Realm.setDefaultConfiguration(config);
    }

}
