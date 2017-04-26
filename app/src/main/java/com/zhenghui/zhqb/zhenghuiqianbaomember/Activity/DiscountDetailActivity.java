package com.zhenghui.zhqb.zhenghuiqianbaomember.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Application.MyApplication;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.ShopDetailsModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.MoneyUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class DiscountDetailActivity extends MyBaseActivity {


    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.txt_mark)
    TextView txtMark;
    @InjectView(R.id.txt_key2)
    TextView txtKey2;
    @InjectView(R.id.txt_key1)
    TextView txtKey1;
    @InjectView(R.id.txt_name)
    TextView txtName;
    @InjectView(R.id.txt_phone)
    TextView txtPhone;
    @InjectView(R.id.txt_time)
    TextView txtTime;
    @InjectView(R.id.txt_detail)
    TextView txtDetail;
    @InjectView(R.id.txt_validTime)
    TextView txtValidTime;
    @InjectView(R.id.btn_confirm)
    Button btnConfirm;

    private String name;
    private String phone;

    private String code;
    private SharedPreferences userInfoSp;

    private ShopDetailsModel.StoreTicketsBean model;

    private boolean isBuy;
    private boolean isStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount_detail);
        ButterKnife.inject(this);
        MyApplication.getInstance().addActivity(this);

        inits();
        getDatas();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    private void inits() {



        isBuy = getIntent().getBooleanExtra("isBuy", false);
        isStore = getIntent().getBooleanExtra("isStore", false);

        System.out.println("isBuy="+isBuy);
        System.out.println("isStore="+isStore);

        if (isStore) {
            if (isBuy) {
                btnConfirm.setVisibility(View.VISIBLE);
            } else{
                btnConfirm.setVisibility(View.GONE);
            }
        }

        code = getIntent().getStringExtra("code");
        name = getIntent().getStringExtra("name");
        phone = getIntent().getStringExtra("phone");
        userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
    }


    /**
     * 获取折扣券详情
     */
    public void getDatas() {
        JSONObject object = new JSONObject();
        try {
            object.put("code", code);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808256", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    model = gson.fromJson(jsonObject.toString(), new TypeToken<ShopDetailsModel.StoreTicketsBean>() {
                    }.getType());

                    setView();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


            @Override
            public void onTip(String tip) {
                Toast.makeText(DiscountDetailActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(DiscountDetailActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setView() {
        txtKey1.setText("满" + MoneyUtil.moneyFormatDouble(model.getKey1()) + "元可用");
        txtKey2.setText(((int) model.getKey2() / 1000) + "");
        txtName.setText(name);
        txtPhone.setText(phone);
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        Date end = new Date(model.getValidateEnd());
        Date start = new Date(model.getValidateStart());
        txtTime.setText("有效期至" + s.format(end));

        txtValidTime.setText(s.format(start) + "至" + s.format(end));

        txtDetail.setText(model.getDescription());

    }

    @OnClick({R.id.layout_back, R.id.btn_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.btn_confirm:
                if (userInfoSp.getString("userId", null) == null) {
                    Toast.makeText(DiscountDetailActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(DiscountDetailActivity.this, LoginActivity.class));
                } else {
                    tip();
                }
                break;
        }
    }

    private void tip() {
        new AlertDialog.Builder(this).setTitle("提示")
                .setMessage("是否使用" + ((int) model.getKey2() / 1000) + "钱包币购买此折扣券?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        buy();
                    }
                }).setNegativeButton("取消", null).show();
    }

    public void buy() {
        JSONObject object = new JSONObject();
        try {
            object.put("code", code);
            object.put("token", userInfoSp.getString("token", null));
            object.put("userId", userInfoSp.getString("userId", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808260", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(DiscountDetailActivity.this, "购买成功", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(DiscountDetailActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(DiscountDetailActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
