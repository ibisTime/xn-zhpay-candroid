package com.zhenghui.zhqb.zhenghuiqianbaomember.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Activity.MainActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Activity.ShakeListActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.ShakeModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.LoginUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.RootUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by dell1 on 2016/12/12.
 */

public class ShakeFragment extends Fragment implements SensorEventListener {


    private static final int START_SHAKE = 0x1;
    private static final int AGAIN_SHAKE = 0x2;
    private static final int END_SHAKE = 0x3;

    @InjectView(R.id.img_up)
    ImageView imgUp;
    @InjectView(R.id.img_down)
    ImageView imgDown;
    @InjectView(R.id.layout_shake)
    LinearLayout layoutShake;
    @InjectView(R.id.layout_no)
    LinearLayout layoutNo;
    @InjectView(R.id.img_bg)
    ImageView imgBg;

    private String latitude = "";
    private String longitude = "";

    private SharedPreferences userInfoSp;
    private AMapLocationClient mLocationClient;

    private List<ShakeModel> list;

    // Fragment主视图
    private View view;

    // 传感器Manager
    private SensorManager mSensorManager;
    // 加速度传感器
    private Sensor mAccelerometerSensor;
    //手机震动
    private Vibrator mVibrator;
    //记录摇动状态
    private boolean isShake = false;

    private MyHandler mHandler;

    private MainActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_shake, null);
        ButterKnife.inject(this, view);

        inits();

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        //获取 SensorManager 负责管理传感器
        mSensorManager = ((SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE));
        if (mSensorManager != null) {
            //获取加速度传感器
            mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (mAccelerometerSensor != null) {
                mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_UI);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        // 务必要在pause中注销 mSensorManager
        // 否则会造成界面退出后摇一摇依旧生效的bug
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    private void inits() {
        activity = (MainActivity) getActivity();
        mHandler = new MyHandler(this);

        list = new ArrayList<>();
        userInfoSp = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        //获取Vibrator震动服务
        mVibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
    }

    private void initLocation() {
        mLocationClient = new AMapLocationClient(getActivity());
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        option.setOnceLocation(true);
        mLocationClient.setLocationOption(option);
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {

                        latitude = aMapLocation.getLatitude() + "";
                        longitude = aMapLocation.getLongitude() + "";

                        postLocation();
                    } else {
                        //定位失败
                        Log.i("ShakeFragment", "Locate---->FAILED");
                        Log.i("ShakeFragment", "aMapLocation.getErrorCode()---->" + aMapLocation.getErrorCode());

                        Toast.makeText(getActivity(), "定位失败", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
        mLocationClient.startLocation();
    }

    private void postLocation() {

        TelephonyManager tm = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        String DEVICE_ID = tm.getDeviceId();

        if (DEVICE_ID == null) {
            DEVICE_ID = "error_noDeviceId";
        }

        JSONObject object = new JSONObject();
        try {
            object.put("token", userInfoSp.getString("token", null));
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("deviceNo", DEVICE_ID);
            object.put("latitude", latitude);
            object.put("longitude", longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808457", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                if (result.equals("indexOutOf")) {
                    layoutNo.setVisibility(View.VISIBLE);

                } else {
                    try {
                        JSONArray jsonArray = new JSONArray(result);

                        Gson gson = new Gson();
                        list = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<ShakeModel>>() {
                        }.getType());

                        if (list.size() == 0) {
                            Toast.makeText(getActivity(), "您的附近没有汇赚宝哦，换个位置再试试吧", Toast.LENGTH_LONG).show();
                        } else {
                            startActivity(new Intent(getActivity(), ShakeListActivity.class).putExtra("json", result));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(getActivity(), tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(getActivity(), "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick({R.id.layout_no})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_no:
                layoutNo.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        int type = event.sensor.getType();

        if (activity.isShake) {
            if (RootUtil.isDeviceRooted()) {
                Toast.makeText(activity, "您的设备已root,不能参加摇一摇", Toast.LENGTH_SHORT).show();
            } else {

                if (type == Sensor.TYPE_ACCELEROMETER) {
                    //获取三个方向值
                    float[] values = event.values;
                    float x = values[0];
                    float y = values[1];
                    float z = values[2];

                    if ((Math.abs(x) > 17 || Math.abs(y) > 17 || Math
                            .abs(z) > 17) && !isShake) {
                        isShake = true;
                        Thread thread = new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                try {

                                    //开始震动 发出提示音 展示动画效果
                                    mHandler.obtainMessage(START_SHAKE).sendToTarget();
                                    Thread.sleep(500);
                                    //再来一次震动提示
                                    mHandler.obtainMessage(AGAIN_SHAKE).sendToTarget();
                                    Thread.sleep(500);
                                    mHandler.obtainMessage(END_SHAKE).sendToTarget();

                                    if (userInfoSp.getString("userId", null) == null) {
                                        isShake = false;
                                        LoginUtil.toLogin(getActivity());

                                        return;
                                    } else {

                                        initLocation();
                                    }

                                    Looper.prepare();


                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        thread.start();
                    }
                }

            }

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private static class MyHandler extends Handler {
        ShakeFragment fragment;

        public MyHandler(ShakeFragment fragment) {
            this.fragment = fragment;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case START_SHAKE:
                    //This method requires the caller to hold the permission VIBRATE.
                    fragment.mVibrator.vibrate(300);
//                    fragment.startAnimation(false);
                    fragment.imgBg.setBackgroundResource(R.mipmap.shake_after);
                    //发出提示音
                    break;
                case AGAIN_SHAKE:
                    fragment.mVibrator.vibrate(300);
                    break;
                case END_SHAKE:
                    //整体效果结束, 将震动设置为false
                    fragment.isShake = false;
//                    fragment.startAnimation(true);
                    fragment.imgBg.setBackgroundResource(R.mipmap.shake_before);
                    break;
            }
        }
    }

    /**
     * 开启 摇一摇动画
     *
     * @param isBack 是否是返回初识状态
     */
    private void startAnimation(boolean isBack) {
        //动画坐标移动的位置的类型是相对自己的
        int type = Animation.RELATIVE_TO_SELF;

        float topFromY;
        float topToY;
        float bottomFromY;
        float bottomToY;
        if (isBack) {
            topFromY = -0.5f;
            topToY = 0;
            bottomFromY = 0.5f;
            bottomToY = 0;
        } else {
            topFromY = 0;
            topToY = -0.5f;
            bottomFromY = 0;
            bottomToY = 0.5f;
        }

        //上面图片的动画效果
        TranslateAnimation topAnim = new TranslateAnimation(
                type, 0, type, 0, type, topFromY, type, topToY
        );
        topAnim.setDuration(450);
        //动画终止时停留在最后一帧~不然会回到没有执行之前的状态
        topAnim.setFillAfter(true);

        //底部的动画效果
        TranslateAnimation bottomAnim = new TranslateAnimation(
                type, 0, type, 0, type, bottomFromY, type, bottomToY
        );
        bottomAnim.setDuration(450);
        bottomAnim.setFillAfter(true);

        if (isBack) {
            bottomAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    //当动画结束后 , 将中间两条线GONE掉, 不让其占位
//                    imgUp.setVisibility(View.GONE);
//                    imgDown.setVisibility(View.GONE);
                }
            });
        }
        //设置动画
        imgUp.startAnimation(topAnim);
        imgDown.startAnimation(bottomAnim);

    }

}
