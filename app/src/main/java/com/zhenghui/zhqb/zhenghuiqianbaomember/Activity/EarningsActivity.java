package com.zhenghui.zhqb.zhenghuiqianbaomember.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.AssetsModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.MoneyUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.zhenghui.zhqb.zhenghuiqianbaomember.R.id.txt_earnings;
import static com.zhenghui.zhqb.zhenghuiqianbaomember.R.id.txt_fhq;
import static com.zhenghui.zhqb.zhenghuiqianbaomember.R.id.txt_fund;

public class EarningsActivity extends MyBaseActivity {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(txt_fund)
    TextView txtFund;
    @InjectView(txt_fhq)
    TextView txtFhq;
    @InjectView(txt_earnings)
    TextView txtEarnings;
    @InjectView(R.id.layout_earnings)
    LinearLayout layoutEarnings;
    @InjectView(R.id.txt_limit)
    TextView txtLimit;

    private List<AssetsModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earnings);
        ButterKnife.inject(this);

        inits();
        getData();
        getDatas();
        getAccount();
    }

    private void inits() {
        list = new ArrayList<>();
    }

    @OnClick({R.id.layout_back, R.id.layout_earnings})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.layout_earnings:
                startActivity(new Intent(this,RightsActivity.class));
                break;
        }
    }

    /**
     *  查询所需消费额
     */
    private void getData() {

        JSONObject object = new JSONObject();
        try {
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("token", userInfoSp.getString("token", null));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808418", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    txtLimit.setText(MoneyUtil.moneyFormatDouble(500000 - jsonObject.getDouble("costAmount")));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(EarningsActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(EarningsActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     *  查询分红权与今日收益
     */
    private void getDatas() {

        JSONObject object = new JSONObject();
        try {
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("token", userInfoSp.getString("token", null));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808419", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    txtFhq.setText(jsonObject.getString("stockCount"));
                    txtEarnings.setText(MoneyUtil.moneyFormatDouble(jsonObject.getDouble("todayProfitAmount")));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(EarningsActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(EarningsActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getAccount() {
        JSONObject object = new JSONObject();
        try {
            object.put("token", userInfoSp.getString("token", null));
            object.put("userId", "USER_POOL_ZHPAY");
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

                    list.addAll(lists);

                    setAssets();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(EarningsActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(EarningsActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAssets() {
        for (AssetsModel model : list) {
            if (model.getCurrency().equals("FRB")) {

                txtFund.setText("¥" + MoneyUtil.moneyFormatDouble(model.getAmount()) + "");

            }
        }
    }
}
