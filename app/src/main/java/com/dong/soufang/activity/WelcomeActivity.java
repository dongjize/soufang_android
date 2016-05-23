package com.dong.soufang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.dong.soufang.R;

/**
 * Description: 欢迎页面
 * <p>
 * Author: dong
 * Date: 16/3/14
 */
public class WelcomeActivity extends AppCompatActivity {
    private static final int GO_HOME = 1;
    private static final int GO_LOGIN = 2;
    private static final int POST_DELAYED = 2000;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent;
            switch (msg.what) {
                case GO_HOME:
                    intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
                case GO_LOGIN:
//                    intent = new Intent(WelcomeActivity.this, LoginActivity.class);
//                    startActivity(intent);
                    break;
            }
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        handler.sendEmptyMessageDelayed(GO_HOME, POST_DELAYED);

    }

}
