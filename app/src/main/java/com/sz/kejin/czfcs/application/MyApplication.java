package com.sz.kejin.czfcs.application;

import android.app.Application;
import android.content.Context;


import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
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

        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }
}
