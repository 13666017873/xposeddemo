package com.example.administrator.xposeddemo.utils;

import android.app.Activity;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

public class MyWebViewClient extends WebViewClient {
    String url;
    WebView view;
    Activity activity;
    TextView textView;

    public MyWebViewClient(Activity activity) {
        //  this.textView =textView;
        this.activity = activity;
    }

    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }

    public void onPageFinished(WebView view, String url) {
        CookieManager cookieManager = CookieManager.getInstance();
        String CookieStr = cookieManager.getCookie(url);
        Toast.makeText(activity, CookieStr, Toast.LENGTH_LONG).show();

        Log.e("dfdf", CookieStr);
        super.onPageFinished(view, url);
    }
}

