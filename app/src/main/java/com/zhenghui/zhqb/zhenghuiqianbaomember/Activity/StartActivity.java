package com.zhenghui.zhqb.zhenghuiqianbaomember.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.zhenghui.zhqb.zhenghuiqianbaomember.application.MyApplication;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class StartActivity extends Activity {

    @InjectView(R.id.txt_pass)
    TextView txtPass;

    int time = 3500;

    private Timer timer;
    private TimerTask task;

    private SharedPreferences userInfoSp;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.arg1 == 1) {
                startApp();
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.inject(this);
        MyApplication.getInstance().addActivity(this);

        try{

        }catch (Exception e){
            e.printStackTrace();
        }

        // 竖屏显示
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        inits();
        startTime();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    private void inits() {
        userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
    }

    @OnClick(R.id.txt_pass)
    public void onClick() {

    }

    private void startTime() {
        timer = new Timer();
        task = new TimerTask() {

            @Override
            public void run() {
                Message message = handler.obtainMessage();
                message.arg1 = 1;
                handler.sendMessage(message);
            }
        };
        timer.schedule(task, time);
    }

    private void stopTime() {
        timer.cancel();
    }

    private void startApp() {
//        if (userInfoSp.getString("userId", null) != null) {
            startActivity(new Intent(StartActivity.this, MainActivity.class));
//        } else {
//            startActivity(new Intent(StartActivity.this, LoginActivity.class));
//        }
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        stopTime();
        finish();
    }


}
