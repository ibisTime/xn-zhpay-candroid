package com.zhenghui.zhqb.zhenghuiqianbaomember.activity;

import android.content.Context;
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
import com.zhenghui.zhqb.zhenghuiqianbaomember.adapter.DiscountShopAdapter;
import com.zhenghui.zhqb.zhenghuiqianbaomember.application.MyApplication;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.ShopDetailsModel;
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

public class DiscountStoreActivity extends MyBaseActivity implements SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener,AdapterView.OnItemClickListener {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.list_discount)
    ListView listDiscount;
    @InjectView(R.id.swipe_container)
    RefreshLayout swipeContainer;

    private String code;

    private int page = 1;
    private int pageSize = 10;

    private ShopDetailsModel model;
    private List<ShopDetailsModel.StoreTicketsBean> list;
    private DiscountShopAdapter adapter;

    private String name = "";
    private String phone = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount_store);
        ButterKnife.inject(this);
        MyApplication.getInstance().addActivity(this);

        inits();
        initEvent();
        initRefreshLayout();
    }

    private void initRefreshLayout() {
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeContainer.setOnRefreshListener(this);
        swipeContainer.setOnLoadListener(this);
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


        code = getIntent().getStringExtra("code");
        name = getIntent().getStringExtra("name");
        phone = getIntent().getStringExtra("phone");
        userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

    }

    private void initEvent() {
        listDiscount.setOnItemClickListener(this);
    }

    @OnClick(R.id.layout_back)
    public void onClick() {
        finish();
    }

    private void getDatas() {
        JSONObject object = new JSONObject();
        try {
            object.put("type", "");
            object.put("name", "");
            object.put("storeCode", code);
            object.put("status", "1");
            object.put("start", page);
            object.put("limit", pageSize);
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("systemCode", appConfigSp.getString("systemCode", null));
            object.put("companyCode", appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808255", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    List<ShopDetailsModel.StoreTicketsBean> lists = gson.fromJson(jsonObject.getJSONArray("list").toString(), new TypeToken<ArrayList<ShopDetailsModel.StoreTicketsBean>>() {
                    }.getType());

                    list.clear();
                    list.addAll(lists);
                    adapter = new DiscountShopAdapter(DiscountStoreActivity.this, list, name, phone);
                    listDiscount.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(DiscountStoreActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(DiscountStoreActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (list.get(i).getStatus().equals("1")) {
            startActivity(new Intent(DiscountStoreActivity.this, DiscountDetailActivity.class)
                    .putExtra("isStore", true)
                    .putExtra("isBuy", true)
                    .putExtra("name", name)
                    .putExtra("phone", phone)
                    .putExtra("code", list.get(i).getCode()));
        } else {
            startActivity(new Intent(DiscountStoreActivity.this, DiscountDetailActivity.class)
                    .putExtra("isStore", true)
                    .putExtra("isBuy", false)
                    .putExtra("name", name)
                    .putExtra("phone", phone)
                    .putExtra("code", list.get(i).getCode()));
        }
    }

    @Override
    public void onRefresh() {
        swipeContainer.postDelayed(new Runnable() {

            @Override
            public void run() {
                swipeContainer.setRefreshing(false);
                page = 1;
                getDatas();
                // 更新数据
                // 更新完后调用该方法结束刷新
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
                getDatas();
            }
        }, 1500);
    }

}
