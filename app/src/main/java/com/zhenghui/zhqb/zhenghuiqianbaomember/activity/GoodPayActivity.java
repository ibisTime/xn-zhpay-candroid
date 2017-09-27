package com.zhenghui.zhqb.zhenghuiqianbaomember.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.application.MyApplication;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.PayResult;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.MoneyUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.WxUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_002051;
import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_808052;

public class GoodPayActivity extends MyBaseActivity {


    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.txt_currency)
    TextView txtCurrency;
    @InjectView(R.id.txt_price)
    TextView txtPrice;
    @InjectView(R.id.txt_subsidy)
    TextView txtSubsidy;
    @InjectView(R.id.img_subsidy)
    ImageView imgSubsidy;
    @InjectView(R.id.layout_subsidy)
    LinearLayout layoutSubsidy;
    @InjectView(R.id.txt_balace)
    TextView txtBalace;
    @InjectView(R.id.img_balace)
    ImageView imgBalace;
    @InjectView(R.id.layout_balance)
    LinearLayout layoutBalance;
    @InjectView(R.id.txt_lmq)
    TextView txtLmq;
    @InjectView(R.id.img_lmq)
    ImageView imgLmq;
    @InjectView(R.id.layout_lmq)
    LinearLayout layoutLmq;
    @InjectView(R.id.txt_weixin)
    TextView txtWeixin;
    @InjectView(R.id.img_weixin)
    ImageView imgWeixin;
    @InjectView(R.id.layout_weixin)
    LinearLayout layoutWeixin;
    @InjectView(R.id.txt_zhifubao)
    TextView txtZhifubao;
    @InjectView(R.id.img_zhifubao)
    ImageView imgZhifubao;
    @InjectView(R.id.layout_ali)
    LinearLayout layoutAli;
    @InjectView(R.id.textView2)
    TextView textView2;
    @InjectView(R.id.txt_finallyPrice)
    TextView txtFinallyPrice;
    @InjectView(R.id.txt_pay)
    TextView txtPay;
    private String payWay = "22";

    private double rmb;
    private double gwb;
    private double qbb;
    private double yunfei;

    private String code;
    private String type;
    private String currency;

    private double rate = 1.0;

    private boolean shopCart;

    private ArrayList<String> codeList;
    private SharedPreferences userInfoSp;
    private SharedPreferences appConfigSp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_pay);
        ButterKnife.inject(this);
        MyApplication.getInstance().addActivity(this);

        inits();
        setView();

        getRate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    private void inits() {

        code = getIntent().getStringExtra("code");
        type = getIntent().getStringExtra("type");
        currency = getIntent().getStringExtra("currency");
        rmb = getIntent().getDoubleExtra("rmb", 0.00);
        gwb = getIntent().getDoubleExtra("gwb", 0.00);
        qbb = getIntent().getDoubleExtra("qbb", 0.00);
        yunfei = getIntent().getDoubleExtra("yunfei", 0.00);

        shopCart = getIntent().getBooleanExtra("shopCart", false);
        codeList = getIntent().getStringArrayListExtra("codeList");
        userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        appConfigSp = getSharedPreferences("appConfig", Context.MODE_PRIVATE);

        if (type.equals("G01")) {
            txtSubsidy.setText("礼品券");
            payWay = "20";

            layoutLmq.setVisibility(View.GONE);
            layoutWeixin.setVisibility(View.GONE);
            layoutAli.setVisibility(View.GONE);
            layoutBalance.setVisibility(View.GONE);
        } else {
//            if (currency.equals("1")) {
//                txtBalace.setText("分润支付");
//            } else {
//                txtBalace.setText("贡献值分润支付");
//            }
        }

        if (userInfoSp.getString("isGxz", "").equals("0")) {
            layoutBalance.setVisibility(View.GONE);
        }

    }

    @OnClick({R.id.layout_back, R.id.layout_subsidy, R.id.txt_balace, R.id.layout_lmq, R.id.layout_weixin, R.id.layout_ali, R.id.txt_pay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.layout_subsidy:
                intImage();
                payWay = "22";
                imgSubsidy.setBackgroundResource(R.mipmap.pay_choose);

                txtFinallyPrice.setText(txtPrice.getText().toString());
                break;

            case R.id.txt_balace:
                intImage();
                if (type.equals("G01")) {
                    payWay = "20";
                } else {
                    payWay = "23";
                }
                imgBalace.setBackgroundResource(R.mipmap.pay_choose);

                txtFinallyPrice.setText(txtPrice.getText().toString());
                break;

            case R.id.layout_lmq:
                intImage();
                payWay = "21";
                imgLmq.setBackgroundResource(R.mipmap.pay_choose);

                if (txtPrice.getText() != null) {
                    if (!txtPrice.getText().toString().equals(""))
                        txtFinallyPrice.setText(MoneyUtil.moneyFormatDouble(Double.parseDouble(txtPrice.getText().toString()) * rate * 1000) + "");
                }
                break;

            case R.id.layout_weixin:
                intImage();
                payWay = "2";
                imgWeixin.setBackgroundResource(R.mipmap.pay_choose);

                txtFinallyPrice.setText(txtPrice.getText().toString());
                break;

            case R.id.layout_ali:
                intImage();
                payWay = "3";
                imgZhifubao.setBackgroundResource(R.mipmap.pay_choose);

                txtFinallyPrice.setText(txtPrice.getText().toString());
                break;

            case R.id.txt_pay:
                if (payWay.equals("20") || payWay.equals("22") || payWay.equals("21") || payWay.equals("23")) {
                    if (userInfoSp.getString("tradepwdFlag", "").equals("0")) {
                        Toast.makeText(this, "请先设置支付密码", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(GoodPayActivity.this, ModifyTradeActivity.class).putExtra("isModify", false));
                    } else {
                        tip(view);
                    }
                } else {
                    if (shopCart) {
//                        shoppingCartPay("");
                    } else {
                        pay("");
                    }
                }
                break;
        }
    }

    private void intImage() {
        imgSubsidy.setBackgroundResource(R.mipmap.pay_unchoose);
        imgBalace.setBackgroundResource(R.mipmap.pay_unchoose);
        imgLmq.setBackgroundResource(R.mipmap.pay_unchoose);
        imgWeixin.setBackgroundResource(R.mipmap.pay_unchoose);
        imgZhifubao.setBackgroundResource(R.mipmap.pay_unchoose);
    }


    private void getRate() {
        JSONObject object = new JSONObject();
        try {
            object.put("fromCurrency", "CNY");
            object.put("toCurrency", "LMQ");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_002051, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    rate = jsonObject.getDouble("rate");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(GoodPayActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(GoodPayActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void pay(String tradePwd) {
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(code);
//        for(int i=0; i<codeList.size(); i++){
//            jsonArray.put(codeList.get(i));
//        }

        JSONObject object = new JSONObject();
        try {
            object.put("codeList", jsonArray);
            object.put("payType", payWay);
            object.put("tradePwd", tradePwd);
            object.put("token", userInfoSp.getString("token", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_808052, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    if (payWay.equals("20") || payWay.equals("22") || payWay.equals("21") || payWay.equals("23")) {

                        Toast.makeText(GoodPayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        finish();

                    } else if (payWay.equals("2")) {

                        if (WxUtil.check(GoodPayActivity.this)) {
                            WxUtil.pay(GoodPayActivity.this, jsonObject);
                        }

                    } else if (payWay.equals("3")) {
                        AliPay(jsonObject.getString("signOrder"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onTip(String tip) {
//                Toast.makeText(GoodPayActivity.this, tip, Toast.LENGTH_SHORT).show();
//                if (tip.equals("本次支付将扣除分润，您还未实名认证")) {
//                    startActivity(new Intent(GoodPayActivity.this, AuthenticateActivity.class));
//                }
                Toast.makeText(GoodPayActivity.this, tip.split("_")[0], Toast.LENGTH_SHORT).show();
                if (tip.split("_").length > 1) {
                    startActivity(new Intent(GoodPayActivity.this, AuthenticateActivity.class));
                }
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(GoodPayActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private void shoppingCartPay(String tradePwd) {
//
//        JSONArray jsonArray = new JSONArray();
//        for (int i = 0; i < codeList.size(); i++) {
//            jsonArray.put(codeList.get(i));
//        }
//
//        JSONObject object = new JSONObject();
//        try {
//            object.put("codeList", jsonArray);
//            object.put("payType", payWay);
//            object.put("tradePwd", tradePwd);
//            object.put("token", userInfoSp.getString("token", null));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        new Xutil().post(CODE_808052, object.toString(), new Xutil.XUtils3CallBackPost() {
//            @Override
//            public void onSuccess(String result) {
//                try {
//                    JSONObject jsonObject = new JSONObject(result);
//
//                    if (payWay.equals("1")) {
//
//                        Toast.makeText(GoodPayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
//                        finish();
//
//                    } else if (payWay.equals("2")) {
//
//                        if (WxUtil.check(GoodPayActivity.this)) {
//                            WxUtil.pay(GoodPayActivity.this, jsonObject);
//                        }
//
//                    } else if (payWay.equals("3")) {
//                        AliPay(jsonObject.getString("signOrder"));
//
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//
//            @Override
//            public void onTip(String tip) {
//                Toast.makeText(GoodPayActivity.this, tip, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onError(String error, boolean isOnCallback) {
//                Toast.makeText(GoodPayActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }


    private void setView() {
//
//        switch (type){
//
//            case "G01":
//                txtCurrency.setText("礼品券");
//                break;
//
//            default:
//                txtCurrency.setText("¥");
//                break;
//
//        }
        txtPrice.setText(MoneyUtil.moneyFormatDouble(rmb + yunfei));
        txtFinallyPrice.setText(MoneyUtil.moneyFormatDouble(rmb + yunfei));

    }

    private void AliPay(String info) {
        final String payInfo = info;

        Runnable payRunnable = new Runnable() {

            @Override

            public void run() {

                // 构造PayTask 对象
                PayTask alipay = new PayTask(GoodPayActivity.this);
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

                    if (resultStatus.equals("9000")) {
                        Toast.makeText(GoodPayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        GoodPayActivity.this.finish();
                    }

//                    Toast.makeText(ShopPayActivity.this, payResult.getResult(),
//                            Toast.LENGTH_LONG).show();
                    break;

            }
        }

        ;

    };

    private void tip(View view) {

        // 一个自定义的布局，作为显示的内容
        View mview = LayoutInflater.from(this).inflate(R.layout.popup_trade, null);

        final EditText edtTradePwd = (EditText) mview.findViewById(R.id.edt_tradePwd);

        TextView txtCancel = (TextView) mview.findViewById(R.id.txt_cancel);
        TextView txtConfirm = (TextView) mview.findViewById(R.id.txt_confirm);

        final PopupWindow popupWindow = new PopupWindow(mview,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);

        popupWindow.setTouchable(true);
        popupWindow.setAnimationStyle(R.style.PopupAnimation);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });


        txtCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                popupWindow.dismiss();
            }
        });

        txtConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (edtTradePwd.getText().toString().trim().equals("")) {
                    Toast.makeText(GoodPayActivity.this, "请输入支付密码", Toast.LENGTH_SHORT).show();
                } else {
                    popupWindow.dismiss();

                    if (shopCart) {
//                        shoppingCartPay(edtTradePwd.getText().toString().toString());
                    } else {
                        pay(edtTradePwd.getText().toString().toString());
                    }
                }
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.corners_layout));
        // 设置好参数之后再show
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 50);

    }

}
