package com.zhenghui.zhqb.zhenghuiqianbaomember.Activity;

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
import android.view.View;
import android.widget.EditText;
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

public class ShopPayActivity extends MyBaseActivity {


    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.edt_price)
    EditText edtPrice;
    @InjectView(R.id.txt_discount)
    TextView txtDiscount;
    @InjectView(R.id.layout_discount)
    LinearLayout layoutDiscount;
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
    @InjectView(R.id.txt_pay)
    TextView txtPay;
    @InjectView(R.id.txt_discountMoney)
    TextView txtDiscountMoney;

    private String ticketCode = "";

    private String code;
    private SharedPreferences userInfoSp;

    private String payWay = "1";
    private double key1 = 0;
    private double key2 = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        ButterKnife.inject(this);
        MyApplication.getInstance().addActivity(this);

        inits();
        initEditText();
        getBalance();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    private void inits() {
        code = getIntent().getStringExtra("code");
        userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

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
                txtFinallyPrice.setText(editable.toString());
            }
        });

    }


    @OnClick({R.id.layout_back, R.id.layout_discount, R.id.img_balace, R.id.img_weixin, R.id.img_zhifubao, R.id.txt_pay})
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
                        Toast.makeText(ShopPayActivity.this, "金额必须大于等于0.01元", Toast.LENGTH_SHORT).show();
                    } else {
//                        if(txtDiscount.getText().toString().equals("选择折扣券")){
//                            Toast.makeText(this, "请选择折扣券", Toast.LENGTH_SHORT).show();
//                        }else{
//                        pay();
//                        }
//                        getIp();
                        pay();
                    }
                } else {
                    Toast.makeText(ShopPayActivity.this, "请输入消费金额", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void intImage() {
        imgBalace.setBackgroundResource(R.mipmap.pay_unchoose);
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

                    double frb = 0;
                    double gxjl = 0;
                    for (AssetsModel model : lists) {
                        if (model.getCurrency().equals("FRB")) {
                            frb = model.getAmount();
                        }else if (model.getCurrency().equals("GXJL")) {
                            gxjl = model.getAmount();
                        }
                    }
                    txtBalace.setText("余额:"+ MoneyUtil.moneyFormatDouble(frb+gxjl));
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

    private void pay() {
        JSONObject object = new JSONObject();
        try {
            object.put("userId", userInfoSp.getString("userId", null));
//            object.put("ip", ip);
            object.put("storeCode", code);
            object.put("payType", payWay);
            object.put("ticketCode", ticketCode);
            object.put("amount", (int) (Double.parseDouble(edtPrice.getText().toString().trim()) * 1000)+"");
            object.put("token", userInfoSp.getString("token", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808241", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    if(payWay.equals("1")){
                        Toast.makeText(ShopPayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else if(payWay.equals("2")){
                        if(WxUtil.check(ShopPayActivity.this)){
                            WxUtil.pay(ShopPayActivity.this,jsonObject);
                        }
                    }else{
                        AliPay(jsonObject.getString("signOrder"));
                    }

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

    private void AliPay(String info){
        final String payInfo = info;

//                EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);

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

                    System.out.println("resultInfo="+resultInfo);
                    System.out.println("resultStatus="+resultStatus);

                    if(resultStatus.equals("9000")){
                        Toast.makeText(ShopPayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        ShopPayActivity.this.finish();
                    }

//                    Toast.makeText(ShopPayActivity.this, payResult.getResult(),
//                            Toast.LENGTH_LONG).show();
                    break;

            }
        };

    };

}
