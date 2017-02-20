package com.zhenghui.zhqb.zhenghuiqianbaomember.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Application.MyApplication;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.PersonalModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.services.MyService;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AccountActivity extends MyBaseActivity {


    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.txt_nickname)
    TextView txtNickname;
    @InjectView(R.id.layout_nickname)
    LinearLayout layoutNickname;
    @InjectView(R.id.txt_phone)
    TextView txtPhone;
    @InjectView(R.id.layout_phone)
    LinearLayout layoutPhone;
    @InjectView(R.id.layout_authentication)
    LinearLayout layoutAuthentication;
    @InjectView(R.id.layout_loginPwd)
    LinearLayout layoutLoginPwd;
    @InjectView(R.id.layout_payPwd)
    LinearLayout layoutPayPwd;
    @InjectView(R.id.txt_authentication)
    TextView txtAuthentication;
    @InjectView(R.id.txt_logout)
    TextView txtLogout;

    private PersonalModel model;

    private SharedPreferences userInfoSp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
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
    }

    private void setView() {
        txtPhone.setText(model.getMobile());
        txtNickname.setText(model.getNickname());

        if(model.getRealName() != null){

            txtAuthentication.setText(model.getRealName());

        }
    }

    @OnClick({R.id.layout_back, R.id.layout_nickname, R.id.layout_phone, R.id.layout_authentication, R.id.layout_loginPwd, R.id.layout_payPwd, R.id.txt_logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.layout_nickname:
                startActivity(new Intent(AccountActivity.this, ModifyAliasActivity.class).putExtra("nickname", model.getNickname()));
                break;

            case R.id.layout_phone:
                if(model.getTradepwdFlag().equals("0")){
                    Toast.makeText(this, "请先设置支付密码", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AccountActivity.this, ModifyTradeActivity.class).putExtra("isModify",false).putExtra("phone",model.getMobile()));
                }else{
                    startActivity(new Intent(AccountActivity.this, ModifyPhoneActivity.class));
                }
                break;

            case R.id.layout_authentication:
                if (txtAuthentication.getText().toString().trim().equals("")) {
                    startActivity(new Intent(AccountActivity.this, AuthenticateActivity.class));
                } else {
                    Toast.makeText(this, "您已实名认证", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.layout_loginPwd:
                startActivity(new Intent(AccountActivity.this, ModifyPasswordActivity.class).putExtra("phone",model.getMobile()));
                break;

            case R.id.layout_payPwd:
                if(model.getTradepwdFlag().equals("0")){ // 未设置支付密码
                    startActivity(new Intent(AccountActivity.this, ModifyTradeActivity.class).putExtra("isModify",false).putExtra("phone",model.getMobile()));
                }else{
                    startActivity(new Intent(AccountActivity.this, ModifyTradeActivity.class).putExtra("isModify",true).putExtra("phone",model.getMobile()));
                }

                break;

            case R.id.txt_logout:
                logout();
                break;
        }
    }

    private void logout() {

        EMClient.getInstance().logout(true, new EMCallBack() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                SharedPreferences.Editor editor = userInfoSp.edit();
                editor.putString("userId", null);
                editor.putString("token", null);
                editor.commit();

                stopService(new Intent(AccountActivity.this,MyService.class));

                MainActivity.instance.finish();
                PersonalActivity.instance.finish();
                finish();

                startActivity(new Intent(AccountActivity.this, LoginActivity.class));
            }

            @Override
            public void onProgress(int progress, String status) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onError(int code, String message) {
                // TODO Auto-generated method stub

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    /**
     * 获取用户详情
     */
    private void getData() {
        JSONObject object = new JSONObject();
        try {
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("token", userInfoSp.getString("token", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new Xutil().post("805056", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    model = gson.fromJson(jsonObject.toString(), new TypeToken<PersonalModel>() {
                    }.getType());

                    setView();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(AccountActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(AccountActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
