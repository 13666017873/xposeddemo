package com.example.administrator.xposeddemo.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.administrator.xposeddemo.R;
import com.example.administrator.xposeddemo.utils.MyWebViewClient;

public class AlipayLoginActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alipay_login);

        this.initView();
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    private void initView() {

        WebView webView = findViewById(R.id.activity_alipay_login_web);
        webView.loadUrl("https://authsu18.alipay.com/login/index.htm");
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                view.requestFocus();
            }
        });

        webView.setWebViewClient(new MyWebViewClient(AlipayLoginActivity.this));


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.evaluateJavascript("javascript:getInitData()", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {

                    Log.e("valuejs", value);
                    //此处为 js 返回的结果
                }
            });
        }
    }
}
