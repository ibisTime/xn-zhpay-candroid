package com.zhenghui.zhqb.zhenghuiqianbaomember.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenghui.zhqb.zhenghuiqianbaomember.adapter.BillAdapter;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.BillModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.WalletModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.RefreshLayout;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_802503;
import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_802524;

public class TreeBillActivity extends MyBaseActivity implements SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.txt_title)
    TextView txtTitle;
    @InjectView(R.id.list_bill)
    ListView listBill;
    @InjectView(R.id.swipe_container)
    RefreshLayout swipeContainer;
    @InjectView(R.id.txt_history)
    TextView txtHistory;

    private int page = 1;
    private int pageSize = 10;

    private List<BillModel> list;
    private BillAdapter adapter;

    private String bizType;
    private String currency;
    private String accountNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tree_bill);
        ButterKnife.inject(this);

        inits();
        initListView();
        initRefreshLayout();
        getMoney();
    }

    private void inits() {
        list = new ArrayList<>();
        adapter = new BillAdapter(this, list);

        bizType = getIntent().getStringExtra("bizType");
        currency = getIntent().getStringExtra("currency");

    }

    private void initListView() {
        listBill.setAdapter(adapter);
    }

    private void initRefreshLayout() {
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeContainer.setOnRefreshListener(this);
        swipeContainer.setOnLoadListener(this);
    }

    @OnClick({R.id.layout_back, R.id.txt_history})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.txt_history:
                startActivity(new Intent(TreeBillActivity.this, BillHistoryActivity.class)
                        .putExtra("code", accountNumber));
                break;
        }
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

        new Xutil().post(CODE_802503, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray jsonArray = new JSONArray(result);

                    Gson gson = new Gson();
                    List<WalletModel> lists = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<WalletModel>>() {
                    }.getType());

                    setMoney(lists);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(TreeBillActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(TreeBillActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setMoney(List<WalletModel> lists) {
        for (WalletModel model : lists) {
            switch (model.getCurrency()) {
                case "HBYJ": // 红包业绩
                    accountNumber = model.getAccountNumber();
                    break;
            }
        }
        getData();
    }

    private void getData() {
        JSONObject object = new JSONObject();
        try {
//            object.put("userId", userInfoSp.getString("userId", null));
            object.put("accountNumber", accountNumber);
            object.put("token", userInfoSp.getString("token", null));
            object.put("systemCode", appConfigSp.getString("systemCode", null));
            object.put("type", "C");
            object.put("bizType", bizType);
            object.put("currency", currency);
            object.put("start", page);
            object.put("limit", pageSize);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_802524, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    List<BillModel> lists = gson.fromJson(jsonObject.getJSONArray("list").toString(), new TypeToken<ArrayList<BillModel>>() {
                    }.getType());

                    list.addAll(lists);
                    adapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(TreeBillActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(TreeBillActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRefresh() {
        swipeContainer.postDelayed(new Runnable() {

            @Override
            public void run() {
                swipeContainer.setRefreshing(false);
                page = 1;
                getData();
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
                getData();
            }
        }, 1500);
    }

}
