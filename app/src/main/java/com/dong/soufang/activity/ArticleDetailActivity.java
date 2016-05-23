package com.dong.soufang.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.dong.soufang.R;
import com.dong.soufang.activity.base.BaseWebViewActivity;
import com.dong.soufang.http.BaseResult;
import com.dong.soufang.http.Callback;
import com.dong.soufang.http.HttpApi;
import com.dong.soufang.http.RequestBean;

import java.util.HashMap;

/**
 * Description: 资讯详情网页
 * <p>
 * Author: dong
 * Date: 16/3/17
 */
public class ArticleDetailActivity extends BaseWebViewActivity {
    private static final String TAG = ArticleDetailActivity.class.getSimpleName();
    private String url;
    private int newsId;
    private boolean isCollected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolbar();
        Bundle bundle = getIntent().getExtras();
        newsId = bundle.getInt("id");
        isCollected = bundle.getBoolean("is_collected");
        url = HttpApi.GetArticlesListApi + newsId;
        loadData(url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_heart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_collect:
                if (isCollected) {
                    item.setIcon(R.mipmap.heart_white);
                } else {
                    item.setIcon(R.mipmap.heart_red);
                }
                isCollected = !isCollected;
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void collectArticle() {
        HashMap<String, Object> params = new HashMap<>();
        RequestBean requestBean = new RequestBean(TAG, HttpApi.GetCollectArticleApi, params);
        httpHandler.collectArticle(requestBean, new Callback() {
            @Override
            public void onSuccess(BaseResult result) {
                isCollected = true;

            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }

    @Override
    protected void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
