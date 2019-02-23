
package com.example.administrator.xposeddemo.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;

import com.example.administrator.xposeddemo.bean.TimeBean;

import org.litepal.LitePal;

import java.util.Date;
import java.util.List;

public class LisetnerService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getAlarmTime();
        return START_REDELIVER_INTENT;
    }

    private void getAlarmTime() {

        List<TimeBean> timeBeans = LitePal.findAll(TimeBean.class);

        if (timeBeans.size() == 0) {
            return;
        }

        Date date = new Date();
        long time = date.getTime();


    }


    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
