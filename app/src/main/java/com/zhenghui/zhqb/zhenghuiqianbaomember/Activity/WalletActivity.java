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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Application.MyApplication;
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
    @InjectView(R.id.txt_fenrun)
    TextView txtFenrun;
    @InjectView(R.id.layout_withdrawal)
    LinearLayout layoutWithdrawal;
    @InjectView(R.id.layout_frb)
    LinearLayout layoutFrb;
    @InjectView(R.id.txt_gongxian)
    TextView txtGongxian;
    @InjectView(R.id.layout_gxb)
    LinearLayout layoutGxb;
    @InjectView(R.id.txt_qianbao)
    TextView txtQianbao;
    @InjectView(R.id.layout_qbb)
    LinearLayout layoutQbb;
    @InjectView(R.id.txt_gouwu)
    TextView txtGouwu;
    @InjectView(R.id.layout_gwb)
    LinearLayout layoutGwb;
    @InjectView(R.id.txt_hongbao)
    TextView txtHongbao;
    @InjectView(R.id.layout_beFenrun)
    LinearLayout layoutBeFenrun;
    @InjectView(R.id.layout_hbb)
    LinearLayout layoutHbb;
    @InjectView(R.id.txt_hongbaoyeji)
    TextView txtHongbaoyeji;
    @InjectView(R.id.layout_be)
    LinearLayout layoutBe;
    @InjectView(R.id.layout_hbyj)
    LinearLayout layoutHbyj;

    private SharedPreferences userInfoSp;
    private SharedPreferences appConfigSp;

    private List<WalletModel> list;

    private double frb;
    private double gxb;
    private double hbb;
    private double hbyj;

    private String frbCode;
    private String gxbCode;
    private String qbbCode;
    private String gwbCode;
    private String hbbCode;
    private String hbyjCode;

    private PopupWindow popupWindow;

    private String[] type = {"转贡献奖励", "转分润"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        ButterKnife.inject(this);
        MyApplication.getInstance().addActivity(this);

        inits();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getBalance();
        getDatas();
    }

    private void inits() {
        list = new ArrayList<>();

        userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        appConfigSp = getSharedPreferences("appConfig", Context.MODE_PRIVATE);

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

    private void getDatas() {
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

                    System.out.println("list.size()=" + list.size());
                    setView();

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


    private void setView() {
        for (WalletModel model : list) {
            switch (model.getCurrency()) {
                case "CNY": // 人名币
                    break;

                case "FRB": // 分润
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

                    gwbCode = model.getAccountNumber();
                    break;

                case "QBB": // 钱包币
                    txtQianbao.setText(MoneyUtil.moneyFormatDouble(model.getAmount()));

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



    }

    private void chooseType() {
        new AlertDialog.Builder(this).setTitle("请选择兑换方式").setSingleChoiceItems(
                type, -1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            startActivity(new Intent(WalletActivity.this, TransFenRunActivity.class)
                                    .putExtra("type", "54")
                                    .putExtra("balance", hbyj));
                        } else {
                            startActivity(new Intent(WalletActivity.this, TransFenRunActivity.class)
                                    .putExtra("type", "52")
                                    .putExtra("balance", hbyj));
                        }
                        dialog.dismiss();
                    }
                }).setNegativeButton("取消", null).show();
    }

    public void inputMoney(View view, final int id) {

        // 一个自定义的布局，作为显示的内容
        View viewPopup = LayoutInflater.from(this).inflate(
                R.layout.popup_money, null);

        final EditText edtMoney = (EditText) viewPopup.findViewById(R.id.edt_money);
        TextView txtConfirm = (TextView) viewPopup.findViewById(R.id.txt_confirm);
        edtMoney.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
        switch (id) {
            case R.id.layout_withdrawal:
                edtMoney.setHint("请输入提现金额");
                break;

            case R.id.layout_beFenrun:
                edtMoney.setHint("请输入转换金额");
                break;

            case R.id.layout_be:
                edtMoney.setHint("请输入转换金额");
                break;
        }

        //设置字符过滤
        edtMoney.setFilters(new InputFilter[]{new InputFilter() {
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

        popupWindow = new PopupWindow(viewPopup,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        popupWindow.setTouchable(true);
//        popupWindow.setAnimationStyle(R.style.PopupAnimation);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        txtConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!edtMoney.getText().toString().toString().equals("")) {
                    if (Double.parseDouble(edtMoney.getText().toString().trim()) == 0.0) {
                        Toast.makeText(WalletActivity.this, "金额必须大于等于0.01元", Toast.LENGTH_SHORT).show();
                    } else {
                        switch (id) {
                            case R.id.layout_withdrawal:

                                popupWindow.dismiss();
                                break;

                            case R.id.layout_beFenrun:
//                                trans((int)(Double.parseDouble(edtMoney.getText().toString().trim())*1000),50);
                                break;

                            case R.id.layout_be:

                                break;

                        }
                    }
                } else {
                    Toast.makeText(WalletActivity.this, "请输入消费金额", Toast.LENGTH_SHORT).show();
                }

            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.corners_layout));
        // 设置好参数之后再show
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

    }


    @OnClick({R.id.layout_back, R.id.layout_withdrawal, R.id.layout_frb, R.id.layout_gxb, R.id.layout_qbb, R.id.layout_gwb, R.id.layout_beFenrun, R.id.layout_hbb, R.id.layout_be, R.id.layout_hbyj})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.layout_withdrawal:
                if(userInfoSp.getString("identityFlag", null).equals("1")){ //identityFlag 实名认证标示 1有 0 无

                    if(userInfoSp.getString("tradepwdFlag", null).equals("1")){ // tradepwdFlag 交易密码标示 1有 0 无

                        startActivity(new Intent(WalletActivity.this, WithdrawalsActivity.class)
                                .putExtra("balance", frb)
                                .putExtra("accountNumber", frbCode));

                    } else {

                        Toast.makeText(this, "请先设置交易密码", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(WalletActivity.this, ModifyTradeActivity.class).putExtra("isModify",false));

                    }

                }else{
                    Toast.makeText(this, "请先实名认证", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(WalletActivity.this, AuthenticateActivity.class));
                }


                break;

            case R.id.layout_beFenrun:
                startActivity(new Intent(WalletActivity.this, TransFenRunActivity.class)
                        .putExtra("type", "50")
                        .putExtra("balance", hbb));
                break;

            case R.id.layout_be:
                chooseType();
                break;

            case R.id.layout_frb:
                startActivity(new Intent(WalletActivity.this,BillActivity.class).putExtra("accountNumber",frbCode));
                break;

            case R.id.layout_gxb:
                startActivity(new Intent(WalletActivity.this,BillActivity.class).putExtra("accountNumber",gxbCode));
                break;

            case R.id.layout_qbb:
                startActivity(new Intent(WalletActivity.this,BillActivity.class).putExtra("accountNumber",qbbCode));
                break;

            case R.id.layout_gwb:
                startActivity(new Intent(WalletActivity.this,BillActivity.class).putExtra("accountNumber",gwbCode));
                break;

            case R.id.layout_hbb:
                startActivity(new Intent(WalletActivity.this,BillActivity.class).putExtra("accountNumber",hbbCode));
                break;

            case R.id.layout_hbyj:
                startActivity(new Intent(WalletActivity.this,BillActivity.class).putExtra("accountNumber",hbyjCode));
                break;
        }
    }
}
