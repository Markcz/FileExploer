package com.example.mark.fileexploer.activity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.example.mark.fileexploer.R;

public class AboutActivity extends Activity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);


        webView = (WebView) findViewById(R.id.web_about);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/index.html");
    }

    @Override
    public void finish() {
        super.finish();
        //为当前activity添加结束动画
        overridePendingTransition(0,R.anim.out_alpha);
    }
}
