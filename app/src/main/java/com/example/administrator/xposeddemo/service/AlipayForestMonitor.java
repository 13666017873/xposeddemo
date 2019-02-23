package com.example.administrator.xposeddemo.service;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.example.administrator.xposeddemo.utils.Config;

import java.util.List;

public class AlipayForestMonitor {


    /**
     * 启动支付宝界面
     * adb shell am start com.eg.android.AlipayGphone/com.eg.android.AlipayGphone.AlipayLogin
     */
    public static void startAlipay(Context mContext) {
        Intent intent = new Intent();
        intent.setPackage("com.eg.android.AlipayGphone");
        intent.setClassName("com.eg.android.AlipayGphone", "com.eg.android.AlipayGphone.AlipayLogin");
        mContext.startActivity(intent);
    }

    /**
     * 自动点击进入蚂蚁森林界面
     */
    public static void enterForestUI(AccessibilityNodeInfo nodeInfo) {
        Log.d(Config.TAG, "enterForestUI ");
        if (nodeInfo != null) {
            // 找到界面中蚂蚁森林的文字
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("蚂蚁森林");

            if (list == null) {
                Log.d(Config.TAG, "enterForestUI finding no");
                return;
            } else {
                Log.d(Config.TAG, "enterForestUI finding yes");
            }

            for (AccessibilityNodeInfo item : list) {
                /**
                 *  蚂蚁森林本身不可点击，但是他的父控件可以点击
                 */
                AccessibilityNodeInfo parent = item.getParent();
                if (null != parent && parent.isClickable()) {
                    parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    Log.d(Config.TAG, "item = " + item.toString() + ", parent click = " + parent.toString());
                    break;
                }
            }
        }
    }
}
