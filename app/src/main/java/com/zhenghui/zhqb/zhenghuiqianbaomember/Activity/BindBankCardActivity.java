package com.zhenghui.zhqb.zhenghuiqianbaomember.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.zhenghui.zhqb.zhenghuiqianbaomember.application.MyApplication;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.BankModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class BindBankCardActivity extends MyBaseActivity {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.txt_title)
    TextView txtTitle;
    @InjectView(R.id.txt_name)
    TextView txtName;
    @InjectView(R.id.txt_bankName)
    TextView txtBankName;
    @InjectView(R.id.edt_cardId)
    EditText edtCardId;
    @InjectView(R.id.txt_confirm)
    TextView txtConfirm;
    @InjectView(R.id.txt_delete)
    TextView txtDelete;

    private String[] bank;
    private String[] bankCode;
    private String bc;

    private List<BankModel> list;

    private SharedPreferences userInfoSp;
    private SharedPreferences appConfigSp;

    private boolean isModifi;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_bank_card);
        ButterKnife.inject(this);
        MyApplication.getInstance().addActivity(this);

        inits();
        getBank();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    private void inits() {
        list = new ArrayList<>();

        userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        appConfigSp = getSharedPreferences("appConfig", Context.MODE_PRIVATE);

        code = getIntent().getStringExtra("code");
        isModifi = getIntent().getBooleanExtra("isModifi", false);
        if (isModifi) {
            txtDelete.setVisibility(View.VISIBLE);
            txtTitle.setText("修改银行卡");
            getData();
        } else {
            txtDelete.setVisibility(View.GONE);
            txtName.setText(userInfoSp.getString("realName", ""));
        }


    }

    @OnClick({R.id.layout_back, R.id.txt_confirm, R.id.txt_bankName, R.id.txt_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.txt_confirm:
                if (check()) {
                    if (isModifi) {
                        tip(view);
                    } else {
                        bindBankCard();
                    }
                }
                break;

            case R.id.txt_bankName:
                chooseBankCard();
                break;

            case R.id.txt_delete:
                tip();
                break;
        }
    }

    private void chooseBankCard() {
        new AlertDialog.Builder(this).setTitle("请选择银行").setSingleChoiceItems(
                bank, -1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//                        txtBankCard.setText(list.get(which).getBankName());
                        txtBankName.setText(bank[which]);

                        bc = bankCode[which];

                        dialog.dismiss();
                    }
                }).setNegativeButton("取消", null).show();
    }

    /**
     * 获取银行卡渠道
     */
    private void getBank() {
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
                    if (bankCode == null) {
                        bankCode = new String[list.size()];
                    }

                    for (int i = 0; i < list.size(); i++) {
                        bank[i] = list.get(i).getBankName();
                    }
                    for (int i = 0; i < list.size(); i++) {
                        bankCode[i] = list.get(i).getBankCode();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(BindBankCardActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(BindBankCardActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getData() {
        JSONObject object = new JSONObject();
        try {
            object.put("token", userInfoSp.getString("token", null));
            object.put("code", code);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("802017", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    txtName.setText(jsonObject.getString("realName"));
                    edtCardId.setText(jsonObject.getString("bankcardNumber"));
                    txtBankName.setText(jsonObject.getString("bankName"));
                    bc = jsonObject.getString("bankCode");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(BindBankCardActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(BindBankCardActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void bindBankCard() {
        JSONObject object = new JSONObject();
        try {
            object.put("realName", txtName.getText().toString().trim());
            object.put("bankcardNumber", edtCardId.getText().toString().trim());
            object.put("bankName", txtBankName.getText().toString().trim());
            object.put("bankCode", bc);
            object.put("currency", "CNY");
            object.put("type", "C");
            object.put("token", userInfoSp.getString("token", null));
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("systemCode", appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("802010", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                finish();
                Toast.makeText(BindBankCardActivity.this, "绑定成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(BindBankCardActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(BindBankCardActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void modifiBankCard(String tradePwd) {
        JSONObject object = new JSONObject();
        try {
            object.put("realName", txtName.getText().toString().trim());
            object.put("bankcardNumber", edtCardId.getText().toString().trim());
            object.put("bankName", txtBankName.getText().toString().trim());
            object.put("bankCode", bc);
            object.put("code", code);
            object.put("status", "1");
            object.put("tradePwd", tradePwd);
            object.put("token", userInfoSp.getString("token", null));
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("systemCode", appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("802013", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                finish();
                Toast.makeText(BindBankCardActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(BindBankCardActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(BindBankCardActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean check() {
        if (txtName.getText().toString().equals("")) {
            Toast.makeText(this, "请填写您的姓名", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (txtBankName.getText().toString().equals("")) {
            Toast.makeText(this, "请填写银行名称", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edtCardId.getText().toString().length() < 16 || edtCardId.getText().toString().length() > 19) {
            Toast.makeText(this, "请填写正确的银行卡号", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

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
                    Toast.makeText(BindBankCardActivity.this, "请输入支付密码", Toast.LENGTH_SHORT).show();
                } else {
                    popupWindow.dismiss();

                    modifiBankCard(edtTradePwd.getText().toString().toString());
                }
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.corners_layout));
        // 设置好参数之后再show
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 50);

    }

    private void tip() {
        new AlertDialog.Builder(this).setTitle("提示")
                .setMessage("您确定要删除该银行卡吗?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        delete();
                    }
                }).setNegativeButton("取消", null).show();
    }

    private void delete() {
        JSONObject object = new JSONObject();
        try {
            object.put("code", code);
            object.put("token", userInfoSp.getString("token", null));
            object.put("systemCode", appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("802011", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(BindBankCardActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(BindBankCardActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(BindBankCardActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
