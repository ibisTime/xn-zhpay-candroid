package com.zhenghui.zhqb.zhenghuiqianbaomember.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_805158;

public class MyService extends Service {

    private SharedPreferences userInfoSp;
    private AMapLocationClient mLocationClient;

    private String latitude = "";
    private String longitude = "";

    private Timer timer;
    private TimerTask task;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if(userInfoSp.getString("userId",null) != null){
                timer.cancel();
                initLocation();
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
        inits();
        startTime();
        return super.onStartCommand(intent, flags, startId);
    }

    private void inits() {
        userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void startTime() {
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                Message message = handler.obtainMessage();
                handler.sendMessage(message);
            }

        };

        timer.schedule(task, 60000);
    }

    private void initLocation() {
        mLocationClient = new AMapLocationClient(getApplicationContext());
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        option.setOnceLocation(true);
        mLocationClient.setLocationOption(option);
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {

                        latitude = aMapLocation.getLatitude()+"";
                        longitude = aMapLocation.getLongitude()+"";

                        String city = aMapLocation.getCity();
                        String district = aMapLocation.getDistrict();
                        String province = aMapLocation.getProvince();

                        SharedPreferences.Editor editor = userInfoSp.edit();
                        editor.putString("province",province);
                        editor.putString("city",city);
                        editor.putString("district",district);
                        editor.putString("latitude",latitude);
                        editor.putString("longitude",longitude);
                        editor.commit();

                        postLocation();
                    } else {
                        //定位失败
                        Log.i("Service_location","Locate---->FAILED");

                    }
                }
            }
        });
        mLocationClient.startLocation();
    }

    private void postLocation(){
        System.out.println("postLocation()");
        JSONObject object = new JSONObject();
        try {
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("token", userInfoSp.getString("token", null));
            object.put("latitude", latitude);
            object.put("longitude", longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_805158, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                startTime();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(getApplicationContext(), tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
//                Toast.makeText(getApplicationContext(), "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
