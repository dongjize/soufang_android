package com.dong.soufang.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.dong.soufang.R;
import com.dong.soufang.activity.base.BaseActivity;

/**
 * Description: 我的账户
 * <p/>
 * Author: dong
 * Date: 16/3/21
 */
public class MyAccountActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        initToolbar();
    }

    @Override
    protected void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
