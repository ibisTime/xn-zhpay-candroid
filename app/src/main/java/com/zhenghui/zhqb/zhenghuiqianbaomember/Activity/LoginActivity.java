package com.zhenghui.zhqb.zhenghuiqianbaomember.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Application.MyApplication;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class LoginActivity extends MyBaseActivity {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.txt_register)
    TextView txtRegister;
    @InjectView(R.id.edt_phone)
    EditText edtPhone;
    @InjectView(R.id.edt_password)
    EditText edtPassword;
    @InjectView(R.id.btn_login)
    Button btnLogin;
    @InjectView(R.id.txt_forget)
    TextView txtForget;

    private String log;

    private SharedPreferences appConfigSp;
    private SharedPreferences userInfoSp;

    public static LoginActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        MyApplication.getInstance().addActivity(this);

        inits();
        if(userInfoSp.getString("userId",null) != null){
            if(EMClient.getInstance().isLoggedInBefore()){
                //已经登录，可以直接进入会话界面
                finish();
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
            }
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    private void inits() {
        instance = this;
        log = getIntent().getStringExtra("log");
        if(log != null){
            Toast.makeText(LoginActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
        }

        appConfigSp = getSharedPreferences("appConfig", Context.MODE_PRIVATE);
        userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
    }


    @OnClick({R.id.layout_back, R.id.txt_register, R.id.btn_login, R.id.txt_forget})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                break;

            case R.id.txt_register:
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                break;

            case R.id.btn_login:
                if(edtPhone.getText().toString().trim().length() != 11){
                    Toast.makeText(LoginActivity.this, "请填写正确的手机号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(edtPassword.getText().toString().trim().length() == 0){
                    Toast.makeText(LoginActivity.this, "请填写密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                login();
                break;

            case R.id.txt_forget:
                startActivity(new Intent(LoginActivity.this,ModifyPasswordActivity.class));
                break;
        }
    }

    private void login(){
        JSONObject object = new JSONObject();
        try {
            object.put("loginName",edtPhone.getText().toString().trim());
            object.put("loginPwd",edtPassword.getText().toString().trim());
            object.put("kind","f1");
            object.put("companyCode","");
            object.put("systemCode",appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("805043",object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                SharedPreferences.Editor editor = userInfoSp.edit();

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    editor.putString("userId",jsonObject.getString("userId"));
                    editor.putString("token",jsonObject.getString("token"));
                    editor.commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                signin();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(LoginActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(LoginActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signin(){
        EMClient.getInstance().login(userInfoSp.getString("userId",null), "888888", new EMCallBack() {
            @Override
            public void onSuccess() {
                finish();
                startActivity(new Intent(LoginActivity.this,MainActivity.class));

            }

            @Override
            public void onError(int i, String s) {
                Log.i("Qian","登录失败 " + i + ", " + s);
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            back(); //调用双击退出函数
        }
        return false;
    }

    private void back(){
        finish();
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }

}
