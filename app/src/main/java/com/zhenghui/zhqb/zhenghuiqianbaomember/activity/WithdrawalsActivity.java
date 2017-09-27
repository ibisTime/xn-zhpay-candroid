package com.zhenghui.zhqb.zhenghuiqianbaomember.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.application.MyApplication;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.BankModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.MyBankCardModel;
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

import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_802015;
import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_802029;
import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_802750;

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
    @InjectView(R.id.txt_tip2)
    TextView txtTip2;
    @InjectView(R.id.txt_tip3)
    TextView txtTip3;
    @InjectView(R.id.txt_tip4)
    TextView txtTip4;

//    private String[] bank;
    //    private String[] bank = { "Item1", "Item2", "Item3", "Item4", "Item5", "Item6", "Item7", "Item8", "Item9"};
    private List<BankModel> list;
    private List<MyBankCardModel> bankCardList;

    private SharedPreferences userInfoSp;
    private SharedPreferences appConfigSp;

    private double balance;
    private String bankName;
    private String bankcardNumber;
    private String accountNumber;

    private double CUSERQXFL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawals);
        ButterKnife.inject(this);
        MyApplication.getInstance().addActivity(this);

        inits();
        initEditText();

        getTip();
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

        edtPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!editable.toString().trim().equals("")){
                    txtTip4.setText("* 本次提现手续费:"+MoneyUtil.doubleFormatSXF(Double.parseDouble(editable.toString()) * CUSERQXFL));
                }else {
                    txtTip4.setText("* 本次提现手续费:0");
                }
            }
        });
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
                        if (txtBankCard.getText().toString().equals("选择银行卡")) {
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

        if (data == null)
            return;

        if (!data.getStringExtra("bankName").equals("")) {
            bankName = data.getStringExtra("bankName");
            bankcardNumber = data.getStringExtra("bankcardNumber");

            txtBankCard.setText(bankName);
        }

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

        new Xutil().post(CODE_802015, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    ArrayList<MyBankCardModel> lists = gson.fromJson(jsonObject.getJSONArray("list").toString(), new TypeToken<ArrayList<MyBankCardModel>>() {
                    }.getType());

                    bankCardList.addAll(lists);
                    if (bankCardList.size() > 0) {
                        bankName = bankCardList.get(0).getBankName();
                        bankcardNumber = bankCardList.get(0).getBankcardNumber();

                        txtBankCard.setText(bankName);
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
            object.put("type", "C_RMB");
            object.put("token", userInfoSp.getString("token", null));
            object.put("systemCode", appConfigSp.getString("systemCode", null));
            object.put("companyCode", appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_802029, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    txtTip.setText("1.每月最大取现次数为"+jsonObject.getString("USER_MONTIMES")+"次");
                    txtTip2.setText("2.提现金额是" + jsonObject.getString("USER_QXBS") + "的倍数，单笔最高" + jsonObject.getString("USER_QXDBZDJE"));
                    txtTip3.setText("3.取现手续费:" + (jsonObject.getDouble("USER_QXFL")*100)+"%");

                    CUSERQXFL = jsonObject.getDouble("USER_QXFL");
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
            object.put("payCardInfo", bankName);
            object.put("applyNote", "Android用户端取现");
            object.put("applyUser", userInfoSp.getString("userId", null));
            object.put("tradePwd", edtRepassword.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_802750, object.toString(), new Xutil.XUtils3CallBackPost() {
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