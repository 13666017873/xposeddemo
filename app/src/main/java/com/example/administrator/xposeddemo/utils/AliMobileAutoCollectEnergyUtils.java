package com.example.administrator.xposeddemo.utils;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

public class AliMobileAutoCollectEnergyUtils {
    private static String TAG = "AliMobileAutoCollectEnergyUtils";
    private static ArrayList<String> canCollectEnergyidList = new ArrayList();
    public static Object curH5Fragment;
    private static Object curH5PageImpl;
    private static String curUserid;
    private static ArrayList<String> friendsRankUseridList = new ArrayList();
    public static Activity h5Activity;
    private static boolean isWebViewRefresh;
    private static Integer pageCount = Integer.valueOf(0);
    private static String selfUserid;
    private static Integer totalEnergy = Integer.valueOf(0);

    public static void autoGetCanCollectUserIdList(final ClassLoader loader, String response) {
        if (isWebViewRefresh) {
            finishWork();
            return;
        }
        if (parseFrienRankPageDataResponse(response)) {
            showToast("开始获取可以收取能量的好友信息...");
            new Thread(new Runnable() {
                public void run() {
                    AliMobileAutoCollectEnergyUtils.rpcCall_FriendRankList(loader);
                }
            }) {
            }.start();
        } else {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("friendsRankUseridList ");
            stringBuilder.append(friendsRankUseridList);
            Log.i(str, stringBuilder.toString());
            if (friendsRankUseridList.size() > 0) {
                showToast("开始获取每个好友能够偷取的能量信息...");
                Iterator it = friendsRankUseridList.iterator();
                while (it.hasNext()) {
                    String userId = (String) it.next();
                    curUserid = userId;
                    rpcCall_CanCollectEnergy(loader, userId);
                    canCollectEnergyidList.clear();
                }
                Log.i(TAG, "collect energy finish refresh webview...");
            }
            refreshWebView();
        }
    }

    public static void autoGetCanCollectBubbleIdList(ClassLoader loader, String response) {
        if (!TextUtils.isEmpty(response) && response.contains("collectStatus")) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.optJSONArray("bubbles");
                String userName = jsonObject.getJSONObject("userEnergy").getString("displayName");
                if (jsonArray != null && jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        if ("AVAILABLE".equals(jsonObject1.optString("collectStatus"))) {
                            rpcCall_CollectEnergy(loader, jsonObject1.optString("userId"), Integer.valueOf(jsonObject1.optInt("id")), userName);
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    public static boolean isRankList(String response) {
        return !TextUtils.isEmpty(response) && response.contains("friendRanking");
    }

    public static boolean isUserDetail(String response) {
        return !TextUtils.isEmpty(response) && response.contains("userEnergy");
    }

    private static void refreshWebView() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("一共收取了");
        stringBuilder.append(totalEnergy);
        stringBuilder.append("g能量");
        showToast(stringBuilder.toString());
        isWebViewRefresh = true;
    }

    private static void finishWork() {
        isWebViewRefresh = false;
        String str = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("一共收取了");
        stringBuilder.append(totalEnergy);
        stringBuilder.append("g能量");
        Log.i(str, stringBuilder.toString());
    }

    private static boolean parseFrienRankPageDataResponse(String response) {
        try {
            JSONArray optJSONArray = new JSONObject(response).optJSONArray("friendRanking");
            int i = 0;
            if (optJSONArray == null || optJSONArray.length() == 0) {
                return false;
            }
            while (i < optJSONArray.length()) {
                JSONObject jsonObject = optJSONArray.getJSONObject(i);
                boolean optBoolean = jsonObject.optBoolean("canCollectEnergy");
                String userId = jsonObject.optString("userId");
                if (optBoolean && !friendsRankUseridList.contains(userId)) {
                    friendsRankUseridList.add(userId);
                }
                i++;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    private static void rpcCall_FriendRankList(ClassLoader loader) {
        try {
            Method rpcCallMethod = getRpcCallMethod(loader);
            JSONArray jsonArray = new JSONArray();
            JSONObject json = new JSONObject();
            json.put("av", "5");
            json.put("ct", "android");
            json.put("pageSize", pageCount.intValue() * 20);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("");
            stringBuilder.append((pageCount.intValue() * 20) + 1);
            json.put("startPoint", stringBuilder.toString());
            Integer num = pageCount;
            pageCount = Integer.valueOf(pageCount.intValue() + 1);
            jsonArray.put(json);
            String str = TAG;
            stringBuilder = new StringBuilder();
            stringBuilder.append("call friendranklist params:");
            stringBuilder.append(jsonArray);
            Log.i(str, stringBuilder.toString());
            rpcCallMethod.invoke(null, new Object[]{"alipay.antmember.forest.h5.queryEnergyRanking", jsonArray.toString(), "", Boolean.valueOf(true), null, null, Boolean.valueOf(false), curH5PageImpl, Integer.valueOf(0), "", Boolean.valueOf(false), Integer.valueOf(-1)});
        } catch (Exception e) {
            String str2 = TAG;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("rpcCall_FriendRankList err: ");
            stringBuilder2.append(Log.getStackTraceString(e));
            Log.i(str2, stringBuilder2.toString());
        }
    }

    private static void rpcCall_CanCollectEnergy(ClassLoader loader, String userId) {
        Exception e;
        try {
            Method rpcCallMethod = getRpcCallMethod(loader);
            JSONArray jsonArray = new JSONArray();
            JSONObject json = new JSONObject();
            json.put("av", "5");
            json.put("ct", "android");
            json.put("pageSize", 3);
            json.put("startIndex", 0);
            try {
                json.put("userId", userId);
                jsonArray.put(json);
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("call cancollect energy params:");
                stringBuilder.append(jsonArray);
                Log.i(str, stringBuilder.toString());
                rpcCallMethod.invoke(null, new Object[]{"alipay.antmember.forest.h5.queryNextAction", jsonArray.toString(), "", Boolean.valueOf(true), null, null, Boolean.valueOf(false), curH5PageImpl, Integer.valueOf(0), "", Boolean.valueOf(false), Integer.valueOf(-1)});
                rpcCallMethod.invoke(null, new Object[]{"alipay.antmember.forest.h5.pageQueryDynamics", jsonArray.toString(), "", Boolean.valueOf(true), null, null, Boolean.valueOf(false), curH5PageImpl, Integer.valueOf(0), "", Boolean.valueOf(false), Integer.valueOf(-1)});
            } catch (Exception e2) {
                e = e2;
            }
        } catch (Exception e3) {
            e = e3;
            String str2 = userId;
            String str3 = TAG;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("rpcCall_CanCollectEnergy err: ");
            stringBuilder2.append(Log.getStackTraceString(e));
            Log.i(str3, stringBuilder2.toString());
        }
    }

    public static void rpcCall_CollectEnergy(ClassLoader loader, String userId, Integer bubbleId, String userName) {
        try {
            Method rpcCallMethod = getRpcCallMethod(loader);
            JSONArray jsonArray = new JSONArray();
            JSONArray bubbleAry = new JSONArray();
            bubbleAry.put(bubbleId);
            JSONObject json = new JSONObject();
            json.put("av", "5");
            json.put("ct", "android");
            json.put("userId", userId);
            json.put("bubbleIds", bubbleAry);
            jsonArray.put(json);
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("call cancollect energy params:");
            stringBuilder.append(jsonArray);
            Log.i(str, stringBuilder.toString());
            Object resp = rpcCallMethod.invoke(null, new Object[]{"alipay.antmember.forest.h5.collectEnergy", jsonArray.toString(), "", Boolean.valueOf(true), null, null, Boolean.valueOf(false), curH5PageImpl, Integer.valueOf(0), "", Boolean.valueOf(false), Integer.valueOf(-1)});
            String str2;
            StringBuilder stringBuilder2;
            if (parseCollectEnergyResponse((String) resp.getClass().getMethod("getResponse", new Class[0]).invoke(resp, new Object[0]))) {
                str2 = TAG;
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append("collect energy userid:");
                stringBuilder2.append(userId);
                stringBuilder2.append(",bubbleId");
                stringBuilder2.append(bubbleId);
                stringBuilder2.append(",userName");
                stringBuilder2.append(userName);
                stringBuilder2.append("succ...");
                Log.i(str2, stringBuilder2.toString());
                return;
            }
            str2 = TAG;
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append("collect energy userid:");
            stringBuilder2.append(userId);
            stringBuilder2.append(",bubbleId");
            stringBuilder2.append(bubbleId);
            stringBuilder2.append(",userName");
            stringBuilder2.append(userName);
            stringBuilder2.append("fail...");
            Log.i(str2, stringBuilder2.toString());
        } catch (Exception e) {
            String str3 = TAG;
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("rpcCall_CanCollectEnergy err: ");
            stringBuilder3.append(Log.getStackTraceString(e));
            Log.i(str3, stringBuilder3.toString());
        }
    }

    public static Method getRpcCallMethod(ClassLoader loader) {
        try {
            Field aF = curH5Fragment.getClass().getDeclaredField("a");
            aF.setAccessible(true);
            Object viewHolder = aF.get(curH5Fragment);
            Field hF = viewHolder.getClass().getDeclaredField("h");
            hF.setAccessible(true);
            curH5PageImpl = hF.get(viewHolder);
            Class<?> h5PageClazz = loader.loadClass("com.alipay.mobile.h5container.api.H5Page");
            Class<?> jsonClazz = loader.loadClass("com.alibaba.fastjson.JSONObject");
            Class<?> rpcClazz = loader.loadClass("com.alipay.mobile.nebulabiz.rpc.H5RpcUtil");
            if (curH5PageImpl != null) {
                return rpcClazz.getMethod("rpcCall", new Class[]{String.class, String.class, String.class, Boolean.TYPE, jsonClazz, String.class, Boolean.TYPE, h5PageClazz, Integer.TYPE, String.class, Boolean.TYPE, Integer.TYPE});
            }
        } catch (Exception e) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("getRpcCallMethod err: ");
            stringBuilder.append(Log.getStackTraceString(e));
            Log.i(str, stringBuilder.toString());
        }
        return null;
    }

    public static boolean parseCollectEnergyResponse(String response) {
        if (!TextUtils.isEmpty(response) && response.contains("failedBubbleIds")) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.optJSONArray("bubbles");
                for (int i = 0; i < jsonArray.length(); i++) {
                    totalEnergy = Integer.valueOf(totalEnergy.intValue() + jsonArray.getJSONObject(i).optInt("collectedEnergy"));
                }
                if ("SUCCESS".equals(jsonObject.optString("resultCode"))) {
                    return true;
                }
            } catch (Exception e) {
            }
        }
        return false;
    }

    private static void showToast(final String str) {
        if (h5Activity != null) {
            try {
                h5Activity.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(AliMobileAutoCollectEnergyUtils.h5Activity, str, Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                String str2 = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("showToast err: ");
                stringBuilder.append(Log.getStackTraceString(e));
                Log.i(str2, stringBuilder.toString());
            }
        }
    }
}
