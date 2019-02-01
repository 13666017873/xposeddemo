package com.example.administrator.xposeddemo.hook;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.example.administrator.xposeddemo.utils.AliMobileAutoCollectEnergyUtils;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class XposedHook implements IXposedHookLoadPackage {

    private static String TAG = "XposedHookPyj";
    private static boolean first = false;

    private class ApplicationAttachMethodHook extends XC_MethodHook {
        private ApplicationAttachMethodHook() {
        }

        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            super.afterHookedMethod(param);
            if (!XposedHook.first) {
                final ClassLoader loader = ((Context) param.args[0]).getClassLoader();
                Class clazz = loader.loadClass("com.alipay.mobile.nebulacore.ui.H5FragmentManager");
                if (!(clazz == null || loader.loadClass("com.alipay.mobile.nebulacore.ui.H5Fragment") == null)) {
                    XposedHelpers.findAndHookMethod(clazz, "pushFragment", new Object[]{loader.loadClass("com.alipay.mobile.nebulacore.ui.H5Fragment"), Boolean.TYPE, Bundle.class, Boolean.TYPE, Boolean.TYPE, new XC_MethodHook() {
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("cur fragment: ");
                            stringBuilder.append(param.args[0]);
                            Log.i("fragment", stringBuilder.toString());
                            AliMobileAutoCollectEnergyUtils.curH5Fragment = param.args[0];
                        }
                    }});
                }
                clazz = loader.loadClass("com.alipay.mobile.nebulacore.ui.H5Activity");
                if (clazz != null) {
                    XposedHelpers.findAndHookMethod(clazz, "onResume", new Object[]{new XC_MethodHook() {
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            AliMobileAutoCollectEnergyUtils.h5Activity = (Activity) param.thisObject;
                        }
                    }});
                }
                clazz = loader.loadClass("com.alipay.mobile.nebulabiz.rpc.H5RpcUtil");
                if (clazz != null) {
                    XposedHook.first = true;
                    Log.i(XposedHook.TAG, "first");
                    Class<?> h5PageClazz = loader.loadClass("com.alipay.mobile.h5container.api.H5Page");
                    Class<?> jsonClazz = loader.loadClass("com.alibaba.fastjson.JSONObject");
                    if (!(h5PageClazz == null || jsonClazz == null)) {
                        XposedHelpers.findAndHookMethod(clazz, "rpcCall", new Object[]{String.class, String.class, String.class, Boolean.TYPE, jsonClazz, String.class, Boolean.TYPE, h5PageClazz, Integer.TYPE, String.class, Boolean.TYPE, Integer.TYPE, new XC_MethodHook() {
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                super.beforeHookedMethod(param);
                            }

                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                super.afterHookedMethod(param);
                                Object resp = param.getResult();
                                if (resp != null) {
                                    String response = (String) resp.getClass().getMethod("getResponse", new Class[0]).invoke(resp, new Object[0]);
                                    String access$200 = XposedHook.TAG;
                                    StringBuilder stringBuilder = new StringBuilder();
                                    stringBuilder.append("response: ");
                                    stringBuilder.append(response);
                                    Log.i(access$200, stringBuilder.toString());
                                    if (AliMobileAutoCollectEnergyUtils.isRankList(response)) {
                                        Log.i(XposedHook.TAG, "autoGetCanCollectUserIdList");
                                        AliMobileAutoCollectEnergyUtils.autoGetCanCollectUserIdList(loader, response);
                                    }
                                    if (AliMobileAutoCollectEnergyUtils.isUserDetail(response)) {
                                        Log.i(XposedHook.TAG, "autoGetCanCollectBubbleIdList");
                                        AliMobileAutoCollectEnergyUtils.autoGetCanCollectBubbleIdList(loader, response);
                                    }
                                }
                            }
                        }});
                    }
                }
            }
        }
    }

    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        Log.i(TAG, lpparam.packageName);
        if ("com.eg.android.AlipayGphone".equals(lpparam.packageName)) {
            hookSecurity(lpparam);
            hookRpcCall(lpparam);
        }
    }

    private void hookSecurity(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            Class loadClass = lpparam.classLoader.loadClass("android.util.Base64");
            if (loadClass != null) {
                XposedHelpers.findAndHookMethod(loadClass, "decode", new Object[]{String.class, Integer.TYPE, new XC_MethodHook() {
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                    }
                }});
            }
            loadClass = lpparam.classLoader.loadClass("android.app.Dialog");
            if (loadClass != null) {
                XposedHelpers.findAndHookMethod(loadClass, "show", new Object[]{new XC_MethodHook() {
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        try {
                            throw new NullPointerException();
                        } catch (Exception e) {
                            Log.i("Tag", e.toString());
                        }
                    }
                }});
            }
            loadClass = lpparam.classLoader.loadClass("com.alipay.mobile.base.security.CI");
            if (loadClass != null) {
                XposedHelpers.findAndHookMethod(loadClass, "a", new Object[]{loadClass, Activity.class, new XC_MethodReplacement() {
                    protected Object replaceHookedMethod(XC_MethodHook.MethodHookParam param) {
                        return null;
                    }
                }});
                XposedHelpers.findAndHookMethod(loadClass, "a", new Object[]{String.class, String.class, String.class, new XC_MethodHook() {
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        param.setResult(null);
                    }
                }});
            }
        } catch (Throwable e) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("hookSecurity err:");
            stringBuilder.append(Log.getStackTraceString(e));
            Log.i(str, stringBuilder.toString());
        }
    }

    private void hookRpcCall(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            XposedHelpers.findAndHookMethod(Application.class, "attach", new Object[]{Context.class, new ApplicationAttachMethodHook()});
        } catch (Exception e2) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("hookRpcCall err:");
            stringBuilder.append(Log.getStackTraceString(e2));
            Log.i(str, stringBuilder.toString());
        }
    }
}