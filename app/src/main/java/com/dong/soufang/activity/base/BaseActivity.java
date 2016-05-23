package com.dong.soufang.activity.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.dong.soufang.MainApplication;
import com.dong.soufang.http.HttpHandler;
import com.dong.soufang.http.RequestUtils;

/**
 * Description: Activity父类
 * <p>
 * Author: dong
 * Date: 16/3/14
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected Context _context;
    public HttpHandler httpHandler;
    public RequestUtils requestUtils;
    protected Toolbar toolbar;
    protected ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.getInstance().addActivity(this);
        _context = this;
        requestUtils = new RequestUtils();
        httpHandler = new HttpHandler(this);

        setProgressDialog();
    }

    protected abstract void initToolbar();

    protected void setProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("正在加载...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
    }

    protected void showProgressDialog() {
        if (progressDialog != null) {
            progressDialog.show();
        }
    }

    protected void showProgressDialog(String msg) {
        if (progressDialog != null) {
            progressDialog.setMessage(msg);
            progressDialog.show();
        }
    }

    protected String getStringExtra(String key) {
        Intent intent = getIntent();
        String value = intent.getStringExtra(key);
        if (value == null) {
            String scheme = intent.getScheme();
            Uri uri = intent.getData();
            if (scheme != null && uri != null) {
                value = uri.getQueryParameter(key);
            }
        }
        return value;
    }

    protected int getIntExtra(String key, int defaultValue) {
        Intent intent = getIntent();
        int value = intent.getIntExtra(key, defaultValue);
        if (value == defaultValue) {
            String strValue = getStringExtra(key);
            try {
                if (strValue != null) value = Integer.parseInt(strValue);
            } catch (Exception e) {
                value = defaultValue;
            }
        }
        return value;
    }

    protected boolean getBooleanExtra(String key, boolean defaultValue) {
        Intent intent = getIntent();
        boolean value = intent.getBooleanExtra(key, defaultValue);
        if (value == defaultValue) {
            String strValue = getStringExtra(key);
            if (strValue != null) {
                if (strValue.equals("true")) {
                    value = true;
                } else {
                    value = false;
                }
            }
        }
        return value;
    }

    protected void showToast(String msg) {
        Toast.makeText(_context, msg, Toast.LENGTH_SHORT).show();
    }

    protected void showLongToast(String msg) {
        Toast.makeText(_context, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    protected void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
