package com.zhenghui.zhqb.zhenghuiqianbaomember.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Adapter.OrderAdapter;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Application.MyApplication;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.OrderModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.RefreshLayout;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class OrderListActivity extends MyBaseActivity implements AdapterView.OnItemClickListener,SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.line_all)
    View lineAll;
    @InjectView(R.id.layout_all)
    LinearLayout layoutAll;
    @InjectView(R.id.line_waitPay)
    View lineWaitPay;
    @InjectView(R.id.layout_waitPay)
    LinearLayout layoutWaitPay;
    @InjectView(R.id.line_waitConfirm)
    View lineWaitConfirm;
    @InjectView(R.id.layout_waitConfirm)
    LinearLayout layoutWaitConfirm;
    @InjectView(R.id.list_order)
    ListView listOrder;
    @InjectView(R.id.swipe_container)
    RefreshLayout swipeContainer;

    private OrderAdapter adapter;
    private List<OrderModel> list;

    private String state = "";
    private int page = 1;
    private int pageSize = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        ButterKnife.inject(this);
        MyApplication.getInstance().addActivity(this);

        inits();
        initListView();
        initRefreshLayout();
        getDatas(state);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    private void inits() {
        list = new ArrayList<>();
        adapter = new OrderAdapter(this, list);
    }

    private void initListView() {
        listOrder.setAdapter(adapter);
        listOrder.setOnItemClickListener(this);
    }

    private void initRefreshLayout() {
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeContainer.setOnRefreshListener(this);
        swipeContainer.setOnLoadListener(this);
    }

    @OnClick({R.id.layout_back, R.id.layout_all, R.id.layout_waitPay, R.id.layout_waitConfirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.layout_all:
                initBtn();
                lineAll.setVisibility(View.VISIBLE);
                layoutAll.setBackgroundColor(getResources().getColor(R.color.white));

                page = 1;
                state = "";
                getDatas(state);

                break;

            case R.id.layout_waitPay:
                initBtn();
                lineWaitPay.setVisibility(View.VISIBLE);
                layoutWaitPay.setBackgroundColor(getResources().getColor(R.color.white));

                page = 1;
                state = "1";
                getDatas(state);
                break;

            case R.id.layout_waitConfirm:
                initBtn();
                lineWaitConfirm.setVisibility(View.VISIBLE);
                layoutWaitConfirm.setBackgroundColor(getResources().getColor(R.color.white));

                page = 1;
                state = "3";
                getDatas(state);
                break;
        }
    }

    private void initBtn() {
        layoutAll.setBackgroundColor(getResources().getColor(R.color.grayfa));
        layoutWaitPay.setBackgroundColor(getResources().getColor(R.color.grayfa));
        layoutWaitConfirm.setBackgroundColor(getResources().getColor(R.color.grayfa));

        lineAll.setVisibility(View.INVISIBLE);
        lineWaitPay.setVisibility(View.INVISIBLE);
        lineWaitConfirm.setVisibility(View.INVISIBLE);
    }

    public void getDatas(String status) {

        JSONObject object = new JSONObject();
        try {
            object.put("applyUser", userInfoSp.getString("userId", null));
            object.put("mobile", "");
            object.put("start", page);
            object.put("limit", pageSize);
            object.put("status", status);
            object.put("token", userInfoSp.getString("token", null));
            object.put("systemCode", appConfigSp.getString("systemCode", null));
//            object.put("companyCode", appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808068", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    ArrayList<OrderModel> lists = gson.fromJson(jsonObject.getJSONArray("list").toString(), new TypeToken<ArrayList<OrderModel>>() {
                    }.getType());

                    if(page == 1){
                        list.clear();
                    }
                    list.addAll(lists);
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(OrderListActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(OrderListActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//        if (list.get(i).getStatus().equals("4")) { // 已收货
//            evaluate(i);
//            return;
//        }

        startActivity(new Intent(OrderListActivity.this, OrderDetailsActivity.class).putExtra("code", list.get(i).getCode()));

    }

    @Override
    public void onRefresh() {
        swipeContainer.postDelayed(new Runnable() {

            @Override
            public void run() {
                swipeContainer.setRefreshing(false);
                page = 1;
                getDatas(state);
            }
        }, 1500);
    }

    @Override
    public void onLoad() {
        swipeContainer.postDelayed(new Runnable() {

            @Override
            public void run() {
                swipeContainer.setLoading(false);
                page = page + 1;
                getDatas(state);


            }
        }, 1500);
    }
}
