package com.dong.soufang.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.dong.soufang.R;
import com.dong.soufang.activity.base.BaseActivity;

/**
 * Description:
 * <p>
 * Author: dong
 * Date: 16/3/20
 */
public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initToolbar();

    }

    @Override
    protected void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
