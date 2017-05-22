package com.zhenghui.zhqb.zhenghuiqianbaomember.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Application.MyApplication;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.BankModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.MyBankCardModel;
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

/**
 * Created by Leiq on 2016/12/26.
 * 提现
 */

public class WithdrawalsActivity extends MyBaseActivity {


    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.txt_bankCard)
    TextView txtBankCard;
    @InjectView(R.id.layout_bankCard)
    LinearLayout layoutBankCard;
    @InjectView(R.id.edt_price)
    EditText edtPrice;
    @InjectView(R.id.txt_canUsePrice)
    TextView txtCanUsePrice;
    @InjectView(R.id.edt_repassword)
    EditText edtRepassword;
    @InjectView(R.id.txt_confirm)
    TextView txtConfirm;
    @InjectView(R.id.txt_tip)
    TextView txtTip;

    private String[] bank;
    //    private String[] bank = { "Item1", "Item2", "Item3", "Item4", "Item5", "Item6", "Item7", "Item8", "Item9"};
    private List<BankModel> list;
    private List<MyBankCardModel> bankCardList;

    private SharedPreferences userInfoSp;
    private SharedPreferences appConfigSp;

    private double balance;
    private String subbranch;
    private String bankcardNumber;
    private String accountNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawals);
        ButterKnife.inject(this);
        MyApplication.getInstance().addActivity(this);

        inits();
        initEditText();

        getTip();
        getData();
        getList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    private void inits() {
        list = new ArrayList<>();
        bankCardList = new ArrayList<>();
        balance = getIntent().getDoubleExtra("balance", 0.00);
        accountNumber = getIntent().getStringExtra("accountNumber");

        txtCanUsePrice.setText("可提现金额" + MoneyUtil.moneyFormatDouble(balance) + "元");

        userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        appConfigSp = getSharedPreferences("appConfig", Context.MODE_PRIVATE);
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
    }

    @OnClick({R.id.layout_back, R.id.layout_bankCard, R.id.txt_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.layout_bankCard:
                startActivityForResult(new Intent(WithdrawalsActivity.this, BankCardActivity.class).putExtra("isWithdrawal", true), 0);
                break;

            case R.id.txt_confirm:

                if (!edtPrice.getText().toString().toString().equals("")) {
                    if (Double.parseDouble(edtPrice.getText().toString().trim()) == 0.0) {
                        Toast.makeText(WithdrawalsActivity.this, "金额必须大于等于0.01元", Toast.LENGTH_SHORT).show();
                    } else {
                        if (txtBankCard.getText().toString().equals("添加银行卡")) {
                            Toast.makeText(WithdrawalsActivity.this, "请先添加银行卡", Toast.LENGTH_SHORT).show();
                        } else {
                            if (edtRepassword.getText().toString().length() == 0) {
                                Toast.makeText(WithdrawalsActivity.this, "请输入支付密码", Toast.LENGTH_SHORT).show();
                            } else {
                                withdrawal();
                            }
                        }
                    }
                } else {
                    Toast.makeText(WithdrawalsActivity.this, "请输入提现金额", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (!data.getStringExtra("bankName").equals("")) {
            txtBankCard.setText(data.getStringExtra("bankName"));
        }

        subbranch = data.getStringExtra("subbranch");
        bankcardNumber = data.getStringExtra("bankcardNumber");


    }

    private void chooseBankCard() {
        new AlertDialog.Builder(this).setTitle("请选择银行").setSingleChoiceItems(
                bank, 0, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//                        txtBankCard.setText(list.get(which).getBankName());
                        txtBankCard.setText(bank[which]);

                        dialog.dismiss();
                    }
                }).setNegativeButton("取消", null).show();
    }

    /**
     * 获取银行卡渠道
     */
    private void getData() {
        JSONObject object = new JSONObject();
        try {
            object.put("token", userInfoSp.getString("token", null));
            object.put("bankCode", "");
            object.put("bankName", "");
            object.put("channelType", "");
            object.put("payType", "WAP");
            object.put("status", "");
            object.put("paybank", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new Xutil().post("802116", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray jsonObject = new JSONArray(result);

                    Gson gson = new Gson();
                    list = gson.fromJson(jsonObject.toString(), new TypeToken<ArrayList<BankModel>>() {
                    }.getType());

                    if (bank == null) {
                        bank = new String[list.size()];

                    }
                    for (int i = 0; i < list.size(); i++) {
                        bank[i] = list.get(i).getBankName();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(WithdrawalsActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(WithdrawalsActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getList() {

        JSONObject object = new JSONObject();
        try {
            object.put("systemCode", appConfigSp.getString("systemCode", null));
            object.put("token", userInfoSp.getString("token", null));
            object.put("bankcardNumber", "");
            object.put("bankName", "");
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("realName", "");
            object.put("type", "");
            object.put("status", "1");
            object.put("start", "1");
            object.put("limit", "1");
            object.put("orderColumn", "");
            object.put("orderDir", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("802015", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    ArrayList<MyBankCardModel> lists = gson.fromJson(jsonObject.getJSONArray("list").toString(), new TypeToken<ArrayList<MyBankCardModel>>() {
                    }.getType());

                    bankCardList.addAll(lists);
                    if (bankCardList.size() > 0) {
                        System.out.println("bankCardList.get(0).getSubbranch()=" + bankCardList.get(0).getSubbranch());
                        System.out.println("bankCardList.get(0).getBankCode()=" + bankCardList.get(0).getBankCode());

                        subbranch = bankCardList.get(0).getSubbranch();
                        bankcardNumber = bankCardList.get(0).getBankcardNumber();

                        System.out.println("subbranch=" + subbranch);
                        System.out.println("bankcardCode=" + bankcardNumber);

                        txtBankCard.setText(bankCardList.get(0).getBankName());
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(WithdrawalsActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(WithdrawalsActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTip() {

        JSONObject object = new JSONObject();
        try {
            object.put("systemCode", appConfigSp.getString("systemCode", null));
            object.put("companyCode", appConfigSp.getString("systemCode", null));
            object.put("token", userInfoSp.getString("token", null));
            object.put("key", "CUSERMONTIMES");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("802027", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    txtTip.setText("* 每月最大取现次数为"+jsonObject.getString("cvalue")+"次");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(WithdrawalsActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(WithdrawalsActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void withdrawal() {

        System.out.println("(Double.parseDouble(edtPrice.getText().toString().trim())=" + Double.parseDouble(edtPrice.getText().toString().trim()));

        JSONArray accountNumberList = new JSONArray();
        accountNumberList.put(accountNumber);

        JSONObject object = new JSONObject();
        try {
            object.put("systemCode", appConfigSp.getString("systemCode", null));
            object.put("token", userInfoSp.getString("token", null));
            object.put("accountNumber", accountNumber);
            object.put("amount", (int) (Double.parseDouble(edtPrice.getText().toString().trim()) * 1000));
            object.put("payCardNo", bankcardNumber);
            // 开户行
            object.put("payCardInfo", subbranch);
            object.put("applyNote", "");
            object.put("applyUser", userInfoSp.getString("userId", null));
            object.put("tradePwd", edtRepassword.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("802750", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(WithdrawalsActivity.this, "提现申请成功", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(WithdrawalsActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(WithdrawalsActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
