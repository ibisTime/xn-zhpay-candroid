package com.zhenghui.zhqb.zhenghuiqianbaomember.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenghui.zhqb.zhenghuiqianbaomember.application.MyApplication;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.PersonalModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.List;

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
    @InjectView(R.id.edt_code)
    EditText edtCode;
    @InjectView(R.id.btn_send)
    TextView btnSend;
    @InjectView(R.id.edt_trade)
    EditText edtTrade;
    @InjectView(R.id.btn_confirm)
    Button btnConfirm;

    private String code = "";
    private double price = 0.00;

    private PersonalModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate);
        ButterKnife.inject(this);
        MyApplication.getInstance().addActivity(this);

        inits();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e("LQ", "scheme:" + intent.getScheme());
        Uri uri = intent.getData();
        if (uri != null) {
            Log.e("LQ", "scheme: " + uri.getScheme());
            Log.e("LQ", "host: " + uri.getHost());
            Log.e("LQ", "port: " + uri.getPort());
            Log.e("LQ", "path: " + uri.getPath());
            Log.e("LQ", "queryString: " + uri.getQuery());
            Log.e("LQ", "queryParameter: " + uri.getQueryParameter("biz_content"));

            if(null != uri.getQueryParameter("biz_content")){
                try {
                    JSONObject object = new JSONObject(uri.getQueryParameter("biz_content"));
                    check(object.getString("biz_no"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    private void inits() {
        code = getIntent().getStringExtra("code");
        price = getIntent().getDoubleExtra("price",0.00);
    }

    @OnClick({R.id.layout_back, R.id.btn_confirm, R.id.btn_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.btn_confirm:
                if (edtName.getText().toString().equals("")) {
                    Toast.makeText(this, "请填写真实姓名", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (edtIdentity.getText().toString().length() == 15 || edtIdentity.getText().toString().length() == 18) {

                } else {
                    Toast.makeText(this, "请填写正确的身份证号码", Toast.LENGTH_SHORT).show();
                    return;
                }
//                if (edtCardId.getText().toString().length() < 16 || edtCardId.getText().toString().length() > 19) {
//                    Toast.makeText(this, "请填写正确的银行卡号", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if (edtPhone.getText().toString().length() != 11) {
//                    Toast.makeText(this, "请填写正确的手机号码", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if (edtTrade.getText().toString().length() < 6) {
//                    Toast.makeText(this, "请设置6位支付密码", Toast.LENGTH_SHORT).show();
//                }
                authenticate();
//                authentication();
                break;
        }
    }


    /**
     * 实名认证
     */
    private void authenticate() {

        JSONObject object = new JSONObject();
        try {
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("realName", edtName.getText().toString().trim());
            object.put("idKind", "1");
            object.put("idNo", edtIdentity.getText().toString().trim());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        new Xutil().post("805191", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    if(jsonObject.getBoolean("isSuccess")){
                        Toast.makeText(AuthenticateActivity.this, "您已通过实名认证", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        doVerify(jsonObject.getString("url"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
     * 实名认证结果查询
     */
    private void check(String bizNo) {
        JSONObject object = new JSONObject();
        try {
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("bizNo", bizNo);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        new Xutil().post("805192", object.toString(), new Xutil.XUtils3CallBackPost() {

            @Override
            public void onSuccess(String result) {
                System.out.println("result="+result);
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    if(jsonObject.getBoolean("isSuccess")){
                        SharedPreferences.Editor editor = userInfoSp.edit();
                        editor.putString("realName", edtName.getText().toString().trim());
                        editor.commit();

                        getData();

                    }else{
                        Toast.makeText(AuthenticateActivity.this, "认证失败", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


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
     * 启动支付宝进行认证
     *
     * @param url 开放平台返回的URL
     */
    private void doVerify(String url) {
        if (hasApplication()) {
            Intent action = new Intent(Intent.ACTION_VIEW);
            StringBuilder builder = new StringBuilder();
            builder.append("alipays://platformapi/startapp?appId=20000067&url=");
            builder.append(URLEncoder.encode(url));
            action.setData(Uri.parse(builder.toString()));
            startActivity(action);
        } else {
            //处理没有安装支付宝的情况
            new AlertDialog.Builder(this)
                    .setMessage("是否下载并安装支付宝完成认证?")
                    .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent action = new Intent(Intent.ACTION_VIEW);
                            action.setData(Uri.parse("https://m.alipay.com"));
                            startActivity(action);
                        }
                    }).setNegativeButton("算了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
        }
    }

    /**
     * 判断是否安装了支付宝
     *
     * @return true 为已经安装
     */
    private boolean hasApplication() {
        PackageManager manager = getPackageManager();
        Intent action = new Intent(Intent.ACTION_VIEW);
        action.setData(Uri.parse("alipays://"));
        List<ResolveInfo> list = manager.queryIntentActivities(action, PackageManager.GET_RESOLVED_FILTER);
        return list != null && list.size() > 0;
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


                    Toast.makeText(AuthenticateActivity.this, "认证成功", Toast.LENGTH_SHORT).show();
                    if(code != null){
                        startActivity(new Intent(AuthenticateActivity.this, TreePayActivity.class)
                                .putExtra("price",price)
                                .putExtra("code",code));
                    }else{
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
}
