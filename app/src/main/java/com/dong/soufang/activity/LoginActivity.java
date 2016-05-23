package com.dong.soufang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dong.soufang.GlobalData;
import com.dong.soufang.R;
import com.dong.soufang.activity.base.BaseActivity;
import com.dong.soufang.http.BaseResult;
import com.dong.soufang.http.Callback;
import com.dong.soufang.http.HttpApi;
import com.dong.soufang.http.RequestBean;

import java.util.HashMap;

/**
 * Description: 登陆页面
 * <p/>
 * Author: dong
 * Date: 5/13/16
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private EditText etMobile, etPassword;
    private TextView tvRegister, tvFindPwd;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initToolbar();

        etMobile = (EditText) findViewById(R.id.et_mobile);
        etPassword = (EditText) findViewById(R.id.et_password);
        tvRegister = (TextView) findViewById(R.id.tv_to_register);
        tvRegister.setOnClickListener(this);
        tvFindPwd = (TextView) findViewById(R.id.tv_find_password);
        tvFindPwd.setOnClickListener(this);
        loginBtn = (Button) findViewById(R.id.btn_login);
        loginBtn.setOnClickListener(this);
    }

    @Override
    protected void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_to_register:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            case R.id.tv_find_password:
                showToast("待实现");
                break;
            case R.id.btn_login:
                String mobile = etMobile.getText().toString();
                String password = etPassword.getText().toString();
                HashMap<String, Object> params = new HashMap<>();
                params.put("mobile", mobile);
                params.put("password", password);
                RequestBean requestBean = new RequestBean(TAG, HttpApi.PostLoginApi, params);
                httpHandler.userLogin(requestBean, new Callback() {
                    @Override
                    public void onSuccess(BaseResult result) {
                        GlobalData.isLogin = true;
                    }

                    @Override
                    public void onFailure(String msg) {
                        showToast(msg);
                    }
                });
                break;
        }
    }

    private void getUserData() {
        HashMap<String, Object> params = new HashMap<>();
        RequestBean requestBean = new RequestBean(TAG, HttpApi.GetPersonalInfoApi, params);
        httpHandler.getPersonalInfo(requestBean, new Callback() {
            @Override
            public void onSuccess(BaseResult result) {

            }

            @Override
            public void onFailure(String msg) {
                showToast(msg);
            }
        });
    }
}
