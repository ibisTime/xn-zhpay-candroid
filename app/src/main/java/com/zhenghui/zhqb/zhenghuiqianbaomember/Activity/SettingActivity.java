package com.zhenghui.zhqb.zhenghuiqianbaomember.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenghui.zhqb.zhenghuiqianbaomember.application.MyApplication;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.PersonalModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.services.MyService;
import com.zhenghui.zhqb.zhenghuiqianbaomember.services.UpdateService;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SettingActivity extends MyBaseActivity {


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
    @InjectView(R.id.view)
    View view;
    @InjectView(R.id.txt_bankCard)
    TextView txtBankCard;
    @InjectView(R.id.layout_bankCard)
    LinearLayout layoutBankCard;
    @InjectView(R.id.layout_systemMessage)
    LinearLayout layoutSystemMessage;
    @InjectView(R.id.layout_about)
    LinearLayout layoutAbout;
    @InjectView(R.id.layout_address)
    LinearLayout layoutAddress;
    @InjectView(R.id.txt_versionName)
    TextView txtVersionName;
    @InjectView(R.id.layout_version)
    LinearLayout layoutVersion;

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

        if (model.getRealName() != null) {

            txtAuthentication.setText(model.getRealName());

        }

        txtVersionName.setText("V"+getVersionName());
    }

    @OnClick({R.id.layout_back, R.id.layout_nickname, R.id.layout_phone, R.id.layout_authentication,
            R.id.layout_loginPwd, R.id.layout_payPwd, R.id.txt_logout, R.id.layout_bankCard,
            R.id.layout_systemMessage, R.id.layout_address, R.id.layout_about, R.id.layout_version})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.layout_nickname:
                startActivity(new Intent(SettingActivity.this, ModifyAliasActivity.class).putExtra("nickname", model.getNickname()));
                break;

            case R.id.layout_phone:
                if (model.getTradepwdFlag().equals("0")) {
                    Toast.makeText(this, "请先设置支付密码", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SettingActivity.this, ModifyTradeActivity.class).putExtra("isModify", false));
                } else {
                    startActivity(new Intent(SettingActivity.this, ModifyPhoneActivity.class));
                }
                break;

            case R.id.layout_authentication:
                if (txtAuthentication.getText().toString().trim().equals("")) {
                    startActivity(new Intent(SettingActivity.this, AuthenticateActivity.class));
                } else {
                    Toast.makeText(this, "您已实名认证", Toast.LENGTH_SHORT).show();
                }


                break;

            case R.id.layout_loginPwd:
                startActivity(new Intent(SettingActivity.this, ModifyPasswordActivity.class).putExtra("phone", model.getMobile()));
                break;

            case R.id.layout_payPwd:
                if (model.getTradepwdFlag().equals("0")) { // 未设置支付密码
                    startActivity(new Intent(SettingActivity.this, ModifyTradeActivity.class).putExtra("isModify", false).putExtra("phone", model.getMobile()));
                } else {
                    startActivity(new Intent(SettingActivity.this, ModifyTradeActivity.class).putExtra("isModify", true).putExtra("phone", model.getMobile()));
                }

                break;

            case R.id.txt_logout:
                logOut();
                break;

            case R.id.layout_bankCard:
                if (txtAuthentication.getText().toString().trim().equals("")) {
                    Toast.makeText(this, "请先实名认证", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SettingActivity.this, AuthenticateActivity.class));
                } else {
                    startActivity(new Intent(SettingActivity.this, BankCardActivity.class));
                }

                break;

            case R.id.layout_systemMessage:
                startActivity(new Intent(SettingActivity.this, SystemMessageActivity.class));
                break;

            case R.id.layout_address:
                startActivity(new Intent(SettingActivity.this, AddressSelectActivity.class));
                break;

            case R.id.layout_version:
                getVersion();
                break;

            case R.id.layout_about:
                startActivity(new Intent(SettingActivity.this, RichTextActivity.class).putExtra("cKey", "aboutus"));
                break;
        }
    }

    /**
     * 退出登录
     */
    private void logOut() {

        RequestParams params = new RequestParams(Xutil.URL + Xutil.LOGOUT);
        params.addBodyParameter("token", userInfoSp.getString("token", null));

        System.out.println("url=" + Xutil.URL + Xutil.LOGOUT);
        System.out.println("token=" + userInfoSp.getString("token", null));

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                return false;
            }

            @Override
            public void onSuccess(String result) {

                System.out.println("result=" + result);

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getJSONObject("data").getBoolean("isSuccess")) {
                        SharedPreferences.Editor editor = userInfoSp.edit();
                        editor.putString("userId", null);
                        editor.putString("token", null);
                        editor.commit();

                        MainActivity.instance.finish();
                        finish();

                        stopService(new Intent(SettingActivity.this, MyService.class));
                        startActivity(new Intent(SettingActivity.this, MainActivity.class));
                    } else {

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
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

                    SharedPreferences.Editor editor = userInfoSp.edit();
                    editor.putString("mobile", model.getMobile());
                    editor.putString("realName", model.getRealName());
                    editor.putString("nickName", model.getNickname());
                    editor.putString("identityFlag", model.getIdentityFlag());
                    editor.putString("tradepwdFlag", model.getTradepwdFlag());
                    editor.putString("userRefereeName", model.getUserRefereeName());
                    if (null != model.getUserExt().getPhoto()) {
                        editor.putString("photo", model.getUserExt().getPhoto());
                    }
                    String address = model.getUserExt().getProvince() + model.getUserExt().getCity() + model.getUserExt().getArea();
                    editor.putString("address", address);
                    editor.commit();

                    setView();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(SettingActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(SettingActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getVersion(){
        JSONObject object = new JSONObject();
        try {
            object.put("key", "cVersionCode");
            object.put("systemCode", appConfigSp.getString("systemCode", null));
            object.put("companyCode", appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new Xutil().post("615917", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    int versionCode = Integer.parseInt(jsonObject.getString("cvalue"));

                    if(versionCode > getVersionCode()){
                        update();
                    }else {
                        Toast.makeText(SettingActivity.this, "当前已是最新版本哦", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(SettingActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(SettingActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void update() {
        new AlertDialog.Builder(this).setTitle("提示")
                .setMessage("发现新版本请及时更新")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        startService(new Intent(SettingActivity.this, UpdateService.class)
                                .putExtra("appname", "zhqb-release")
                                .putExtra("appurl", "http://m.zhenghuijituan.com/app/zhqb-release.apk"));

                    }
                }).setNegativeButton("取消", null).show();
    }

}
