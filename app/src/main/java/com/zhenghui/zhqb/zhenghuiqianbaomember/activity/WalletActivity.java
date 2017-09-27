package com.zhenghui.zhqb.zhenghuiqianbaomember.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.WalletModel;
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

import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_802503;

public class WalletActivity extends MyBaseActivity {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.txt_balance)
    TextView txtBalance;
    @InjectView(R.id.txt_frozenAmount)
    TextView txtFrozenAmount;
    @InjectView(R.id.txt_gongxian)
    TextView txtGongxian;
    @InjectView(R.id.layout_gxb)
    LinearLayout layoutGxb;
    @InjectView(R.id.txt_fenrun)
    TextView txtFenrun;
    @InjectView(R.id.layout_frb)
    LinearLayout layoutFrb;
    @InjectView(R.id.txt_lpq)
    TextView txtLpq;
    @InjectView(R.id.layout_lpq)
    LinearLayout layoutLpq;
    @InjectView(R.id.txt_szjf)
    TextView txtSzjf;
    @InjectView(R.id.layout_szjf)
    LinearLayout layoutSzjf;
    @InjectView(R.id.txt_qianbao)
    TextView txtQianbao;
    @InjectView(R.id.layout_qbb)
    LinearLayout layoutQbb;
    @InjectView(R.id.txt_lmq)
    TextView txtLmq;
    @InjectView(R.id.layout_lmq)
    LinearLayout layoutLmq;
    @InjectView(R.id.activity_wallet)
    LinearLayout activityWallet;

    private double frb;
    private double frFrozen;
    private double btFrozen;
    private double gxb;
    private double lpq;
    private double qbb;
    private double lmq;
    private double szjf;
    private double btb;

    private String frbCode;
    private String gxbCode;
    private String qbbCode;
    private String lmqCode;
    private String lpqCode;
    private String btbCode;
    private String szjfCode;

    private List<WalletModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        ButterKnife.inject(this);

        inits();

//        getBalance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMoney();
    }

    private void inits() {
        list = new ArrayList<>();
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

        new Xutil().post(CODE_802503, object.toString(), new Xutil.XUtils3CallBackPost() {
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
                Toast.makeText(WalletActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(WalletActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setMoney() {
        for (WalletModel model : list) {
            switch (model.getCurrency()) {
                case "CNY": // 人名币
                    break;

                case "FRB": // 分润
                    txtFenrun.setText(MoneyUtil.moneyFormatDouble(model.getAmount()));
                    frb = model.getAmount();
                    frFrozen = model.getFrozenAmount();

                    frbCode = model.getAccountNumber();
                    break;

                case "GXJL": // 贡献奖励
                    txtGongxian.setText(MoneyUtil.moneyFormatDouble(model.getAmount()));
                    gxb = model.getAmount();

                    gxbCode = model.getAccountNumber();
                    break;

                case "SZJF": // 数字积分
                    txtSzjf.setText(MoneyUtil.moneyFormatDouble(model.getAmount()));
                    szjf = model.getAmount();

                    szjfCode = model.getAccountNumber();
                    break;

                case "QBB": // 钱包币
                    txtQianbao.setText(MoneyUtil.moneyFormatDouble(model.getAmount()));
                    qbb = model.getAmount();

                    qbbCode = model.getAccountNumber();
                    break;

                case "LPQ": // 礼品券
                    txtLpq.setText(MoneyUtil.moneyFormatDouble(model.getAmount()));
                    lpq = model.getAmount();

                    lpqCode = model.getAccountNumber();
                    break;

                case "LMQ": // 联盟券
                    txtLmq.setText(MoneyUtil.moneyFormatDouble(model.getAmount()));
                    lmq = model.getAmount();

                    lmqCode = model.getAccountNumber();
                    break;

                case "BTB": // 补贴
                    txtBalance.setText(MoneyUtil.moneyFormatDouble(model.getAmount()));
                    btb = model.getAmount();
                    btFrozen = model.getFrozenAmount();

                    btbCode = model.getAccountNumber();
                    break;
            }
        }
//        txtBalance.setText(MoneyUtil.moneyFormatDouble(frb + gxb));
        txtFrozenAmount.setText("提现中金额(含手续费):" + MoneyUtil.moneyFormatDouble(btFrozen + frFrozen));
    }


    @OnClick({R.id.layout_back, R.id.layout_gxb, R.id.layout_frb, R.id.layout_lpq, R.id.layout_qbb,
            R.id.layout_szjf, R.id.layout_lmq,R.id.txt_balance})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.layout_gxb:
                startActivity(new Intent(this, BillActivity.class)
                        .putExtra("accountNumber", gxbCode)
                        .putExtra("accountAmount", gxb)
                        .putExtra("accountName", "gxb"));
                break;

            case R.id.layout_frb:
                startActivity(new Intent(this, BillActivity.class)
                        .putExtra("accountNumber", frbCode)
                        .putExtra("accountAmount", frb)
                        .putExtra("accountName", "frb"));
                break;

            case R.id.layout_lpq:
                startActivity(new Intent(this, BillActivity.class)
                        .putExtra("accountNumber", lpqCode)
                        .putExtra("accountAmount", lpq)
                        .putExtra("accountName", "lpq"));
                break;

            case R.id.layout_qbb:
                startActivity(new Intent(this, BillActivity.class)
                        .putExtra("accountNumber", qbbCode)
                        .putExtra("accountAmount", qbb)
                        .putExtra("accountName", "qbb"));
                break;

            case R.id.layout_szjf:
                startActivity(new Intent(this, BillActivity.class)
                        .putExtra("accountNumber", szjfCode)
                        .putExtra("accountAmount", szjf)
                        .putExtra("accountName", "szjf"));
                break;

            case R.id.layout_lmq:
                startActivity(new Intent(this, BillActivity.class)
                        .putExtra("accountNumber", lmqCode)
                        .putExtra("accountAmount", lmq)
                        .putExtra("accountName", "lmq"));
                break;

            case R.id.txt_balance:
                startActivity(new Intent(this, BillActivity.class)
                        .putExtra("accountNumber", btbCode)
                        .putExtra("accountAmount", btb)
                        .putExtra("accountName", "btb"));
                break;
        }
    }
}
