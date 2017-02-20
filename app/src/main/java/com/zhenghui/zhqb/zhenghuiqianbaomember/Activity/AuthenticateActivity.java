package com.zhenghui.zhqb.zhenghuiqianbaomember.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhenghui.zhqb.zhenghuiqianbaomember.Application.MyApplication;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AuthenticateActivity extends MyBaseActivity {


    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.edt_name)
    EditText edtName;
    @InjectView(R.id.edt_identity)
    EditText edtIdentity;
    @InjectView(R.id.edt_cardId)
    EditText edtCardId;
    @InjectView(R.id.edt_phone)
    EditText edtPhone;
    @InjectView(R.id.edt_code)
    EditText edtCode;
    @InjectView(R.id.btn_send)
    TextView btnSend;
    @InjectView(R.id.edt_trade)
    EditText edtTrade;
    @InjectView(R.id.btn_confirm)
    Button btnConfirm;

    private SharedPreferences userInfoSp;
    private SharedPreferences appConfigSp;

    // 验证码是否已发送 未发送false 已发送true
    private boolean isCodeSended = false;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate);
        ButterKnife.inject(this);
        MyApplication.getInstance().addActivity(this);

        inits();

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

    @OnClick({R.id.layout_back, R.id.btn_confirm, R.id.btn_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.btn_send:
                if(edtPhone.getText().length() == 11){
                    if(isCodeSended){
                        Toast.makeText(AuthenticateActivity.this, "验证码每60秒发送发送一次", Toast.LENGTH_SHORT).show();
                    }else{
                        sendCode();
                    }
                }else{
                    Toast.makeText(AuthenticateActivity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btn_confirm:
                if (edtName.getText().toString().equals("")) {
                    Toast.makeText(this, "请填写真实姓名", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (edtIdentity.getText().toString().length() == 15 || edtIdentity.getText().toString().length() == 18) {

                }else{
                    Toast.makeText(this, "请填写正确的身份证号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (edtCardId.getText().toString().length() < 16 || edtCardId.getText().toString().length() > 19) {
                    Toast.makeText(this, "请填写正确的银行卡号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (edtPhone.getText().toString().length() != 11) {
                    Toast.makeText(this, "请填写正确的手机号码", Toast.LENGTH_SHORT).show();
                    return;
                }
//                if (edtTrade.getText().toString().length() < 6) {
//                    Toast.makeText(this, "请设置6位交易密码", Toast.LENGTH_SHORT).show();
//                }
                authenticate();
                break;
        }
    }

    private void authenticate() {

        JSONObject object = new JSONObject();
        try {
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("realName", edtName.getText().toString().trim());
            object.put("idKind", "1");
            object.put("idNo", edtIdentity.getText().toString().trim());
            object.put("cardNo", edtCardId.getText().toString().trim());
            object.put("bindMobile", edtPhone.getText().toString().trim());
            object.put("token", userInfoSp.getString("token", null));

//            object.put("tradePwd", edtTrade.getText().toString().trim());
//            object.put("tradePwdStrength", "1");
//            object.put("smsCaptcha", edtCode.getText().toString().trim());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        new Xutil().post("805190", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                SharedPreferences.Editor editor = userInfoSp.edit();
                editor.putString("realName",edtName.getText().toString().trim());
                editor.commit();

                finish();
                Toast.makeText(AuthenticateActivity.this, "认证成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(AuthenticateActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(AuthenticateActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void sendCode(){
        JSONObject object = new JSONObject();
        try {
            object.put("mobile",edtPhone.getText().toString().trim());
            object.put("bizType","805048");
            object.put("kind","f1");
            object.put("systemCode",appConfigSp.getString("systemCode",null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("805046",object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                isCodeSended = true;
                Toast.makeText(AuthenticateActivity.this, "短信已发送，请注意查收", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(AuthenticateActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(AuthenticateActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     *  验证码发送倒计时
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
