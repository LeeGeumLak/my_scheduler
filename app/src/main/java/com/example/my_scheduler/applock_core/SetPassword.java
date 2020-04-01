package com.example.my_scheduler.applock_core;

import android.app.Application;

public class SetPassword extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LockManager.getInstance().enableAppLock(this);
    }
}


