package com.zhenghui.zhqb.zhenghuiqianbaomember.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_615025;

public class JewelRecordService extends Service {

    private SharedPreferences userInfoSp;
    private SharedPreferences appConfigSp;

    private String result;

    private Timer timer;
    private TimerTask task;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if(userInfoSp.getString("userId",null) != null){
                timer.cancel();
                getJewelRecord();
            }else {
                timer.cancel();
                startTime();
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("intent.getStringExtra(\"appname\")="+intent.getStringExtra("appname"));
        System.out.println("intent.getStringExtra(\"appurl\")="+intent.getStringExtra("appurl"));

        inits();

        getJewelRecord();
        return super.onStartCommand(intent, flags, startId);
    }

    private void inits() {
        userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        appConfigSp = getSharedPreferences("appConfig", Context.MODE_PRIVATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(null != timer){
            timer.cancel();
        }
    }

    private void startTime() {
        sendReceiver();
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                Message message = handler.obtainMessage();
                handler.sendMessage(message);
            }

        };

        timer.schedule(task, 10000);
    }

    private void sendReceiver(){
        //发送广播
        Intent intent=new Intent();
        intent.putExtra("result", result);
        intent.setAction("com.zhenghui.zhqb.zhenghuiqianbaomember.service.JewelRecordService");
        sendBroadcast(intent);
    }

    private void getJewelRecord() {

        JSONObject object = new JSONObject();
        try {
            object.put("userId", "");
            object.put("jewelCode", "");
            object.put("start", "1");
            object.put("limit", "10");
            object.put("orderDir", "");
            object.put("status","123");
            object.put("orderColumn", "");
            object.put("systemCode", appConfigSp.getString("systemCode", null));
            object.put("companyCode", appConfigSp.getString("systemCode", null));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_615025, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String c) {

                result = c;
                startTime();

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(getApplicationContext(), tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(getApplicationContext(), "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
