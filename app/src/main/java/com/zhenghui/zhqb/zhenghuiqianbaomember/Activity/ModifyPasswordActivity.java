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

public class ModifyPasswordActivity extends MyBaseActivity {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.edt_phone)
    EditText edtPhone;
    @InjectView(R.id.edt_code)
    EditText edtCode;
    @InjectView(R.id.btn_send)
    TextView btnSend;
    @InjectView(R.id.edt_password)
    EditText edtPassword;
    @InjectView(R.id.edt_repassword)
    EditText edtRepassword;
    @InjectView(R.id.btn_confirm)
    Button btnConfirm;

    private SharedPreferences preferences;


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
        setContentView(R.layout.activity_modify_password);
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
        edtPhone.setText(getIntent().getStringExtra("phone"));
        preferences = getSharedPreferences("appConfig", Context.MODE_PRIVATE);
    }

    @OnClick({R.id.layout_back, R.id.btn_send, R.id.btn_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.btn_send:
                if(edtPhone.getText().length() == 11){
                    if(isCodeSended){
                        Toast.makeText(ModifyPasswordActivity.this, "验证码每60秒发送发送一次", Toast.LENGTH_SHORT).show();
                    }else{
                        sendCode();
                    }
                }else{
                    Toast.makeText(ModifyPasswordActivity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.btn_confirm:
                if(edtPhone.getText().length() != 11){
                    Toast.makeText(ModifyPasswordActivity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(edtCode.getText().toString().trim().length() != 4){
                    Toast.makeText(ModifyPasswordActivity.this, "请填写正确的验证码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(edtPassword.getText().toString().trim().equals(edtRepassword.getText().toString().trim())){
                    modify();
                }else{
                    Toast.makeText(ModifyPasswordActivity.this, "两次输入密码不相同", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }


    private void modify() {
        JSONObject object = new JSONObject();
        try {
            object.put("mobile",edtPhone.getText().toString().trim());
            object.put("newLoginPwd",edtPassword.getText().toString().trim());
            object.put("smsCaptcha",edtCode.getText().toString().toString());
            object.put("loginPwdStrength","1");
            object.put("kind","f1");
            object.put("systemCode",preferences.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("805048", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(ModifyPasswordActivity.this, "修改密码成功", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(ModifyPasswordActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(ModifyPasswordActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     *
     */
    private void sendCode(){
        JSONObject object = new JSONObject();
        try {
            object.put("mobile",edtPhone.getText().toString().trim());
            object.put("bizType","805048");
            object.put("kind","f1");
            object.put("systemCode",preferences.getString("systemCode",null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("805904",object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                isCodeSended = true;
                startTime();
                Toast.makeText(ModifyPasswordActivity.this, "短信已发送，请注意查收", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(ModifyPasswordActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(ModifyPasswordActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
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
