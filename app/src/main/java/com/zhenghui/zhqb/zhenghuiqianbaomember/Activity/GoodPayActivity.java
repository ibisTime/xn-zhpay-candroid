package com.zhenghui.zhqb.zhenghuiqianbaomember.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhenghui.zhqb.zhenghuiqianbaomember.Application.MyApplication;
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

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class GoodPayActivity extends MyBaseActivity {


    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.txt_rmb_1)
    TextView txtRmb1;
    @InjectView(R.id.txt_rmb_2)
    TextView txtRmb2;
    @InjectView(R.id.txt_rmb_3)
    TextView txtRmb3;
    @InjectView(R.id.txt_gwb_1)
    TextView txtGwb1;
    @InjectView(R.id.txt_gwb_2)
    TextView txtGwb2;
    @InjectView(R.id.txt_gwb_3)
    TextView txtGwb3;
    @InjectView(R.id.txt_qbb_1)
    TextView txtQbb1;
    @InjectView(R.id.txt_qbb_2)
    TextView txtQbb2;
    @InjectView(R.id.txt_balace)
    TextView txtBalace;
    @InjectView(R.id.img_balace)
    ImageView imgBalace;
    @InjectView(R.id.img_weixin)
    ImageView imgWeixin;
    @InjectView(R.id.img_zhifubao)
    ImageView imgZhifubao;
    @InjectView(R.id.textView2)
    TextView textView2;
    @InjectView(R.id.txt_finallyPrice)
    TextView txtFinallyPrice;
    @InjectView(R.id.txt_pay)
    TextView txtPay;
    @InjectView(R.id.layout_wx)
    LinearLayout layoutWx;
    @InjectView(R.id.layout_ali)
    LinearLayout layoutAli;

    private String payWay = "1";

    private double rmb;
    private double gwb;
    private double qbb;
    private String code;
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    private void inits() {

        code = getIntent().getStringExtra("code");
        rmb = getIntent().getDoubleExtra("rmb",0.00);
        gwb = getIntent().getDoubleExtra("gwb",0.00);
        qbb = getIntent().getDoubleExtra("qbb",0.00);
        shopCart = getIntent().getBooleanExtra("shopCart",false);
        codeList = getIntent().getStringArrayListExtra("codeList");
        userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        appConfigSp = getSharedPreferences("appConfig", Context.MODE_PRIVATE);

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
                getIp();
                break;
        }
    }

    private void intImage() {
        imgBalace.setBackgroundResource(R.mipmap.pay_unchoose);
        imgWeixin.setBackgroundResource(R.mipmap.pay_unchoose);
        imgZhifubao.setBackgroundResource(R.mipmap.pay_unchoose);
    }

    private void getIp() {

        RequestParams params = new RequestParams("http://121.43.101.148:5601/forward-service/ip");
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
                    if(shopCart){
                        shoppingCartPay(jsonObject.getString("ip"));
                    }else{
                        pay(jsonObject.getString("ip"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("onError:" + ex.getMessage());
                Toast.makeText(GoodPayActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
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
            object.put("ip", ip);
            object.put("code", code);
            object.put("payType", payWay);
            object.put("token", userInfoSp.getString("token", null));
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("systemCode", appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808059", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    if (payWay.equals("1")) {

                        Toast.makeText(GoodPayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        finish();

                    } else if (payWay.equals("2")) {

                        if (WxUtil.check(GoodPayActivity.this)) {
                            WxUtil.pay(GoodPayActivity.this, jsonObject);
                        }

                    } else if (payWay.equals("3")) {

                    }

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

    private void shoppingCartPay(String ip) {

        JSONArray jsonArray = new JSONArray();
        for(int i=0; i<codeList.size(); i++){
            jsonArray.put(codeList.get(i));
        }

        JSONObject object = new JSONObject();
        try {
            object.put("ip", ip);
            object.put("codeList", jsonArray);
            object.put("payType", payWay);
            object.put("token", userInfoSp.getString("token", null));
            object.put("applyUser", userInfoSp.getString("userId", null));
            object.put("systemCode", appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808060", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    if (payWay.equals("1")) {

                        Toast.makeText(GoodPayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        finish();

                    } else if (payWay.equals("2")) {

                        if (WxUtil.check(GoodPayActivity.this)) {
                            WxUtil.pay(GoodPayActivity.this, jsonObject);
                        }

                    } else if (payWay.equals("3")) {

                    }

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



    private void setView() {

        if (rmb == 0) {
            txtRmb1.setVisibility(View.GONE);
            txtRmb2.setVisibility(View.GONE);
            txtRmb3.setVisibility(View.GONE);

            layoutWx.setVisibility(View.INVISIBLE);
            layoutAli.setVisibility(View.INVISIBLE);

        } else {
            txtRmb1.setVisibility(View.VISIBLE);
            txtRmb2.setVisibility(View.VISIBLE);
            txtRmb3.setVisibility(View.VISIBLE);

            txtRmb2.setText(MoneyUtil.moneyFormatDouble(rmb));
            if (gwb == 0 && qbb == 0) {
                txtRmb3.setText("");
            }
        }

        if (gwb == 0) {
            txtGwb1.setVisibility(View.GONE);
            txtGwb2.setVisibility(View.GONE);
            txtGwb3.setVisibility(View.GONE);
        } else {
            txtGwb1.setVisibility(View.VISIBLE);
            txtGwb2.setVisibility(View.VISIBLE);
            txtGwb3.setVisibility(View.VISIBLE);

            txtGwb2.setText(MoneyUtil.moneyFormatDouble(gwb));
            if (qbb == 0) {
                txtGwb3.setText("");
            }
        }

        if (qbb == 0) {
            txtQbb1.setVisibility(View.GONE);
            txtQbb2.setVisibility(View.GONE);
        } else {
            txtQbb1.setVisibility(View.VISIBLE);
            txtQbb2.setVisibility(View.VISIBLE);

            txtQbb2.setText(MoneyUtil.moneyFormatDouble(qbb));
        }

    }
}
