package com.zhenghui.zhqb.zhenghuiqianbaomember.Activity;

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
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.PayResult;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.WalletModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.MoneyUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.WxUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class JewelPayActivity extends MyBaseActivity {


    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.edt_price)
    TextView edtPrice;
    @InjectView(R.id.txt_balance)
    TextView txtBalance;
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
    @InjectView(R.id.layout_wechat)
    LinearLayout layoutWechat;
    @InjectView(R.id.layout_ali)
    LinearLayout layoutAli;
    @InjectView(R.id.layout_balance)
    LinearLayout layoutBalance;

    private String code;
    private String price;
    private String number;
    private String currency;

    private String payWay = "1";

    private List<WalletModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jewel_pay);
        ButterKnife.inject(this);

        inits();
        getBalance();
    }

    private void inits() {
        code = getIntent().getStringExtra("code");
        price = getIntent().getStringExtra("price");
        number = getIntent().getStringExtra("number");
        currency = getIntent().getStringExtra("currency");

        list = new ArrayList<>();

        edtPrice.setText(price);


//        if (currency.equals("CNY")) {
//            // 人民币支付
//            layoutBalance.setVisibility(View.GONE);
//            payWay = "3";
//
//            // 支付宝,微信支付
//            layoutAli.setVisibility(View.VISIBLE);
//            layoutWechat.setVisibility(View.GONE);
//        }else{
//            // 余额支付
//            layoutBalance.setVisibility(View.VISIBLE);
//            payWay = "1";
//
//            // 支付宝,微信支付
//            layoutAli.setVisibility(View.GONE);
//            layoutWechat.setVisibility(View.GONE);
//        }

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
                        Toast.makeText(JewelPayActivity.this, "金额必须大于等于0.01元", Toast.LENGTH_SHORT).show();
                    } else {
//                        if(txtDiscount.getText().toString().equals("选择折扣券")){
//                            Toast.makeText(this, "请选择折扣券", Toast.LENGTH_SHORT).show();
//                        }else{
                        getIp();
//                        }

                    }
                } else {
                    Toast.makeText(JewelPayActivity.this, "请输入消费金额", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void intImage() {
        imgBalace.setBackgroundResource(R.mipmap.pay_unchoose);
        imgWeixin.setBackgroundResource(R.mipmap.pay_unchoose);
        imgZhifubao.setBackgroundResource(R.mipmap.pay_unchoose);
    }

    private void getIp() {

        RequestParams params = new RequestParams(Xutil.URL + Xutil.PORT + "/forward-service/ip");
        x.http().get(params, new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                return false;
            }

            @Override
            public void onSuccess(String result) {
                System.out.println("resul=" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    pay(jsonObject.getString("ip"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("onError:" + ex.getMessage());
                Toast.makeText(JewelPayActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }


    private void pay(String ip) {
        JSONObject object = new JSONObject();
        try {
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("times", number);
            object.put("payType", payWay);
            object.put("jewelCode", code);
            object.put("ip", ip);
            object.put("token", userInfoSp.getString("token", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("615021", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                System.out.println("result=" + result);
                JSONObject jsonObject = null;
                try {
                    if (!result.equals("true")) {
                        jsonObject = new JSONObject(result);
                    }

                    if (payWay.equals("1")) {
                        Toast.makeText(JewelPayActivity.this, "参与成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else if (payWay.equals("2")) {
                        if (WxUtil.check(JewelPayActivity.this)) {
                            WxUtil.pay(JewelPayActivity.this, jsonObject);
                        }
                    } else {
                        AliPay(jsonObject.getString("signOrder"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(JewelPayActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(JewelPayActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getBalance() {
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

                    list.addAll(lists);
                    setMoney();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(JewelPayActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(JewelPayActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setMoney() {
        for (WalletModel model : list) {
            if (model.getCurrency().equals(currency)) {
                if(!model.getCurrency().equals("CNY")){
                    txtBalance.setText("余额(" + MoneyUtil.moneyFormatDouble(model.getAmount()) + MoneyUtil.getCurrency(currency) + ")");
                }
            }
        }

    }

    private void AliPay(String info) {
        final String payInfo = info;

//                EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);

        Runnable payRunnable = new Runnable() {

            @Override

            public void run() {

                // 构造PayTask 对象
                PayTask alipay = new PayTask(JewelPayActivity.this);
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

                    System.out.println("resultInfo=" + resultInfo);
                    System.out.println("resultStatus=" + resultStatus);

                    if (resultStatus.equals("9000")) {
                        Toast.makeText(JewelPayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        JewelPayActivity.this.finish();
                    }

//                    Toast.makeText(ShopPayActivity.this, payResult.getResult(),
//                            Toast.LENGTH_LONG).show();
                    break;

            }
        }

        ;

    };

}