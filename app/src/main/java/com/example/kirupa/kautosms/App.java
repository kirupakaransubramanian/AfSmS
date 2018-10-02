package com.example.kirupa.kautosms;

import android.app.Application;
import android.content.Context;

public class App extends Application {

    private static Context mContext;

    public void onCreate() {
        super.onCreate();
        App.mContext = getApplicationContext();
    }

    public static Context getAppContext() {
        return App.mContext;
    }
}