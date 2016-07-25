package com.dong.soufang.activity.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toolbar;

/**
 * Description:
 * <p>
 * Author: dong
 * Date: 7/25/16
 */
public class ToolbarActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    class ToolbarOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

            }
        }
    }

    protected void onTitleBarBackClick() {
        finish();
    }

    protected void onTitleBarRightBtnClick() {

    }
}
