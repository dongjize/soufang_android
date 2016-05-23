package com.dong.soufang.activity.map;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.dong.soufang.R;
import com.dong.soufang.activity.base.BaseActivity;

/**
 * Description:
 * <p/>
 * Author: dong
 * Date: 16/4/10
 */
public class OldHousesListMapActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
