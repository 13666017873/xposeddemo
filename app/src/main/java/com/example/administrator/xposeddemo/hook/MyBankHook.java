package com.example.administrator.xposeddemo.hook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class MyBankHook {

    public void hook(final ClassLoader classLoader, final Context context) {

//        try {
//            Class<?> classLoader1 = classLoader.loadClass("com.mybank.android.phone.bill.ui.BillListActivity");
//
//
//            for(Method s : classLoader1.getMethods()){
//                Log.e("dfdf", s.getName());
//                Log.e("dfdf", s);
//            }
//
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }

        XposedBridge.log("hook=====" + classLoader.toString());

        try {
            Class<?> insertTradeMessageInfo = XposedHelpers.findClass("com.mybank.android.phone.customer.setting.ui.AboutActivity", classLoader);
            XposedBridge.hookAllMethods(insertTradeMessageInfo, "onCreate", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    try {
                        XposedBridge.log("beforeHookedMethod=================" + param.method.getName());
                    } catch (Exception e) {

                    }
                    super.beforeHookedMethod(param);
                }
            });
        } catch (Exception e) {
            XposedBridge.log(e.toString());
        }

//        XposedHelpers.findAndHookMethod("com.mybank.android.phone.bill.ui.BillListActivity", classLoader, "getItemView", new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) {
//                XposedBridge.log("toBillItems=========messageInfo========" + param.getResult());
//            }
//        });

        try {
            Class<?> insertTradeMessageInfo = XposedHelpers.findClass("com.mybank.android.phone.bill.ui.BillListActivity", classLoader);

//            ClassLoader classLoader1 = insertTradeMessageInfo.getClassLoader();
//            classLoader1

            XposedBridge.hookAllMethods(insertTradeMessageInfo, "onCreate", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    try {

//                        ClassLoader appClassLoader = context1.getClassLoader();

//                        Object object = param.args[0];
//                        String messageInfo = (String) XposedHelpers.callMethod(object, "toString");
//
//                        String content = getTextCenter(messageInfo, "param=", "'");
                        XposedBridge.log("getItemView=========messageInfo========" + param.args[0]);
//                        XposedBridge.log("toBillItems=========getName========" + param.method.getName());
//                        XposedBridge.log("toBillItems=========getModifiers========" + param.method.getModifiers());
                        XposedBridge.log("getItemView=========getResult========" + param.getResult());
//                        XposedBridge.log("toBillItems=========length========" + param.args.length);
                    } catch (Exception e) {
                    }
                    super.beforeHookedMethod(param);
                }
            });
        } catch (Exception e) {
            XposedBridge.log(e.toString());
        }
    }

    public String getTextCenter(String text,String begin,String end){
        try {
            int b = text.indexOf(begin)+begin.length();
            int e = text.indexOf(end,b);
            return text.substring(b,e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "error";
        }
    }

}
