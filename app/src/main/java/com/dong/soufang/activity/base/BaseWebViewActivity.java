package com.dong.soufang.activity.base;

import android.os.Bundle;

import com.dong.soufang.R;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

public abstract class BaseWebViewActivity extends BaseActivity {
    protected WebView webView;
    protected String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_web_view);

        webView = (WebView) findViewById(R.id.web_view);
//        url = getStringExtra("url");
//        loadData(url);

    }

    protected void loadData(String url) {
        if (webView != null) {
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView webView, String s) {
                    super.onPageFinished(webView, s);
                    //TODO
                }
            });
            webSettings.setDomStorageEnabled(true);
            webSettings.setPluginState(WebSettings.PluginState.ON);
            loadUrl(url);
        }
    }

    private void loadUrl(String url) {
        if (webView != null) {
            webView.loadUrl(url);
        }
    }

}
