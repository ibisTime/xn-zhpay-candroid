package com.zhenghui.zhqb.zhenghuiqianbaomember.Activity;

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

import com.zhenghui.zhqb.zhenghuiqianbaomember.Application.MyApplication;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.MoneyUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

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

    private SharedPreferences userInfoSp;
    private SharedPreferences appConfigSp;

    // 0:提现,50:红包转分润,52:红包业绩转分润,54:红包业绩转贡献奖励
    private String type;
    private Double balance;
    private String accountNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans_fen_run);
        ButterKnife.inject(this);
        MyApplication.getInstance().addActivity(this);

        inits();
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
        accountNumber = getIntent().getStringExtra("accountNumber");
        if (type.equals("0")) {
            txtTitle.setText("提现");
            txtType.setText("提现金额");
        } else if (type.equals("50")) {
            txtTitle.setText("红包转分润");
            txtType.setText("转分润金额");
        } else if (type.equals("52")) {
            txtTitle.setText("红包业绩转分润");
            txtType.setText("转分润金额");
        } else {
            txtTitle.setText("红包业绩转贡献奖励");
            txtType.setText("转贡献奖励金额");
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
            object.put("systemCode", appConfigSp.getString("systemCode", null));
            object.put("transAmount", (int) (Double.parseDouble(edtAmount.getText().toString().trim()) * 1000));
            object.put("bizType", type + "");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("802518", object.toString(), new Xutil.XUtils3CallBackPost() {
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
}
