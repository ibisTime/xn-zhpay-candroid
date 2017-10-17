package com.zhenghui.zhqb.zhenghuiqianbaomember.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.adapter.OrderDetailsAdapter;
import com.zhenghui.zhqb.zhenghuiqianbaomember.application.MyApplication;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.OrderModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.MoneyUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.OrderStatusUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_808053;
import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_808057;
import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_808066;
import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_808240;

public class OrderDetailsActivity extends MyBaseActivity {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.list_order)
    ListView listOrder;
    @InjectView(R.id.txt_btn)
    TextView txtBtn;
    @InjectView(R.id.txt_cancel)
    TextView txtCancel;
    @InjectView(R.id.txt_title)
    TextView txtTitle;

    TextView txtOrderId;
    TextView txtTime;
    TextView txtNote;
    TextView txtStatus;
    TextView txtConsignee;
    TextView txtYunfei;
    TextView txtPrice;
    TextView txtParameter;
    TextView txtPhone;
    TextView txtAddress;

    LinearLayout layoutNote;
    LinearLayout layoutPrice;

    TextView txtLogistics;
    TextView txtLogisticsId;
    LinearLayout layoutLogistics;

    private View headView;
    private View footView;

    private String yunfei;

    private String code;
    private OrderModel model;

    private OrderDetailsAdapter adapter;
    private List<OrderModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        ButterKnife.inject(this);
        MyApplication.getInstance().addActivity(this);

        inits();
        initHeadView();
        initFootView();
        initListView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDatas();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    private void inits() {
        list = new ArrayList<>();
        model = new OrderModel();
        adapter = new OrderDetailsAdapter(this, list);

        code = getIntent().getStringExtra("code");

    }

    private void initHeadView() {
        headView = LayoutInflater.from(this).inflate(R.layout.head_order_details, null);

        txtTime = (TextView) headView.findViewById(R.id.txt_time);
        txtNote = (TextView) headView.findViewById(R.id.txt_note);
        txtPrice = (TextView) headView.findViewById(R.id.txt_price);
        txtPhone = (TextView) headView.findViewById(R.id.txt_phone);
        txtStatus = (TextView) headView.findViewById(R.id.txt_status);
        txtYunfei = (TextView) headView.findViewById(R.id.txt_yunfei);
        txtOrderId = (TextView) headView.findViewById(R.id.txt_orderId);
        txtAddress = (TextView) headView.findViewById(R.id.txt_address);
        txtParameter = (TextView) headView.findViewById(R.id.txt_parameter);
        txtConsignee = (TextView) headView.findViewById(R.id.txt_consignee);

        layoutNote = (LinearLayout) headView.findViewById(R.id.layout_note);
        layoutPrice = (LinearLayout) headView.findViewById(R.id.layout_price);

    }

    private void initFootView() {
        footView = LayoutInflater.from(this).inflate(R.layout.foot_order_details, null);

        txtLogistics = (TextView) footView.findViewById(R.id.txt_logistics);
        txtLogisticsId = (TextView) footView.findViewById(R.id.txt_logisticsId);
        layoutLogistics = (LinearLayout) footView.findViewById(R.id.layout_logistics);

    }

    private void initListView() {
        listOrder.addHeaderView(headView);
        listOrder.addFooterView(footView);
        listOrder.setAdapter(adapter);
    }


    public void getDatas() {

        JSONObject object = new JSONObject();
        try {
            object.put("code", code);
            object.put("token", appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_808066, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    model = gson.fromJson(jsonObject.toString(), new TypeToken<OrderModel>() {
                    }.getType());

                    list.clear();
                    list.add(model);
                    adapter.notifyDataSetChanged();

                    setView();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(OrderDetailsActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(OrderDetailsActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setView() {
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d5 = new Date(model.getApplyDatetime());
        txtTime.setText(s.format(d5));

        txtOrderId.setText(model.getCode());
        txtPhone.setText(model.getReMobile());
        txtAddress.setText(model.getReAddress());
        txtConsignee.setText(model.getReceiver());

        yunfei = MoneyUtil.moneyFormatDouble(model.getYunfei());
        if(model.getStore().getType().equals("G01")){
            txtYunfei.setText("礼品券 " + yunfei);
        }else{
            txtYunfei.setText("¥ " + yunfei);
        }
        txtParameter.setText(model.getProductSpecsName());

        txtStatus.setText(OrderStatusUtil.getOrderStatus(model.getStatus()));

        if(model.getApplyNote() != null){
            if(!model.getApplyNote().equals("")){
                txtNote.setText(model.getApplyNote());
                layoutNote.setVisibility(View.VISIBLE);
            }
        }

        if(txtStatus.getText().equals("待收货") || txtStatus.getText().equals("已收货") || txtStatus.getText().equals("快递异常")){
            layoutLogistics.setVisibility(View.VISIBLE);
            txtLogistics.setText(model.getLogisticsCompany());
            txtLogisticsId.setText(model.getLogisticsCode());
        }else{
            layoutLogistics.setVisibility(View.GONE);
        }

        if(model.getStatus().equals("1")){
            txtBtn.setVisibility(View.VISIBLE);
            txtCancel.setVisibility(View.VISIBLE);
            txtBtn.setText("去支付");

        }else if(model.getStatus().equals("2")){
            txtBtn.setVisibility(View.GONE);
            txtCancel.setVisibility(View.GONE);

        }else if(model.getStatus().equals("3")){
            txtBtn.setVisibility(View.VISIBLE);
            txtBtn.setText("去收货");

        }else if(model.getStatus().equals("91")) {
            txtBtn.setVisibility(View.GONE);
            txtCancel.setVisibility(View.VISIBLE);
            txtCancel.setText("订单已取消");

        }

        layoutPrice.setVisibility(View.VISIBLE);

        if (model.getStore().getType().equals("G01")){
            txtPrice.setText("礼品券"+MoneyUtil.moneyFormatDouble(model.getAmount1()));
        }else {
            if (model.getProduct().getPayCurrency().equals("4")){
                txtPrice.setText("钱包币"+MoneyUtil.moneyFormatDouble(model.getAmount1()));
            }else{
                txtPrice.setText("¥"+MoneyUtil.moneyFormatDouble(model.getAmount1()));
            }
        }

    }

    @OnClick({R.id.layout_back, R.id.txt_btn, R.id.txt_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.txt_btn:
                if(txtBtn.getText().toString().equals("去支付")){
                    startActivity(new Intent(OrderDetailsActivity.this, GoodPayActivity.class)
                            .putExtra("rmb",model.getAmount1())
                            .putExtra("gwb",model.getAmount2())
                            .putExtra("qbb",model.getAmount3())
                            .putExtra("currency",model.getProduct().getPayCurrency())
                            .putExtra("type",model.getStore().getType())
                            .putExtra("payCurrency", model.getProduct().getPayCurrency())
                            .putExtra("yunfei",Double.parseDouble(yunfei)*1000)
                            .putExtra("code",model.getCode()));
                }else{
                    confirmGet();
                }
                break;

            case R.id.txt_cancel:
                if(txtCancel.getText().toString().equals("取消订单")){
                    tipCancel();
                }
                break;
        }
    }

    /**
     * 确认收货
     */
    private void confirmGet() {

        JSONObject object = new JSONObject();
        try {
            object.put("code", model.getCode());
            object.put("updater", userInfoSp.getString("userId",null));
            object.put("remark", "确认收货");
            object.put("token", appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("object.toString()=" + object.toString());

        new Xutil().post(CODE_808057, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                getDatas();
                tip();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(OrderDetailsActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(OrderDetailsActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * 取消订单
     */
    private void cancel() {

        JSONObject object = new JSONObject();
        try {
            object.put("code", model.getCode());
            object.put("userId", userInfoSp.getString("userId",null));
            object.put("remark", "用户取消订单");
            object.put("token", appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_808053, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(OrderDetailsActivity.this, "订单取消成功", Toast.LENGTH_SHORT).show();
                getDatas();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(OrderDetailsActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(OrderDetailsActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void tip() {
        new AlertDialog.Builder(this).setTitle("提示")
                .setMessage("收货成功，给个好评吧!")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        good();
                    }
                }).setNegativeButton("残忍拒绝", null).show();
    }

    private void tipCancel() {
        new AlertDialog.Builder(this).setTitle("提示")
                .setMessage("您确定要取消此订单吗?")
                .setPositiveButton("确定取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        cancel();
                    }
                }).setNegativeButton("不取消", null).show();
    }

    public void good() {
        JSONObject object = new JSONObject();
        try {
            object.put("storeCode", model.getProduct().getCode());
            object.put("type", "3");
            object.put("token", userInfoSp.getString("token", null));
            object.put("userId", userInfoSp.getString("userId", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_808240, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                Toast.makeText(OrderDetailsActivity.this, "评价成功", Toast.LENGTH_SHORT).show();
                finish();

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(OrderDetailsActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(OrderDetailsActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
