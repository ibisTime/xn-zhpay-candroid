package com.zhenghui.zhqb.zhenghuiqianbaomember.activity;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.MoneyUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_802029;
import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_802418;


public class TransferActivity extends MyBaseActivity {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.txt_title)
    TextView txtTitle;
    @InjectView(R.id.edt_name)
    EditText edtName;
    @InjectView(R.id.edt_amount)
    EditText edtAmount;
    @InjectView(R.id.txt_balance)
    TextView txtBalance;
    @InjectView(R.id.edt_tradePwd)
    EditText edtTradePwd;
    @InjectView(R.id.btn_confirm)
    Button btnConfirm;
    @InjectView(R.id.txt_tip)
    TextView txtTip;

    private Double balance;

    PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        ButterKnife.inject(this);

        inits();
        initEditText();

        getTip();
    }

    private void inits() {
        balance = getIntent().getDoubleExtra("balance", 0.00);
        txtBalance.setText("可转账余额：" + MoneyUtil.moneyFormatDouble(balance));
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
                if(check()){
                    tip(view);
                }
                break;
        }
    }

    private boolean check(){
        if(edtName.getText().toString().trim().length() != 11){
            Toast.makeText(this, "请填写正确的收款人账号", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edtAmount.getText().toString().trim().equals("")) {
            Toast.makeText(this, "请输入转账金额", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (Double.parseDouble(edtAmount.getText().toString().trim()) == 0.0) {
            Toast.makeText(this, "金额必须大于等于0.01元", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edtTradePwd.getText().toString().length() == 0) {
            Toast.makeText(this, "请输入支付密码", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }

    private void tip(View view) {

        // 一个自定义的布局，作为显示的内容
        View mview = LayoutInflater.from(this).inflate(R.layout.popup_transfer, null);

        TextView txtPhone = (TextView) mview.findViewById(R.id.txt_phone);
        TextView txtAmount = (TextView) mview.findViewById(R.id.txt_amount);

        TextView txtCancel = (TextView) mview.findViewById(R.id.txt_cancel);
        TextView txtConfirm = (TextView) mview.findViewById(R.id.txt_confirm);

        popupWindow = new PopupWindow(mview,
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

        txtPhone.setText("收款人账号:"+edtName.getText().toString().trim());
        txtAmount.setText("转账金额:"+edtAmount.getText().toString().trim());

        txtCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                popupWindow.dismiss();
            }
        });

        txtConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                popupWindow.dismiss();
                transfer();
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.corners_layout));
        // 设置好参数之后再show
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 50);

    }

    private void getTip() {

        JSONObject object = new JSONObject();
        try {
            object.put("type", "TR");
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
                    edtAmount.setHint("转账金额为"+jsonObject.getString("TRANSAMOUNTBS")+"的倍数");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(TransferActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(TransferActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void transfer() {

        JSONObject object = new JSONObject();
        try {
            object.put("fromUserId", userInfoSp.getString("userId", null));
            object.put("toMobile", edtName.getText().toString().trim());
            object.put("amount", (int) (Double.parseDouble(edtAmount.getText().toString().trim()) * 1000));
            object.put("tradePwd", edtTradePwd.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_802418, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Toast.makeText(TransferActivity.this, "转账成功", Toast.LENGTH_SHORT).show();
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(TransferActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(TransferActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
