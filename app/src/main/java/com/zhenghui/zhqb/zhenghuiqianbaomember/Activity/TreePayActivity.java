package com.zhenghui.zhqb.zhenghuiqianbaomember.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Application.MyApplication;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.AssetsModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.PayResult;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.WalletModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.MoneyUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.WxUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class TreePayActivity extends MyBaseActivity {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.edt_price)
    TextView edtPrice;
    @InjectView(R.id.txt_balace)
    TextView txtBalace;
    @InjectView(R.id.img_balace)
    ImageView imgBalace;
    @InjectView(R.id.img_weixin)
    ImageView imgWeixin;
    @InjectView(R.id.img_zhifubao)
    ImageView imgZhifubao;
    @InjectView(R.id.txt_finallyPrice)
    TextView txtFinallyPrice;
    @InjectView(R.id.txt_discountMoney)
    TextView txtDiscountMoney;
    @InjectView(R.id.txt_pay)
    TextView txtPay;

    private String code;
    private double price;

    private String payWay = "1";

    private SharedPreferences userInfoSp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tree_pay);
        ButterKnife.inject(this);
        MyApplication.getInstance().addActivity(this);

        inits();
        getBalance();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    private void inits() {
        code = getIntent().getStringExtra("code");
        price = getIntent().getDoubleExtra("price", 0.0);
        edtPrice.setText(MoneyUtil.moneyFormatDouble(price));
        userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        setResult(0, new Intent().putExtra("isPay", false));
    }

    @OnClick({R.id.layout_back, R.id.img_balace, R.id.img_weixin, R.id.img_zhifubao, R.id.txt_pay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.img_balace:
                intImage();
                payWay = "1";
                imgBalace.setBackgroundResource(R.mipmap.pay_choose);

                break;

            case R.id.img_weixin:
                intImage();
                payWay = "2";
                imgWeixin.setBackgroundResource(R.mipmap.pay_choose);

                break;

            case R.id.img_zhifubao:
                intImage();
                payWay = "3";
                imgZhifubao.setBackgroundResource(R.mipmap.pay_choose);
                break;

            case R.id.txt_pay:

                if (!edtPrice.getText().toString().toString().equals("")) {
                    if (Double.parseDouble(edtPrice.getText().toString().trim()) == 0.0) {
                        Toast.makeText(TreePayActivity.this, "金额必须大于等于0.01元", Toast.LENGTH_SHORT).show();
                    } else {
//                        if(txtDiscount.getText().toString().equals("选择折扣券")){
//                            Toast.makeText(this, "请选择折扣券", Toast.LENGTH_SHORT).show();
//                        }else{
//                        getIp();
//                        }
                        pay();
                    }
                } else {
                    Toast.makeText(TreePayActivity.this, "请输入消费金额", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void intImage() {
        imgBalace.setBackgroundResource(R.mipmap.pay_unchoose);
        imgWeixin.setBackgroundResource(R.mipmap.pay_unchoose);
        imgZhifubao.setBackgroundResource(R.mipmap.pay_unchoose);
    }

    private void pay() {
        JSONObject object = new JSONObject();
        try {
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("payType", payWay);
            object.put("hzbTemplateCode", code);
            object.put("token", userInfoSp.getString("token", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("615110", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);

                    if(payWay.equals("1")){
                        Toast.makeText(TreePayActivity.this, "购买成功", Toast.LENGTH_SHORT).show();
                    }else if(payWay.equals("2")){
                        if(WxUtil.check(TreePayActivity.this)){
                            WxUtil.pay(TreePayActivity.this,jsonObject);
                        }
                    }else {
                        AliPay(jsonObject.getString("signOrder"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                finish();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(TreePayActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(TreePayActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getMoney() {
        JSONObject object = new JSONObject();
        try {
            object.put("token", userInfoSp.getString("token", null));
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("systemCode", appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("802503", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray jsonArray = new JSONArray(result);

                    Gson gson = new Gson();
                    List<WalletModel> lists = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<WalletModel>>() {
                    }.getType());

                    for (WalletModel model : lists) {
                        switch (model.getCurrency()) {
                            case "FRB": // 分润
                                txtBalace.setText("分润("+ MoneyUtil.moneyFormatDouble(model.getAmount())+")");
                                break;
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(TreePayActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(TreePayActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getBalance(){
        JSONObject object = new JSONObject();
        try {
            object.put("token", userInfoSp.getString("token", null));
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("systemCode", appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("802503", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONArray jsonArray = new JSONArray(result);
                    Gson gson = new Gson();
                    List<AssetsModel> lists = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<AssetsModel>>() {
                    }.getType());

                    for (AssetsModel model : lists) {
                        if (model.getCurrency().equals("FRB")) {

                            txtBalace.setText("分润:"+ MoneyUtil.moneyFormatDouble(model.getAmount()) + "");

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(TreePayActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(TreePayActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void AliPay(String info){
        final String payInfo = info;

        Runnable payRunnable = new Runnable() {

            @Override

            public void run() {

                // 构造PayTask 对象
                PayTask alipay = new PayTask(TreePayActivity.this);
                // 调用支付接口，获取支付结果

                String result = alipay.pay(payInfo, true);

                Message msg = new Message();

                msg.what = 1;

                msg.obj = result;

                mHandler.sendMessage(msg);

            }

        };

        Thread payThread = new Thread(payRunnable);

        payThread.start();
    }

    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {

            switch (msg.what) {

                case 1:

                    PayResult payResult = new PayResult((String) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签

                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();

                    System.out.println("resultInfo="+resultInfo);
                    System.out.println("resultStatus="+resultStatus);

                    if(resultStatus.equals("9000")){
                        Toast.makeText(TreePayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        TreePayActivity.this.finish();
                    }

//                    Toast.makeText(ShopPayActivity.this, payResult.getResult(),
//                            Toast.LENGTH_LONG).show();
                    break;

            }
        };

    };
}
