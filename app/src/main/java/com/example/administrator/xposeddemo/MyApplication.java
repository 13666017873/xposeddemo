package com.example.administrator.xposeddemo;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.example.administrator.xposeddemo.service.AccessibilityServiceMonitor;
import com.example.administrator.xposeddemo.utils.AlarmTaskUtil;

import org.litepal.LitePal;

public class MyApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this.getApplicationContext();
        LitePal.initialize(this);
    }

    public static void startAlarmTask(Context mContext, int hour, int minu) {
        Intent intent = new Intent(mContext, AccessibilityServiceMonitor.class);
        intent.setAction(AccessibilityServiceMonitor.ACTION_ALAM_TIMER);
        intent.putExtra("hour", hour);
        intent.putExtra("minu", minu);
        AlarmTaskUtil.starRepeatAlarmTaskByService(mContext, hour, minu, 0, intent);
    }


    /**
     * 获取Application的context对象
     *
     * @return
     */
    public static Context getContext() {
        return mContext;
    }
}
