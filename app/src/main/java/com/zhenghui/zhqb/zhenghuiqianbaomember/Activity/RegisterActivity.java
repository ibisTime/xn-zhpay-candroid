package com.zhenghui.zhqb.zhenghuiqianbaomember.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.lljjcoder.citypickerview.widget.CityPicker;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Application.MyApplication;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.StringUtils;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class RegisterActivity extends MyBaseActivity {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.layout_address)
    LinearLayout layoutAddress;
    @InjectView(R.id.edt_phone)
    EditText edtPhone;
    @InjectView(R.id.edt_code)
    EditText edtCode;
    @InjectView(R.id.edt_userReferee)
    EditText edtUserReferee;
    @InjectView(R.id.btn_send)
    TextView btnSend;
    @InjectView(R.id.edt_password)
    EditText edtPassword;
    @InjectView(R.id.btn_register)
    Button btnRegister;
    @InjectView(R.id.txt_treaty)
    TextView txtTreaty;
    @InjectView(R.id.txt_address)
    TextView txtAddress;
    @InjectView(R.id.img_scan)
    ImageView imgScan;

    // 验证码是否已发送 未发送false 已发送true
    private boolean isCodeSended = false;

    private SharedPreferences userInfoSp;
    private SharedPreferences appConfigSp;

    private AMapLocationClient mLocationClient;

    private int i = 60;
    private Timer timer;
    private TimerTask task;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            btnSend.setText(i + "秒后重发");
            if (msg.arg1 == 0) {
                stopTime();
            } else {
                startTime();
            }
            super.handleMessage(msg);
        }
    };

    private String mCurrentProviceName;
    private String mCurrentCityName;
    private String mCurrentDistrictName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.inject(this);
        MyApplication.getInstance().addActivity(this);

        inits();
        initLocation();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    private void inits() {
        userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        appConfigSp = getSharedPreferences("appConfig", Context.MODE_PRIVATE);
    }

    private void initLocation() {
        mLocationClient = new AMapLocationClient(this);
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        option.setOnceLocation(true);
        mLocationClient.setLocationOption(option);
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        String city = aMapLocation.getCity();
                        String district = aMapLocation.getDistrict();
                        String province = aMapLocation.getProvince();
                        Log.e("onLocationChanged", "city: " + city);
                        Log.e("onLocationChanged", "district: " + district);
                        Log.e("onLocationChanged", "province: " + province);

                        mCurrentProviceName = province;
                        mCurrentCityName = city;
                        mCurrentDistrictName = district;

                        layoutAddress.setVisibility(View.GONE);
                        txtAddress.setText(mCurrentProviceName + mCurrentCityName + mCurrentDistrictName);
                        String location = StringUtils.extractLocation(city, district);
                    } else {
                        //定位失败
//                        updateLocateState(LocateState.FAILED, "");
                        layoutAddress.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        mLocationClient.startLocation();
    }

    @OnClick({R.id.layout_back, R.id.btn_send, R.id.btn_register, R.id.txt_address, R.id.img_scan})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.btn_send:
                if (isCodeSended) {
                    Toast.makeText(RegisterActivity.this, "验证码每60秒发送发送一次", Toast.LENGTH_SHORT).show();
                } else {
                    if (edtPhone.getText().toString().trim().length() != 11) {
                        Toast.makeText(RegisterActivity.this, "请填写正确的手机号码", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    sendCode();
                }

                break;

            case R.id.btn_register:
                if (edtPhone.getText().toString().trim().length() != 11) {
                    Toast.makeText(RegisterActivity.this, "请填写正确的手机号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (edtCode.getText().toString().trim().length() != 4) {
                    Toast.makeText(RegisterActivity.this, "请填写正确的验证码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (edtPassword.getText().toString().trim().length() < 6) {
                    Toast.makeText(RegisterActivity.this, "密码不能小于6位", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (edtUserReferee.getText().toString().trim().length() != 11) {
                    Toast.makeText(RegisterActivity.this, "请填写推荐人手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (txtAddress.getText().toString().trim().equals("")) {
                    Toast.makeText(RegisterActivity.this, "请选择省市区", Toast.LENGTH_SHORT).show();
                    return;
                }

                register();
                break;

            case R.id.txt_address:
                cityPicker();
                break;

            case R.id.img_scan:
                Intent intent = new Intent(RegisterActivity.this, CaptureActivity.class);
                startActivityForResult(intent, 100);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == 100) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);

                    if(result.length() != 11 ){
                        Toast.makeText(RegisterActivity.this, "请扫描正确的二维码", Toast.LENGTH_LONG).show();
                    }else{
                        edtUserReferee.setText(result);
                    }

                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(RegisterActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void register() {
        JSONObject object = new JSONObject();
        try {
            object.put("mobile", edtPhone.getText().toString().trim());
            object.put("loginPwd", edtPassword.getText().toString().trim());
            object.put("smsCaptcha", edtCode.getText().toString().trim());
            object.put("loginPwdStrength", "1");
            object.put("userReferee", edtUserReferee.getText().toString().trim());
            object.put("kind", "f1");
            object.put("isRegHx", "1");
            object.put("province", mCurrentProviceName);
            object.put("city", mCurrentCityName);
            object.put("area", mCurrentDistrictName);
            object.put("systemCode", appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("object.toString()=" + object.toString());

        new Xutil().post("805041", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(RegisterActivity.this, "注册成功,为您自动登录", Toast.LENGTH_SHORT).show();
                login();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(RegisterActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(RegisterActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void sendCode() {
        JSONObject object = new JSONObject();
        try {
            object.put("mobile", edtPhone.getText().toString().trim());
            object.put("bizType", "805041");
            object.put("kind", "f1");
            object.put("systemCode", appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("805904", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                isCodeSended = true;
                startTime();
                Toast.makeText(RegisterActivity.this, "短信已发送，请注意查收", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(RegisterActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(RegisterActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void login() {
        JSONObject object = new JSONObject();
        try {
            object.put("loginName", edtPhone.getText().toString().trim());
            object.put("loginPwd", edtPassword.getText().toString().trim());
            object.put("kind", "f1");
            object.put("companyCode", "");
            object.put("systemCode", appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("805043", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                SharedPreferences.Editor editor = userInfoSp.edit();

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    editor.putString("userId", jsonObject.getString("userId"));
                    editor.putString("token", jsonObject.getString("token"));
                    editor.commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                signin();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(RegisterActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(RegisterActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signin() {
        EMClient.getInstance().login(userInfoSp.getString("userId", null), "888888", new EMCallBack() {
            @Override
            public void onSuccess() {
                finish();
                LoginActivity.instance.finish();
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));

            }

            @Override
            public void onError(int i, String s) {
                Log.i("Qian", "登录失败 " + i + ", " + s);
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

    private void cityPicker(){
        CityPicker cityPicker = new CityPicker.Builder(RegisterActivity.this)
                .textSize(18)
                .titleBackgroundColor("#ffffff")
                .titleTextColor("#ffffff")
                .backgroundPop(0xa0000000)
                .confirTextColor("#FE4332")
                .cancelTextColor("#FE4332")
                .province(userInfoSp.getString("province","北京市"))
                .city(userInfoSp.getString("city","北京市"))
                .district(userInfoSp.getString("district","昌平区"))
                .textColor(Color.parseColor("#000000"))
                .provinceCyclic(true)
                .cityCyclic(false)
                .districtCyclic(false)
                .visibleItemsCount(7)
                .itemPadding(10)
                .onlyShowProvinceAndCity(false)
                .build();
        cityPicker.show();

        //监听方法，获取选择结果
        cityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
            @Override
            public void onSelected(String... citySelected) {
                //省份
                mCurrentProviceName = citySelected[0];
                //城市
                mCurrentCityName = citySelected[1];
                //区县（如果设定了两级联动，那么该项返回空）
                mCurrentDistrictName = citySelected[2];
                //邮编
                String code = citySelected[3];

                txtAddress.setText(mCurrentProviceName + mCurrentCityName + mCurrentDistrictName);
            }
        });
    }

    /**
     * 验证码发送倒计时
     */
    private void startTime() {
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                i--;
                Message message = handler.obtainMessage();
                message.arg1 = i;
                handler.sendMessage(message);
            }

        };

        timer.schedule(task, 1000);
    }

    private void stopTime() {
        isCodeSended = false;
        i = 60;
        btnSend.setText("重新发送");
        timer.cancel();
    }


}
