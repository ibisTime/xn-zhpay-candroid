package com.zhenghui.zhqb.zhenghuiqianbaomember.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.WalletModel;
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

public class WalletActivity extends MyBaseActivity {


    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.txt_balance)
    TextView txtBalance;
    @InjectView(R.id.txt_gongxian)
    TextView txtGongxian;
    @InjectView(R.id.layout_gxb)
    LinearLayout layoutGxb;
    @InjectView(R.id.txt_fenrun)
    TextView txtFenrun;
    @InjectView(R.id.layout_frb)
    LinearLayout layoutFrb;
    @InjectView(R.id.txt_hongbao)
    TextView txtHongbao;
    @InjectView(R.id.layout_hbb)
    LinearLayout layoutHbb;
    @InjectView(R.id.txt_hongbaoyeji)
    TextView txtHongbaoyeji;
    @InjectView(R.id.layout_hbyj)
    LinearLayout layoutHbyj;
    @InjectView(R.id.txt_qianbao)
    TextView txtQianbao;
    @InjectView(R.id.layout_qbb)
    LinearLayout layoutQbb;
    @InjectView(R.id.txt_gouwu)
    TextView txtGouwu;
    @InjectView(R.id.layout_gwb)
    LinearLayout layoutGwb;
    @InjectView(R.id.activity_wallet)
    LinearLayout activityWallet;

    private double frb;
    private double gxb;
    private double hbb;
    private double qbb;
    private double gwb;
    private double hbyj;

    private String frbCode;
    private String gxbCode;
    private String qbbCode;
    private String gwbCode;
    private String hbbCode;
    private String hbyjCode;

    private List<WalletModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        ButterKnife.inject(this);

        inits();
        getMoney();
//        getBalance();
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
                Toast.makeText(WalletActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(WalletActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getBalance(){
        JSONObject object = new JSONObject();
        try {
            object.put("token", userInfoSp.getString("token", null));
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("systemCode", appConfigSp.getString("systemCode", null));
            object.put("companyCode", appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808801", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                System.out.println("result="+result );

                txtBalance.setText(MoneyUtil.moneyFormatDouble(Double.parseDouble(result)));

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
//                    "¥" +
                    txtFenrun.setText(MoneyUtil.moneyFormatDouble(model.getAmount()));
                    frb = model.getAmount();

                    frbCode = model.getAccountNumber();
                    break;

                case "GXJL": // 贡献奖励
                    txtGongxian.setText(MoneyUtil.moneyFormatDouble(model.getAmount()));
                    gxb = model.getAmount();

                    gxbCode = model.getAccountNumber();
                    break;

                case "GWB": // 购物币
                    txtGouwu.setText(MoneyUtil.moneyFormatDouble(model.getAmount()));
                    gwb = model.getAmount();

                    gwbCode = model.getAccountNumber();
                    break;

                case "QBB": // 钱包币
                    txtQianbao.setText(MoneyUtil.moneyFormatDouble(model.getAmount()));
                    qbb = model.getAmount();

                    qbbCode = model.getAccountNumber();
                    break;

                case "HBB": // 红包
                    txtHongbao.setText(MoneyUtil.moneyFormatDouble(model.getAmount()));
                    hbb = model.getAmount();

                    hbbCode = model.getAccountNumber();
                    break;

                case "HBYJ": // 红包业绩
                    txtHongbaoyeji.setText(MoneyUtil.moneyFormatDouble(model.getAmount()) + "");
                    hbyj = model.getAmount();

                    hbyjCode = model.getAccountNumber();
                    break;
            }
        }

        txtBalance.setText(MoneyUtil.moneyFormatDouble(frb + gxb));
    }



    @OnClick({R.id.layout_back, R.id.layout_gxb, R.id.layout_frb, R.id.layout_hbb, R.id.layout_hbyj, R.id.layout_qbb, R.id.layout_gwb})
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

            case R.id.layout_hbb:
                startActivity(new Intent(this, BillActivity.class)
                        .putExtra("accountNumber", hbbCode)
                        .putExtra("accountAmount", hbb)
                        .putExtra("accountName", "hbb"));
                break;

            case R.id.layout_hbyj:
                startActivity(new Intent(this, BillActivity.class)
                        .putExtra("accountNumber", hbyjCode)
                        .putExtra("accountAmount", hbyj)
                        .putExtra("accountName", "hbyj"));
                break;

            case R.id.layout_qbb:
                startActivity(new Intent(this, BillActivity.class)
                        .putExtra("accountNumber", qbbCode)
                        .putExtra("accountAmount", qbb)
                        .putExtra("accountName", "qbb"));
                break;

            case R.id.layout_gwb:
                startActivity(new Intent(this, BillActivity.class)
                        .putExtra("accountNumber", gwbCode)
                        .putExtra("accountAmount", gwb)
                        .putExtra("accountName", "gwb"));
                break;
        }
    }
}
