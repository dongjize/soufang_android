package com.dong.soufang.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dong.soufang.R;
import com.dong.soufang.activity.base.BaseActivity;
import com.dong.soufang.http.BaseResult;
import com.dong.soufang.http.Callback;
import com.dong.soufang.http.HttpApi;
import com.dong.soufang.http.RequestBean;

import java.util.HashMap;

/**
 * Description: 用户注册页面
 * <p/>
 * Author: dong
 * Date: 5/16/16
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button registerBtn, verifyCodeBtn;
    private EditText etMobile, etVerifyCode, etPassword;
    private String mobile, verifyCode, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etMobile = (EditText) findViewById(R.id.et_mobile);
        etVerifyCode = (EditText) findViewById(R.id.et_verify_code);
        etPassword = (EditText) findViewById(R.id.et_password);
        registerBtn = (Button) findViewById(R.id.btn_register);
        verifyCodeBtn = (Button) findViewById(R.id.btn_verify_code);
        registerBtn.setOnClickListener(this);
        verifyCodeBtn.setOnClickListener(this);
    }

    @Override
    protected void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void userRegister() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("password", password);
        RequestBean requestBean = new RequestBean(TAG, HttpApi.PostRegisterApi, params);
        httpHandler.userRegister(requestBean, new Callback() {
            @Override
            public void onSuccess(BaseResult result) {
                result.getData();
            }

            @Override
            public void onFailure(String msg) {
                showToast(msg);
            }
        });
    }

    private void getVerifyCode() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_verify_code:
                break;
            case R.id.btn_register:
                mobile = etMobile.getText().toString();
                password = etPassword.getText().toString();

                break;
        }
    }
}
