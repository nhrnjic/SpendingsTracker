package com.example.nihadhrnjic.spendingstracker;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by nihadhrnjic on 6/23/17.
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
