package com.zhenghui.zhqb.zhenghuiqianbaomember.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhenghui.zhqb.zhenghuiqianbaomember.application.MyApplication;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.MoneyUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_802027;
import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_802410;

public class TransFenRunActivity extends MyBaseActivity {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.txt_balance)
    TextView txtBalance;
    @InjectView(R.id.edt_amount)
    EditText edtAmount;
    @InjectView(R.id.btn_confirm)
    Button btnConfirm;
    @InjectView(R.id.activity_trans_fen_run)
    LinearLayout activityTransFenRun;
    @InjectView(R.id.txt_title)
    TextView txtTitle;
    @InjectView(R.id.txt_type)
    TextView txtType;
    @InjectView(R.id.txt_tip)
    TextView txtTip;

    private SharedPreferences userInfoSp;
    private SharedPreferences appConfigSp;

    private String type;
    private Double balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans_fen_run);
        ButterKnife.inject(this);
        MyApplication.getInstance().addActivity(this);

        inits();
        getTip();
        initEditText();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    private void inits() {
        type = getIntent().getStringExtra("type");
        balance = getIntent().getDoubleExtra("balance", 0.00);

        if (type.equals("HBB")) {
            txtTitle.setText("红包转贡献值");
            txtType.setText("转贡献值金额");
        } else {
            txtTitle.setText("红包业绩转贡献值");
            txtType.setText("转贡献值金额");
        }

        txtBalance.setText(MoneyUtil.moneyFormatDouble(balance));

        userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        appConfigSp = getSharedPreferences("appConfig", Context.MODE_PRIVATE);

    }

    private void initEditText() {
        edtAmount.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
        //设置字符过滤
        edtAmount.setFilters(new InputFilter[]{new InputFilter() {
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
    }

    @OnClick({R.id.layout_back, R.id.btn_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.btn_confirm:
                if (!edtAmount.getText().toString().toString().equals("")) {
                    if (Double.parseDouble(edtAmount.getText().toString().trim()) == 0.0) {
                        Toast.makeText(TransFenRunActivity.this, "金额必须大于等于0.01元", Toast.LENGTH_SHORT).show();
                    } else {
                        trans();
                    }
                } else {
                    Toast.makeText(TransFenRunActivity.this, "请输入金额", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private void trans() {
        JSONObject object = new JSONObject();
        try {
            object.put("token", userInfoSp.getString("token", null));
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("fromAmount", Double.parseDouble(edtAmount.getText().toString().trim()) * 1000);
            object.put("fromCurrency", type);
            object.put("toCurrency", "GXJL");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_802410, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                Toast.makeText(TransFenRunActivity.this, "兑换成功", Toast.LENGTH_SHORT).show();
                finish();

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(TransFenRunActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(TransFenRunActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTip() {
        JSONObject object = new JSONObject();
        try {
            object.put("companyCode", appConfigSp.getString("systemCode", null));
            object.put("systemCode", appConfigSp.getString("systemCode", null));
            object.put("key", "EXCTIMES");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_802027, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    txtTip.setText("*每个用户每月可以操作"+jsonObject.getString("cvalue")+"次");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(TransFenRunActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(TransFenRunActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
