package com.zhenghui.zhqb.zhenghuiqianbaomember.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.application.MyApplication;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.AssetsModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.PayResult;
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

import static com.zhenghui.zhqb.zhenghuiqianbaomember.R.id.layout_balace;
import static com.zhenghui.zhqb.zhenghuiqianbaomember.R.id.layout_subsidy;
import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_002051;
import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_802503;
import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_808241;

public class ShopPayActivity extends MyBaseActivity {


    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.edt_price)
    EditText edtPrice;
    @InjectView(R.id.txt_discount)
    TextView txtDiscount;
    @InjectView(R.id.layout_discount)
    LinearLayout layoutDiscount;
    @InjectView(R.id.txt_subsidy)
    TextView txtSubsidy;
    @InjectView(R.id.img_subsidy)
    ImageView imgSubsidy;
    @InjectView(layout_subsidy)
    LinearLayout layoutSubsidy;
    @InjectView(R.id.txt_balace)
    TextView txtBalace;
    @InjectView(R.id.img_balace)
    ImageView imgBalace;
    @InjectView(layout_balace)
    LinearLayout layoutBalace;
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
    @InjectView(R.id.layout_wx)
    LinearLayout layoutWx;
    @InjectView(R.id.txt_zhifubao)
    TextView txtZhifubao;
    @InjectView(R.id.img_zhifubao)
    ImageView imgZhifubao;
    @InjectView(R.id.layout_ali)
    LinearLayout layoutAli;
    @InjectView(R.id.txt_finallyPrice)
    TextView txtFinallyPrice;
    @InjectView(R.id.txt_discountMoney)
    TextView txtDiscountMoney;
    @InjectView(R.id.txt_pay)
    TextView txtPay;
    private String ticketCode = "";

    private String code;
    private String currency;
    private SharedPreferences userInfoSp;

    private String payWay = "22";
    private double key1 = 0;
    private double key2 = 0;

    private double rate = 1.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        ButterKnife.inject(this);
        MyApplication.getInstance().addActivity(this);

        inits();
        initEditText();

        getRate();
        getBalance();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    private void inits() {
        code = getIntent().getStringExtra("code");
        currency = getIntent().getStringExtra("currency");
        userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        if (userInfoSp.getString("isGxz", "").equals("0")) {
            txtBalace.setVisibility(View.GONE);
        }

        if(currency.equals("1")){ // 补贴支付
            layoutBalace.setVisibility(View.GONE);
        }
    }

    private void initEditText() {
        edtPrice.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
        //设置字符过滤
        edtPrice.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(".") && dest.toString().length() == 0) {
                    return "0.";
                }
                if (dest.toString().contains(".")) {
                    int index = dest.toString().indexOf(".");
                    int mlength = dest.toString().substring(index).length();
                    if (mlength == 3) {
                        return "";
                    }
                }
                return null;
            }
        }});

        edtPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equals("")) {
                    txtFinallyPrice.setText("0");
                } else {
                    if (payWay.equals("21")) {
                        txtFinallyPrice.setText(MoneyUtil.moneyFormatDouble(Double.parseDouble(editable.toString()) * rate * 1000) + "");
                    } else {
                        txtFinallyPrice.setText(editable.toString());

                    }
                }
            }
        });

    }


    @OnClick({R.id.layout_back, R.id.layout_discount, layout_subsidy, layout_balace, R.id.layout_lmq, R.id.layout_wx,
            R.id.layout_ali, R.id.txt_pay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.layout_discount:
                if (!edtPrice.getText().toString().toString().equals("")) {
                    if (Double.parseDouble(edtPrice.getText().toString().trim()) == 0.0) {
                        Toast.makeText(ShopPayActivity.this, "金额必须大于等于0.01元", Toast.LENGTH_SHORT).show();
                    } else {
                        startActivityForResult(new Intent(ShopPayActivity.this, DiscountActivity.class)
                                .putExtra("isShop", true)
                                .putExtra("storeCode", code)
                                .putExtra("money", (int) (Double.parseDouble(edtPrice.getText().toString().trim()) * 1000)), 0);
                    }
                } else {
                    Toast.makeText(ShopPayActivity.this, "请先输入消费金额", Toast.LENGTH_SHORT).show();
                }

                break;

            case layout_subsidy:
                intImage();
                payWay = "22";
                imgSubsidy.setBackgroundResource(R.mipmap.pay_choose);

                txtFinallyPrice.setText(edtPrice.getText().toString());
                break;

            case layout_balace:
                intImage();
                payWay = "23";
                imgBalace.setBackgroundResource(R.mipmap.pay_choose);

                txtFinallyPrice.setText(edtPrice.getText().toString());
                break;

            case R.id.layout_wx:
                intImage();
                payWay = "2";
                imgWeixin.setBackgroundResource(R.mipmap.pay_choose);

                txtFinallyPrice.setText(edtPrice.getText().toString());
                break;

            case R.id.layout_ali:
                intImage();
                payWay = "3";
                imgZhifubao.setBackgroundResource(R.mipmap.pay_choose);

                txtFinallyPrice.setText(edtPrice.getText().toString());
                break;

            case R.id.layout_lmq:
                intImage();
                payWay = "21";
                imgLmq.setBackgroundResource(R.mipmap.pay_choose);

                if (edtPrice.getText() != null) {
                    if (!edtPrice.getText().toString().equals(""))
                        txtFinallyPrice.setText(MoneyUtil.moneyFormatDouble(Double.parseDouble(edtPrice.getText().toString()) * rate * 1000) + "");
                }

                break;

            case R.id.txt_pay:

                if (!edtPrice.getText().toString().toString().equals("")) {
                    if (Double.parseDouble(edtPrice.getText().toString().trim()) == 0.0) {
                        Toast.makeText(ShopPayActivity.this, "金额必须大于等于0.01元", Toast.LENGTH_SHORT).show();
                    } else {

                        if (payWay.equals("22") || payWay.equals("23") || payWay.equals("21")) {
                            if (userInfoSp.getString("tradepwdFlag", "").equals("0")) {
                                Toast.makeText(this, "请先设置支付密码", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ShopPayActivity.this, ModifyTradeActivity.class).putExtra("isModify", false));
                            } else {
                                tip(view);
                            }
                        } else {
                            pay("");
                        }

                    }
                } else {
                    Toast.makeText(ShopPayActivity.this, "请输入消费金额", Toast.LENGTH_SHORT).show();
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        key1 = data.getDoubleExtra("key1", 0);
        key2 = data.getDoubleExtra("key2", 0);
        ticketCode = data.getStringExtra("ticketCode");
        if (!data.getStringExtra("ticketName").equals("")) {
            txtDiscount.setText(data.getStringExtra("ticketName"));

            double money = Double.parseDouble(edtPrice.getText().toString().trim()) * 1000;
            double finalPrice = money - key2;
            txtFinallyPrice.setText(MoneyUtil.moneyFormatDouble(finalPrice));
            txtDiscountMoney.setText("(优惠" + MoneyUtil.moneyFormatDouble(key2) + "元)");
        }

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

        new Xutil().post(CODE_802503, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONArray jsonArray = new JSONArray(result);
                    Gson gson = new Gson();
                    List<AssetsModel> lists = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<AssetsModel>>() {
                    }.getType());

                    double frb = 0;
                    double gxjl = 0;
                    for (AssetsModel model : lists) {
                        if (model.getCurrency().equals("FRB")) {
                            frb = model.getAmount();
                        } else if (model.getCurrency().equals("GXJL")) {
                            gxjl = model.getAmount();
                        }
                    }
//                    txtBalace.setText("余额:"+ MoneyUtil.moneyFormatDouble(frb+gxjl));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(ShopPayActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(ShopPayActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
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
                Toast.makeText(ShopPayActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(ShopPayActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void pay(String tradePwd) {
        JSONObject object = new JSONObject();
        try {
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("storeCode", code);
            object.put("payType", payWay);
            object.put("ticketCode", ticketCode);
            object.put("tradePwd", tradePwd);
            object.put("amount", (int) (Double.parseDouble(edtPrice.getText().toString().trim()) * 1000) + "");
            object.put("token", userInfoSp.getString("token", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_808241, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    if (payWay.equals("22") || payWay.equals("21") || payWay.equals("23")) {
                        Toast.makeText(ShopPayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else if (payWay.equals("2")) {
                        JSONObject jsonObject = new JSONObject(result);
                        if (WxUtil.check(ShopPayActivity.this)) {
                            WxUtil.pay(ShopPayActivity.this, jsonObject);
                        }
                    } else {
                        JSONObject jsonObject = new JSONObject(result);
                        AliPay(jsonObject.getString("signOrder"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(ShopPayActivity.this, tip.split("_")[0], Toast.LENGTH_SHORT).show();
                if (tip.split("_").length > 1) {
                    startActivity(new Intent(ShopPayActivity.this, AuthenticateActivity.class));
                }
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(ShopPayActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void AliPay(String info) {
        final String payInfo = info;

        Runnable payRunnable = new Runnable() {

            @Override

            public void run() {

                // 构造PayTask 对象
                PayTask alipay = new PayTask(ShopPayActivity.this);
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
                        Toast.makeText(ShopPayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        ShopPayActivity.this.finish();
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
                    Toast.makeText(ShopPayActivity.this, "请输入支付密码", Toast.LENGTH_SHORT).show();
                } else {
                    popupWindow.dismiss();
                    pay(edtTradePwd.getText().toString().toString());
                }
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.corners_layout));
        // 设置好参数之后再show
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 50);

    }

}
