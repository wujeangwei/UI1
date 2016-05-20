package com.unisound.unicar.gui.application;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.unisound.unicar.gui.preference.UserPerferenceUtil;
import com.unisound.unicar.gui.utils.LogcatHelper;

public class CrashApplication extends Application {
    public static LogcatHelper mLogcatHelper = null;

    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(this);
        CrashHandler crashHandler = CrashHandler.getInstance();
        // 指定Crash时的处理程序
        crashHandler.setCrashHanler(getApplicationContext());

        mLogcatHelper = LogcatHelper.getInstance(this);
        if (UserPerferenceUtil.getLogcatEnable(this)) {
            if (!mLogcatHelper.isRunning()) {
                mLogcatHelper.start();
            }
        }
    }
}
