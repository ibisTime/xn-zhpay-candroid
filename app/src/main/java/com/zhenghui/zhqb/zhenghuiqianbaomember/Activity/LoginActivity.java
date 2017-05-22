package com.zhenghui.zhqb.zhenghuiqianbaomember.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
    @InjectView(R.id.box_remenber)
    CheckBox boxRemenber;

    SharedPreferences.Editor editor;
    private boolean isRemenberPwd = false;

    private String log;


    public static LoginActivity instance;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(LoginActivity.this, "暂时无法连接，请稍候重试!", Toast.LENGTH_SHORT).show();
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        MyApplication.getInstance().addActivity(this);

        inits();

        boxRemenber.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                System.out.println("b="+b);

                isRemenberPwd = b;
                editor.putBoolean("isRemenberPwd",isRemenberPwd);
                editor.commit();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    private void inits() {
        instance = this;
        log = getIntent().getStringExtra("log");

        edtPhone.setText(userInfoSp.getString("loginName",""));
        edtPassword.setText(userInfoSp.getString("loginPwd",""));

        System.out.println("loginName="+userInfoSp.getString("loginName",""));
        System.out.println("loginPwd="+userInfoSp.getString("loginPwd",""));

        editor = userInfoSp.edit();
        isRemenberPwd = userInfoSp.getBoolean("isRemenberPwd",false);
        boxRemenber.setChecked(isRemenberPwd);

    }


    @OnClick({R.id.layout_back, R.id.txt_register, R.id.btn_login, R.id.txt_forget})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                back();
                break;

            case R.id.txt_register:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;

            case R.id.btn_login:
                if (edtPhone.getText().toString().trim().length() != 11) {
                    Toast.makeText(LoginActivity.this, "请填写正确的手机号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (edtPassword.getText().toString().trim().length() == 0) {
                    Toast.makeText(LoginActivity.this, "请填写密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                login();
                break;

            case R.id.txt_forget:
                startActivity(new Intent(LoginActivity.this, ModifyPasswordActivity.class));
                break;
        }
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
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    editor.putString("userId", jsonObject.getString("userId"));
                    editor.putString("token", jsonObject.getString("token"));

                    System.out.println("isRemenberPwd="+isRemenberPwd);
                    if(isRemenberPwd){
                        editor.putString("loginName", edtPhone.getText().toString().trim());
                        editor.putString("loginPwd", edtPassword.getText().toString().trim());
                    }else {
                        editor.putString("loginName", "");
                        editor.putString("loginPwd", "");
                    }
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

    private void signin() {

        System.out.println("userInfoSp.getString(\"userId\",null)=" + userInfoSp.getString("userId", null));
        EMClient.getInstance().login(userInfoSp.getString("userId", null), "888888", new EMCallBack() {
            @Override
            public void onSuccess() {
                back();

            }

            @Override
            public void onError(int i, String s) {
                if (i == 200) {
                    logout();
                } else {
                    Message message = handler.obtainMessage();
                    message.obj = "登录失败: " + i + ", " + s;
                    handler.sendMessage(message);
                    Log.i("EMClient_login", "登录失败 " + i + ", " + s);
                }

            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            back();
        }
        return false;
    }

    private void back() {
        MainActivity.instance.finish();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void logout() {

        EMClient.getInstance().logout(true, new EMCallBack() {

            @Override
            public void onSuccess() {
                signin();
            }

            @Override
            public void onProgress(int progress, String status) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onError(int code, String message) {
                Message msg = handler.obtainMessage();
                msg.obj = "退出失败: " + code + ", " + message;
                handler.sendMessage(msg);

            }
        });
    }

}
