package com.dong.soufang.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.dong.soufang.R;
import com.dong.soufang.activity.base.BaseActivity;
import com.dong.soufang.http.HttpApi;
import com.dong.soufang.util.ImageLoaderUtils;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * Description:
 * <p/>
 * Author: dong
 * Date: 7/25/16
 */
public class ArticleDetailActivityV2 extends BaseActivity {
    private String url;
    private int newsId;
    private boolean isCollected;
    private WebView webView;

    private ImageView ivTitle;
    private String avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list2);

        webView = (WebView) findViewById(R.id.web_view);
        ivTitle = (ImageView) findViewById(R.id.iv_title);

        initToolbar();
        Bundle bundle = getIntent().getExtras();
        newsId = bundle.getInt("id");
        isCollected = bundle.getBoolean("is_collected");
        avatar = bundle.getString("avatar");
        url = HttpApi.GetArticlesListApi + "/" + newsId;

        ImageLoaderUtils.display(HttpApi.IMAGE_URL + avatar, ivTitle);

        loadData(url);
    }

    @Override
    protected void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
