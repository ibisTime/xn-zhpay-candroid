package com.zhenghui.zhqb.zhenghuiqianbaomember.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
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
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.PayResult;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.MoneyUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.WxUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_002051;
import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_808250;
import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_808251;

/**
 * Created by lei on 2017/7/26.
 */

public class GiftBuyActivity extends MyBaseActivity {


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
    @InjectView(R.id.txt_discountMoney)
    TextView txtDiscountMoney;
    @InjectView(R.id.txt_pay)
    TextView txtPay;
    @InjectView(R.id.txt_type)
    TextView txtType;

    private String code;
    private String type;

    private double rate = 1.0;

    private String payWay = "1";
    private String currency = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_buy);
        ButterKnife.inject(this);

        inits();
        initEditText();

        getRate();
    }

    private void inits() {
        code = getIntent().getStringExtra("code");
        type = getIntent().getStringExtra("type");

        if (type.equals("gift")) {
            currency = "LPQ";
            txtType.setText("购买数量");
            edtPrice.setHint("请输入礼品券数量");
        }else {
            currency = "LMQ";
            txtType.setText("购买数量");
            edtPrice.setHint("请输入联盟券数量");
        }

    }

    private void initEditText() {
        edtPrice.setInputType(InputType.TYPE_CLASS_NUMBER);
        //设置字符过滤
//        edtPrice.setFilters(new InputFilter[]{new InputFilter() {
//            @Override
//            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
//                if (source.equals(".") && dest.toString().length() == 0) {
//                    return "0.";
//                }
//                if (dest.toString().contains(".")) {
//                    int index = dest.toString().indexOf(".");
//                    int mlength = dest.toString().substring(index).length();
//                    if (mlength == 3) {
//                        return "";
//                    }
//                }
//                return null;
//            }
//        }});

        edtPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().equals("")){
                    txtFinallyPrice.setText("0");
                }else
                    txtFinallyPrice.setText(MoneyUtil.moneyFormatDouble(Double.parseDouble(editable.toString()) / rate * 1000)+"");
            }
        });

    }

    private void intImage() {
        imgBalace.setBackgroundResource(R.mipmap.pay_unchoose);
        imgWeixin.setBackgroundResource(R.mipmap.pay_unchoose);
        imgZhifubao.setBackgroundResource(R.mipmap.pay_unchoose);
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
                        Toast.makeText(GiftBuyActivity.this, "金额必须大于等于0.01元", Toast.LENGTH_SHORT).show();
                    } else {

                        if(payWay.equals("1")){
                            if (userInfoSp.getString("tradepwdFlag", "").equals("0")) {
                                Toast.makeText(this, "请先设置支付密码", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(GiftBuyActivity.this, ModifyTradeActivity.class).putExtra("isModify", false));
                            } else {
                                tip(view);
                            }
                        }else {
                            pay("");
                        }

                    }
                } else {

                    if (type.equals("gift")) {
                        Toast.makeText(GiftBuyActivity.this, "请输入礼品券数量", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(GiftBuyActivity.this, "请输入联盟券数量", Toast.LENGTH_SHORT).show();
                    }


                }
                break;
        }
    }

    private void getRate() {
        JSONObject object = new JSONObject();
        try {
            object.put("fromCurrency", "CNY");
            object.put("toCurrency", currency);
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
                Toast.makeText(GiftBuyActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(GiftBuyActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void pay(String tradePwd) {
        JSONObject object = new JSONObject();
        try {
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("storeCode", code);
            object.put("payType", payWay);
            object.put("tradePwd", tradePwd);
            object.put("quantity", (int) (Double.parseDouble(edtPrice.getText().toString().trim()) * 1000) + "");
            object.put("token", userInfoSp.getString("token", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String code;
        if (type.equals("gift")){
            code = CODE_808250;
        }else {
            code = CODE_808251;
        }

        new Xutil().post(code, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    if (payWay.equals("1")) {
                        Toast.makeText(GiftBuyActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        finish();

                    } else if (payWay.equals("2")) {
                        JSONObject jsonObject = new JSONObject(result);
                        if (WxUtil.check(GiftBuyActivity.this)) {
                            WxUtil.pay(GiftBuyActivity.this, jsonObject);
                        }

                    } else if (payWay.equals("3")) {
                        JSONObject jsonObject = new JSONObject(result);
                        AliPay(jsonObject.getString("signOrder"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {

                Toast.makeText(GiftBuyActivity.this, tip.split("_")[0], Toast.LENGTH_SHORT).show();
                if (tip.split("_").length > 1) {
                    startActivity(new Intent(GiftBuyActivity.this, AuthenticateActivity.class));
                }
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(GiftBuyActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void AliPay(String info) {
        final String payInfo = info;

        Runnable payRunnable = new Runnable() {

            @Override

            public void run() {

                // 构造PayTask 对象
                PayTask alipay = new PayTask(GiftBuyActivity.this);
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
                        Toast.makeText(GiftBuyActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        GiftBuyActivity.this.finish();
                    }

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
                    Toast.makeText(GiftBuyActivity.this, "请输入支付密码", Toast.LENGTH_SHORT).show();
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
