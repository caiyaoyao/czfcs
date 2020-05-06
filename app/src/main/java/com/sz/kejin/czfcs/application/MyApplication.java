package com.sz.kejin.czfcs.application;

import android.app.Application;
import android.content.Context;


import com.facebook.drawee.backends.pipeline.Fresco;
import com.sz.kejin.czfcs.crash.AppUncaughtExceptionHandler;

import androidx.multidex.MultiDex;


public class MyApplication extends Application {
    private static final String TAG = "MyApplication";
    public  static  MyApplication app;


    public static MyApplication getInstance() {
        return  app;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        // 捕捉异常

        AppUncaughtExceptionHandler.getInstance().init(this);
        Fresco.initialize(this);

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }
}
