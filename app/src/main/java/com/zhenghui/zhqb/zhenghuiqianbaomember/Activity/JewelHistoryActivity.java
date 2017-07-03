package com.zhenghui.zhqb.zhenghuiqianbaomember.activity;

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
import com.zhenghui.zhqb.zhenghuiqianbaomember.adapter.TargetAdapter;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.TargetModel;
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

public class JewelHistoryActivity extends MyBaseActivity implements SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener, AdapterView.OnItemClickListener {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.list_histoty)
    ListView listHistoty;
    @InjectView(R.id.swipe_container)
    RefreshLayout swipeContainer;

    private String templateCode;

    private TargetAdapter adapter;
    private List<TargetModel> list;

    private int page = 1;
    private int pageSize = 10;

    public String color = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jewel_history);
        ButterKnife.inject(this);

        inits();
        initRefreshLayout();
        initListView();
        getData();

    }

    private void inits() {
        list = new ArrayList<>();
        adapter = new TargetAdapter(this, list);

        templateCode = getIntent().getStringExtra("templateCode");

    }

    private void initRefreshLayout() {
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeContainer.setOnRefreshListener(this);
        swipeContainer.setOnLoadListener(this);
    }

    private void initListView() {
        listHistoty.setAdapter(adapter);
        listHistoty.setOnItemClickListener(this);
    }

    @OnClick(R.id.layout_back)
    public void onClick() {
        finish();
    }

    private void getData() {

        JSONObject object = new JSONObject();
        try {
            object.put("status", "1");
            object.put("templateCode", templateCode);
            object.put("dateStart", "");
            object.put("dateEnd", "");
            object.put("start", page);
            object.put("limit", pageSize);
            object.put("orderDir", "");
            object.put("orderColumn", "");
            object.put("systemCode", appConfigSp.getString("systemCode", null));
            object.put("companyCode", appConfigSp.getString("systemCode", null));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("615015", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    List<TargetModel> lists = gson.fromJson(jsonObject.getJSONArray("list").toString(), new TypeToken<ArrayList<TargetModel>>() {
                    }.getType());

                    if (page == 1) {
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
                Toast.makeText(JewelHistoryActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(JewelHistoryActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        if (i > 2) {
            if (i % 3 == 0) {
                color = "orange";
            } else if (i % 3 == 1) {
                color = "yellow";
            } else if (i % 3 == 2) {
                color = "green";
            }
        } else {
            if (i == 0) {
                color = "orange";
            } else if (i == 1) {
                color = "yellow";
            } else if (i == 2) {
                color = "green";
            }
        }

        startActivity(new Intent(JewelHistoryActivity.this, MyDuoBaoNumActivity.class)
                .putExtra("winNumber", list.get(i).getWinNumber())
                .putExtra("winer", list.get(i).getUser().getNickname())
                .putExtra("winTime", list.get(i).getWinDatetime())
                .putExtra("model", list.get(i))
                .putExtra("color", color)
                .putExtra("code", list.get(i).getCode()));

    }

}
