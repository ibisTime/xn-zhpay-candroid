package com.zhenghui.zhqb.zhenghuiqianbaomember.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Adapter.OrderDetailsAdapter;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Application.MyApplication;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.OrderModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
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

public class OrderDetailsActivity extends MyBaseActivity {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.list_order)
    ListView listOrder;
    @InjectView(R.id.txt_btn)
    TextView txtBtn;

    TextView txtOrderId;
    TextView txtTime;
    TextView txtStatus;
    TextView txtConsignee;
    TextView txtPhone;
    TextView txtAddress;

    TextView txtLogistics;
    TextView txtLogisticsId;
    LinearLayout layoutLogistics;


    private View headView;
    private View footView;

    private String code;
    private SharedPreferences appConfigSp;
    private SharedPreferences userInfoSp;

    private OrderModel model;

    private OrderDetailsAdapter adapter;
    private List<OrderModel.ProductOrderListBean> list;



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

        userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        appConfigSp = getSharedPreferences("appConfig", Context.MODE_PRIVATE);

    }

    private void initHeadView() {
        headView = LayoutInflater.from(this).inflate(R.layout.head_order_details, null);

        txtTime = (TextView) headView.findViewById(R.id.txt_time);
        txtPhone = (TextView) headView.findViewById(R.id.txt_phone);
        txtStatus = (TextView) headView.findViewById(R.id.txt_status);
        txtOrderId = (TextView) headView.findViewById(R.id.txt_orderId);
        txtAddress = (TextView) headView.findViewById(R.id.txt_address);
        txtConsignee = (TextView) headView.findViewById(R.id.txt_consignee);

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

        new Xutil().post("808072", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    model = gson.fromJson(jsonObject.toString(), new TypeToken<OrderModel>() {
                    }.getType());

                    list.clear();
                    list.addAll(model.getProductOrderList());
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

        txtPhone.setText(model.getReMobile());


        txtOrderId.setText(model.getCode());
        txtAddress.setText(model.getReAddress());
        txtConsignee.setText(model.getReceiver());
        txtLogistics.setText(model.getLogisticsCompany());
        txtLogisticsId.setText(model.getLogisticsCode());


        txtStatus.setText(OrderStatusUtil.getOrderStatus(model.getStatus()));

        if(txtStatus.getText().equals("待收货") || txtStatus.getText().equals("已收货") || txtStatus.getText().equals("快递异常")){
            layoutLogistics.setVisibility(View.VISIBLE);
        }else{
            layoutLogistics.setVisibility(View.GONE);
        }

        if(model.getStatus().equals("1")){
            txtBtn.setVisibility(View.VISIBLE);
            txtBtn.setText("去支付");
        }else if(model.getStatus().equals("3")){
            txtBtn.setVisibility(View.VISIBLE);
            txtBtn.setText("去收货");
        }else {
            txtBtn.setVisibility(View.GONE);
        }

    }

    @OnClick({R.id.layout_back, R.id.txt_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.txt_btn:
                if(txtBtn.getText().toString().equals("去支付")){
                    startActivity(new Intent(OrderDetailsActivity.this, GoodPayActivity.class).putExtra("code",model.getCode()));
                }else{
                    confirmGet();
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

        new Xutil().post("808057", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                getDatas();
//                Toast.makeText(OrderDetailsActivity.this, "收货成功", Toast.LENGTH_SHORT).show();
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
